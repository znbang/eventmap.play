package controllers.story.download;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

public class DownloadWfxs implements Download {
    @Override
    public boolean supports(String url) {
        return url.startsWith("https://www.wfxs.org/");
    }

    @Override
    public DownloadResult download(String url) throws Exception {
        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage page = client.getPage(url);
        Document doc = Jsoup.parse(page.asXml());
        doc.select("h1 > a").remove();
        String title = doc.select("#content > h1").text();

        StringBuilder sb = new StringBuilder();
        for (TextNode node : doc.selectFirst("#content").textNodes()) {
            String line = node.text().trim();
            if (!line.isEmpty()) {
                sb.append(line).append("\n");
            }
        }

        String next = doc.select("#thumb > a:nth-child(4)").attr("href");
        if (next.endsWith("/0.html")) {
            url = null;
        } else {
            url = url.substring(0, url.indexOf("/", url.indexOf("://") + 3)) + next;
        }

        return new DownloadResult(title, sb.toString(), url);
    }
}
