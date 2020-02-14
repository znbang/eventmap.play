package controllers.story.download;

import models.story.BookJob;

public class DownloadCenter {
    private static Downloader[] downloaders = new Downloader[] {
            new Ck101Downloader(),
            new LewenDownloader(),
    };

    public static boolean supports(String url) {
        for (Downloader a : downloaders) {
            if (a.supports(url)) {
                return true;
            }
        }
        return false;
    }

    public static void add(String bookId, String title) {
        new BookJob(bookId, title).save();
    }

    public static void download(String bookId, String url, int page) throws Exception {
        for (Downloader a : downloaders) {
            if (a.supports(url)) {
                a.download(bookId, url, page);
            }
        }
        throw new RuntimeException("Not supported: " + url);
    }
}
