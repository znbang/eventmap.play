package controllers.event;

import controllers.Authenticate;
import controllers.Controller;
import models.event.Event;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import play.db.jpa.Transactional;
import play.mvc.Before;

import java.util.List;

public class Events extends Controller {
    @Before(unless = {"index", "show"})
    static void requireLogin() {
        if (getCurrentUser() == null) {
            Authenticate.login();
        }
    }

    @Transactional(readOnly = true)
    public static void index() {
        int page = params.get("page", Integer.class);
        int size = params.get("size", Integer.class);
        long totalPage = getTotalPage(eventService.count(), page, size);
        if (page < 1 || page > totalPage) {
            notFound();
        }
        List<Event> events = eventService.listEvent(page, size);
        render(events, totalPage, page, size);
    }

    @Transactional(readOnly = true)
    public static void show(String id) {
        Event event = Event.findById(id);
        if (event == null) {
            notFound();
        }
        render(event);
    }

    @Transactional(readOnly = true)
    public static void user() {
        int page = params.get("page", Integer.class);
        int size = params.get("size", Integer.class);
        String userId = getCurrentUser().getId();
        long totalPage = getTotalPage(eventService.countByUser(userId), page, size);
        if (page < 1 || page > totalPage) {
            notFound();
        }
        List<Event> events = eventService.listEventByUser(userId, page, size);
        render(events, totalPage, page, size);
    }

    @Transactional(readOnly = true)
    public static void form(String id) {
        Event event = id == null ? new Event() : eventService.getEventByUser(id, getCurrentUser().getId());
        if (event == null) {
            notFound();
        }
        render("@form", id, event);
    }

    @Transactional
    public static void save(String id, Event event) {
        checkAuthenticity();

        validation.required("event.name", event.name).message("event.name_is_required");
        validation.required("event.location", event.location).message("event.location_is_required");
        validation.required("event.startDate", event.startDate).message("event.start_date_is_required");
        validation.required("event.endDate", event.endDate).message("event.end_date_is_required");
        // validation.required("event.url", event.url).message("event.url_is_required");
        validation.required("event.detail", Jsoup.clean(event.detail, Whitelist.none()).trim()).message("event.detail_is_required");
        validation.required("event.lng", event.lng).message("event.lng_is_required");
        validation.required("event.lat", event.lat).message("event.lat_is_required");
        validation.required("event.zoom", event.zoom).message("event.zoom_is_required");

        if (validation.hasErrors()) {
            render("@form", id, event);
        }

        String userId = getCurrentUser().getId();
        Event entity = id == null ? new Event() : eventService.getEventByUser(id, userId);
        if (entity == null) {
            notFound();
        }
        entity.userId = userId;
        entity.copyFrom(event);
        entity.save();
        user();
    }

    @Transactional
    public static void delete(String id) {
        Event event = eventService.getEventByUser(id, getCurrentUser().getId());
        if (event == null) {
            notFound();
        }
        event.delete();
        user();
    }
}
