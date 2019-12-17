package com.arkumbra.fileserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class EndToEndTest {

  private static final String ext = ".txt";

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private ClientRunner clientRunner;
  private ServerRunner serverRunner;

  @Before
  public void setUp() throws Exception {
//    this.serverRunner = new ServerRunner();
//    serverRunner.launch(folder.getRoot().getAbsolutePath(), ext);
//
//    this.clientRunner = new ClientRunner();
//    clientRunner.run();
  }

  // TODO change test to use the socket classes, not runners
  @Test
  public void test() throws InterruptedException {
    ServerRunnable server = new ServerRunnable(folder.getRoot().getAbsolutePath(), ext);
    new Thread(server).start();

    Thread.sleep(2000);

    TestReader reader = new TestReader();

    ClientRunnable client = new ClientRunnable(reader, System.out, System.err);
    new Thread(client).start();
    reader.append("index");


    Thread.sleep(1000);
    System.out.println("test");
  }

}

class ClientRunnable implements Runnable {
  private ClientRunner clientRunner;

  /* Pass in input stream so that client can be controlled */
  public ClientRunnable(TestReader testReader, PrintStream out, PrintStream err) {
    this.clientRunner = new ClientRunner(testReader, out, err);
  }

  @Override
  public void run() {
    clientRunner.run();
  }
}

class ServerRunnable implements Runnable {
  private String path;
  private String ext;

  public ServerRunnable(String path, String ext) {
    this.path = path;
    this.ext = ext;
  }

  @Override
  public void run() {
//    this.serverRunner = new ServerRunner();
//    while (true)
    new ServerRunner().launch(path, ext);
  }
}

/** Custom instance so that we can override nextLine() */
//class TestScanner extends Scanner {
//  private final Queue<String> queue = new ArrayDeque<>();
//
//  public TestScanner() {
//    super(System.in);
//  }
//
//  public void append(String line) {
//    synchronized (queue) {
//      queue.add(line);
//    }
//  }
//
//  @Override
//  public String nextLine() {
//    while (true) {
//      synchronized (queue) {
//        if (! queue.isEmpty()) {
//          return queue.poll();
//        }
//      }
//
//      // sleep until something is sent in
//      try {
//        Thread.sleep(100);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }
//}

class TestReader extends BufferedReader {

  private final Queue<String> queue = new ArrayDeque<>();

  public TestReader() {
    super(Reader.nullReader());
  }

  public void append(String line) {
    synchronized (queue) {
      queue.add(line);
    }
  }

  @Override
  public String readLine() {
    while (true) {
      synchronized (queue) {
        if (!queue.isEmpty()) {
          String item =  queue.poll();
          System.out.println(item);
          return item;
        }
      }

      // sleep until something is sent in
      try {
        System.out.println("Blocking");
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
