package framework.web.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

class MediaAttacher {

  private final MediaFinder mediaFinder;

  MediaAttacher(File report) {
    this.mediaFinder = new MediaFinder(report.getParentFile());
  }

  void attachMedia(Map<FqdnTestName, ExtentTest> testStatusMap) {
    List<File> screenshots = this.mediaFinder.getScreenshots();
    List<File> videos = this.mediaFinder.getVideos();

    testStatusMap.forEach(
        (fqdnTestName, extentTest) -> {
          screenshots.stream()
              .filter(this.fileNamePrefixEquals(fqdnTestName.getMethodName()))
              .max(Comparator.comparingLong(File::lastModified))
              .ifPresent(
                  screenshot -> extentTest.addScreenCaptureFromPath("./" + screenshot.getName()));

          videos.stream()
              .filter(this.fileNamePrefixEquals(fqdnTestName.getMethodName()))
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
                    .filter(this.fileNamePrefixEquals(fqdnTestName.getMethodName()))
                    .max(Comparator.comparingLong(File::lastModified))
                    .ifPresent(
                        screenshot ->
                            extentTest.addScreenCaptureFromPath(
                                "./"
                                    + maybeMediaDirForTestClass.get().getName()
                                    + "/"
                                    + screenshot.getName()));

                List<File> videos = this.mediaFinder.getVideos(dir);

                videos.stream()
                    .filter(this.fileNamePrefixEquals(fqdnTestName.getMethodName()))
                    .max(Comparator.comparingLong(File::lastModified))
                    .ifPresent(
                        video -> {
                          switch (extentTest.getStatus()) {
                            case FAIL:
                              extentTest.fail(
                                  MarkupHelper.toTable(
                                      new MyVideo(
                                          "./"
                                              + maybeMediaDirForTestClass.get().getName()
                                              + "/"
                                              + video.getName())));
                              break;
                            case PASS:
                              extentTest.pass(
                                  MarkupHelper.toTable(
                                      new MyVideo(
                                          "./"
                                              + maybeMediaDirForTestClass.get().getName()
                                              + "/"
                                              + video.getName())));
                              break;
                          }
                        });
              });
        });
  }

  private Predicate<File> fileNamePrefixEquals(String expectedPrefix) {
    return file -> {
      Optional<String> maybeValidInfix =
          Stream.of("_arg0_", "_driver_")
              .filter(infix -> file.getName().contains(infix))
              .findFirst();

      return maybeValidInfix
          .filter(
              infix ->
                  file.getName().substring(0, file.getName().indexOf(infix)).equals(expectedPrefix))
          .isPresent();
    };
  }
}
