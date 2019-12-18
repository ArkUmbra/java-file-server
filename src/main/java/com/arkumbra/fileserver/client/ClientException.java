package com.arkumbra.fileserver.client;

public class ClientException extends Exception {

  public ClientException(Exception e) {
    super(e);
  }

  public ClientException(String message) {
    super(message);
  }
}
