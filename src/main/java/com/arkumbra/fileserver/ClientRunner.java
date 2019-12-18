package com.arkumbra.fileserver;

import com.arkumbra.fileserver.client.Client;
import com.arkumbra.fileserver.client.ClientException;
import com.arkumbra.fileserver.client.SocketClient;
import com.arkumbra.fileserver.message.Response;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Starts the command line client tool, ready to accept user input
 */
public class ClientRunner {

  private final Client client;
  private final InputStream in;
  private final PrintStream out;
  private final PrintStream err;

  public static void main(String... args) {
    new ClientRunner().run();
  }


  public ClientRunner() {
    this.client = new SocketClient();

    this.in = System.in;
    this.out = System.out;
    this.err = System.err;
  }

  public ClientRunner(Client client, InputStream in, PrintStream out, PrintStream err) {
    this.client = client;
    this.in = in;
    this.out = out;
    this.err = err;
  }

  public void run() {
    boolean completed = false;

    try {
      Response initResponse = client.initConnection();
      log(initResponse.getMsg());
    } catch (ClientException e) {
      err("Error establishing connection to server: " + e.getMessage());
      return;
    }

    Scanner scanner = new Scanner(in);

    while (! completed) {
      out.print("$ ");
      String input = scanner.nextLine();
      completed = handleInput(input);
    }
  }

  /***
   * @return boolean indicating whether program has completed
   */
  private boolean handleInput(String input) {
    Response response;
    try {
      response = client.handleCommand(input);

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

    } catch (ClientException e) {
      err(e.getMessage());
      err("An exception was encountered, so the program will now close");
      return true;
    }
  }

  private void log(String msg) {
    out.println(msg);
  }

  private void err(String msg) {
    err.println(msg);
  }

}
