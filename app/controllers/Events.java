package controllers;

import models.Event;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import play.db.jpa.Transactional;
import play.mvc.Before;

import java.util.List;

public class Events extends Controller {
    @Before(unless = {"index", "show"})
    static void requireLogin() {
        if (Security.CurrentUser.get() == null) {
            Authenticate.login();
        }
    }

    @Before
    static void initPagination() {
        if (params.get("page") == null) {
            params.put("page", "1");
        }
        if (params.get("size") == null) {
            params.put("size", "15");
        }
    }

    @Transactional(readOnly = true)
    public static void index() {
        int page = params.get("page", Integer.class);
        int size = params.get("size", Integer.class);
        long totalElements = Event.count();
        if (page < 1 || page > (totalElements + size - 1) / size) {
            notFound();
        }
        List<Event> events = Event.find("order by startDate desc").fetch(page, size);
        render(events, page, size, totalElements);
    }

    @Transactional(readOnly = true)
    public static void show(String id) {
        Event event = Event.findById(id);
        if (event != null) {
            render(event);
        } else {
            notFound();
        }
    }

    @Transactional(readOnly = true)
    public static void user() {
        int page = params.get("page", Integer.class);
        int size = params.get("size", Integer.class);
        String userId = Security.CurrentUser.get().getId();
        long totalElements = Event.count("byUserId", userId);
        if (page < 1 || page > (totalElements + size - 1) / size) {
            notFound();
        }
        List<Event> events = Event.find("userId=?1 order by startDate desc", userId).fetch(page, size);
        render(events, page, size, totalElements);
    }

    @Transactional(readOnly = true)
    public static void form(String id) {
        Event event = id == null ? new Event() : Event.findByIdAndUserId(id, Security.CurrentUser.get().getId());
        if (event != null) {
            render("@form", id, event);
        } else {
            notFound();
        }
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

        String userId = Security.CurrentUser.get().getId();
        Event entity = id == null ? new Event() : Event.findByIdAndUserId(id, userId);
        if (entity != null) {
            entity.userId = userId;
            entity.copyFrom(event);
            entity.save();
            user();
        } else {
            notFound();
        }
    }

    @Transactional
    public static void delete(String id) {
        Event event = Event.findByIdAndUserId(id, Security.CurrentUser.get().getId());
        if (event != null) {
            event.delete();
            user();
        } else {
            notFound();
        }
    }
}
