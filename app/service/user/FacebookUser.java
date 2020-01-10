package service.user;

import models.AuthProvider;

public class FacebookUser implements AuthUserInfo {
    private AuthProvider provider = AuthProvider.FACEBOOK;
    private String id;
    private String email;
    private String name;

    @Override
    public AuthProvider getProvider() {
        return provider;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FacebookUser{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
