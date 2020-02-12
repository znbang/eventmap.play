package controllers;

import models.User;
import models.UserSession;
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
        User user = Cache.get(session.getId(), User.class);
        if (user != null) {
            return user;
        }

        user = UserSession.findUserById(session.getId());
        if (user != null) {
            Cache.set(session.getId(), user, "300s");
        }

        return user;
    }

    protected static void setCurrentUser(User user) {
        Cache.set(session.getId(), user, "300s");
        new UserSession(session.getId(), user.getId()).save();
    }

    protected static void resetCurrentUser() {
        Cache.delete(session.getId());
        UserSession.delete("id=?1", session.getId());
        session.clear();
    }

    protected static long getTotalPage(long totalElements, int page, int size) {
        long totalPage = (totalElements + size - 1) / size;
        return totalPage == 0 ? 1 : totalPage;
    }
}
