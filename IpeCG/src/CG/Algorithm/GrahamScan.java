package CG.Algorithm;

import CG.Object.Point;
import Ipe.Object.Use;

import java.util.ArrayList;

public class GrahamScan {
    ArrayList<Point> vertices = new ArrayList<>();

    public GrahamScan(ArrayList<Use> uses) {
        if(uses.size() >= 3) {
            setVertices(uses);
            sortVertices();
            // ...
        }
        else {
            System.out.println("requires at least 3 points");
        }
    }

    public void sortVertices() {

    }

    public void setVertices(ArrayList<Use> uses) {
        for (Use u : uses) {
            vertices.add(new Point(Double.parseDouble(u.pos.x), Double.parseDouble(u.pos.y)));
        }
    }
}
