package com.arkumbra.fileserver.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class FileFetcherImpl implements FileFetcher {

  private final Predicate<Path> filterByExtension;
  private final FileFetcherCollector collectByFileNames = new FileFetcherCollector();

  private final Path path;

  public FileFetcherImpl(String baseDir, String supportedExtension) {
    this.filterByExtension = FileFetcherFilter.byExtension(supportedExtension);
    this.path = Path.of(baseDir);

    System.out.println("Serving " + supportedExtension + " documents from " + baseDir);
  }

  /**
   *
   * @return list of file names in the server directory which match the predefined file extension
   * @throws IOException error listing files
   */
  public synchronized Collection<String> listAllFiles() throws IOException {
    return Files.list(path)
        .filter(filterByExtension)
        .collect(collectByFileNames);
  }

  /**
   *
   * @param filename whose content is requested
   * @return Contents of file as string
   * @throws IOException in case where reading of file has failed
   */
  public synchronized String get(String filename) throws IOException {
    byte[] contents = Files.readAllBytes(Path.of(path.toString() + "/" + filename));
    return new String(contents, Charset.forName("UTF-8"));
  }

}


/** Filter file list */
class FileFetcherFilter {
  public static Predicate<Path> byExtension(String extension) {
    return p -> p.toFile().getName().endsWith(extension);
  }
}

/**
 * Collect list of file objects into list of file names
 */
class FileFetcherCollector implements Collector<Path, List<String>, List<String>> {

  @Override
  public Supplier<List<String>> supplier() {
    return ArrayList::new;
  }

  @Override
  public BiConsumer<List<String>, Path> accumulator() {
    return (list, path) -> list.add(path.getFileName().toString());
  }

  @Override
  public BinaryOperator<List<String>> combiner() {
    return (left, right) -> {
      left.addAll(right);
      return left;
    };
  }

  @Override
  public Function<List<String>, List<String>> finisher() {
    return (list) -> list;
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Collections.emptySet();
  }
}

