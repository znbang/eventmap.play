package controllers.story.download;

import models.story.Chapter;
import play.jobs.Job;

public class SaveChapterJob extends Job<Void> {
    private final Chapter chapter;

    public SaveChapterJob(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public void doJob() throws Exception {
        chapter.save();
    }
}
