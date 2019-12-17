package com.arkumbra.fileserver;

import com.arkumbra.fileserver.client.Client;
import com.arkumbra.fileserver.client.SocketClient;
import com.arkumbra.fileserver.message.Response;
import java.util.Scanner;

/**
 * Starts the command line client tool, ready to accept user input
 */
public class ClientRunner {

  private Client client = new SocketClient();

  public static void main(String... args) {
    new ClientRunner().run();
  }

  public void run() {
    boolean completed = false;

    try {
      Response initResponse = client.initConnection();
      log(initResponse.getMsg());
    } catch (Exception e) {
      err("Error establishing connection to server: " + e.getMessage());
    }

    Scanner scanner = new Scanner(System.in);
    while (! completed) {
      System.out.print("$ ");
      String input = scanner.nextLine();
      completed = handleInput(input);
    }
  }


  // TODO instead of handling on client side, just send message raw to server and let it handle
  /***
   * @return boolean indicating whether program has completed
   */
  private boolean handleInput(String input) {
    try {
      Response response = client.handleCommand(input);
      if (response.isClosed()) {
        log("Exiting");
        return true;

      } else {
        if (response.isError()) {
          err(response.getMsg());
        } else {
          log(response.getMsg());
        }

        return false;
      }


    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }

////    System.out.println("Your input: " + input);
//    if (input.equals("quit") || input.equals("q")) {
//      return true;
//    } else if (input.equals("index")) {
//
//    } else if (input.startsWith("get ")) {
//
//    } else {
//      printErr(UNKNOWN_COMMAND);
//    }
//
//    return false;
  }

  private void log(String msg) {
    System.out.println(msg);
  }

  private void err(String msg) {
    System.err.println(msg);
  }

}
