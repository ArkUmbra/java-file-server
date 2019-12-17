package com.arkumbra.fileserver;

import com.arkumbra.fileserver.client.Client;
import com.arkumbra.fileserver.client.SocketClient;
import com.arkumbra.fileserver.message.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.Buffer;
import java.util.Scanner;

/**
 * Starts the command line client tool, ready to accept user input
 */
public class ClientRunner {

  private final Client client = new SocketClient();
//  private final InputStream in;
//  private final Scanner scanner;
  private final BufferedReader bufferedReader;
  private final PrintStream out;
  private final PrintStream err;

  public static void main(String... args) {
    new ClientRunner().run();
  }

  public ClientRunner() {
//    this.in = System.in;
//    this.scanner = new Scanner(System.in);
    Reader reader = new InputStreamReader(System.in);
    this.bufferedReader = new BufferedReader(reader);
    this.out = System.out;
    this.err = System.err;
  }

  /**
   * Override the input stream
   * @param in
   */
  public ClientRunner(BufferedReader reader, PrintStream out, PrintStream err) {
    this.bufferedReader = reader;
    this.out = out;
    this.err = err;
  }

  public void run() {
    boolean completed = false;

    try {
      Response initResponse = client.initConnection();
      log(initResponse.getMsg());
    } catch (Exception e) {
      err("Error establishing connection to server: " + e.getMessage());
    }

//    Scanner scanner = new Scanner(in);

    while (! completed) {
      out.print("$ ");
      String input = readLine();
      completed = handleInput(input);
    }
  }

  private String readLine() {
    try {
      return bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
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


  }

  private void log(String msg) {
    out.println(msg);
  }

  private void err(String msg) {
    err.println(msg);
  }

}
