package controllers;

import models.User;
import play.cache.Cache;

import java.io.Serializable;

public class Security extends Controller {
    public static class CurrentUser implements Serializable {
        private final String id;
        private final String email;

        public CurrentUser(String id, String email) {
            this.id = id;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public static void set(User user) {
            Cache.set(session.getId(), new CurrentUser(user.id, user.email));
        }

        public static CurrentUser get() {
            return Cache.get(session.getId(), CurrentUser.class);
        }
    }
}
