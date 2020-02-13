package models;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends Model {
    @Enumerated
    public AuthProvider provider;
    public String uid;
    public String email;
    public String name;
    public LocalDateTime createdAt;

    @PrePersist
    void beforePersist() {
        this.id = RandomId.generate();
        this.createdAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return email.equals("znbang@gmail.com");
    }
}