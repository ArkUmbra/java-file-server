package com.arkumbra.fileserver.client;

import java.io.IOException;
import java.net.ServerSocket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SocketClientTest {

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

    subjectUnderTest.initConnection();
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
