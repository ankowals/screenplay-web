package screenplay;

import org.openqa.selenium.WebDriver;
import framework.web.pom.page.PageFactory;
import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public class BrowseTheWeb implements Ability {

    private final WebDriver driver;

    public BrowseTheWeb(WebDriver driver) {
        this.driver = driver;
    }

    public static BrowseTheWeb with(WebDriver driver) {
        return new BrowseTheWeb(driver);
    }
    public static PageFactory as(Actor actor) { return new PageFactory(actor.using(BrowseTheWeb.class).getDriver()); }
    public WebDriver getDriver() {
        return driver;
    }
}
