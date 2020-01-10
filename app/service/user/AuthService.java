package service.user;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import play.Logger;
import play.Play;
import play.mvc.Router;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Gson gson = new Gson();

    public AuthUserInfo loginWithGoogle(String code) {
        String accessToken = getGoogleAccessToken(code);
        return accessToken == null ? null : getGoogleUserInfo(accessToken);
    }

    public AuthUserInfo loginWithFacebook(String code) {
        String accessToken = getFacebookAccessToken(code);
        return accessToken == null ? null : getFacebookUserInfo(accessToken);
    }

    private String getAccessToken(String url, Map<String, String> model) {
        HttpRequest req = HttpRequest.post(url)
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .send(gson.toJson(model));
        if (req.ok()) {
            JsonObject jsonObject = gson.fromJson(req.body(), JsonObject.class);
            return jsonObject.get("access_token").getAsString();
        } else {
            Logger.error("Get access token failed(%s): %s", req.code(), req.body());
        }
        return null;
    }

    private String getGoogleAccessToken(String code) {
        Map<String, String> model = new HashMap<>();
        model.put("code", code);
        model.put("client_id", Play.configuration.getProperty("google.client_id"));
        model.put("client_secret", Play.configuration.getProperty("google.client_secret"));
        model.put("redirect_uri", Router.getFullUrl("Authenticate.loginWithGoogle").replace("http://eventmap.", "https://eventmap."));
        model.put("grant_type", "authorization_code");
        return getAccessToken("https://oauth2.googleapis.com/token", model);
    }

    private AuthUserInfo getGoogleUserInfo(String accessToken) {
        HttpRequest req = HttpRequest.get("https://www.googleapis.com/oauth2/v3/userinfo")
                .accept(HttpRequest.CONTENT_TYPE_JSON)
                .header(HttpRequest.HEADER_AUTHORIZATION, "Bearer " + accessToken);
        if (req.ok()) {
            return gson.fromJson(req.body(), GoogleUser.class);
        } else {
            Logger.error("Get google user info failed(%s): %s", req.code(), req.body());
            return null;
        }
    }

    private String getFacebookAccessToken(String code) {
        Map<String, String> model = new HashMap<>();
        model.put("code", code);
        model.put("client_id", Play.configuration.getProperty("facebook.client_id"));
        model.put("client_secret", Play.configuration.getProperty("facebook.client_secret"));
        model.put("redirect_uri", Router.getFullUrl("Authenticate.loginWithFacebook").replace("http://eventmap.", "https://eventmap."));
        return getAccessToken("https://graph.facebook.com/v5.0/oauth/access_token", model);
    }

    private AuthUserInfo getFacebookUserInfo(String accessToken) {
        HttpRequest req = HttpRequest.get("https://graph.facebook.com/v5.0/me?fields=about,name,picture{url},email")
                .accept(HttpRequest.CONTENT_TYPE_JSON)
                .header(HttpRequest.HEADER_AUTHORIZATION, "Bearer " + accessToken);
        if (req.ok()) {
            return gson.fromJson(req.body(), FacebookUser.class);
        } else {
            Logger.error("Get facebook user info failed(%s): %s", req.code(), req.body());
            return null;
        }
    }
}
