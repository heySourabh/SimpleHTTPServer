# SimpleHTTPServer
This is a simple http server to run and test web pages while you are designing them.
This server also supports GET and POST requests, and therefore can be used to run remote services.

### To run the server on the default port (8000) simply use the command:

`java -jar ./dist/SimpleHTTPServer.jar`

#### Now you can open a web browser and enter http://localhost:8000 to open the webpage/index.html

### To run the server on a specific port run the command:

`java -jar ./dist/SimpleHTTPServer.jar <port>`

This will serve the page `./webpage/index.html` and use the folder `webpage` as the base folder for next upcoming requests.


