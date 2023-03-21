package Ipe.Object;

import java.util.ArrayList;

public class Path {
    public ArrayList<Point> points = new ArrayList<>();
    public int length;

    public Path(ArrayList<Point> pos) {
        this.points = pos;
        length = pos.size();
    }
}
