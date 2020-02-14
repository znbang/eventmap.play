package controllers.story.download;

import com.github.kevinsawicki.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class QingDouDownloader implements Downloader {
    @Override
    public boolean supports(String url) {
        return url.startsWith("https://www.qingdou.net/");
    }

    @Override
    public void download(String bookId, String url, int page) throws Exception {
        while (url != null) {
            page = page + 1;
            HttpRequest req = HttpRequest.get(url);
            if (!req.ok()) {
                throw new RuntimeException(String.format("status: %d, response: %s", req.code(), req.body()));
            }

            String html = req.body();
            Document doc = Jsoup.parse(html);
            String title = doc.selectFirst(".kfyd > h2").text();

            StringBuilder sb = new StringBuilder();
            for (Element node : doc.select("#content > p")) {
                String line = node.text().trim();
                sb.append(line).append("\n");
            }

            saveChapter(bookId, page, url, title, sb.toString());

            String next = "";
            for (Element node : doc.select("#thumb > a")) {
                if (node.text().contains("下一章")) {
                    next = node.attr("href");
                }
            }
            if (next.endsWith(".html")) {
                url = url.substring(0, url.indexOf("/", url.indexOf("://") + 3)) + next;
            } else {
                url = null;
            }
            Thread.sleep(1000);
        }
    }
}
