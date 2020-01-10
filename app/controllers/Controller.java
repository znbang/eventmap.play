package controllers;

import play.mvc.Before;
import service.user.AuthService;
import service.user.UserService;

public class Controller extends play.mvc.Controller {
    static final AuthService authService = new AuthService();
    static final UserService userService = new UserService();

    @Before
    static void initCurrentUser() {
        renderArgs.put("currentUser", Security.CurrentUser.get());
    }
}
