package com.arkumbra.fileserver;

import com.arkumbra.fileserver.file.FileFetcher;
import com.arkumbra.fileserver.file.FileFetcherImpl;
import com.arkumbra.fileserver.server.Server;
import com.arkumbra.fileserver.server.ServerFactory;
import com.arkumbra.fileserver.server.ServerFactoryImpl;
import java.io.IOException;

public class ServerRunner {

  private final FileFetcher fileFetcher;
  private final ServerFactory serverFactory;

  public static void main(String... args) {
    new ServerRunner(args).launch();
  }

  public ServerRunner(String... args) {
    String baseDir = parseBaseDir(args);
    String extension = ".txt";
    this.fileFetcher = new FileFetcherImpl(baseDir, extension);
    this.serverFactory = new ServerFactoryImpl();
  }

  public ServerRunner(FileFetcher fileFetcher, ServerFactory serverFactory) {
    this.fileFetcher = fileFetcher;
    this.serverFactory = serverFactory;
  }

  private String parseBaseDir(String... args) throws RunnerException {
    if (args == null || args.length == 0) {
      throw new RunnerException("Please specify a directory to serve. "
          + "The first program argument will be read");
    }

    return args[0];
  }

  public void launch() {
    try {
      Server socketServer = serverFactory.createServer(fileFetcher);
      socketServer.launch();

    } catch (IOException e) {
      throw new RunnerException(e);
    }
  }

}
