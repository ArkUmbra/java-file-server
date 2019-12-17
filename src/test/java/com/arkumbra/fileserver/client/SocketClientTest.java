package com.arkumbra.fileserver.client;

import static org.junit.Assert.assertFalse;

import com.arkumbra.fileserver.message.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SocketClientTest {

  private ExecutorService executor = Executors.newSingleThreadExecutor();
  private SocketClient subjectUnderTest;
  private ServerSocket server;

  @Before
  public void setUp() throws IOException {
    int port = 12345;
    this.subjectUnderTest = new SocketClient();
    server = new ServerSocket(port);
  }

  @Test
  public void testInitConnection() throws IOException {
    // set up server first

    Future<Response> response = asyncInitConnection();
    assertFalse(response.isDone());



  }

  private Future<Response> asyncInitConnection() throws IOException {
    return executor.submit(() -> {
      return subjectUnderTest.initConnection();
    });
  }

  private void acceptClientAndSen(String msg) throws IOException {
    Socket pairedWithClient = server.accept();

//    try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
//
//    }
  }

  @After
  public void tearDown() {
    try {
      server.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
