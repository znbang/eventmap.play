package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
public class UserSession extends Model {
    public String userId;
    public LocalDateTime createdAt;

    public UserSession(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public static User findUserById(String id) {
        return find("select u from User u inner join UserSession s on s.userId=u.id and s.id=:id")
                .setParameter("id", id)
                .first();
    }
}
