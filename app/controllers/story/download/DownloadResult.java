package controllers.story.download;

public class DownloadResult {
    public final String title;
    public final String body;
    public final String next;

    public DownloadResult(String title, String body, String next) {
        this.title = title;
        this.body = body;
        this.next = next;
    }
}
