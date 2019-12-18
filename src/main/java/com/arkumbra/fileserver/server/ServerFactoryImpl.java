package com.arkumbra.fileserver.server;

import com.arkumbra.fileserver.file.FileFetcher;
import java.io.IOException;

public class ServerFactoryImpl implements ServerFactory {

  public Server createServer(FileFetcher fileFetcher) throws IOException {
    return new SocketServer(fileFetcher);
  }

}
