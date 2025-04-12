Katana spec:

microframework with lightweight http server

- lightweight http server
Likely connection pool of http connections.


- routing
Default routes initialization is done

- request processing

- DI framework
Basic Container and intizitialization already present. Now need to add something like autowiring.
Maybe explicit mapping/configuration

- error handling

packages:
- core 
shared logic, framework core
- networking
http server, connection pool management
- routing 
