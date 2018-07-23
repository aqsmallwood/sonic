HTTP Client
This application exposes a root route, which accepts XML in the form of a GET queryString
The XML must follow the structure
```
<request command="command><element>data</element></request>
```
The following functionality is exposed
```
<request command="print></request> - Prints all Ticket Data
<request command="print><ticketid>1</ticketid></request> - Prints Data for Ticket 1
<request command="add><tickettitle>New Ticket Title</tickettitle></request> - Prints Data for Ticket 1
```

Alternative to the root route, is the /ticket route, which overloads getting data about a single ticket
GET /ticket/1

Telnet Client
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


mvn package
java -jar target/o