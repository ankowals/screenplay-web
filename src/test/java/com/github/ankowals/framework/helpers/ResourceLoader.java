package com.github.ankowals.framework.helpers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class ResourceLoader {

  public String asString(String path) throws IOException {
    try (InputStream inputStream = this.toInputStream(path);
        InputStreamReader inputStreamReader =
            new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }

  public File asFile(String path) throws IOException {
    File tmp = Files.createTempFile("", ".tmp").toFile();
    tmp.deleteOnExit();

    // preserve original file name
    Path targetPath =
        tmp.toPath()
            .resolveSibling(
                "%s.%s"
                    .formatted(FilenameUtils.getBaseName(path), FilenameUtils.getExtension(path)));

    File target = targetPath.toFile();
    target.deleteOnExit();

    Files.move(tmp.toPath(), target.toPath());

    try (InputStream inputStream = this.toInputStream(path);
        OutputStream outputStream = new FileOutputStream(target, false)) {
      inputStream.transferTo(outputStream);

      return target;
    }
  }

  public byte[] asBytes(String path) throws IOException {
    try (InputStream inputStream = this.toInputStream(path)) {
      return IOUtils.toByteArray(inputStream);
    }
  }

  private InputStream toInputStream(String path) throws FileNotFoundException {
    InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(path);

    if (inputStream == null) {
      throw new FileNotFoundException(String.format("Resource file '%s' not found!", path));
    }

    return inputStream;
  }
}
