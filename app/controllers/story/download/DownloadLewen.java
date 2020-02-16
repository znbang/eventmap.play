package controllers.story.download;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.hankcs.hanlp.HanLP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;

public class DownloadLewen implements Download {
    @Override
    public boolean supports(String url) {
        return url.startsWith("https://www.lewenn.com/");
    }

    @Override
    public DownloadResult download(String url) throws Exception {
        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage page = client.getPage(url);
        String html = page.asXml();
        Document doc = Jsoup.parse(html);
        String title = doc.select(".bookname > h1").text();
        title = HanLP.convertToTraditionalChinese(title);

        StringBuilder sb = new StringBuilder();
        doc.select("#content > script").remove();
        doc.select("#content > div").remove();
        List<TextNode> textNodes = new ArrayList<>();
        textNodes.addAll(doc.selectFirst("div#content").textNodes());
        textNodes.addAll(doc.selectFirst("#content > span").textNodes());
        for (TextNode textNode : textNodes) {
            String line = textNode.text().trim();
            line = line.replace("　　", "");
            if (!line.isEmpty()) {
                line = HanLP.convertToTraditionalChinese(line);
                sb.append(line).append("\n");
            }
        }

        String next = doc.select(".bottem2 > a.next").attr("href");
        if (next.endsWith(".html")) {
            url = url.substring(0, url.indexOf("/", url.indexOf("://") + 3)) + next;
        } else {
            url = null;
        }

        return new DownloadResult(title, sb.toString(), url);
    }
}
