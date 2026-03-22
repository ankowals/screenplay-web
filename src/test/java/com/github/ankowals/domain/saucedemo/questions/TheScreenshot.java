package com.github.ankowals.domain.saucedemo.questions;

import com.github.ankowals.abilities.BrowseTheWeb;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TheScreenshot {
  public static Question<File> storedUnder(Path path) {
    return actor -> {
      WebDriver webDriver = UseAbility.of(actor).to(BrowseTheWeb.class).driver();
      byte[] bytes = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);

      return Files.write(path, bytes).toFile();
    };
  }
}
