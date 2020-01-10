package controllers;

import models.User;
import play.cache.Cache;
import service.user.AuthUserInfo;

public class Authenticate extends Controller {
    public static void login() {
        render();
    }

    public static void loginWithGoogle(String code) {
        AuthUserInfo userInfo = authService.loginWithGoogle(code);
        User user = userService.login(userInfo);
        Security.CurrentUser.set(user);
        Application.index();
    }

    public static void loginWithFacebook(String code) {
        AuthUserInfo userInfo = authService.loginWithFacebook(code);
        User user = userService.login(userInfo);
        Security.CurrentUser.set(user);
        Application.index();
    }

    public static void logout() {
        Cache.delete(session.getId());
        session.clear();
        Application.index();
    }
}
