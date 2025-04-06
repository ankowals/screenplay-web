package framework.web.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

class MediaAttacher {

  private final MediaFinder mediaFinder;

  MediaAttacher(File report) {
    this.mediaFinder = new MediaFinder(report.getParentFile());
  }

  void attachMedia(Map<FqdnTestName, ExtentTest> testStatusMap) {
    List<File> screenshots = this.mediaFinder.getScreenshots();
    List<File> videos = this.mediaFinder.getVideos();
    List<File> txtFiles = this.mediaFinder.getTextFiles();

    testStatusMap.forEach(
        (fqdnTestName, extentTest) -> {
          screenshots.stream()
              .filter(this.startsWith(fqdnTestName.getMethodName()))
              .forEach(
                  screenshot ->
                      extentTest.addScreenCaptureFromPath(
                          "./" + screenshot.getName(), screenshot.getName()));

          videos.stream()
              .filter(this.startsWith(fqdnTestName.getMethodName()))
              .max(Comparator.comparingLong(File::lastModified))
              .ifPresent(
                  video -> {
                    switch (extentTest.getStatus()) {
                      case FAIL:
                        extentTest.fail(MarkupHelper.toTable(new MyVideo("./" + video.getName())));
                        break;
                      case PASS:
                        extentTest.pass(MarkupHelper.toTable(new MyVideo("./" + video.getName())));
                    }
                  });

          txtFiles.stream()
              .filter(this.startsWith(fqdnTestName.getMethodName()))
              .forEach(
                  txtFile -> {
                    switch (extentTest.getStatus()) {
                      case FAIL:
                        extentTest.fail(
                            String.format(
                                "<a href='./%s'>click to view %s</a>",
                                txtFile.getName(), txtFile.getName()));
                        break;
                      case PASS:
                        extentTest.pass(
                            String.format(
                                "<a href='./%s'>click to view %s</a>",
                                txtFile.getName(), txtFile.getName()));
                    }
                  });
        });
  }

  void attachNestedMedia(Map<FqdnTestName, ExtentTest> testStatusMap) {
    List<File> dirs = this.mediaFinder.getMediaDirs();

    testStatusMap.forEach(
        (fqdnTestName, extentTest) -> {
          Optional<File> maybeMediaDirForTestClass =
              dirs.stream()
                  .filter(dir -> fqdnTestName.getClassName().equals(dir.getName()))
                  .findFirst();

          maybeMediaDirForTestClass.ifPresent(
              dir -> {
                List<File> screenshots = this.mediaFinder.getScreenshots(dir);

                screenshots.stream()
                    .filter(this.startsWith(fqdnTestName.getMethodName()))
                    .forEach(
                        screenshot ->
                            extentTest.addScreenCaptureFromPath(
                                String.format(
                                    "./%s/%s",
                                    maybeMediaDirForTestClass.get().getName(),
                                    screenshot.getName()),
                                screenshot.getName()));

                List<File> videos = this.mediaFinder.getVideos(dir);

                videos.stream()
                    .filter(this.startsWith(fqdnTestName.getMethodName()))
                    .max(Comparator.comparingLong(File::lastModified))
                    .ifPresent(
                        video -> {
                          switch (extentTest.getStatus()) {
                            case FAIL:
                              extentTest.fail(
                                  MarkupHelper.toTable(
                                      new MyVideo(
                                          String.format(
                                              "./%s/%s",
                                              maybeMediaDirForTestClass.get().getName(),
                                              video.getName()))));
                              break;
                            case PASS:
                              extentTest.pass(
                                  MarkupHelper.toTable(
                                      new MyVideo(
                                          String.format(
                                              "./%s/%s",
                                              maybeMediaDirForTestClass.get().getName(),
                                              video.getName()))));
                              break;
                          }
                        });

                List<File> txtFiles = this.mediaFinder.getTextFiles(dir);

                txtFiles.stream()
                    .filter(this.startsWith(fqdnTestName.getMethodName()))
                    .forEach(
                        txtFile -> {
                          switch (extentTest.getStatus()) {
                            case FAIL:
                              extentTest.fail(
                                  String.format(
                                      "<a href='./%s/%s'>click to view %s</a>",
                                      maybeMediaDirForTestClass.get().getName(),
                                      txtFile.getName(),
                                      txtFile.getName()));
                              break;
                            case PASS:
                              extentTest.pass(
                                  String.format(
                                      "<a href='./%s/%s'>click to view %s</a>",
                                      maybeMediaDirForTestClass.get().getName(),
                                      txtFile.getName(),
                                      txtFile.getName()));
                              break;
                          }
                        });
              });
        });
  }

  private Predicate<File> startsWith(String expectedPrefix) {
    List<String> patterns =
        List.of(expectedPrefix + "_arg0_", expectedPrefix + "_driver_", expectedPrefix + "-");

    return file -> patterns.stream().anyMatch(p -> file.getName().startsWith(p));
  }
}
