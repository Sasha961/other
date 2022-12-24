import java.util.*;

public class CopyLink {

    private List<CopyLink> links;

    private String link;

    private int depth;

    private CopyLink copyNewLink;

    public CopyLink(String link) {
        this.link = link;
        depth = 0;
        links = new ArrayList<>();
        copyNewLink = null;
    }

    public synchronized void addLink(CopyLink copyLink) {
        links.add(copyLink);
        copyLink.set(this);
    }

    public String getLink() {
        return link;
    }

    public int getDepth() {
        return depth;
    }

    private void set(CopyLink copyLink) {
        this.copyNewLink = copyLink;
        this.depth = setDepth();
    }

    private synchronized int setDepth() {

        if (copyNewLink == null) {
            return 0;
        }
        return copyNewLink.getDepth() + 1;
    }

    public List<CopyLink> getLinks() {
        return links;
    }
}
