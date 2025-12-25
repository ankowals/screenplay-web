package framework.web.wdm.session;

import framework.web.wdm.session.storage.LocalStorage;
import framework.web.wdm.session.storage.SessionStorage;

public record Session(Cookies cookies, LocalStorage localStorage, SessionStorage sessionStorage) {}
