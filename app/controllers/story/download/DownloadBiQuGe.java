package controllers.story.download;

import com.github.kevinsawicki.http.HttpRequest;
import com.hankcs.hanlp.HanLP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DownloadBiQuGe implements Download {
    @Override
    public boolean supports(String url) {
        return url.startsWith("https://www.biquge5200.cc/");
    }

    @Override
    public DownloadResult download(String url) {
        HttpRequest req = HttpRequest.get(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                .referer("https://www.biquge5200.cc/");

        if (!req.ok()) {
            throw new RuntimeException(String.format("status: %d, response: %s", req.code(), req.body()));
        }

        String html = req.body("GB18030");
        Document doc = Jsoup.parse(html);
        String title = doc.select(".bookname > h1").text();

        StringBuilder sb = new StringBuilder();
        for (Element elem : doc.selectFirst("#content").children()) {
            String line = elem.text().trim();
            line = line.replace("\u3000\u3000", "");
            line = HanLP.convertToTraditionalChinese(line);
            sb.append(line).append("\n");
            if (!line.isEmpty()) {
                sb.append("\n");
            }
        }

        String next = doc.select(".bottem2 > a:nth-child(5)").attr("href");
        if (next.endsWith("/")) {
            url = null;
        } else {
            url = next;
        }

        return new DownloadResult(title, sb.toString(), url);
    }
}
