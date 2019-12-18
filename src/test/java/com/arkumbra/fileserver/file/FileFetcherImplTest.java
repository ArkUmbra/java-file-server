package com.arkumbra.fileserver.file;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileFetcherImplTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private Random random;
  private FileFetcher subjectUnderTest;


  @Before
  public void setUp() {
    this.random = new Random();

    String fileExtension = ".txt";
    this.subjectUnderTest = new FileFetcherImpl(folder.getRoot().getAbsolutePath(), fileExtension);
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
    FileFetcher ff = new FileFetcherImpl("/Users/luke/scrapbook/java-file-server", ".txt");
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
