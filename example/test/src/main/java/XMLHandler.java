import ch.openchvote.voter.Voter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

//    //private Voter voter; // класс
//
//
//    // Буферизация XML
//    @Override
//    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//
//        // if (qName.equals("voter")){ // как разделить
//        //     voter = new Voter();
//        //}
////        System.out.println(qName);
//    }
//
//    @Override
//    public void endElement(String uri, String localName, String qName) throws SAXException {
////        System.out.println(qName);
//    }
//     Doctors doc = new Doctors();
//    String thisElement = "";
//
//    public Doctors getResult(){
//        return doc;
//    }
//
//    @Override
//    public void startDocument() throws SAXException {
//        System.out.println("Start parse XML...");
//    }
//
//    @Override
//    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//        thisElement = qName;
//    }
//
//    @Override
//    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//        thisElement = "";
//    }
//
//    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
//        if (thisElement.equals("id")) {
//            doc.setId(new Integer(new String(ch, start, length)));
//        }
//        if (thisElement.equals("fam")) {
//            doc.setFam(new String(ch, start, length));
//        }
//        if (thisElement.equals("name")) {
//            doc.setName(new String(ch, start, length));
//        }
//        if (thisElement.equals("otc")) {
//            doc.setOtc(new String(ch, start, length));
//        }
//        if (thisElement.equals("dateb")) {
//            doc.setDateb(new String(ch, start, length));
//        }
//        if (thisElement.equals("datep")) {
//            doc.setDatep(new String(ch, start, length));
//        }
//        if (thisElement.equals("datev")) {
//            doc.setDatev(new String(ch, start, length));
//        }
//        if (thisElement.equals("datebegin")) {
//            doc.setDatebegin(new String(ch, start, length));
//        }
//        if (thisElement.equals("dateend")) {
//            doc.setDateend(new String(ch, start, length));
//        }
//        if (thisElement.equals("vdolid")) {
//            doc.setVdolid(new Integer(new String(ch, start, length)));
//        }
//        if (thisElement.equals("specid")) {
//            doc.setSpecid(new Integer(new String(ch, start, length)));
//        }
//        if (thisElement.equals("klavid")) {
//            doc.setKlavid(new Integer(new String(ch, start, length)));
//        }
//        if (thisElement.equals("stav")) {
//            doc.setStav(new Float(new String(ch, start, length)));
//        }
//        if (thisElement.equals("progid")) {
//            doc.setProgid(new Integer(new String(ch, start, length)));
//        }
//    }

//    @Override
//    public void endDocument() {
//        System.out.println("Stop parse XML...");
//    }
}
}
