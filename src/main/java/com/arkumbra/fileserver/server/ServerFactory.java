package com.arkumbra.fileserver.server;

import com.arkumbra.fileserver.file.FileFetcher;
import java.io.IOException;

public interface ServerFactory {

  Server createServer(FileFetcher fileFetcher) throws IOException;

}
