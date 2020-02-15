package models.story;

import models.Model;
import models.RandomId;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book_jobs")
public class BookJob extends Model {
    private LocalDateTime createdAt;
    private boolean done;
    private String bookId;
    private String title;
    private String message;

    BookJob() {}

    public BookJob(String bookId, String title) {
        this.bookId = bookId;
        this.title = title;
        this.message = "";
    }

    @PrePersist
    void beforePersist() {
        this.id = RandomId.generate();
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void done(String message) {
        this.done = true;
        this.message = message;
    }

    public static List<BookJob> listPending() {
        return find("done=false").fetch();
    }
}
