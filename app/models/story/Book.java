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

    public Book() {}

    public Book(String userId) {
        this.userId = userId;
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

    public static long countByUserId(String userId, String q) {
        q = q == null ? "" : q.trim();
        if (q.isEmpty()) {
            return count("userId=?1", userId);
        } else {
            return count("userId=?1 and (title like ?2 or author like ?2)", userId, "%" + q + "%");
        }
    }

    public static List<Book> listByUserId(String userId, String q, int page, int size) {
        q = q == null ? "" : q.trim();
        if (q.isEmpty()) {
            return find("userId=:userId order by updatedAt desc")
                    .setParameter("userId", userId)
                    .fetch(page, size);
        } else {
            return find("userId=:userId and (title like :q or author like :q) order by updatedAt desc")
                    .setParameter("userId", userId)
                    .setParameter("q", "%" + q + "%")
                    .fetch(page, size);
        }
    }

    public static Book findByUserId(String id, String userId) {
        return find("id=:id and userId=:userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .first();
    }

    public static Book findByUrl(String url) {
        return find("url=:url")
                .setParameter("url", url)
                .first();
    }

    public static Book findByTitleAndAuthor(String title, String author) {
        return find("title=:title and author=:author")
                .setParameter("title", title)
                .setParameter("author", author)
                .first();
    }

    public String getLastUrl() {
        String url = Chapter.find("select a.url from Chapter a where a.bookId=:bookId order by page desc")
                .setParameter("bookId", id)
                .first();
        return url == null ? this.url : url;
    }

    public int getLastPage() {
        return Chapter.find("select coalesce(max(a.page), 0) from Chapter a where a.bookId=:bookId")
                .setParameter("bookId", id)
                .first();
    }

    public void deleteChapters() {
        Chapter.delete("bookId=?1", id);
    }
}
