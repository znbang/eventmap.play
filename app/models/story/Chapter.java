package models.story;

import models.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chapters")
public class Chapter extends Model {
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public String bookId;
    public String title;
    public String body;
    public Integer page;

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
