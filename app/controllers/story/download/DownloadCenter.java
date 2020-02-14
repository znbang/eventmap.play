package controllers.story.download;

import models.story.BookJob;

public class DownloadCenter {
    private static Downloader[] downloaders = new Downloader[] {
            new BiQuGeDownloader(),
            new Ck101Downloader(),
            new LewenDownloader(),
            new QingDouDownloader(),
    };

    private static Downloader find(String url) {
        for (Downloader a : downloaders) {
            if (a.supports(url)) {
                return a;
            }
        }
        return null;
    }

    public static boolean supports(String url) {
        return find(url) != null;
    }

    public static void add(String bookId, String title) {
        new BookJob(bookId, title).save();
    }

    public static void download(String bookId, String url, int page) throws Exception {
        Downloader a = find(url);
        if (a == null) {
            throw new RuntimeException("Not supported: " + url);
        }
        a.download(bookId, url, page);
    }
}
