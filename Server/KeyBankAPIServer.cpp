/* 
 * File:   KeyBankAPIServer.cpp
 * Author: Chris Benton <bentoncl at bentoncl@miamioh.edu>,
 *          Andrew Owens <owensam2 at owensam2@miamioh.edu>
 *
 * Copyright 2018 Chris Benton
 */

#include <ext/stdio_filebuf.h>
#include <unistd.h>
#include <sys/wait.h>
#include <boost/asio.hpp>
#include <cstdlib>
#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <unordered_map>
#include <iomanip>
#include <mutex>
#include <thread>
#include <queue>
#include <time.h>

using namespace std;
using namespace boost::asio;
using namespace boost::asio::ip;

using ConstStr = const std::string&;
using TcpStreamPtr = std::shared_ptr<tcp::iostream>;

// Server Variables
std::string writeResponse(ConstStr code, ConstStr msg);
std::string processRequest(ConstStr type, ConstStr request);
void serveClient(std::istream& is, std::ostream& os);
void threadClient(TcpStreamPtr client);
void runServer(int port);

const std::string OK = "200 OK";
const std::string BAD = "400 Bad Request";
const std::string NOT_FOUND = "404 Not Found";
const std::string ERROR = "500 Internal Server Error";

//Caller Node Class
//Holds all relevant information about callers
//such as n ID and a time to be called
class callerNode {
    
public:
    std::string id, day, hour, min;
    
    callerNode(std::string id = "null",
    std::string day = "0", std::string hour = "0", std::string min = "0")
    {
        this->id = id;
        this->day = day;
        this->hour = hour;
        this->min = min;
    }
    
    ~callerNode()
    {   
    }
    
};

//Customer Node Variables
std::queue<callerNode> callQueue;
std::vector<callerNode> callBackVec;

//Functions Related to the Queue

//Get the Next Caller in the Queue
callerNode getFirst() {
    return callQueue.front();
}

callerNode getBack() {
    return callQueue.back();
}

//Get the current Queue waiting time
//Each caller is assumed a 5 minute time slot
int getQueueTime() {
    int time = callQueue.size();
    time = time*5;    
    return time;
}

//Add Caller to Queue
std::string addCaller(std::string id) {
    callerNode newCaller(id);
    callQueue.push(newCaller);
    return "Successfully Added " + id + " to the queue!"; 
}

std::string nextQueueTime() {
    const int openTime = 7; //8am
    const int closeTime = 16; //5pm
    std::string queueTime;
    
    std::time_t t = std::time(0);
    std::tm* now = std::localtime(&t);
    
    //if its not business hours
    if(now->tm_hour >= closeTime || now->tm_hour < openTime) {
        if(now->tm_hour < openTime) {
        queueTime = std::to_string(now->tm_wday) + " "
                + std::to_string(openTime);
        } else {
            queueTime = std::to_string(now->tm_wday + 1) + " "
                + std::to_string(openTime);
        }
    } //if the end of the queue is past business hours
    else if(now->tm_hour == (closeTime-1) && now->tm_min > getQueueTime()) {
        if(now->tm_hour < openTime) {
        queueTime = std::to_string(now->tm_wday) + " "
                + std::to_string(openTime);
        } else {
            queueTime = std::to_string(now->tm_wday + 1) + " "
                + std::to_string(openTime);
        }
    } //if the end of the queue is within business hours
    else {
        queueTime = std::to_string(now->tm_hour) 
                + std::to_string(getQueueTime());
    }
    return queueTime;
}

std::string callback(std::string request) {
    int idLoc = request.find("id=");    
    int dayLoc = request.find("day=");
    int hourLoc = request.find("hour=");
    int minLoc = request.find("min=");
    std::string id = request.substr(idLoc + 3, dayLoc - idLoc - 4);
    std::string day, hour, min, time;
    //get day from request
    day = request.substr(dayLoc + 4, hourLoc - dayLoc - 5);
    //get hour from request
    hour = request.substr(hourLoc + 5, minLoc - hourLoc - 6);
    //get min from request 
    min = request.substr(minLoc + 4, request.size() - minLoc);
    
    callerNode node(id, day, hour, min);
    callBackVec.push_back(node);
    
    time = day + "_" + hour + "_" + min;
    
    return "Scheduled a Callback for " + id + " at " + time;
}

