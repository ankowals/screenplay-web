package com.github.ankowals.framework.web.wdm.session;

import com.github.ankowals.framework.web.wdm.session.storage.LocalStorage;
import com.github.ankowals.framework.web.wdm.session.storage.SessionStorage;

public record Session(Cookies cookies, LocalStorage localStorage, SessionStorage sessionStorage) {}
