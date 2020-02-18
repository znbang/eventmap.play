package controllers.story.download;

import models.story.Book;
import models.story.BookJob;
import models.story.Chapter;
import play.Logger;
import play.jobs.Job;

public class DownloadCenter {
    private static Download[] downloaders = new Download[] {
            new DownloadBiQuGe(),
            new DownloadCk101(),
            new DownloadLewen(),
            new DownloadQingDou(),
            new DownloadShuQuGe(),
            new DownloadWfxs(),
    };

    private static Download find(String url) {
        for (Download a : downloaders) {
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

    public static void download() {
        for (BookJob job : BookJob.listPending()) {
            try {
                download(job.getId(), job.getBookId());
                done(job.getId(), "");
            } catch (Exception e) {
                try {
                    done(job.getId(), e.getMessage());
                } catch (Exception ex) {
                    Logger.error(ex, "Update BookJob failed.");
                }
            }
        }
    }

    private static void download(String jobId, String bookId) throws Exception {
        Book book = Book.findById(bookId);
        String url = book.getLastUrl();

        Download downloader = find(url);
        if (downloader == null) {
            throw new RuntimeException("Not supported: " + url);
        }

        // 下載新章節是從最後一章開始，所以要跳過第一個下載結果
        int page = book.getLastPage();
        boolean skipFirstResult = page > 0;

        while (url != null) {
            DownloadResult result = downloader.download(url);
            updateJob(jobId, result.title);
            if (skipFirstResult) {
                skipFirstResult = false;
            } else if (result.title == null && result.body.trim().isEmpty()) {
                updateJob(jobId, "Empty body");
                return;
            } else {
                page = page + 1;
                save(new Chapter(bookId, page, url, result.title, result.body));
                url = result.next;
            }
            Thread.sleep(500);
        }
    }

    private static void done(String jobId, String message) throws Exception {
        new Job<Void>() {
            @Override
            public void doJob() throws Exception {
                BookJob job = BookJob.findById(jobId);
                job.done(message);
                job.save();
            }
        }.now().get();
    }

    private static void updateJob(String jobId, String message) throws Exception {
        new Job<Void>() {
            @Override
            public void doJob() throws Exception {
                BookJob job = BookJob.findById(jobId);
                job.setMessage(message);
                job.save();
            }
        }.now().get();
    }

    private static void save(Chapter chapter) throws Exception {
        new Job<Void>() {
            @Override
            public void doJob() throws Exception {
                chapter.save();
            }
        }.now().get();
    }
}
