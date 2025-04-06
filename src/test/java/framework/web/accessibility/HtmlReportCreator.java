package framework.web.accessibility;

import com.deque.html.axecore.results.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

// based on
// https://github.com/jonreding2010/axe-core-maven-html/tree/HTML_Reporter/src/main/java/com/deque/html/axecore/selenium

public class HtmlReportCreator {

  private final Results results;
  private final ResultsCounter resultsCounter;
  private final HtmlTemplateProvider htmlTemplateProvider;

  public HtmlReportCreator(Results results) {
    this.results = results;
    this.resultsCounter = new ResultsCounter(this.results);
    this.htmlTemplateProvider = new HtmlTemplateProvider();
  }

  public void create(String destination, String title) throws IOException, ParseException {
    Document doc = Jsoup.parse(this.htmlTemplateProvider.provide(title));
    Element body = doc.body();

    body.appendChild(new Element("h1").text("Accessibility test report " + title));
    body.appendChild(new Element("h3").text("Test environment"));
    body.appendChild(this.createTestEnvironmentDiv(this.results));
    body.appendChild(new Element("h3").appendText("Summary:"));
    body.appendChild(this.createSummaryDiv(this.resultsCounter));

    if (this.results.isErrored()) {
      body.appendChild(new Element("h2").appendText("SCAN ERRORS:"));
      body.appendChild(this.createErrorDiv(this.results));
    }

    body.appendChild(new Element("br"));
    body.appendChild(new Element("br"));

    Element element = new Element("div");

    if (this.resultsCounter.countViolations() > 0) {
      element.appendChild(new Element("br"));
      element.appendChild(
          this.getReadableAxeResults(this.results.getViolations(), ResultType.Violations.name()));
    }

    if (this.resultsCounter.countIncomplete() > 0) {
      element.appendChild(new Element("br"));
      element.appendChild(
          this.getReadableAxeResults(this.results.getIncomplete(), ResultType.Incomplete.name()));
    }

    if (this.resultsCounter.countPasses() > 0) {
      element.appendChild(new Element("br"));
      element.appendChild(
          this.getReadableAxeResults(this.results.getPasses(), ResultType.Passes.name()));
    }

    if (this.resultsCounter.countInapplicable() > 0) {
      element.appendChild(new Element("br"));
      element.appendChild(
          this.getReadableAxeResults(
              this.results.getInapplicable(), ResultType.Inapplicable.name()));
    }

    body.appendChild(element);
    FileUtils.writeStringToFile(new File(destination), doc.outerHtml(), StandardCharsets.UTF_8);
  }

  private Element getReadableAxeResults(List<Rule> rules, String type) {
    Element sectionDiv = new Element("div");
    sectionDiv.attr("class", "majorSection").attr("id", type + "Section");

    sectionDiv.appendChild(new Element("h2").text(type));

    int loops = 1;

    for (Rule rule : rules) {
      Element findingsDiv = new Element("div");
      findingsDiv
          .attr("class", "findings")
          .appendText(String.format("%s:%s", loops++, rule.getHelp()));
      sectionDiv.appendChild(findingsDiv);
      findingsDiv.appendChild(this.createFindingDetailsDiv(rule));

      for (Node node : rule.getNodes()) {
        Element elementNodes = new Element("div");
        elementNodes.attr("class", "htmlTable");
        findingsDiv.appendChild(elementNodes);

        Element htmlAndSelectorWrapper = new Element("div");
        htmlAndSelectorWrapper
            .attr("class", "emThree")
            .text("Html:")
            .appendChild(new Element("br"));

        elementNodes.appendChild(htmlAndSelectorWrapper);

        Element htmlAndSelector =
            new Element("p").attr("class", "wrapOne").html(node.getHtml()).text(node.getHtml());
        htmlAndSelectorWrapper.appendChild(htmlAndSelector);
        htmlAndSelectorWrapper.appendText("Selector(s):");

        htmlAndSelector = new Element("p");
        htmlAndSelector.attr("class", "wrapTwo");

        for (Object target : Collections.singletonList(node.getTarget())) {
          String targetString = target.toString().replace("[", "").replace("]", "");
          htmlAndSelector.text(targetString).html(targetString);
        }

        htmlAndSelectorWrapper.appendChild(htmlAndSelector);
      }
    }

    return sectionDiv;
  }

  private String getDateFormat(String timestamp) throws ParseException {
    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(timestamp);
    return new SimpleDateFormat("dd-MMM-yy HH:mm:ss Z").format(date);
  }

  private Element createTestEnvironmentDiv(Results results) throws ParseException {
    Element element = new Element("div");
    element.attributes().put("class", "emOne").put("id", "reportContext");

    element.text("Url: " + results.getUrl()).appendChild(new Element("br"));
    element
        .appendText("Orientation: " + results.getTestEnvironment().getOrientationType())
        .appendChild(new Element("br"));

    element
        .appendText(
            String.format(
                "Size: %s x  %s",
                results.getTestEnvironment().getwindowWidth(),
                results.getTestEnvironment().getWindowHeight()))
        .appendChild(new Element("br"));

    element
        .appendText("Time: " + this.getDateFormat(results.getTimestamp()))
        .appendChild(new Element("br"));
    element
        .appendText("User agent: " + results.getTestEnvironment().getUserAgent())
        .appendChild(new Element("br"));

    element.appendText(
        String.format(
            "Test engine: %s (%s)",
            results.getTestEngine().getName(), results.getTestEngine().getVersion()));

    return element;
  }

  private Element createSummaryDiv(ResultsCounter resultsCounter) {
    Element element = new Element("div");
    element.attr("class", "emOne");

    element.text("Violation: " + resultsCounter.countViolations()).appendChild(new Element("br"));
    element
        .appendText("Incomplete: " + resultsCounter.countIncomplete())
        .appendChild(new Element("br"));
    element.appendText("Pass: " + resultsCounter.countPasses()).appendChild(new Element("br"));
    element.appendText("Inapplicable: " + resultsCounter.countInapplicable());

    return element;
  }

  private Element createErrorDiv(Results results) {
    Element element = new Element("div");
    element.attributes().put("id", "ErrorMessage");
    element.appendText(results.getErrorMessage());

    return element;
  }

  private Element createFindingDetailsDiv(Rule rule) {
    Element element = new Element("div");
    element.attr("class", "emTwo");

    element.text("Description: " + rule.getDescription()).appendChild(new Element("br"));
    element.appendText("Help: " + rule.getHelp()).appendChild(new Element("br"));

    element
        .appendText("Help URL: ")
        .appendChild(new Element("a").attr("href", rule.getHelpUrl()).text(rule.getHelpUrl()))
        .appendChild(new Element("br"));

    if (!rule.getImpact().isEmpty()) {
      element.appendText("Impact: " + rule.getImpact()).appendChild(new Element("br"));
    }

    element
        .appendText("Tags: ")
        .append(String.join(", ", rule.getTags()))
        .appendChild(new Element("br"));

    if (!rule.getNodes().isEmpty()) {
      element.appendText("Element(s):").appendChild(new Element("br"));
    }

    return element;
  }
}
