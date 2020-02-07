package models.story;

import models.Model;
import models.RandomId;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "books")
public class Book extends Model {
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public String userId;
    public String title;
    public String author;
    public String url;

    @PrePersist
    void beforePersist() {
        this.id = RandomId.generate();
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void trim() {
        title = title.trim();
        author = author.trim();
        url = url.trim();
    }

    public void copyFrom(Book book) {
        this.title = book.title;
        this.author = book.author;
        this.url = book.url;
    }

    public static long countByUserId(String userId) {
        return count("userId=?1", userId);
    }

    public static long countByUserIdForSearch(String userId, String q) {
        return count("userId=?1 and (title like ?2 or author like ?2)", userId, "%" + q + "%");
    }

    public static List<Book> listByUserId(String userId, int page, int size) {
        return find("userId=:userId")
                .setParameter("userId", userId)
                .fetch(page, size);
    }

    public static List<Book> listByUserIdForSearch(String userId, String q, int page, int size) {
        return find("userId=:userId and (title like :q or author like :q)")
                .setParameter("userId", userId)
                .setParameter("q", "%" + q + "%")
                .fetch(page, size);
    }

    public static Book findByUserId(String id, String userId) {
        return find("id=:id and userId=:userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .first();
    }

    public static boolean existsByUrl(String url) {
        return find("url=:url")
                .setParameter("url", url)
                .first() != null;
    }

    public static boolean existsByTitleAndAuthor(String title, String author) {
        return find("title=:title and author=:author")
                .setParameter("title", title)
                .setParameter("author", author)
                .first() != null;
    }
}
