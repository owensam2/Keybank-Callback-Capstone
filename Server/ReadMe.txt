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

- "/QUEUE_TIME" - Get the estimated wait time in minutes
- "/ADD_QUEUE" - Add a user to the on-hold queue immediately 
	- Requires id
- "/NEXT_QUEUE_TIME" - Get the estimated next available time someone can be added to the queue
- "/CALLBACK" - schedule a user to be called back at a specific time
 	- Requires id, day, hour, min
- "/CALLBACK_TIME" - check the time of a user in the callback queue based on their id
	- Requires id
- "/REMOVE" - removes a user scheduled for callback based on their id
     	- Requires id
- "/SHOW" - shows both queues
- "/GET_FRONT" - returns the id of the next user in queue (for testing)
- "/GET_BACK" - returns the id of the last user in queue (for testing)