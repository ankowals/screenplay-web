package framework.web.accessibility;

class HtmlTemplateProvider {

  String provide(String title) {
    String cssStyle =
        """
                .wrap .wrapTwo .wrapThree { margin:2px; max-width: 70vw; }
                .wrapOne { margin-left: 1em; overflow-wrap: anywhere; }
                .wrapTwo { margin-left: 2em; overflow-wrap: anywhere; }
                .wrapThree { margin-left: 3em; overflow-wrap: anywhere; }
                .emOne { margin-left: 1em; overflow-wrap: anywhere; }
                .emTwo { margin-left: 2em; overflow-wrap: anywhere; }
                .emThree { margin-left: 3em; overflow-wrap: anywhere; }
                .majorSection { border: 1px solid black; }
                .findings { border-top: 1px solid black; }
                .htmlTable { border-top: double lightgray; width: 100%; display: table; }
                """;

    String html =
        """
                <!DOCTYPE html>
                    <html lang="en">
                        <head>
                            <meta charset="utf-8">
                            <title>Accessibility test report %s</title>
                            <style>%s</style>
                        </head>
                        <body></body>
                    </html>
                """;

    return html.formatted(title, cssStyle);
  }
}
