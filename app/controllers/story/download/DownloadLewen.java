package controllers.story.download;

import com.github.kevinsawicki.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

public class DownloadLewen implements Download {
    @Override
    public boolean supports(String url) {
        return url.startsWith("https://www.lewenn.com/");
    }

    @Override
    public DownloadResult download(String url) {
        HttpRequest req = HttpRequest.get(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                .referer("https://www.lewenn.com");
        if (!req.ok()) {
            throw new RuntimeException(String.format("status: %d, response: %s", req.code(), req.body()));
        }

        String html = req.body();
        Document doc = Jsoup.parse(html);
        String title = doc.select(".kfyd > h1").text();

        StringBuilder sb = new StringBuilder();
        for (TextNode textNode : doc.selectFirst("div#content").textNodes()) {
            String line = textNode.text().trim();
            if (line.startsWith("　　")) {
                line = line.replace("　　", "");
            }
            if (!line.isEmpty()) {
                sb.append(line).append("\n");
            }
        }

        String next = doc.select("#pager_next").attr("href");
        if (next.endsWith(".html")) {
            url = url.substring(0, url.indexOf("/", url.indexOf("://") + 3)) + next;
        } else {
            url = null;
        }

        return new DownloadResult(title, sb.toString(), url);
    }
}
