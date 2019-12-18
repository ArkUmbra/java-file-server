package com.arkumbra.fileserver.message;

import java.io.Serializable;

/**
 *
 */
public class Response implements Serializable {

  private final boolean isError;
  private final String msg;
  private final boolean closed;

  public Response(boolean isError, String msg, boolean closed) {
    this.isError = isError;
    this.msg = msg;
    this.closed = closed;
  }

  public boolean isError() {
    return isError;
  }

  public String getMsg() {
    return msg;
  }

  public boolean isClosed() {
    return closed;
  }
}
