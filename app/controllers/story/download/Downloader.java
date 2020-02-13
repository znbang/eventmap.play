package controllers.story.download;

import models.story.Chapter;

public interface Downloader {
    default void saveChapter(String bookId, int page, String url, String title, String body) throws Exception {
        new SaveChapterJob(new Chapter(bookId, page, url, title, body)).now().get();
    }

    boolean supports(String url);
    void download(String bookId, String url, int page) throws Exception;
}