std::string getCallbackTime(std::string id) {
    std::string time = "Cannot Find Callback";
    for (int i = 0; i < callBackVec.size(); i++) {
        if (callBackVec[i].id == id) {
            time = callBackVec[i].day + "_" + callBackVec[i].hour 
                    + "_" + callBackVec[i].min;
        }
    }
    return time;
}

void cycleQueue() {
    //timing
    time_t beginTime = time(0);
    double timePassed = difftime(time(0), beginTime);
    //Change cycleTime to change how fast the queue cycles
    double cycleTime = 30.0;
    int testNum = 4;
    //every 30 seconds, remove a "finished" person from queue
        //and add a new person
    while(true) {
        timePassed = difftime(time(0), beginTime);
        if(timePassed > cycleTime) {
            std::string id = "Test " + (std::to_string(testNum));
            addCaller(id);
            testNum++;
            std::cout << "Queue Cycled" << std::endl;
            callQueue.pop();
            beginTime = time(0);
        }
        
        std::time_t t = std::time(0);
        std::tm* now = std::localtime(&t);
        for (int i = 0; i < callBackVec.size(); i++) {
        if (callBackVec[i].day == std::to_string(now->tm_wday)
                && callBackVec[i].hour == std::to_string(now->tm_hour)
                && callBackVec[i].min == std::to_string(now->tm_min)
                ) 
        {
            std::cout << callBackVec[i].id << " has been called!" << "\n";
            callBackVec.erase(callBackVec.begin() + i);
        }
        }
    }   
}

//Process Server Request
std::string processRequest(ConstStr type, ConstStr request) {
    std::string code, message;
    
    // at this point, we know what type of request is being made
    //      (GET, PUT, UPDATE, etc)
    // and we know which arguments are being passed in with the request
    //      (in the form: key1=value1&key2=value2&...&keyn=valuen)
    
    // we need to create a response based on the API call
    
    // we should account for the response code, depending on the success
    // of the request processing (200 - Ok, 404 - Not Found, etc)
    
    if ("GET" == type) {
        // process GET request and generate response message
        
        // if processing is successful, set code to OK
        // else, set it to an error code
    } else if ("POST" == type) {
        // process POST request and generate response message
        
        // if processing is successful, set code to OK
        // else, set it to an error code
    }
    //helps with finding the "Add_Queue arguments
    size_t fAdd = request.find("/ADD_QUEUE");
    size_t fCallback = request.find("/CALLBACK&");
    size_t fCallbackTime = request.find("/CALLBACK_TIME");
    
    code = OK;
    
    /*
     * Commands are accessed through url as
     * 'http://ceclnx01.cec.miamioh.edu:2019/<COMMAND>'
     * 
     * any arguments are formatted as '&<ARG>=<DATA>'
     * 
     * for example: a command to check the scheduled callback time of a user is
     * 'http://ceclnx01.cec.miamioh.edu:2019/CALLBACK_TIME&id=user'
     * 
     * Full command list:
     * 
     * "/QUEUE_TIME" - Get the estimated wait time in minutes
     * "/ADD_QUEUE" - Add a user to the queue immediately 
     *      Requires id
     * "/NEXT_QUEUE_TIME" - Get the estimated next available time someone can
     *                      be added to the queue
     * "/CALLBACK" - schedule a user to be called back at a specific time
     *      Requires id, day, hour, min
     * "/CALLBACK_TIME" - check the time of a user in the callback queue
     *                      based on their id
     *      Requires id
     * "/GET_FRONT" - returns the id of the next user in queue (for testing)
     * "/GET_BACK" - returns the id of the last user in queue (for testing)     
     */
    
    
    // Get Queue Time
    if (request == "/QUEUE_TIME") {
        message = std::to_string(getQueueTime());
    } // Add Caller to Queue
    else if(fAdd != std::string::npos) {
        //full request should be '/ADD_QUEUE&id=<id>'"
        int idLoc = request.find("id=");
        std::string id = request.substr(idLoc + 3, request.size() - idLoc);
        message = addCaller(id);
    } //Get the next available time to be called back
    else if(request == "/NEXT_QUEUE_TIME") {
        message = nextQueueTime();
    } //Schedule a callback 
    else if(fCallback != std::string::npos) {
        //full request should be in '/CALLBACK&id=<id>&day=<day>&hour=<hour>&min=<min>
        //day is any int 0-6, 0 being sunday and 6 being saturday
        //hour is any int 0-23, 0 being midnight
        //min should be any int 0-59, 0 being the top of the hour
               
        message = callback(request);
    } else if(fCallbackTime != std::string::npos) {
        //full request should be '/CALLBACK_TIME&id=<id>'
        int idLoc = request.find("id=");
        std::string id = request.substr(idLoc + 3, request.size() - idLoc);
        message = getCallbackTime(id);
    }
    // Get Next Person in Queue (mostly for testing purposes
    else if(request == "/GET_FIRST") {
        message = getFirst().id;
    }
    else if(request == "/GET_BACK") {
        message = getBack().id;
    } else {
        code = BAD;
        message = request;
    }
    
    message += "\r\n\r\n";      // required to end the HTTP message
    return writeResponse(code, message);
}

