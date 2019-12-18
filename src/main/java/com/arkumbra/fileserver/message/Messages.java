package com.arkumbra.fileserver.message;

/**
 * Constants indicating the messages that the server may choose to reply with
 */
public class Messages {

  public static final String WELCOME = "Welcome to the Java file server. Enter your selection to continue.";
  public static final String VALID_COMMANDS = "Valid commands are 'index', 'get <file-name>', "
      + "and 'quit'/'q'";
  public static final String GREETING = WELCOME + "\n" + VALID_COMMANDS;
  public static final String UNKNOWN_COMMAND = "Unknown Command";
  public static final String FILE_NOT_FOUND = "Error. File not found";

}
