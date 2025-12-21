package framework.web.wdm.session;

public record Session(Cookies cookies, LocalStorage localStorage, SessionStorage sessionStorage) {}
