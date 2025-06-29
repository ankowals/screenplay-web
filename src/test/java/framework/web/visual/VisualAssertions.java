package framework.web.visual;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.github.romankh3.image.comparison.model.Rectangle;
import framework.helpers.ResourceLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;

/*
Always run visual tests in container & grab reference screenshots in container.
Otherwise, expect size mismatch errors.
*/

// or use https://github.com/Visual-Regression-Tracker/Visual-Regression-Tracker
// or use https://github.com/ngoanh2n/image-comparator
public class VisualAssertions {

  private final File actualFile;
  private final ResourceLoader resourceLoader;
  private List<Rectangle> excludedRectangles = new ArrayList<>();
  private List<WebElement> excludedWebElements = new ArrayList<>();

  private VisualAssertions(File actualFile) {
    this.actualFile = actualFile;
    this.resourceLoader = new ResourceLoader();
  }

  public static VisualAssertions assertThat(File actualFile) {
    return new VisualAssertions(actualFile);
  }

  public VisualAssertions excluding(Rectangle... rectangles) {
    this.excludedRectangles = new ArrayList<>(List.of(rectangles));
    return this;
  }

  // may not work well when scaling factor in used
  public VisualAssertions excluding(WebElement... webElements) {
    return this.excluding(List.of(webElements));
  }

  public VisualAssertions excluding(List<WebElement> webElements) {
    this.excludedWebElements = webElements;
    return this;
  }

  public void isEqualTo(String resource) throws IOException {
    if (!Boolean.parseBoolean(System.getenv("VISUAL_ASSERTIONS_ENABLED"))) {
      return;
    }

    this.excludedRectangles.addAll(this.toRectangles(this.excludedWebElements));

    File expectedFile = this.resourceLoader.asFile(resource);
    File diffFile =
        new File(this.actualFile.toPath().toAbsolutePath().toString().replace(".png", "-diff.png"));

    BufferedImage expectedImage = ImageIO.read(expectedFile);
    BufferedImage actualImage = ImageIO.read(this.actualFile);

    ImageComparisonResult imageComparisonResult =
        this.doCreateImageComparison(expectedImage, actualImage, diffFile).compareImages();

    if (Boolean.parseBoolean(System.getenv("VISUAL_ASSERTIONS_REPLACE_SCREENSHOTS"))) {
      Path resourcePath = Paths.get(new File("src/test/resources").getAbsolutePath(), resource);

      Files.copy(this.actualFile.toPath(), resourcePath, StandardCopyOption.REPLACE_EXISTING);
    }

    Assertions.assertThat(imageComparisonResult.getImageComparisonState())
        .as(
            "Comparing actual image %s of size %s x %s"
                    .formatted(
                        this.actualFile.getName(), actualImage.getWidth(), actualImage.getHeight())
                + " with expected image %s of size %s x %s"
                    .formatted(
                        expectedFile.getName(),
                        expectedImage.getWidth(),
                        expectedImage.getHeight()))
        .isEqualTo(ImageComparisonState.MATCH);

    // if comparison successful remove diff image
    Files.deleteIfExists(diffFile.toPath());
  }

  private ImageComparison doCreateImageComparison(
      BufferedImage expectedImage, BufferedImage actualImage, File diffFile) {
    return new ImageComparison(expectedImage, actualImage)
        .setDestination(diffFile)
        .setExcludedAreas(this.excludedRectangles)
        .setExcludedRectangleFilling(true, 50)
        .setDrawExcludedRectangles(true)
        .setDifferenceRectangleFilling(true, 50)
        .setPixelToleranceLevel(0.1F);
  }

  private List<Rectangle> toRectangles(List<WebElement> webElements) {
    return webElements.stream()
        .map(
            webElement -> {
              org.openqa.selenium.Rectangle r = webElement.getRect();

              return new Rectangle(
                  r.getX(),
                  r.getY(),
                  r.getX() + r.getDimension().getWidth(),
                  r.getY() + r.getDimension().getHeight());
            })
        .toList();
  }
}
