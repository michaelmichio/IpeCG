package Ipe.Object;

import java.util.HashMap;

public class Text {
    public String text;
    public HashMap<String, String> attributes = new HashMap<>();

    public Text(String text, HashMap<String, String> attributes) {
        this.text = text;
        this.attributes = attributes;
    }
}
