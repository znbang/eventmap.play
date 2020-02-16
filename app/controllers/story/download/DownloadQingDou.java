package controllers.story.download;

import com.github.kevinsawicki.http.HttpRequest;
import com.hankcs.hanlp.HanLP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DownloadQingDou implements Download {
    @Override
    public boolean supports(String url) {
        return url.startsWith("https://www.qingdou.net/");
    }

    @Override
    public DownloadResult download(String url) {
        HttpRequest req = HttpRequest.get(url);
        if (!req.ok()) {
            throw new RuntimeException(String.format("status: %d, response: %s", req.code(), req.body()));
        }

        String html = req.body();
        Document doc = Jsoup.parse(html);
        String title = doc.selectFirst(".kfyd > h2").text();
        title = HanLP.convertToTraditionalChinese(title);

        StringBuilder sb = new StringBuilder();
        for (Element node : doc.select("#content > p")) {
            String line = node.text().trim();
            line = HanLP.convertToTraditionalChinese(line);
            if (!line.startsWith("喜歡") || !line.endsWith("更新速度最快。")) {
                sb.append(line).append("\n");
            }
        }

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

        return new DownloadResult(title, sb.toString(), url);
    }
}
