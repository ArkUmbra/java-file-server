package com.luke.fileserver.file;

//import static com.luke.fileserver.file.FileFetcherCollector.toFileNames;

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

public class FileFetcher {

  private Predicate<Path> filterByExtension = FileFetcherFilter.byExtension(".txt");
  private FileFetcherCollector collectByFileNames = new FileFetcherCollector();


  private final Path path;

  public FileFetcher(String baseDir) {
    path = Path.of(baseDir);
  }

  public Collection<String> listAllFiles() throws IOException {
    List<String> filtered = Files.list(path)
        //.filter(FileFetcherFilter.byExtension(".txt"))
        .filter(filterByExtension)
//        .collect(toFileNames());
        .collect(collectByFileNames);

    return filtered;
  }

  public String get(String filename) throws IOException {
    byte[] contents = Files.readAllBytes(Path.of(path.toString() + "/" + filename));
    return new String(contents, Charset.forName("UTF-8"));
  }

}

class FileFetcherFilter /*implements Predicate<Path>*/ {

  public static Predicate<Path> byExtension(String extension) {
    return p -> p.toFile().getName().endsWith(extension);//p.getFileName().endsWith(extension);//p.endsWith(extension);//p.getFileName().endsWith(extension);
  }

//  @Override
//  public boolean test(Path path) {
//    return path.toFile().getName().endsWith(".txt");
//  }
}

class FileFetcherCollector implements Collector<Path, List<String>, List<String>> {

  @Override
  public Supplier<List<String>> supplier() {
    return ArrayList::new;
  }

  @Override
  public BiConsumer<List<String>, Path> accumulator() {
    return (list, path) -> {
      list.add(path.getFileName().toString());
    };
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

//  public static FileFetcherCollector toFileNames() {
//    return new FileFetcherCollector();
//  }
//  public static Collect

//  public static <T> Collector<T, ?, List<T>> toFileNames() {
//    return new CollectorImpl<>((Supplier<List<T>>) ArrayList::new, List::add,
//        (left, right) -> { left.addAll(right); return left; },
//        CH_ID);

}

