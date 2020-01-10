package service.user;

import models.AuthProvider;

public class GoogleUser implements AuthUserInfo {
    private AuthProvider provider = AuthProvider.GOOGLE;
    private String sub;
    private String email;
    private String name;

    @Override
    public AuthProvider getProvider() {
        return provider;
    }

    @Override
    public String getId() {
        return sub;
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
        return "GoogleUser{" +
                "sub='" + sub + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
