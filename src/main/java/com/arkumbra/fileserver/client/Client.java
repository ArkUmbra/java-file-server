package com.arkumbra.fileserver.client;

import com.arkumbra.fileserver.message.Response;

public interface Client {


  /**
   * Establish connection with the server, if required
   * @return connection response from server
   * @throws ClientException when connection issue, etc arises
   */
  Response initConnection() throws ClientException;

  /**
   *
   * @param input command
   * @return response indicating success and contents
   * @throws ClientException when connection issue, etc arises
   */
  Response handleCommand(String input) throws ClientException;

}
