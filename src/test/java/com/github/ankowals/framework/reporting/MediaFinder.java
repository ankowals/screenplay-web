package com.github.ankowals.framework.reporting;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MediaFinder {

  private final List<File> files;

  MediaFinder(File parentDir) {
    this.files = Arrays.stream(Objects.requireNonNull(parentDir.listFiles())).toList();
  }

  // directory per fqdn test class name
  List<File> getMediaDirs() {
    return this.files.stream().filter(File::isDirectory).toList();
  }

  List<File> getScreenshots(File dir) {
    return this.doGetScreenshots(this.doListFiles(dir));
  }

  List<File> getScreenshots() {
    return this.doGetScreenshots(this.files);
  }

  List<File> getVideos(File dir) {
    return this.doGetVideos(this.doListFiles(dir));
  }

  List<File> getVideos() {
    return this.doGetVideos(this.files);
  }

  List<File> getTextFiles(File dir) {
    return this.doGetTextFiles(this.doListFiles(dir));
  }

  List<File> getTextFiles() {
    return this.doGetTextFiles(this.files);
  }

  List<File> getArchives(File dir) {
    return this.doGetArchives(this.doListFiles(dir));
  }

  List<File> getArchives() {
    return this.doGetArchives(this.files);
  }

  private List<File> doGetScreenshots(List<File> files) {
    return files.stream()
        .filter(File::isFile)
        .filter(file -> file.getName().endsWith(".png"))
        .toList();
  }

  private List<File> doGetVideos(List<File> files) {
    return files.stream()
        .filter(File::isFile)
        .filter(
            file -> {
              String name = file.getName();
              return name.endsWith(".mp4") || name.endsWith(".flv") || name.endsWith(".webm");
            })
        .toList();
  }

  private List<File> doGetTextFiles(List<File> files) {
    return files.stream()
        .filter(File::isFile)
        .filter(
            file -> {
              String name = file.getName();
              return name.endsWith(".txt")
                  || name.endsWith(".html")
                  || name.endsWith(".json")
                  || name.endsWith(".yaml")
                  || name.endsWith(".yml")
                  || name.endsWith(".log");
            })
        .toList();
  }

  private List<File> doGetArchives(List<File> files) {
    return files.stream()
        .filter(File::isFile)
        .filter(file -> file.getName().endsWith(".zip"))
        .toList();
  }

  private List<File> doListFiles(File dir) {
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("Not a directory: " + dir);
    }

    return Arrays.stream(Objects.requireNonNull(dir.listFiles())).toList();
  }
}
