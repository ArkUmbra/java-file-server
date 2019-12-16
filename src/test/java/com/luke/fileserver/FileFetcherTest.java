package com.luke.fileserver;

import static org.junit.Assert.assertEquals;

import com.luke.fileserver.file.FileFetcher;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileFetcherTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private Random random;
  private FileFetcher subjectUnderTest;


  @Before
  public void setUp() {
    this.random = new Random();
    // TODO create random dir in tmp
    // TODO clean up
    this.subjectUnderTest = new FileFetcher(folder.getRoot().getAbsolutePath());
  }

  @Test
  public void testListAllFiles() throws IOException {
    int matchingFiles = random.nextInt(25);
    int nonMatchingFiles = random.nextInt(25);
    createRandomFiles(".txt", matchingFiles);
    createRandomFiles(".txt2", nonMatchingFiles);


    Collection<String> files = subjectUnderTest.listAllFiles();
    System.out.println(files);
    assertEquals(matchingFiles, files.size());
  }

  @Test
  public void testGetFile() throws IOException {
    FileFetcher ff = new FileFetcher("/Users/luke/scrapbook/java-file-server");
    String fileContent = ff.get("test.txt");
    System.out.println(fileContent);
  }

  private void createRandomFiles(String ext, int numberOfFiles) throws IOException {
    for (int i = 0; i < numberOfFiles; i++) {
      String filename = "unittest" + random.nextInt() + ext;
      folder.newFile(filename);
    }
  }

}
