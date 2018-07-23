## Java XML Service
This application creates a web service for managing tickets. The application accepts XML specifying commands to print tickets or add tickets.
In the case of adding, a ticketTitle is to be provided, and metadata will be created for it for id, timestamp, etc. When printing tickets,
a ticket ID can be specified, otherwise all tickets are printed.

## App Structure
The application was built with the intention of being easily extensible. Because of this I tried to generalize the trigger mechanism (Routes), the required stream input/output functions (Handler/Controller),
and the actions to be performed (Services). I aimed to create a Service which is used by two different types of clients to prove loose coupling in the app.

The HTTP server listens on a port with a global context. When a request comes in, the RequestHandler is in charge of delegating work to a Controller based on the URIPath.
In this application two Controllers are defined, the IndexController, and the TicketController. The IndexController prints and adds tickets with xml passed into GET query strings.
The TicketController uses a more RESTful approach to print tickets, by specifying the ticket id in the URI Path, still returning XML.

Both of these Controllers use the TicketService, which just stores an ArrayList of Tickets. This is a static list in place of a real data store.
The TicketService also exposes a means to add and query the Tickets.

The Controller then takes the string returned from the service, edits as necessary to create a response body, and wraps it in a Response object which also stores the response code.

The RequestHandler is responsible for writing the Response returned by the Contoller to the connection output stream.

### Telnet Server
The telnet server is similar to the HttpServer, but simply reads lines of data from a SocketServer, and parses a command from them.
If the service and command are provided are valid, the server will call to the Service to execute the command and get a response.
The telnet server creates threads for every connection, which will exist until those connections are closed.


## HTTP Client
The following functionality is exposed via GET / methods
```
/?<request command="print></request> - Prints all Ticket Data
/?<request command="print><ticketid>1</ticketid></request> - Prints Data for Ticket 1
/?<request command="add><tickettitle>New Ticket Title</tickettitle></request> - Creates new Ticket with given title
```

Alternative to the root route, is the /ticket route, which overloads getting data about a single ticket
GET /ticket/1

## Telnet Client
```
$ telnet localhost 7000
Trying ::1...
Connected to localhost.
Escape character is '^]'.
tickets print
{1=1 - Create Java XML Service}
tickets print 2
Ticket 2 does not exist
tickets add Create telnet server
Ticket created
tickets print 2
2 - create telnet server
tickets print
{1=1 - Create Java XML Service, 2=2 - create telnet server}
```

The app is divided into two main threads, the HTTP server thread, and the Telnet client.
The telnet client is implemented with a simple SocketServer, it reads commands and executes them via a Service.
The HTTP server implements com.sun.net.httpserver.HttpServer, requests are routed through com.aqsmallwood.handlers.RequestHandler to Controller(s).

## Building and running the project
```
mvn package
java -jar target/sonic.jar
java -jar -DtelnetPort=7000 -DhttpPort=5000 -DhttpThreads=5 target/sonic.jar
```


### Quick Links
```
http://localhost:5000/?<request command="print"></request>
http://localhost:5000/?<request command="print"><ticketid>1</ticketid></request>
http://localhost:5000/?<request command="add"><tickettitle>New Ticket</tickettitle></request>
http://localhost:5000/ticket/1
```