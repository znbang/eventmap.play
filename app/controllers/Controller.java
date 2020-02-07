package controllers;

import models.User;
import play.cache.Cache;
import play.mvc.Before;
import service.EventService;
import service.user.AuthService;
import service.user.UserService;

public class Controller extends play.mvc.Controller {
    protected static final AuthService authService = new AuthService();
    protected static final UserService userService = new UserService();
    protected static final EventService eventService = new EventService();

    @Before
    protected static void initCurrentUser() {
        renderArgs.put("currentUser", getCurrentUser());
    }

    @Before
    protected static void initPagination() {
        if (params.get("page") == null) {
            params.put("page", "1");
        }
        if (params.get("size") == null) {
            params.put("size", "15");
        }
    }

    protected static User getCurrentUser() {
        return Cache.get(session.getId(), User.class);
    }

    protected static void setCurrentUser(User user) {
        Cache.set(session.getId(), user);
    }

    protected static void resetCurrentUser() {
        Cache.delete(session.getId());
        session.clear();
    }

    protected static long getTotalPage(long totalElements, int page, int size) {
        long totalPage = (totalElements + size - 1) / size;
        return totalPage == 0 ? 1 : totalPage;
    }
}
