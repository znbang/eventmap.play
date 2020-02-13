package models.story;

import models.Model;
import models.RandomId;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chapters")
public class Chapter extends Model {
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public String bookId;
    public Integer page;
    public String url;
    public String title;
    public String body;

    Chapter() {}

    @PrePersist
    void beforePersist() {
        this.id = RandomId.generate();
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    public Chapter(String bookId, Integer page, String url, String title, String body) {
        this.bookId = bookId;
        this.page = page;
        this.url = url;
        this.title = title;
        this.body = body;
    }

    public static long countByBookId(String bookId) {
        return count("byBookId", bookId);
    }

    public static List<Chapter> listByBookId(String bookId) {
        return find("bookId=:bookId order by page")
                .setParameter("bookId", bookId)
                .fetch();
    }

    public static List<Chapter> listByBookId(String bookId, int page, int size) {
        return find("bookId=:bookId order by page")
                .setParameter("bookId", bookId)
                .fetch(page, size);
    }

    public static Chapter findByBookIdAndPage(String bookId, int page) {
        return find("bookId=:bookId and page=:page")
                .setParameter("bookId", bookId)
                .setParameter("page", page)
                .first();
    }
}
