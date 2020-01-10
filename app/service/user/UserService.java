package service.user;

import models.User;

public class UserService {
    public User login(AuthUserInfo userInfo) {
        User user = User.find("byEmailAndProvider", userInfo.getEmail(), userInfo.getProvider()).first();
        if (user != null) {
            user.email = userInfo.getEmail();
            user.name = userInfo.getName();
            user.save();
        } else {
            user = new User();
            user.provider = userInfo.getProvider();
            user.uid = userInfo.getId();
            user.email = userInfo.getEmail();
            user.name = userInfo.getName();
            user.save();
        }
        return user;
    }
}
