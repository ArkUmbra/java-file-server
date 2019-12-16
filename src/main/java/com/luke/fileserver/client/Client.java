package com.luke.fileserver.client;

import java.util.Collection;

public interface Client {

  /**
   * List all available files
   */
  Collection<String> index();

  String get(String filename) throws FileDoesNotExistException;




}