/**************************************************************************/
/*********** Should not need to modify anything below this line ***********/
/**************************************************************************/

/**
 * Write out response to specified parameters.
 * 
 * @param message   Message to be sent to client
 */
std::string writeResponse(ConstStr code, ConstStr message) {
    std::string response;
    
    response += "HTTP/1.1 " + code + "\r\n";
    response += "Server: KeyBankCapStone\r\n";
    response += "Content-Length: " + std::to_string(message.length()) + "\r\n";
    response += "Connection: Close\r\n";
    response += "Content-Type: text/plain\r\n\r\n";
    response += message;
    return response;
}

/**
 * Process HTTP request and provide suitable HTTP
 * response back to the client.
 * 
 * @param is The input stream to read data from client.
 * @param os The output stream to send data to client.
 */
void serveClient(std::istream& is, std::ostream& os) {
    std::string line;       // entire line of incoming text
    std::string reqType;    // GET, FETCH, UPDATE, etc.
    std::string req;        // actual request
    std::string response;   // HTTP response
    int reqLen = 0;
    
    std::getline(is, line);
    reqType = line.substr(0, line.find(" "));
    for (; line[reqLen + reqType.length() + 1] != ' '; reqLen++) { }
    req = line.substr(reqType.length() + 1,  reqLen);
       
    response = processRequest(reqType, req);
    
    while (std::getline(is, line), line != "\r") { }     // read until EOF

    os << response;
}

/**
 * Serve client on own thread
 * 
 * @param client    Client stream
 */
void threadClient(TcpStreamPtr client) {
    serveClient(*client, *client);
}

/**
 * Run the server, the server will start listening for clients
 * on the specified port number
 * 
 * @param port The port number on which the server will listen
 */
void runServer(int port) {
    io_service service;   
    tcp::endpoint myEndpoint(tcp::v4(), port);
    tcp::acceptor server(service, myEndpoint);
    std::cout << "Server is listening on " << port
            << " & ready to process clients...\n";

    while (true) {     
        TcpStreamPtr client = std::make_shared<tcp::iostream>();
        server.accept(*client->rdbuf());
        std::thread thr(threadClient, client);
        thr.detach();
    }
}

int main(int argc, char** argv) {
    //create test callers
    callerNode testNode1("Test 1");
    callerNode testNode2("Test 2");
    callerNode testNode3("Test 3");
    
    //add callers to queue
    callQueue.push(testNode1);
    callQueue.push(testNode2);
    callQueue.push(testNode3);
    
    //run server
    if (argc == 2) {
        std::thread thr(cycleQueue);
        thr.detach();
        const int port = std::stoi(argv[1]);
        runServer(port);
    } else {
        std::cerr << "Invalid command-line arguments specified" << std::endl;
        std::cout << "Use './KeyBankAPIServer <port number>'" << std::endl;
    }
    return 0;
}

