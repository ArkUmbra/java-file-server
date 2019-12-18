package com.arkumbra.fileserver.server;

import com.arkumbra.fileserver.file.FileFetcher;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Server {
  private static final int PORT = 9090;

  private final ServerSocket server;
  private final FileFetcher fileFetcher;

  private List<Thread> handlers = new ArrayList<Thread>();

  public SocketServer(FileFetcher fileFetcher) throws IOException {
    this.server = new ServerSocket(PORT);
    this.fileFetcher = fileFetcher;
  }

  /**
   * Blocking.
   */
  @Override
  public void launch() {
    while (true) {
      try {
        Socket client = server.accept();
        System.out.println("Client accepted");

        // Create a new thread per connection
        ClientMessageHandler handler = new ClientMessageHandler(client, fileFetcher);
        Thread thread = new Thread(handler);
        handlers.add(thread);
        thread.start();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void shutdown() {
    try {
      server.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
