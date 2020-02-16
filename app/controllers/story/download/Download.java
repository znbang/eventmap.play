package controllers.story.download;

public interface Download {
    boolean supports(String url);
    DownloadResult download(String url) throws Exception;
}
