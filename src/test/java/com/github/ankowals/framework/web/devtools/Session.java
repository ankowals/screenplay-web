package com.github.ankowals.framework.web.devtools;

import com.github.ankowals.framework.web.storage.LocalStorage;
import com.github.ankowals.framework.web.storage.SessionStorage;
import java.util.List;
import org.openqa.selenium.devtools.latest.network.model.Cookie;

public record Session(
    List<Cookie> cookies, LocalStorage localStorage, SessionStorage sessionStorage) {}
