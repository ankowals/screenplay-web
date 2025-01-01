package framework.web.reporting;

import java.util.Collections;
import java.util.List;

class MyVideo {

  private final List<String> video;

  MyVideo(String path) {
    String css = "width: 100% !important; height: auto !important;";
    this.video =
        Collections.singletonList(
            "<video style=\"" + css + "\" src=\"" + path + "\" controls></video>");
  }
}
