package controllers.story.download;

import play.jobs.Every;
import play.jobs.Job;

@Every("10s")
public class DownloadJob extends Job<Void> {
    @Override
    public void doJob() throws Exception {
        DownloadCenter.download();
    }
}
