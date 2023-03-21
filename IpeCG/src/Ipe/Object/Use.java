package Ipe.Object;

import java.util.HashMap;

public class Use {
    public Point pos;
    public HashMap<String, String> attributes = new HashMap<>();

    public Use(Point point) {
        this.pos = point;
    }

    public Use(Point point, HashMap<String, String> attributes) {
        this.pos = point;
        this.attributes = attributes;
    }
}
