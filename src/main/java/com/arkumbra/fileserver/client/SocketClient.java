package com.arkumbra.fileserver.client;

import com.arkumbra.fileserver.message.Response;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements Client {
  private static final String address = "127.0.0.1";
  private static final int port = 9090;

  private Socket socket;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  /**
   * Opens a socket to a predefined server endpoint.
   *
   * @return response provider by server when accepting the connection.
   * @throws ClientException if network error
   */
  @Override
  public Response initConnection() throws ClientException {
    try {
      socket = new Socket(address, port);
      this.oos = new ObjectOutputStream(socket.getOutputStream());
      this.ois = new ObjectInputStream(socket.getInputStream());

      return doGreeting();

    } catch (IOException | ClassNotFoundException e) {
      throw new ClientException(e);
    }
  }

  private Response doGreeting() throws IOException, ClassNotFoundException {
    return (Response) ois.readObject();
  }

  /**
   *
   * @param input command to write to server socket
   * @return Response from server
   * @throws ClientException if network error, etc
   */
  @Override
  public Response handleCommand(String input) throws ClientException {
    try {
      // send message to server
      oos.writeObject(input);
      // receive response from server
      return (Response) ois.readObject();

    } catch (IOException e) {
      closeQuietly(socket);
      closeQuietly(oos);
      closeQuietly(ois);
      throw new ClientException(e);

    } catch (ClassNotFoundException e) {
      throw new ClientException(e);
    }
  }

  private void closeQuietly(Closeable closeable) {
    try {
      closeable.close();
    } catch (IOException e) {
      // closing quietly, so don't need to print anything
    }
  }

}
