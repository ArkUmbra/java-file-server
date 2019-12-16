Write a simple network file server with below requirements:

- The server can serve a list of available text files that can be
downloaded by the client.
- Acceptable commands from client are
  - "index" to list out all the available files
  - "get <file-name>". Response with either "ok" or "error"
    - The "ok" message will be followed with the content of the file
    - The "error" message indicates that the specified file does not exist on the server.
  - For other commands, the server can give "unknown command" response
  -  “quit” or “q” to exit the program
- It should accept a command-line parameter at start up specifying
the directory that contains the file that the server can serve. The
files should all be text files.
- Listening PORT can be a constant within the code base.

Things we are looking for :
  - Server should be able to handle multiple incoming connections. Hence, thread should be used
  - Proper error handling
  - Ability to run application from command line. That means including client program and test showing multiple clients accessing the server.
  - Full test coverage