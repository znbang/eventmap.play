package models.event;

import models.Model;
import models.RandomId;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
}
