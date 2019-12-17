package com.arkumbra.fileserver.client;

import com.arkumbra.fileserver.message.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements Client {
  public String address = "127.0.0.1";
  public int port = 9090;

  private Socket socket;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;


  @Override
  public Response initConnection() throws IOException {
    socket = new Socket(address, port);
    this.oos = new ObjectOutputStream(socket.getOutputStream());
    this.ois = new ObjectInputStream(socket.getInputStream());

    return doGreeting();
  }

  private Response doGreeting() throws IOException {
    try {
      return (Response) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new IOException(e);
    }
  }

  @Override
  public Response handleCommand(String input) throws Exception {

    try {
      // send message to server
      oos.writeObject(input);
      // receive response from server
      return (Response) ois.readObject();

    } catch (Exception e) {
      socket.close();
      oos.close();
      ois.close();
      throw e;
    }
  }

}
