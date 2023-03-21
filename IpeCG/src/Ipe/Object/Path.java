package Ipe.Object;

import java.util.ArrayList;
import java.util.HashMap;

public class Path {
    public ArrayList<Point> points = new ArrayList<>();
    public HashMap<String, String> attributes = new HashMap<>();
    public int length;

    public Path(ArrayList<Point> pos) {
        this.points = pos;
        length = pos.size();
    }

    public Path(ArrayList<Point> pos, HashMap<String, String> attributes) {
        this.points = pos;
        this.attributes = attributes;
        length = pos.size();
    }
}
