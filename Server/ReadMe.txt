----- SETTING UP THE SERVER -----
The server is written in C++.
When compiling, the server needs -lboost_system, -lpthread, and -lmysqlpp in the compilation line.

The server accepts the port number as an argument.

----- COMMANDS -------

Commands are accessed through the url:
	'http://ceclnx01.cec.miamioh.edu:<PORT>/<COMMAND>'

Any arguments are formatted as '&<ARG>=<DATA>'

for example: a command to check the scheduled callback time of 'user' on port 2019 is:
	'http://ceclnx01.cec.miamioh.edu:2019/CALLBACK_TIME&id=user'

Full command list:
     * 
     * "/QUEUE_TIME" - Get the estimated wait time in minutes
     *      Returns time in minutes
     * "/ADD_QUEUE" - Add a user to the on-hold queue immediately 
     *      Requires id
     *      Returns 0 if successful
     * "/NEXT_QUEUE_TIME" - Get the estimated next available time someone can
     *                      be added to the queue
     *      Returns time in D HH MM
     * "/CALLBACK" - schedule a user to be called back at a specific time
     *      Requires id, day, hour, min
     *      Returns '0 D HH MM' if successful
     *              if time slot was full, returns '1 D HH MM' of next available time slot, does not
			schedule a callback
     * "/CALLBACK_TIME" - check the time of a user in the callback queue
     *                      based on their id
     *      Requires id
     *      Returns time as D HH MM
     * "/REMOVE" - removes a user scheduled for callback based on their id
     *      Requires id
     *      Returns 0 if successful, otherwise return 1
     * "/NEXT_QUEUE_DAYS" - Get the next two days available to queue in as ints.
     *                      EX. if today is Friday night, the ints returned are
     *                      "1 2" for Monday and Tuesday. Days are 0-6 from Sunday to Saturday 
     *      Returns days as D D
     * "/SHOW" - shows both queues
     *      Returns a visual representaion of the queues
     * "/GET_FRONT" - returns the id of the next user in queue (for testing)
     * "/GET_BACK" - returns the id of the last user in queue (for testing)