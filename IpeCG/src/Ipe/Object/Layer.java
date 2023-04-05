package Ipe.Object;

import java.util.ArrayList;

public class Layer {
    public ArrayList<Path> paths = new ArrayList<>();
    public ArrayList<Use> uses = new ArrayList<>();
    public ArrayList<Text> texts = new ArrayList<Text>();

    public Layer() {
    }

    public Layer(ArrayList<Path> paths, ArrayList<Use> uses, ArrayList<Text> texts) {
        this.paths = paths;
        this.uses = uses;
        this.texts = texts;
    }

    public void addText(Text text) {
        texts.add(text);
    }

    public void addAllText(ArrayList<Text> texts) {
        this.texts = texts;
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
