package com.arkumbra.fileserver;

public class RunnerException extends RuntimeException {

  public RunnerException(String message) {
    super(message);
  }

  public RunnerException(Throwable cause) {
    super(cause);
  }
}
