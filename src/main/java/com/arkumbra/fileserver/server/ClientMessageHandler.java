package com.arkumbra.fileserver.server;

import com.arkumbra.fileserver.file.FileFetcher;
import com.arkumbra.fileserver.message.Messages;
import com.arkumbra.fileserver.message.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.NoSuchFileException;
import java.util.Collection;

public class ClientMessageHandler implements Runnable {

  private String tag;
  private final Socket socket;
  private final FileFetcher fileFetcher;

  public ClientMessageHandler(Socket socket, FileFetcher fileFetcher) {
    this.socket = socket;
    this.fileFetcher = fileFetcher;
  }

  @Override
  public void run() {
    this.tag = Thread.currentThread().getName();

    try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

      // send greeting
      sendMessage(oos, Messages.GREETING);

      doEventLoop(ois, oos);

    } catch (IOException | ClassNotFoundException e) {
      err("Exited due to an exception");
      e.printStackTrace();
    }

  }

  private void doEventLoop(ObjectInputStream ois, ObjectOutputStream oos)
      throws IOException, ClassNotFoundException {

    log("Starting");

    boolean running = true;

    while (running) {
      log("Awaiting input...");
      String command = (String)ois.readObject();
      log("Received command [" + command + "]");

      boolean shouldExit = handleInput(command, oos);
      if (shouldExit) {
        running = false;
      }

    }

    log("Exiting");
  }

  private boolean handleInput(String input, ObjectOutputStream oos) throws IOException {
    // exit check
    if (input.equals("quit") || input.equals("q")) {
      sendClose(oos, "Quitting");
      return true;
    }

    if (input.equals("index")) {
      doIndexCommand(oos);
    } else if (input.startsWith("get ")) {
      doGetFile(oos, input);
    } else {
      sendError(oos, Messages.UNKNOWN_COMMAND);
    }

    return false;
  }

  private void doIndexCommand(ObjectOutputStream oos) throws IOException {
    Collection<String> indexed = fileFetcher.listAllFiles();
    String joined = String.join("\n", indexed);
    sendMessage(oos, joined);
  }

  private void doGetFile(ObjectOutputStream oos, String command) throws IOException {
    String[] tokens = command.split(" ");
    if (tokens.length != 2) {
      sendError(oos, "Bad formatting. Can only get one file at a time");
    } else {
      try {
        String fileContent = fileFetcher.get(tokens[1]);
        sendMessage(oos, fileContent);
      } catch (FileNotFoundException | NoSuchFileException e) {
        sendError(oos, Messages.FILE_NOT_FOUND);
      }
    }
  }

  private void sendMessage(ObjectOutputStream oos, String msg) throws IOException {
    oos.writeObject(new Response(false, msg, false));
  }

  private void sendError(ObjectOutputStream oos, String msg) throws IOException {
    oos.writeObject(new Response(true, msg, false));
  }

  private void sendClose(ObjectOutputStream oos, String msg) throws IOException {
    oos.writeObject(new Response(false, msg, true));
  }

  private void err(String msg) {
    System.err.println(tag + " - " + msg);
  }
  private void log(String msg) {
    System.out.println(tag + " - " + msg);
  }

}
