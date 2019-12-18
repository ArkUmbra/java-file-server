package com.arkumbra.fileserver;

import com.arkumbra.fileserver.file.FileFetcher;
import com.arkumbra.fileserver.server.Server;
import com.arkumbra.fileserver.server.ServerFactory;
import java.io.IOException;
import org.junit.Test;
import org.mockito.Mockito;

public class ServerRunnerTest {

  @Test
  public void testInitialization() {
    String baseDir = "test";
    String ext = "test2";
    new ServerRunner(baseDir, ext);
  }

  @Test(expected = RunnerException.class)
  public void testInitializationWithBadParametersThrowsException() {
    new ServerRunner(/* No arguments */);
  }

  @Test
  public void testPostInitLaunch() throws IOException {
    FileFetcher mockFileFetcher = Mockito.mock(FileFetcher.class);
    ServerFactory mockServerFactory = Mockito.mock(ServerFactory.class);
    Server mockServer = Mockito.mock(Server.class);

    // Return dummy server just for testing
    Mockito.when(mockServerFactory.createServer(mockFileFetcher)).thenReturn(mockServer);

    ServerRunner runner = new ServerRunner(mockFileFetcher, mockServerFactory);
    runner.launch();
  }

  @Test(expected = RunnerException.class)
  public void testProgramQuitsWhenExceptionDuringLaunch() throws IOException {
    FileFetcher mockFileFetcher = Mockito.mock(FileFetcher.class);
    ServerFactory mockServerFactory = Mockito.mock(ServerFactory.class);

    // Return dummy server just for testing
    Mockito.when(mockServerFactory.createServer(mockFileFetcher))
        .thenThrow(new IOException("unit test error"));

    ServerRunner runner = new ServerRunner(mockFileFetcher, mockServerFactory);
    runner.launch();
  }



}
