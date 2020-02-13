package models.story;

import models.Model;
import models.RandomId;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_jobs")
public class BookJob extends Model {
    private LocalDateTime createdAt;
    private String bookId;
    private String title;
    private String status;

    BookJob() {}

    public BookJob(String bookId, String title) {
        this.bookId = bookId;
        this.title = title;
        this.status = "";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
