package Ipe.Object;

import java.util.ArrayList;
import java.util.HashMap;

public class Layer {
    public ArrayList<Path> paths = new ArrayList<>();
    public ArrayList<Use> uses = new ArrayList<>();
    public HashMap<String, String> attributes = new HashMap<>();

    public Layer() {
    }

    public void addUse(Use use) {
        uses.add(use);
    }

    public void addPath(Path path) {
        paths.add(path);
    }
}
