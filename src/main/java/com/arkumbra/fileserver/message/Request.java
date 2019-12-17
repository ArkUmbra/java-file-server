package com.arkumbra.fileserver.message;

import java.io.Serializable;

public class Request implements Serializable {

  private final String message;

  public Request(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
