package framework.web.reporting;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class MediaFinder {

  private final File dir;

  MediaFinder(File parentDir) {
    this.dir = parentDir;
  }

  List<File> getScreenshots(File dir) {
    return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
        .filter(file -> file.getName().endsWith(".png"))
        .toList();
  }

  List<File> getScreenshots() {
    return this.getScreenshots(this.dir);
  }

  List<File> getVideos(File dir) {
    return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
        .filter(
            file ->
                file.getName().endsWith(".mp4")
                    || file.getName().endsWith(".flv")
                    || file.getName().endsWith(".webm"))
        .toList();
  }

  List<File> getVideos() {
    return this.getVideos(this.dir);
  }

  // directory per fqdn test class name
  List<File> getMediaDirs() {
    return Arrays.stream(Objects.requireNonNull(this.dir.listFiles()))
        .filter(File::isDirectory)
        .toList();
  }

  List<File> getTextFiles(File dir) {
    return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
        .filter(
            file ->
                file.getName().endsWith(".txt")
                    || file.getName().endsWith(".html")
                    || file.getName().endsWith(".json")
                    || file.getName().endsWith(".yaml"))
        .toList();
  }

  List<File> getTextFiles() {
    return this.getTextFiles(this.dir);
  }
}
