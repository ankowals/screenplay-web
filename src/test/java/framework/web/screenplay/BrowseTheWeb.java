package framework.web.screenplay;

import org.openqa.selenium.WebDriver;
import framework.web.pom.page.PageFactory;
import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public record BrowseTheWeb(WebDriver driver) implements Ability {
    public static BrowseTheWeb with(WebDriver driver) {
        return new BrowseTheWeb(driver);
    }
    public static PageFactory as(Actor actor) { return new PageFactory(actor.usingAbilityTo(BrowseTheWeb.class).driver()); }
}
