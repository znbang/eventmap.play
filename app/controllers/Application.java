package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import play.db.jpa.JPA;
import play.db.jpa.NoTransaction;
import play.db.jpa.Transactional;
import play.i18n.Lang;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Application extends Controller {
    private static final Gson gson = new Gson();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional(readOnly = true)
    public static void index() {
        List<JsonObject> events = eventService.listActiveEvent().stream().map(event -> {
            JsonObject json = new JsonObject();
            json.addProperty("id", event.id);
            json.addProperty("name", event.name);
            json.addProperty("startDate", event.startDate.format(dtf));
            json.addProperty("endDate", event.endDate.format(dtf));
            json.addProperty("lat", event.lat);
            json.addProperty("lng", event.lng);
            return json;
        }).collect(toList());
        String jsonEvents = gson.toJson(events);
        render(jsonEvents);
    }

    @NoTransaction
    public static void lang(String lang, String redirect) {
        if (lang != null) {
            Lang.change(lang);
        } else {
            badRequest("Parameter lang is required.");
        }
        if (redirect != null && !redirect.isEmpty()) {
            redirect(redirect);
        } else {
            index();
        }
    }

    @Transactional(readOnly = true)
    public static void sitemap() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://eventmap.app/login\n");
        sb.append("https://eventmap.app/events\n");
        JPA.em().createQuery("select a.id from Event a order by a.createdAt", String.class).getResultStream().forEach(id -> {
            sb.append("https://eventmap.app/events/" + id + "\n");
        });
        renderText(sb);
    }
}