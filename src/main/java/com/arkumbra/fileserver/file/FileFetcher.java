package com.arkumbra.fileserver.file;

import java.io.IOException;
import java.util.Collection;

public interface FileFetcher {

  Collection<String> listAllFiles() throws IOException;

  String get(String filename) throws IOException;

}
