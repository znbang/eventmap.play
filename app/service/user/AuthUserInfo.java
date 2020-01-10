package service.user;

import models.AuthProvider;

public interface AuthUserInfo {
    AuthProvider getProvider();
    String getId();
    String getEmail();
    String getName();
}
