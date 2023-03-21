package Ipe.Object;

import java.util.ArrayList;

public class Layer {
    public ArrayList<Path> paths = new ArrayList<>();
    public ArrayList<Use> uses = new ArrayList<>();

    public Layer() {
    }

    public Layer(ArrayList<Path> paths, ArrayList<Use> uses) {
        this.paths = paths;
        this.uses = uses;
    }

    public void addUse(Use use) {
        uses.add(use);
    }

    public void addAllUse(ArrayList<Use> uses) {
        this.uses.addAll(uses);
    }

    public void addPath(Path path) {
        paths.add(path);
    }

    public void addAllPath(ArrayList<Path> paths) {
        this.paths.addAll(paths);
    }
}
