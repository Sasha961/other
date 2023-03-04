import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class ForkJoinLinks extends RecursiveTask<List<CopyLink>> {

    CopyLink copyLink;

    private static final String REGEX = "https://[a-z]+.[a-z]+/[a-z0-9_/]+";

    private static TreeSet<String> allLinks = new TreeSet<>();

    private static List<CopyLink> finalLinks = new ArrayList<>();

    public ForkJoinLinks(CopyLink copyLink) {
        this.copyLink = copyLink;
    }

    @Override
    protected List<CopyLink> compute() {

        List<ForkJoinLinks> linksJoin = new ArrayList<>();
        Document document;

        try {
            document = Jsoup.connect(copyLink.getLink()).get();
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Elements links = document.select("a");

        for (Element elementLink : links) {
            String link = elementLink.attr("abs:href");
            if (isLink(link)) {
                allLinks.add(link);
                copyLink.addLink(new CopyLink(link));
            }
        }

        for (CopyLink link : copyLink.getLinks()) {
            ForkJoinLinks forkJoinLinks = new ForkJoinLinks(link);
            forkJoinLinks.fork();
            linksJoin.add(forkJoinLinks);
            finalLinks.add(link);
        }
        linksJoin.forEach(lj -> lj.join());

        return finalLinks;
    }

    private boolean isLink(String link) {

        if (!allLinks.contains(link) && link.startsWith(copyLink.getLink())
                && link.matches(REGEX)) {
            return true;
        }
        return false;
    }
}
