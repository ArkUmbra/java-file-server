package com.arkumbra.fileserver;

import com.arkumbra.fileserver.client.Client;
import com.arkumbra.fileserver.client.ClientException;
import com.arkumbra.fileserver.message.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ClientRunnerTest {

  private static final Response NO_ERR = new Response(false, "unittest", false);
  private static final Response ERROR = new Response(true, "unittest", false);
  private static final Response CLOSE = new Response(false, "unittest", true);

  private PrintStream outMock;
  private PrintStream errMock;

  @Before
  public void setUp() {
     errMock = Mockito.mock(PrintStream.class);
     outMock = Mockito.mock(PrintStream.class);
  }

  @After
  public void validate() {
    Mockito.validateMockitoUsage();
  }

  @Test
  public void testInitialisation() {
    new ClientRunner();
  }

  @Test
  public void testCanQuitClientRunner() throws Exception {
    String command1 = "unittest-quit";
    String commands = String.join("\n", command1);
    InputStream in = new ByteArrayInputStream(commands.getBytes());

    Client mockClient = Mockito.mock(Client.class);
    ClientRunner subjectUnderTest = new ClientRunner(mockClient, in, outMock, errMock);

    // set up expects
    Mockito.when(mockClient.initConnection()).thenReturn(NO_ERR);
    Mockito.when(mockClient.handleCommand(command1)).thenReturn(CLOSE);

    // If close command is correctly received, should exit
    subjectUnderTest.run();

    Mockito.verify(mockClient).initConnection();
    Mockito.verify(mockClient).handleCommand(command1);
  }

  @Test
  public void testCanAcceptMultipleCommands() throws Exception {
    String command1 = "unittest-index";
    String command2 = "unittest-quit";
    String commands = String.join("\n", command1, command2);
    InputStream in = new ByteArrayInputStream(commands.getBytes());

    Client mockClient = Mockito.mock(Client.class);
    ClientRunner subjectUnderTest = new ClientRunner(mockClient, in, outMock, errMock);

    // set up expects
    Mockito.when(mockClient.initConnection()).thenReturn(NO_ERR);
    Mockito.when(mockClient.handleCommand(command1)).thenReturn(NO_ERR);
    Mockito.when(mockClient.handleCommand(command2)).thenReturn(CLOSE);

    // If close command is correctly received, should exit
    subjectUnderTest.run();

    Mockito.verify(mockClient).initConnection();
    Mockito.verify(mockClient).handleCommand(command1);
    Mockito.verify(mockClient).handleCommand(command2);
  }

  @Test
  public void testCanHandleErrorResponse() throws Exception {
    String command1 = "unittest-error";
    String command2 = "unittest-quit";
    String commands = String.join("\n", command1, command2);
    InputStream in = new ByteArrayInputStream(commands.getBytes());

    Client mockClient = Mockito.mock(Client.class);

    ClientRunner subjectUnderTest = new ClientRunner(mockClient, in, outMock, errMock);

    // set up expects
    Mockito.when(mockClient.initConnection()).thenReturn(NO_ERR);
    Mockito.when(mockClient.handleCommand(command1)).thenReturn(ERROR);
    Mockito.when(mockClient.handleCommand(command2)).thenReturn(CLOSE);


    // If close command is correctly received, should exit
    subjectUnderTest.run();

    Mockito.verify(mockClient).initConnection();
    Mockito.verify(mockClient).handleCommand(command1);
    Mockito.verify(mockClient).handleCommand(command2);
    // verify err gets printed
    Mockito.verify(errMock).println(ERROR.getMsg());
  }

  @Test
  public void testClientQuitsWhenFailsToConnect() throws Exception { ;
    InputStream in = new ByteArrayInputStream(new byte[]{}); // empty, because it should fail before this is needed

    Client mockClient = Mockito.mock(Client.class);

    ClientRunner subjectUnderTest = new ClientRunner(mockClient, in, outMock, errMock);

    // set up expects
    Mockito.when(mockClient.initConnection())
        .thenThrow(new ClientException("unit test exception"));

    // If close command is correctly received, should exit
    subjectUnderTest.run();

    Mockito.verify(mockClient).initConnection();
    // verify err gets printed
    Mockito.verify(errMock).println((String)Mockito.any());
  }

  @Test
  public void testClientWillQuitIfExceptionDuringCommand() throws Exception {
    String command1 = "unittest-error";
    String commands = String.join("\n", command1);
    InputStream in = new ByteArrayInputStream(commands.getBytes());

    Client mockClient = Mockito.mock(Client.class);
    ClientRunner subjectUnderTest = new ClientRunner(mockClient, in, outMock, errMock);

    // set up expects
    Mockito.when(mockClient.initConnection()).thenReturn(NO_ERR);
    Mockito.when(mockClient.handleCommand(command1))
        .thenThrow(new ClientException("unit test exception"));

    // If close command is correctly received, should exit
    subjectUnderTest.run();

    Mockito.verify(mockClient).initConnection();
    Mockito.verify(mockClient).handleCommand(command1);
    Mockito.verify(errMock, Mockito.times(2))
        .println((String)Mockito.any());
  }


}
