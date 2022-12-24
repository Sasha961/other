import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class test1 {
    static List<String> listAllLinks = new ArrayList<>();
    public static void main(String[] args) {

        getAllLinks("https://skillbox.ru/");
    }


        public static void getAllLinks(String path) {
            List<String> listLinks = new ArrayList<>();
            try {
                Document document = Jsoup.connect(path).get();
                Elements elements = document.select("a");

                PrintWriter printWriter = new PrintWriter("src\\main\\resources\\linksSkillbox.txt"); //Method for write links in the file 'linksSkillbox.txt'

                elements.forEach(element ->
                        {
                            String oneLink = element.attr("abs:href");
                            if (!listAllLinks.contains(oneLink)) {

                                System.out.println(oneLink);
                                listLinks.add(oneLink);
                                listAllLinks.add(oneLink);

                                printWriter.write(oneLink);
                            }
                        }
                );
                Thread.sleep(5000);

                for (String link : listLinks) {

                    getAllLinks(link);
                }

                printWriter.flush();
                printWriter.close();
            } catch (
                    Exception ex) {
                ex.getMessage();
            }
        }
    }