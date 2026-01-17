package com.github.ankowals.domain.automationpractice.pom.views;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import com.github.ankowals.framework.web.pom.elements.*;
import com.github.ankowals.framework.web.pom.elements.common.ButtonImpl;
import com.github.ankowals.framework.web.pom.elements.common.InputImpl;
import com.github.ankowals.framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomeView extends BaseView {

  public HomeView(WebDriver driver) {
    super(driver);
  }

  public Button signInButton() {
    WebElement element = this.wait.until(elementToBeClickable(By.xpath("//a[@class='login']")));
    return ButtonImpl.of(element);
  }

  public Button searchButton() {
    WebElement element =
        this.wait.until(elementToBeClickable(By.xpath("//button[@name='submit_search']")));
    return ButtonImpl.of(element);
  }

  public Input searchInput() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//input[@id='search_query_top']")));
    return InputImpl.of(element);
  }
}
