package com.arkumbra.fileserver.client;

import com.arkumbra.fileserver.message.Response;

public interface Client {


  /**
   * Establish connection with the server, if required
   * @return connection response from server
   * @throws Exception
   */
  Response initConnection() throws Exception;

  Response handleCommand(String input) throws Exception;

}
