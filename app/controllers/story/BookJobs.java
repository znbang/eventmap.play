package controllers.story;

import controllers.Controller;
import models.story.BookJob;

import java.util.List;

public class BookJobs extends Controller {
    public static void index() {
        int page = params.get("page", Integer.class);
        int size = params.get("size", Integer.class);
        List<BookJob> jobs = BookJob.find("order by createdAt desc").fetch(page, size);
        long totalPage = getTotalPage(BookJob.count(), page, size);

        render(jobs, totalPage, page, size);
    }
}
