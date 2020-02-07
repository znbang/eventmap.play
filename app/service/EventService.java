package service;

import models.event.Event;

import java.time.*;
import java.util.List;

public class EventService {
    public long count() {
        return Event.count();
    }

    public List<Event> listEvent(int page, int size) {
        return Event.find("order by startDate desc").fetch(page, size);
    }

    public List<Event> listActiveEvent() {
        LocalDateTime today = ZonedDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        return Event.find("endDate>=:today order by startDate desc")
                .bind("today", today)
                .fetch();
    }

    public Event getEventByUser(String id, String userId) {
        return Event.find("byIdAndUserId", id, userId).first();
    }

    public long countByUser(String userId) {
        return Event.count("byUserId", userId);
    }

    public List<Event> listEventByUser(String userId, int page, int size) {
        return Event.find("userId=?1 order by startDate desc", userId).fetch(page, size);
    }
}
