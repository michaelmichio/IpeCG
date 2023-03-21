package Ipe.Object;

import org.w3c.dom.Document;

public class Input {
    public Document doc;
    public String algorithm;

    public Input(Document doc, String algorithm) {
        this.doc = doc;
        this.algorithm = algorithm;
    }
}
