package framework.web.assertions.visual;

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
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;

/*
Always run visual tests in container & grab reference screenshots in container.
Otherwise, expect size mismatch errors.
*/

// or use https://github.com/Visual-Regression-Tracker/Visual-Regression-Tracker
// or use https://github.com/ngoanh2n/image-comparator
public class VisualAssert extends AbstractAssert<VisualAssert, File> {

  private final ResourceLoader resourceLoader;
  private final List<Rectangle> excludedRectangles = new ArrayList<>();

  public VisualAssert(File actual) {
    super(actual, VisualAssert.class);
    this.resourceLoader = new ResourceLoader();
  }

  public static VisualAssert assertThat(File actualFile) {
    return new VisualAssert(actualFile);
  }

  public VisualAssert excluding(Rectangle... rectangles) {
    this.excludedRectangles.addAll(new ArrayList<>(List.of(rectangles)));
    return this;
  }

  public VisualAssert excluding(WebElement... webElements) {
    return this.excluding(List.of(webElements));
  }

  public VisualAssert excluding(List<WebElement> webElements) {
    this.excludedRectangles.addAll(this.toRectangles(webElements));
    return this;
  }

  public VisualAssert isEqualTo(String resource) {
    if (!Boolean.parseBoolean(System.getenv("VISUAL_ASSERTIONS_ENABLED"))) {
      return this;
    }

    File diffFile =
        new File(this.actual.toPath().toAbsolutePath().toString().replace(".png", "-diff.png"));

    File expectedFile;
    BufferedImage expectedImage;
    BufferedImage actualImage;

    try {
      expectedFile = this.resourceLoader.asFile(resource);
      expectedImage = ImageIO.read(expectedFile);
      actualImage = ImageIO.read(this.actual);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ImageComparisonResult imageComparisonResult =
        this.doCreateImageComparison(expectedImage, actualImage, diffFile).compareImages();

    if (Boolean.parseBoolean(System.getenv("VISUAL_ASSERTIONS_REPLACE_SCREENSHOTS"))) {
      Path resourcePath = Paths.get(new File("src/test/resources").getAbsolutePath(), resource);

      try {
        Files.copy(this.actual.toPath(), resourcePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    Assertions.assertThat(imageComparisonResult.getImageComparisonState())
        .as(
            "Comparing actual image %s of size %s x %s"
                    .formatted(
                        this.actual.getName(), actualImage.getWidth(), actualImage.getHeight())
                + " with expected image %s of size %s x %s"
                    .formatted(
                        expectedFile.getName(),
                        expectedImage.getWidth(),
                        expectedImage.getHeight()))
        .isEqualTo(ImageComparisonState.MATCH);

    // if comparison successful remove diff image
    try {
      Files.deleteIfExists(diffFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return this;
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
