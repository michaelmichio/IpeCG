package Ipe.Handler;

import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class OutputHandler {
    public OutputHandler(Document doc) {
        print(doc);
    }

    public void print(Document doc) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource ds = new DOMSource(doc);
            StreamResult sr = new StreamResult(new File("output.ipe"));
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");
            t.transform(ds, sr);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
