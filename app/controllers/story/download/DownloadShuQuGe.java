package controllers.story.download;

import com.github.kevinsawicki.http.HttpRequest;
import com.hankcs.hanlp.HanLP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

public class DownloadShuQuGe implements Download {
    @Override
    public boolean supports(String url) {
        return url.startsWith("http://www.shuquge.com/");
    }

    @Override
    public DownloadResult download(String url) {
        HttpRequest req = HttpRequest.get(url)
                .accept("text/html")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                .referer("http://www.shuquge.com/");

        if (!req.ok()) {
            throw new RuntimeException(String.format("status: %d, response: %s", req.code(), req.body()));
        }

        String html = req.body();
        Document doc = Jsoup.parse(html);
        String title = doc.select(".content > h1").text();

        StringBuilder sb = new StringBuilder();
        for (TextNode node : doc.selectFirst("#content").textNodes()) {
            String line = node.text().trim();
            if (!line.isEmpty()) {
                line = HanLP.convertToTraditionalChinese(line);
                sb.append(line).append("\n");
            }
        }

        String next = doc.select(".page_chapter > ul > li:nth-child(3) > a").attr("href");
        if (next.endsWith("/index.html")) {
            url = null;
        } else {
            url = url.substring(0, url.lastIndexOf("/") + 1) + next;
        }

        return new DownloadResult(title, sb.toString(), url);
    }
}
