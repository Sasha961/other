import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.lang3.StringUtils;

public class Main {

    //    private static String link = "https://lenta.ru/";

    private static String link = "https://skillbox.ru/";

    private static String myFile = "bin/links.txt";

    public static void main(String[] args) throws IOException {

        CopyLink copyLink = new CopyLink(link);
        List<CopyLink> invoke = new ForkJoinPool().invoke(new ForkJoinLinks(copyLink));

        write(invoke);
    }

    public static void write(List<CopyLink> invoke) throws IOException {

        File file = new File(myFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(link + "\n");

        for (CopyLink link1 : invoke) {
            String tab = StringUtils.repeat("\t", link1.getDepth());
            writer.write(tab + link1.getLink() + "\n");
        }

        writer.flush();
        writer.close();
    }
}
