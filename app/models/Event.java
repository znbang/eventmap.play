package models;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.*;
import java.util.List;

@Entity
@Table(name = "events")
public class Event extends Model {
    public String userId;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public String name;
    public String location;
    public String url;
    public String detail;
    public Double lng;
    public Double lat;
    public Integer zoom;

    @PrePersist
    void beforePersist() {
        this.id = RandomId.generate();
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void copyFrom(Event src) {
        this.name = src.name;
        this.location = src.location;
        this.startDate = src.startDate;
        this.endDate = src.endDate;
        this.url = src.url;
        this.detail = src.detail;
        this.lng = src.lng;
        this.lat = src.lat;
        this.zoom = src.zoom;
    }

    public static List<Event> findActive() {
        LocalDateTime today = ZonedDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        return Event.find("endDate>=:today order by startDate desc")
                .bind("today", today)
                .fetch();
    }

    public static Event findByIdAndUserId(String id, String userId) {
        return find("byIdAndUserId", id, userId).first();
    }
}
