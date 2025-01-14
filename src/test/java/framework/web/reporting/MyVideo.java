package framework.web.reporting;

import java.util.Collections;
import java.util.List;

class MyVideo {

  private final List<String> video;

  MyVideo(String path) {
    String css = "width: 100% !important; height: auto !important;";
    this.video =
        Collections.singletonList(
            String.format("<video style='%s' src='%s' controls></video>", css, path));
  }
}
