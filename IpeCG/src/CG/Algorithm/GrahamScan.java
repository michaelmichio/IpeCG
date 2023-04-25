package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;
import Ipe.Object.Text;
import Ipe.Object.Use;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GrahamScan {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<Point> initialPoints = new ArrayList<>();
    public ArrayList<Point> duplicates = new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();
    public Stack<Point> hull = new Stack<>();
    public Point initialPoint;


    public GrahamScan(ArrayList<Use> uses) {
        if(uses.size() > 2) {
            setPoints(uses);
            setInitialLayers();
            sortPoints();
            generateLayers();
        }
        else {
            System.out.println("requires at least 3 points");
        }
    }

    public void setInitialLayers() {
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Use> uses = new ArrayList<>();
        ArrayList<Text> texts = new ArrayList<>();

        initialPoints.addAll(points);

        for (int i = 0; i < initialPoints.size(); i++) {

            attributes = new HashMap<>();

            // USE
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

            attributes = new HashMap<>();

            // TEXT
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(i+1), attributes));
        }
        layers.add(new Layer(null, uses, texts));
    }

    public void generateLayers() {

        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Use> uses = new ArrayList<>();
        ArrayList<Text> texts = new ArrayList<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        // Scenario (circle on initial points)
        for (int i = 0; i < initialPoints.size(); i++) {

            attributes = new HashMap<>();

            // USE
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

            attributes = new HashMap<>();

            // TEXT
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(i+1), attributes));
        }

        attributes = new HashMap<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        layers.add(new Layer(paths, uses, texts));

        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        // Scenario (arc on initial point)
        for (int i = 0; i < initialPoints.size(); i++) {

            attributes = new HashMap<>();

            // USE
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

            attributes = new HashMap<>();

            // TEXT
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(i+1), attributes));
        }

        attributes = new HashMap<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "a"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        attributes.put("arrow", "normal/normal");
        paths.add(new Path(strPoints, attributes));

        layers.add(new Layer(paths, uses, texts));

        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        // Scenario (dashed line to initial point)
        for (int i = 0; i < initialPoints.size(); i++) {

            attributes = new HashMap<>();

            // USE
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

            attributes = new HashMap<>();

            // TEXT
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(i+1), attributes));
        }

        attributes = new HashMap<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "a"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        attributes.put("arrow", "normal/normal");
        paths.add(new Path(strPoints, attributes));

        for (int i = 0; i < points.size(); i++) {
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "l"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            attributes.put("dash", "dashed");
            paths.add(new Path(strPoints, attributes));
        }

        layers.add(new Layer(paths, uses, texts));

        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        // Scenario (remove duplicates)
        for (int i = 0; i < initialPoints.size(); i++) {

            if (!duplicates.contains(initialPoints.get(i))) {
                attributes = new HashMap<>();

                // USE
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("name", "mark/disk(sx)");
                attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
                attributes.put("size", "large (5.0)");
                attributes.put("stroke", "black");
                uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

                attributes = new HashMap<>();

                // TEXT
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(i+1), attributes));
            }
        }

        attributes = new HashMap<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "a"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        attributes.put("arrow", "normal/normal");
        paths.add(new Path(strPoints, attributes));

        for (int i = 0; i < points.size(); i++) {
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "l"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            attributes.put("dash", "dashed");
            paths.add(new Path(strPoints, attributes));
        }

        layers.add(new Layer(paths, uses, texts));

        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        // Scenario (stack on left side layer)
        if (duplicates.size() > 0) {
            for (int i = 0; i < initialPoints.size(); i++) {

                if (!duplicates.contains(initialPoints.get(i))) {
                    attributes = new HashMap<>();

                    // USE
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("name", "mark/disk(sx)");
                    attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
                    attributes.put("size", "large (5.0)");
                    attributes.put("stroke", "black");
                    uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

                    attributes = new HashMap<>();

                    // TEXT
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(i+1), attributes));
                }
            }

            for (int i = 0; i < 18; i++) {
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point("0", String.valueOf(i * 32), "m"));
                strPoints.add(new Ipe.Object.Point("0", String.valueOf((i + 1) * 32), "l"));
                strPoints.add(new Ipe.Object.Point("64", String.valueOf((i + 1) * 32), "l"));
                strPoints.add(new Ipe.Object.Point("64", String.valueOf(i * 32), "l"));
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
            }

            layers.add(new Layer(paths, uses, texts));

            attributes = new HashMap<>();
            paths = new ArrayList<>();
            uses = new ArrayList<>();
            texts = new ArrayList<>();
            strPoints = new ArrayList<>();
        }

        // Scenario (initial circles)
        for (int i = 0; i < initialPoints.size(); i++) {

            if (!duplicates.contains(initialPoints.get(i))) {
                attributes = new HashMap<>();

                // USE
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("name", "mark/disk(sx)");
                attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
                attributes.put("size", "large (5.0)");
                attributes.put("stroke", "black");
                uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

                attributes = new HashMap<>();

                // TEXT
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(i+1), attributes));
            }
        }

        for (int i = 0; i < 18; i++) {
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point("0", String.valueOf(i * 32), "m"));
            strPoints.add(new Ipe.Object.Point("0", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf(i * 32), "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));
        }

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(0).x), String.valueOf(points.get(0).y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(1).x), String.valueOf(points.get(1).y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        layers.add(new Layer(paths, uses, texts));

        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        // Scenario (initial blue lines)
        for (int i = 0; i < initialPoints.size(); i++) {

            if (!duplicates.contains(initialPoints.get(i))) {
                attributes = new HashMap<>();

                // USE
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("name", "mark/disk(sx)");
                attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
                attributes.put("size", "large (5.0)");
                attributes.put("stroke", "black");
                uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

                attributes = new HashMap<>();

                // TEXT
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(i+1), attributes));
            }
        }

        for (int i = 0; i < 18; i++) {
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point("0", String.valueOf(i * 32), "m"));
            strPoints.add(new Ipe.Object.Point("0", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf(i * 32), "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));
        }

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(0).x), String.valueOf(points.get(0).y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(1).x), String.valueOf(points.get(1).y), "e"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));

        hull.push(initialPoint);
        hull.push(points.get(0));
        hull.push(points.get(1));

        for (int i = 0; i < hull.size(); i++) {
            attributes = new HashMap<>();

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", 24 + " " + (8 + (32 * i)));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(i)) + 1), attributes));
        }

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(0).x), String.valueOf(points.get(0).y), "l"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(1).x), String.valueOf(points.get(1).y), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "blue");
        attributes.put("pen", "ultrafat (2.0)");
        paths.add(new Path(strPoints, attributes));

        layers.add(new Layer(paths, uses, texts));

        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        // Scenario (remove circles)
        for (int i = 0; i < initialPoints.size(); i++) {

            if (!duplicates.contains(initialPoints.get(i))) {
                attributes = new HashMap<>();

                // USE
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("name", "mark/disk(sx)");
                attributes.put("pos", (initialPoints.get(i).x) + " " + (initialPoints.get(i).y));
                attributes.put("size", "large (5.0)");
                attributes.put("stroke", "black");
                uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(i).x), String.valueOf(initialPoints.get(i).y)), attributes));

                attributes = new HashMap<>();

                // TEXT
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (initialPoints.get(i).x + 8) + " " + (initialPoints.get(i).y + 8));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(i+1), attributes));
            }
        }

        for (int i = 0; i < 18; i++) {
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point("0", String.valueOf(i * 32), "m"));
            strPoints.add(new Ipe.Object.Point("0", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf((i + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf(i * 32), "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));
        }

        for (int i = 0; i < hull.size(); i++) {
            attributes = new HashMap<>();

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("pos", 24 + " " + (8 + (32 * i)));
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(i)) + 1), attributes));
        }

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(0).x), String.valueOf(points.get(0).y), "l"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(1).x), String.valueOf(points.get(1).y), "l"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "blue");
        attributes.put("pen", "ultrafat (2.0)");
        paths.add(new Path(strPoints, attributes));

        layers.add(new Layer(paths, uses, texts));

        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        // Scenario (content)
        int i = 2;
        while (i < points.size()) {

            attributes = new HashMap<>();
            paths = new ArrayList<>();
            uses = new ArrayList<>();
            texts = new ArrayList<>();
            strPoints = new ArrayList<>();

            // start layer 0
            // start points and numbers
            for (int j = 0; j < initialPoints.size(); j++) {

                if (!duplicates.contains(initialPoints.get(j))) {
                    attributes = new HashMap<>();

                    // USE
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("name", "mark/disk(sx)");
                    attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                    attributes.put("size", "large (5.0)");
                    attributes.put("stroke", "black");
                    uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                    attributes = new HashMap<>();

                    // TEXT
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(j+1), attributes));
                }
            }
            // end points and numbers

            for (int j = 0; j < points.size(); j++) {
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                attributes.put("dash", "dashed");
                paths.add(new Path(strPoints, attributes));
            }

            // start stack
            for (int j = 0; j < 18; j++) {
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
                strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
                strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
                strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
            }
            // end stack

            // start stack of hull
            for (int j = 0; j < hull.size(); j++) {
                attributes = new HashMap<>();

                if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                    attributes.put("pos", 16 + " " + (8 + (32 * j)));
                }
                else {
                    attributes.put("pos", 24 + " " + (8 + (32 * j)));
                }

                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
            }
            // end stack of hull

            // start circle
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "e"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));

            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-1).x), String.valueOf(hull.get(hull.size()-1).y), "e"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));

            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "e"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));
            // end circle

            // start blue
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            for (int j = 0; j < hull.size(); j++) {
                if (j == 0) {
                    strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                }
                else {
                    strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                }
            }
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "blue");
            attributes.put("pen", "ultrafat (2.0)");
            paths.add(new Path(strPoints, attributes));
            // end blue

            layers.add(new Layer(paths, uses, texts));
            // end layer 0

            // start logic
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            Point p1 = new Point(hull.get(hull.size()-2).x, hull.get(hull.size()-2).y);
            Point p2 = new Point(hull.get(hull.size()-1).x, hull.get(hull.size()-1).y);
            Point p3 = new Point(points.get(i).x, points.get(i).y);
            LineSegment lineSegment = new LineSegment(p1, p2);

            if (lineSegment.crossProductToPoint(p3) < 0) {

                // start red dashed layer //
                attributes = new HashMap<>();
                paths = new ArrayList<>();
                uses = new ArrayList<>();
                texts = new ArrayList<>();
                strPoints = new ArrayList<>();

                // start points and numbers
                for (int j = 0; j < initialPoints.size(); j++) {

                    if (!duplicates.contains(initialPoints.get(j))) {
                        attributes = new HashMap<>();

                        // USE
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("name", "mark/disk(sx)");
                        attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                        attributes.put("size", "large (5.0)");
                        attributes.put("stroke", "black");
                        uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                        attributes = new HashMap<>();

                        // TEXT
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("transformations", "translations");
                        attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                        attributes.put("stroke", "black");
                        attributes.put("type", "label");
                        attributes.put("width", "14.6575");
                        attributes.put("height", "18.59");
                        attributes.put("depth", "0.0825");
                        attributes.put("valign", "baseline");
                        texts.add(new Text(String.valueOf(j+1), attributes));
                    }
                }
                // end points and numbers

                for (int j = 0; j < points.size(); j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    attributes.put("dash", "dashed");
                    paths.add(new Path(strPoints, attributes));
                }

                // start stack
                for (int j = 0; j < 18; j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
                    strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("h"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    paths.add(new Path(strPoints, attributes));
                }
                // end stack

                // start stack of hull
                for (int j = 0; j < hull.size(); j++) {
                    attributes = new HashMap<>();

                    if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                        attributes.put("pos", 16 + " " + (8 + (32 * j)));
                    }
                    else {
                        attributes.put("pos", 24 + " " + (8 + (32 * j)));
                    }

                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
                }
                // end stack of hull

                // start circle
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-1).x), String.valueOf(hull.get(hull.size()-1).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
                // end circle

                // start blue
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                for (int j = 0; j < hull.size(); j++) {
                    if (j == 0) {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                    }
                    else {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                    }
                }
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "blue");
                attributes.put("pen", "ultrafat (2.0)");
                paths.add(new Path(strPoints, attributes));
                // end blue

                // start red dashed
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.peek().x), String.valueOf(hull.peek().y), "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "l"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "red");
                attributes.put("pen", "ultrafat (2.0)");
                attributes.put("dash", "dashed");
                paths.add(new Path(strPoints, attributes));
                // end red dashed

                layers.add(new Layer(paths, uses, texts));
                // end red dashed layer //

                // start remove red dashed layer //
                attributes = new HashMap<>();
                paths = new ArrayList<>();
                uses = new ArrayList<>();
                texts = new ArrayList<>();
                strPoints = new ArrayList<>();

                // start points and numbers
                for (int j = 0; j < initialPoints.size(); j++) {

                    if (!duplicates.contains(initialPoints.get(j))) {
                        attributes = new HashMap<>();

                        // USE
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("name", "mark/disk(sx)");
                        attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                        attributes.put("size", "large (5.0)");
                        attributes.put("stroke", "black");
                        uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                        attributes = new HashMap<>();

                        // TEXT
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("transformations", "translations");
                        attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                        attributes.put("stroke", "black");
                        attributes.put("type", "label");
                        attributes.put("width", "14.6575");
                        attributes.put("height", "18.59");
                        attributes.put("depth", "0.0825");
                        attributes.put("valign", "baseline");
                        texts.add(new Text(String.valueOf(j+1), attributes));
                    }
                }
                // end points and numbers

                for (int j = 0; j < points.size(); j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    attributes.put("dash", "dashed");
                    paths.add(new Path(strPoints, attributes));
                }

                // start stack
                for (int j = 0; j < 18; j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
                    strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("h"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    paths.add(new Path(strPoints, attributes));
                }
                // end stack

                // start stack of hull
                for (int j = 0; j < hull.size(); j++) {
                    attributes = new HashMap<>();

                    if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                        attributes.put("pos", 16 + " " + (8 + (32 * j)));
                    }
                    else {
                        attributes.put("pos", 24 + " " + (8 + (32 * j)));
                    }

                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
                }
                // end stack of hull

                // start circle
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-1).x), String.valueOf(hull.get(hull.size()-1).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
                // end circle

                // start blue
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                for (int j = 0; j < hull.size(); j++) {
                    if (j == 0) {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                    }
                    else {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                    }
                }
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "blue");
                attributes.put("pen", "ultrafat (2.0)");
                paths.add(new Path(strPoints, attributes));
                // end blue

                layers.add(new Layer(paths, uses, texts));
                // end remove red dashed layer //

                // start remove blue layer //
                attributes = new HashMap<>();
                paths = new ArrayList<>();
                uses = new ArrayList<>();
                texts = new ArrayList<>();
                strPoints = new ArrayList<>();

                // start points and numbers
                for (int j = 0; j < initialPoints.size(); j++) {

                    if (!duplicates.contains(initialPoints.get(j))) {
                        attributes = new HashMap<>();

                        // USE
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("name", "mark/disk(sx)");
                        attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                        attributes.put("size", "large (5.0)");
                        attributes.put("stroke", "black");
                        uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                        attributes = new HashMap<>();

                        // TEXT
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("transformations", "translations");
                        attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                        attributes.put("stroke", "black");
                        attributes.put("type", "label");
                        attributes.put("width", "14.6575");
                        attributes.put("height", "18.59");
                        attributes.put("depth", "0.0825");
                        attributes.put("valign", "baseline");
                        texts.add(new Text(String.valueOf(j+1), attributes));
                    }
                }
                // end points and numbers

                for (int j = 0; j < points.size(); j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    attributes.put("dash", "dashed");
                    paths.add(new Path(strPoints, attributes));
                }

                // start stack
                for (int j = 0; j < 18; j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
                    strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("h"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    paths.add(new Path(strPoints, attributes));
                }
                // end stack

                // start stack of hull -1
                for (int j = 0; j < hull.size(); j++) {
                    attributes = new HashMap<>();

                    if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                        attributes.put("pos", 16 + " " + (8 + (32 * j)));
                    }
                    else {
                        attributes.put("pos", 24 + " " + (8 + (32 * j)));
                    }

                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
                }
                // end stack of hull -1

                // start circle
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-1).x), String.valueOf(hull.get(hull.size()-1).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
                // end circle

                // start blue -1
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                for (int j = 0; j < hull.size()-1; j++) {
                    if (j == 0) {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                    }
                    else {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                    }
                }
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "blue");
                attributes.put("pen", "ultrafat (2.0)");
                paths.add(new Path(strPoints, attributes));
                // end blue -1

                layers.add(new Layer(paths, uses, texts));
                // end remove blue layer //

                hull.pop();

            }
            else {
                hull.push(points.get(i));

                // start green dashed layer //
                attributes = new HashMap<>();
                paths = new ArrayList<>();
                uses = new ArrayList<>();
                texts = new ArrayList<>();
                strPoints = new ArrayList<>();

                // start points and numbers
                for (int j = 0; j < initialPoints.size(); j++) {

                    if (!duplicates.contains(initialPoints.get(j))) {
                        attributes = new HashMap<>();

                        // USE
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("name", "mark/disk(sx)");
                        attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                        attributes.put("size", "large (5.0)");
                        attributes.put("stroke", "black");
                        uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                        attributes = new HashMap<>();

                        // TEXT
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("transformations", "translations");
                        attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                        attributes.put("stroke", "black");
                        attributes.put("type", "label");
                        attributes.put("width", "14.6575");
                        attributes.put("height", "18.59");
                        attributes.put("depth", "0.0825");
                        attributes.put("valign", "baseline");
                        texts.add(new Text(String.valueOf(j+1), attributes));
                    }
                }
                // end points and numbers

                for (int j = 0; j < points.size(); j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    attributes.put("dash", "dashed");
                    paths.add(new Path(strPoints, attributes));
                }

                // start stack
                for (int j = 0; j < 18; j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
                    strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("h"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    paths.add(new Path(strPoints, attributes));
                }
                // end stack

                // start stack of hull
                for (int j = 0; j < hull.size(); j++) {
                    attributes = new HashMap<>();

                    if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                        attributes.put("pos", 16 + " " + (8 + (32 * j)));
                    }
                    else {
                        attributes.put("pos", 24 + " " + (8 + (32 * j)));
                    }

                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
                }
                // end stack of hull

                // start circle
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-3).x), String.valueOf(hull.get(hull.size()-3).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-1).x), String.valueOf(hull.get(hull.size()-1).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
                // end circle

                // start blue -1
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                for (int j = 0; j < hull.size()-1; j++) {
                    if (j == 0) {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                    }
                    else {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                    }
                }
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "blue");
                attributes.put("pen", "ultrafat (2.0)");
                paths.add(new Path(strPoints, attributes));
                // end blue -1

                // start green dashed
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.peek().x), String.valueOf(hull.peek().y), "l"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "green");
                attributes.put("pen", "ultrafat (2.0)");
                attributes.put("dash", "dashed");
                paths.add(new Path(strPoints, attributes));
                // end green dashed

                layers.add(new Layer(paths, uses, texts));
                // end green dashed layer //

                // start green to blue layer //
                attributes = new HashMap<>();
                paths = new ArrayList<>();
                uses = new ArrayList<>();
                texts = new ArrayList<>();
                strPoints = new ArrayList<>();

                // start points and numbers
                for (int j = 0; j < initialPoints.size(); j++) {

                    if (!duplicates.contains(initialPoints.get(j))) {
                        attributes = new HashMap<>();

                        // USE
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("name", "mark/disk(sx)");
                        attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                        attributes.put("size", "large (5.0)");
                        attributes.put("stroke", "black");
                        uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                        attributes = new HashMap<>();

                        // TEXT
                        attributes.put("layer", String.valueOf(layers.size()));
                        attributes.put("transformations", "translations");
                        attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                        attributes.put("stroke", "black");
                        attributes.put("type", "label");
                        attributes.put("width", "14.6575");
                        attributes.put("height", "18.59");
                        attributes.put("depth", "0.0825");
                        attributes.put("valign", "baseline");
                        texts.add(new Text(String.valueOf(j+1), attributes));
                    }
                }
                // end points and numbers

                for (int j = 0; j < points.size(); j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    attributes.put("dash", "dashed");
                    paths.add(new Path(strPoints, attributes));
                }

                // start stack
                for (int j = 0; j < 18; j++) {
                    attributes = new HashMap<>();
                    strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
                    strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
                    strPoints.add(new Ipe.Object.Point("h"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("stroke", "black");
                    paths.add(new Path(strPoints, attributes));
                }
                // end stack

                // start stack of hull
                for (int j = 0; j < hull.size(); j++) {
                    attributes = new HashMap<>();

                    if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                        attributes.put("pos", 16 + " " + (8 + (32 * j)));
                    }
                    else {
                        attributes.put("pos", 24 + " " + (8 + (32 * j)));
                    }

                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
                }
                // end stack of hull

                // start circle
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-3).x), String.valueOf(hull.get(hull.size()-3).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-2).x), String.valueOf(hull.get(hull.size()-2).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));

                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(hull.size()-1).x), String.valueOf(hull.get(hull.size()-1).y), "e"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
                // end circle

                // start blue
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                for (int j = 0; j < hull.size(); j++) {
                    if (j == 0) {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                    }
                    else {
                        strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                    }
                }
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "blue");
                attributes.put("pen", "ultrafat (2.0)");
                paths.add(new Path(strPoints, attributes));
                // end blue

                layers.add(new Layer(paths, uses, texts));
                // end green to blue layer //

                i++;
            }
            // end logic

            // start remove circle layer //
            attributes = new HashMap<>();
            paths = new ArrayList<>();
            uses = new ArrayList<>();
            texts = new ArrayList<>();
            strPoints = new ArrayList<>();

            // start points and numbers
            for (int j = 0; j < initialPoints.size(); j++) {

                if (!duplicates.contains(initialPoints.get(j))) {
                    attributes = new HashMap<>();

                    // USE
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("name", "mark/disk(sx)");
                    attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                    attributes.put("size", "large (5.0)");
                    attributes.put("stroke", "black");
                    uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                    attributes = new HashMap<>();

                    // TEXT
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("transformations", "translations");
                    attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                    attributes.put("stroke", "black");
                    attributes.put("type", "label");
                    attributes.put("width", "14.6575");
                    attributes.put("height", "18.59");
                    attributes.put("depth", "0.0825");
                    attributes.put("valign", "baseline");
                    texts.add(new Text(String.valueOf(j+1), attributes));
                }
            }
            // end points and numbers

            for (int j = 0; j < points.size(); j++) {
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                attributes.put("dash", "dashed");
                paths.add(new Path(strPoints, attributes));
            }

            // start stack
            for (int j = 0; j < 18; j++) {
                attributes = new HashMap<>();
                strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
                strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
                strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
                strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("stroke", "black");
                paths.add(new Path(strPoints, attributes));
            }
            // end stack

            // start stack of hull
            for (int j = 0; j < hull.size(); j++) {
                attributes = new HashMap<>();

                if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                    attributes.put("pos", 16 + " " + (8 + (32 * j)));
                }
                else {
                    attributes.put("pos", 24 + " " + (8 + (32 * j)));
                }

                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
            }
            // end stack of hull

            // start blue
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            for (int j = 0; j < hull.size(); j++) {
                if (j == 0) {
                    strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
                }
                else {
                    strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
                }
            }
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "blue");
            attributes.put("pen", "ultrafat (2.0)");
            paths.add(new Path(strPoints, attributes));
            // end blue

            layers.add(new Layer(paths, uses, texts));
            // end remove circle layer //
        }

        // Scenario (complete)
        attributes = new HashMap<>();
        paths = new ArrayList<>();
        uses = new ArrayList<>();
        texts = new ArrayList<>();
        strPoints = new ArrayList<>();

        for (int j = 0; j < initialPoints.size(); j++) {

            if (!duplicates.contains(initialPoints.get(j))) {
                attributes = new HashMap<>();

                // USE
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("name", "mark/disk(sx)");
                attributes.put("pos", (initialPoints.get(j).x) + " " + (initialPoints.get(j).y));
                attributes.put("size", "large (5.0)");
                attributes.put("stroke", "black");
                uses.add(new Use(new Ipe.Object.Point(String.valueOf(initialPoints.get(j).x), String.valueOf(initialPoints.get(j).y)), attributes));

                attributes = new HashMap<>();

                // TEXT
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (initialPoints.get(j).x + 8) + " " + (initialPoints.get(j).y + 8));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text(String.valueOf(j+1), attributes));
            }
        }

        for (int j = 0; j < points.size(); j++) {
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(initialPoint.x), String.valueOf(initialPoint.y), "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(j).x), String.valueOf(points.get(j).y), "l"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            attributes.put("dash", "dashed");
            paths.add(new Path(strPoints, attributes));
        }

        for (int j = 0; j < 18; j++) {
            attributes = new HashMap<>();
            strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point("0", String.valueOf(j * 32), "m"));
            strPoints.add(new Ipe.Object.Point("0", String.valueOf((j + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf((j + 1) * 32), "l"));
            strPoints.add(new Ipe.Object.Point("64", String.valueOf(j * 32), "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            paths.add(new Path(strPoints, attributes));
        }

        for (int j = 0; j < hull.size(); j++) {
            attributes = new HashMap<>();

            if ((initialPoints.indexOf(hull.get(j)) + 1) > 9) {
                attributes.put("pos", 16 + " " + (8 + (32 * j)));
            }
            else {
                attributes.put("pos", 24 + " " + (8 + (32 * j)));
            }

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text(String.valueOf(initialPoints.indexOf(hull.get(j)) + 1), attributes));
        }

        attributes = new HashMap<>();
        strPoints = new ArrayList<>();

        for (int j = 0; j < hull.size(); j++) {
            if (j == 0) {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "m"));
            }
            else {
                strPoints.add(new Ipe.Object.Point(String.valueOf(hull.get(j).x), String.valueOf(hull.get(j).y), "l"));
            }
        }

        strPoints.add(new Ipe.Object.Point("h"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "blue");
        attributes.put("pen", "ultrafat (2.0)");
        paths.add(new Path(strPoints, attributes));

        layers.add(new Layer(paths, uses, texts));
    }

    public int getInitialPointIndex() {
        double min = Double.MAX_VALUE;
        int idx = 0;
        for(int i = 0; i < points.size(); i++) {
            if(points.get(i).y < min) {
                min = points.get(i).y;
                idx = i;
            }
        }
        return idx;
    }

    public void sortPoints() {

        int initialPointIndex = getInitialPointIndex();
        initialPoint = points.get(initialPointIndex);
        points.remove(initialPointIndex);

        ArrayList<Point> pointsTemp = new ArrayList<>(points);
        pointsTemp.sort((a, b) -> {
            if (new LineSegment(new Point(initialPoint.x, initialPoint.y), new Point(a.x, a.y)).crossProductToPoint(b) > 0) {
                return -1;
            }
            else if (new LineSegment(new Point(initialPoint.x, initialPoint.y), new Point(a.x, a.y)).crossProductToPoint(b) < 0) {
                return 1;
            }
            else {
                if (a.y > b.y) {
                    duplicates.add(b);
                }
                else if (a.y < b.y) {
                    duplicates.add(a);
                }
                else if (a.x > b.x) {
                    duplicates.add(b);
                }
                else if (a.x < b.x) {
                    duplicates.add(a);
                }
                else {
                    duplicates.add(a);
                }
                return 0;
            }
        });

        for (Point dupe : duplicates) {
            pointsTemp.remove(dupe);
        }

        points.clear();
        points.addAll(pointsTemp);
        pointsTemp.clear();
    }

    public void setPoints(ArrayList<Use> uses) {
        for (Use u : uses) {
            points.add(new Point(Double.parseDouble(u.pos.x), Double.parseDouble(u.pos.y)));
        }
    }
}
