package Ipe.Object;

import java.util.ArrayList;

public class Layer {
    public ArrayList<Path> paths = new ArrayList<>();
    public ArrayList<Use> uses = new ArrayList<>();

    public Layer() {
    }

    public void addUse(Use use) {
        uses.add(use);
    }

    public void addPath(Path path) {
        paths.add(path);
    }
}
