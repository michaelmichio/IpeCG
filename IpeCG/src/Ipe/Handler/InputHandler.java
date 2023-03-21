package Ipe.Handler;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class InputHandler {
    public Document doc;
    public String alg;
    public InputHandler(String[] args) {
        getInputDocs(args[0]);
        getAlgorithm(args[1]);
        new DocumentHandler(doc, alg);
    }

    public void getInputDocs(String docName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setValidating(false);
            dbf.setFeature("http://xml.org/sax/features/namespaces", false);
            dbf.setFeature("http://xml.org/sax/features/validation", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(this.getClass().getClassLoader().getResourceAsStream(docName));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAlgorithm(String algName) {
        for (CGAlgorithms cga : CGAlgorithms.values()) {
            if (algName.equalsIgnoreCase(cga.toString())) {
                alg = algName;
            }
        }
    }
}
