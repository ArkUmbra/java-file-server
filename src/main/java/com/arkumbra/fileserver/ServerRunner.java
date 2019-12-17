package com.arkumbra.fileserver;

import com.arkumbra.fileserver.file.FileFetcher;
import com.arkumbra.fileserver.file.FileFetcherImpl;
import com.arkumbra.fileserver.server.Server;
import com.arkumbra.fileserver.server.SocketServer;

public class ServerRunner {

  public static void main(String... args) {
    if (args == null || args.length == 0) {
      System.err.println("Please specify a directory to serve. "
          + "The first program argument will be read");
      System.exit(1);
    }

    String baseDir = args[0];
    String extension = ".txt";

    new ServerRunner().launch(baseDir, extension);
  }

  private void launch(String baseDir, String extension) {
    FileFetcher fileFetcher = new FileFetcherImpl(baseDir, extension);

    try {
      Server socketServer = new SocketServer(fileFetcher);
      socketServer.launch();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
