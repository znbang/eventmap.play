package controllers;

import models.User;
import play.cache.Cache;
import play.mvc.Before;
import service.EventService;
import service.user.AuthService;
import service.user.UserService;

public class Controller extends play.mvc.Controller {
    static final AuthService authService = new AuthService();
    static final UserService userService = new UserService();
    static final EventService eventService = new EventService();

    @Before
    static void initCurrentUser() {
        renderArgs.put("currentUser", getCurrentUser());
    }

    static User getCurrentUser() {
        return Cache.get(session.getId(), User.class);
    }

    static void setCurrentUser(User user) {
        Cache.set(session.getId(), user);
    }

    static void resetCurrentUser() {
        Cache.delete(session.getId());
        session.clear();
    }
}
