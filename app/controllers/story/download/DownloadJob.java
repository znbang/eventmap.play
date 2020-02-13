package controllers.story.download;

import models.story.Book;
import models.story.BookJob;
import play.jobs.Every;
import play.jobs.Job;

@Every("10s")
public class DownloadJob extends Job<Void> {
    @Override
    public void doJob() throws Exception {
        for (BookJob job : BookJob.<BookJob>findAll()) {
            Book book = Book.findById(job.getBookId());
            String url = book.getLastUrl();
            int page = book.getLastPage();

            try {
                DownloadCenter.download(book.getId(), url, page);
                job.delete();
            } catch (Exception e) {
                job.setStatus(e.getMessage());
                job.save();
            }
        }
    }
}
