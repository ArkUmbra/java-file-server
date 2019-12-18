package com.arkumbra.fileserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.arkumbra.fileserver.client.ClientException;
import com.arkumbra.fileserver.client.SocketClient;
import com.arkumbra.fileserver.file.FileFetcher;
import com.arkumbra.fileserver.file.FileFetcherImpl;
import com.arkumbra.fileserver.message.Messages;
import com.arkumbra.fileserver.message.Response;
import com.arkumbra.fileserver.server.Command;
import com.arkumbra.fileserver.server.SocketServer;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class EndToEndTest {

  private static final String EXTENSION = ".txt";

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private SocketClient socketClient;
  private SocketServer socketServer;
  private Thread serverThread;

  @Before
  public void setUp() throws Exception {
    FileFetcher fileFetcher = new FileFetcherImpl(folder.getRoot().getAbsolutePath(), EXTENSION);

    this.socketClient = new SocketClient();
    this.socketServer = new SocketServer(fileFetcher);

    serverThread = new Thread(() -> socketServer.launch());
    serverThread.start();
  }

  @After
  public void tearDown() throws InterruptedException {
    System.out.print("Shutting down server and disconnecting associated clients");
    socketServer.shutdown();
//    serverThread.interrupt();
  }


  @Test
  public void testGreeting() throws ClientException {
    doGreeting(socketClient);
  }

  @Test
  public void testGreetingWithMultipleClients() throws ClientException {
    for (int i = 0; i < 10; i++) {
      SocketClient client = new SocketClient();
      doGreeting(client);
    }
  }

  private void doGreeting(SocketClient client) throws ClientException {
    Response greeting = client.initConnection();
    assertFalse(greeting.isClosed());
    assertFalse(greeting.isError());
    assertEquals(Messages.GREETING, greeting.getMsg());
  }

  @Test
  public void testListingIndexOfFiles() throws Exception {
    doGreeting(socketClient);

    String validFileName = "somefile.txt";
    String notValidExt = "somefile2.jpg";
    folder.newFile(validFileName);
    folder.newFile(notValidExt);

    doIndex(socketClient, validFileName);
    doGetFile(socketClient, validFileName);
  }

  @Test
  public void testListingIndexOfFilesWithMultipleClients() throws Exception {
    doGreeting(socketClient);

    String validFileName = "somefile.txt";
    String notValidExt = "somefile2.jpg";
    folder.newFile(validFileName);
    folder.newFile(notValidExt);

    // check client 1 gets correct files
    doIndex(socketClient, validFileName);

    // check client 2 gets correct files
    SocketClient client2 = new SocketClient();
    doGreeting(client2);
    doIndex(client2, validFileName);

    // check client 1 still gets correct files after client 2
    doIndex(socketClient, validFileName);
  }

  private void doIndex(SocketClient client, String... expectedFiles) throws ClientException {
    Response indexResponse = client.handleCommand(Command.INDEX);
    assertFalse(indexResponse.isError());
    assertFalse(indexResponse.isClosed());
    System.out.print(indexResponse.getMsg());

    // Check the correct file was listed
    String[] listings = indexResponse.getMsg().split("\n");
    assertEquals(expectedFiles.length, listings.length);
    for (int i = 0; i < expectedFiles.length; i++) {
      assertEquals(expectedFiles[i], listings[i]);
    }
  }

  private void doGetFile(SocketClient client, String filename) throws ClientException {
    Response response = client.handleCommand(Command.GET_PREFIX + filename);
    assertFalse(response.isError());
    assertFalse(response.isClosed());
    System.out.print(response.getMsg());
  }

}
