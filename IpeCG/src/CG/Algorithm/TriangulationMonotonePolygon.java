package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;
import Ipe.Object.Text;
import Ipe.Object.Use;

import java.util.*;

public class TriangulationMonotonePolygon {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();
    public ArrayList<Point> sortedPoints = new ArrayList<>();
    public Point top = new Point();
    public Point bottom = new Point();
    public Queue<Point> leftChain = new LinkedList<>();
    public Queue<Point> rightChain = new LinkedList<>();
    public ArrayList<LineSegment> lineSegments = new ArrayList<>();
    public ArrayList<Point> pointsQueue = new ArrayList<>();

    public TriangulationMonotonePolygon(Path path) {
        if (path.points.size() > 3) {
            setPoints(path);
            sortPoints();
            generateLayers();
        }
        else {
            System.out.println("invalid object");
        }
    }

    public ArrayList<Path> setPolygonIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(0).x), String.valueOf(points.get(0).y), "m"));
        for (int i = 1; i < points.size(); i++) {
            strPoints.add(new Ipe.Object.Point(String.valueOf(points.get(i).x), String.valueOf(points.get(i).y), "l"));
        }
        strPoints.add(new Ipe.Object.Point("h"));
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("stroke", "black");
        attributes.put("pen", "fat (1.2)");
        paths.add(new Path(strPoints, attributes));

        return paths;
    }

    public ArrayList<Use> setPointsIpe() {
        ArrayList<Use> uses = new ArrayList<>();

        for (Point point : points) {
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("name", "mark/disk(sx)");
            attributes.put("pos", point.x + " " + point.y);
            attributes.put("size", "large (5.0)");
            attributes.put("stroke", "black");
            uses.add(new Use(new Ipe.Object.Point(String.valueOf(point.x), String.valueOf(point.y)), attributes));
        }

        return uses;
    }

    public ArrayList<Text> setPointLabelsIpe() {
        ArrayList<Text> texts = new ArrayList<>();
        Queue<Point> leftChainTmp = new LinkedList<>(leftChain);
        Queue<Point> rightChainTmp = new LinkedList<>(rightChain);
        HashMap<String, String> attributes = new HashMap<>();

        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("transformations", "translations");
        attributes.put("pos", (top.x - 16) + " " + (top.y + 16));
        attributes.put("stroke", "black");
        attributes.put("type", "label");
        attributes.put("width", "14.6575");
        attributes.put("height", "18.59");
        attributes.put("depth", "0.0825");
        attributes.put("valign", "baseline");
        texts.add(new Text("$p_{0}$", attributes));

        attributes = new HashMap<>();
        attributes.put("layer", String.valueOf(layers.size()));
        attributes.put("transformations", "translations");
        attributes.put("pos", (bottom.x - 16) + " " + (bottom.y - 16));
        attributes.put("stroke", "black");
        attributes.put("type", "label");
        attributes.put("width", "14.6575");
        attributes.put("height", "18.59");
        attributes.put("depth", "0.0825");
        attributes.put("valign", "baseline");
        texts.add(new Text("$p_{" + (points.size()-1) + "}$", attributes));

        int i = 1;
        while (leftChainTmp.size() > 0 && rightChainTmp.size() > 0) {
            attributes = new HashMap<>();
            if (leftChainTmp.peek().y >= rightChainTmp.peek().y) {
                attributes.put("pos", (leftChainTmp.peek().x - 32) + " " + (leftChainTmp.peek().y));
                leftChainTmp.poll();
            }
            else {
                attributes.put("pos", (rightChainTmp.peek().x + 16) + " " + (rightChainTmp.peek().y));
                rightChainTmp.poll();
            }
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text("$p_{" + i + "}$", attributes));

            i++;
        }
        if (leftChainTmp.size() > 0) {
            while (leftChainTmp.size() > 0) {
                attributes = new HashMap<>();
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (leftChainTmp.peek().x - 32) + " " + (leftChainTmp.peek().y));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text("$p_{" + i + "}$", attributes));

                leftChainTmp.poll();
                i++;
            }
        }
        else if (rightChainTmp.size() > 0) {
            while (rightChainTmp.size() > 0) {
                attributes = new HashMap<>();
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("transformations", "translations");
                attributes.put("pos", (rightChainTmp.peek().x + 16) + " " + (rightChainTmp.peek().y));
                attributes.put("stroke", "black");
                attributes.put("type", "label");
                attributes.put("width", "14.6575");
                attributes.put("height", "18.59");
                attributes.put("depth", "0.0825");
                attributes.put("valign", "baseline");
                texts.add(new Text("$p_{" + i + "}$", attributes));

                rightChainTmp.poll();
                i++;
            }
        }

        return texts;
    }

    public ArrayList<Path> setDataFrameIpe() {
        ArrayList<Path> paths = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "0", "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "0", "l"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "48", "l"));
            strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "48", "l"));
            strPoints.add(new Ipe.Object.Point("h"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "black");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }
        return paths;
    }

    public ArrayList<Text> setDataTextIpe() {
        ArrayList<Text> texts = new ArrayList<>();

        for (int i = 0; i < pointsQueue.size(); i++) {
            HashMap<String, String> attributes = new HashMap<>();

            if (i > 9) {
                attributes.put("pos", (48 * (i + 1)) + 36 + " " + 16);
            }
            else {
                attributes.put("pos", (48 * (i + 1)) + 44 + " " + 16);
            }

            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("transformations", "translations");
            attributes.put("stroke", "black");
            attributes.put("type", "label");
            attributes.put("width", "14.6575");
            attributes.put("height", "18.59");
            attributes.put("depth", "0.0825");
            attributes.put("valign", "baseline");
            texts.add(new Text("$p_{" + sortedPoints.indexOf(pointsQueue.get(i)) + "}$", attributes));
        }

        return texts;
    }

    public ArrayList<Path> setLineSegmentsIpe() {
        ArrayList<Path> paths = new ArrayList<>();

        for (LineSegment ls : lineSegments) {
            HashMap<String, String> attributes = new HashMap<>();
            ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

            strPoints.add(new Ipe.Object.Point(String.valueOf(ls.p1.x), String.valueOf(ls.p1.y), "m"));
            strPoints.add(new Ipe.Object.Point(String.valueOf(ls.p2.x), String.valueOf(ls.p2.y), "l"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "red");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }

        return paths;
    }

    public ArrayList<Path> setHighlightsPointIpe(Point p1, Point p2, Point p3) {
        ArrayList<Path> paths = new ArrayList<>();
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        if (p1 != null) {
            strPoints.add(new Ipe.Object.Point(String.valueOf(p1.x), String.valueOf(p1.y), "e"));
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "red");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }

        if (p2 != null) {
            strPoints = new ArrayList<>();
            strPoints.add(new Ipe.Object.Point(String.valueOf(p2.x), String.valueOf(p2.y), "e"));
            attributes = new HashMap<>();
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "red");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }

        if (p3 != null) {
            strPoints = new ArrayList<>();
            strPoints.add(new Ipe.Object.Point(String.valueOf(p3.x), String.valueOf(p3.y), "e"));
            attributes = new HashMap<>();
            attributes.put("layer", String.valueOf(layers.size()));
            attributes.put("stroke", "red");
            attributes.put("pen", "fat (1.2)");
            paths.add(new Path(strPoints, attributes));
        }

        return paths;
    }

    public ArrayList<Path> setHighlightsQueueIpe(int n) {
        ArrayList<Path> paths = new ArrayList<>();
        if (n == 0) {
            for (int i = 0; i < pointsQueue.size()-2; i++) {
                HashMap<String, String> attributes = new HashMap<>();
                ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "0", "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "0", "l"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "48", "l"));
                strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "48", "l"));
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("opacity", "50%");
                attributes.put("stroke-opacity", "opaque");
                attributes.put("fill", "red");
                paths.add(new Path(strPoints, attributes));
            }
        }
        else if (n == 1) {
            for (int i = 0; i < pointsQueue.size()-1; i++) {
                if (i == pointsQueue.size()-2) {
                    HashMap<String, String> attributes = new HashMap<>();
                    ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

                    strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "0", "m"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "0", "l"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "48", "l"));
                    strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "48", "l"));
                    strPoints.add(new Ipe.Object.Point("h"));
                    attributes.put("layer", String.valueOf(layers.size()));
                    attributes.put("opacity", "50%");
                    attributes.put("stroke-opacity", "opaque");
                    attributes.put("fill", "red");
                    paths.add(new Path(strPoints, attributes));
                }
            }
        }
        else if (n == 2) {
            for (int i = 0; i < pointsQueue.size(); i++) {
                HashMap<String, String> attributes = new HashMap<>();
                ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "0", "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "0", "l"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "48", "l"));
                strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "48", "l"));
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("opacity", "50%");
                attributes.put("stroke-opacity", "opaque");
                attributes.put("fill", "red");
                paths.add(new Path(strPoints, attributes));
            }
        }
        else if (n == 3) {
            for (int i = 0; i < pointsQueue.size()-1; i++) {
                HashMap<String, String> attributes = new HashMap<>();
                ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

                strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "0", "m"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "0", "l"));
                strPoints.add(new Ipe.Object.Point(String.valueOf(((i + 1) * 48) + 80), "48", "l"));
                strPoints.add(new Ipe.Object.Point(String.valueOf((i * 48) + 80), "48", "l"));
                strPoints.add(new Ipe.Object.Point("h"));
                attributes.put("layer", String.valueOf(layers.size()));
                attributes.put("opacity", "50%");
                attributes.put("stroke-opacity", "opaque");
                attributes.put("fill", "red");
                paths.add(new Path(strPoints, attributes));
            }
        }
        return paths;
    }

    public void generateLayers() {
        Queue<Point> leftChainTmp = new LinkedList<>(leftChain);
        Queue<Point> rightChainTmp = new LinkedList<>(rightChain);
        boolean isLeftSide = false;

        // Layer
        ArrayList<Path> paths = new ArrayList<>(setPolygonIpe());
        paths.addAll(setDataFrameIpe());
        ArrayList<Use> uses = new ArrayList<>(setPointsIpe());
        ArrayList<Text> texts = new ArrayList<>(setPointLabelsIpe());
        texts.addAll(setDataTextIpe());
        layers.add(new Layer(paths, uses, texts));

        // Set two initial points
        pointsQueue.add(top);
        // Layer
        paths = new ArrayList<>(setPolygonIpe());
        paths.addAll(setDataFrameIpe());
        paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), null, null));
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setPointLabelsIpe());
        texts.addAll(setDataTextIpe());
        layers.add(new Layer(paths, uses, texts));

        if (leftChainTmp.size() > 0 && rightChainTmp.size() > 0) {
            if (leftChainTmp.peek().y >= rightChainTmp.peek().y) {
                pointsQueue.add(leftChainTmp.peek());
                leftChainTmp.poll();
                isLeftSide = true;
            }
            else {
                pointsQueue.add(rightChainTmp.peek());
                rightChainTmp.poll();
            }
        }
        else if (leftChainTmp.size() > 1) {
            pointsQueue.add(leftChainTmp.peek());
            leftChainTmp.poll();
        }
        else if (rightChainTmp.size() > 0) {
            pointsQueue.add(rightChainTmp.peek());
            rightChainTmp.poll();
        }

        // Layer
        paths = new ArrayList<>(setPolygonIpe());
        paths.addAll(setDataFrameIpe());
        paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setPointLabelsIpe());
        texts.addAll(setDataTextIpe());
        layers.add(new Layer(paths, uses, texts));

        while (leftChainTmp.size() > 0 && rightChainTmp.size() > 0) {

            // Left
            if (leftChainTmp.peek().y >= rightChainTmp.peek().y) {
                pointsQueue.add(leftChainTmp.peek());
                leftChainTmp.poll();

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                // From Left
                if (isLeftSide) {
                    // While there is convex
                    while (pointsQueue.size() > 2) {
                        LineSegment ls = new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-2));
                        // If convex
                        if (ls.crossProductToPoint(pointsQueue.get(pointsQueue.size()-1)) > 0) {
                            lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                            // Layer
                            paths = new ArrayList<>(setPolygonIpe());
                            paths.addAll(setDataFrameIpe());
                            paths.addAll(setLineSegmentsIpe());
                            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                            paths.addAll(setHighlightsQueueIpe(1));
                            uses = new ArrayList<>(setPointsIpe());
                            texts = new ArrayList<>(setPointLabelsIpe());
                            texts.addAll(setDataTextIpe());
                            layers.add(new Layer(paths, uses, texts));

                            pointsQueue.remove(pointsQueue.size()-2);

                            // Layer
                            paths = new ArrayList<>(setPolygonIpe());
                            paths.addAll(setDataFrameIpe());
                            paths.addAll(setLineSegmentsIpe());
                            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                            uses = new ArrayList<>(setPointsIpe());
                            texts = new ArrayList<>(setPointLabelsIpe());
                            texts.addAll(setDataTextIpe());
                            layers.add(new Layer(paths, uses, texts));
                        }
                        // If concave
                        else {
                            break;
                        }
                    }
                }
                // From right (change side)
                else {
                    isLeftSide = true;
                    lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2)));

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                    paths.addAll(setHighlightsQueueIpe(0));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    // Draw all the line segments with each point on the points queue
                    while (pointsQueue.size() > 2) {
                        if (!rightChain.contains(pointsQueue.get(pointsQueue.size()-3))) {
                            pointsQueue.remove(pointsQueue.size()-3);

                            // Layer
                            paths = new ArrayList<>(setPolygonIpe());
                            paths.addAll(setDataFrameIpe());
                            paths.addAll(setLineSegmentsIpe());
                            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                            paths.addAll(setHighlightsQueueIpe(0));
                            uses = new ArrayList<>(setPointsIpe());
                            texts = new ArrayList<>(setPointLabelsIpe());
                            texts.addAll(setDataTextIpe());
                            layers.add(new Layer(paths, uses, texts));

                            break;
                        }

                        lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-3)));

                        // Layer
                        paths = new ArrayList<>(setPolygonIpe());
                        paths.addAll(setDataFrameIpe());
                        paths.addAll(setLineSegmentsIpe());
                        paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                        paths.addAll(setHighlightsQueueIpe(0));
                        uses = new ArrayList<>(setPointsIpe());
                        texts = new ArrayList<>(setPointLabelsIpe());
                        texts.addAll(setDataTextIpe());
                        layers.add(new Layer(paths, uses, texts));

                        pointsQueue.remove(pointsQueue.size()-3);

                        if (pointsQueue.size() == 3) {
                            pointsQueue.remove(0);
                        }

                        // Layer
                        paths = new ArrayList<>(setPolygonIpe());
                        paths.addAll(setDataFrameIpe());
                        paths.addAll(setLineSegmentsIpe());
                        paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                        paths.addAll(setHighlightsQueueIpe(0));
                        uses = new ArrayList<>(setPointsIpe());
                        texts = new ArrayList<>(setPointLabelsIpe());
                        texts.addAll(setDataTextIpe());
                        layers.add(new Layer(paths, uses, texts));
                    }
                }
            }
            // Right
            else {
                pointsQueue.add(rightChainTmp.peek());
                rightChainTmp.poll();

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                // From left (change side)
                if (isLeftSide) {
                    isLeftSide = false;
                    lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2)));

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                    paths.addAll(setHighlightsQueueIpe(0));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    // Draw all the line segments with each point on the points queue
                    while (pointsQueue.size() > 2) {
                        if (!leftChain.contains(pointsQueue.get(pointsQueue.size()-3))) {
                            pointsQueue.remove(pointsQueue.size()-3);

                            // Layer
                            paths = new ArrayList<>(setPolygonIpe());
                            paths.addAll(setDataFrameIpe());
                            paths.addAll(setLineSegmentsIpe());
                            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                            paths.addAll(setHighlightsQueueIpe(0));
                            uses = new ArrayList<>(setPointsIpe());
                            texts = new ArrayList<>(setPointLabelsIpe());
                            texts.addAll(setDataTextIpe());
                            layers.add(new Layer(paths, uses, texts));

                            break;
                        }

                        lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-3)));

                        // Layer
                        paths = new ArrayList<>(setPolygonIpe());
                        paths.addAll(setDataFrameIpe());
                        paths.addAll(setLineSegmentsIpe());
                        paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                        paths.addAll(setHighlightsQueueIpe(0));
                        uses = new ArrayList<>(setPointsIpe());
                        texts = new ArrayList<>(setPointLabelsIpe());
                        texts.addAll(setDataTextIpe());
                        layers.add(new Layer(paths, uses, texts));

                        pointsQueue.remove(pointsQueue.size()-3);

                        if (pointsQueue.size() == 3) {
                            pointsQueue.remove(0);
                        }

                        // Layer
                        paths = new ArrayList<>(setPolygonIpe());
                        paths.addAll(setDataFrameIpe());
                        paths.addAll(setLineSegmentsIpe());
                        paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                        paths.addAll(setHighlightsQueueIpe(0));
                        uses = new ArrayList<>(setPointsIpe());
                        texts = new ArrayList<>(setPointLabelsIpe());
                        texts.addAll(setDataTextIpe());
                        layers.add(new Layer(paths, uses, texts));
                    }
                }
                // From right
                else {
                    // While there is convex
                    while (pointsQueue.size() > 2) {
                        LineSegment ls = new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-2));
                        // If convex
                        if (ls.crossProductToPoint(pointsQueue.get(pointsQueue.size()-1)) < 0) {
                            lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                            // Layer
                            paths = new ArrayList<>(setPolygonIpe());
                            paths.addAll(setDataFrameIpe());
                            paths.addAll(setLineSegmentsIpe());
                            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                            paths.addAll(setHighlightsQueueIpe(1));
                            uses = new ArrayList<>(setPointsIpe());
                            texts = new ArrayList<>(setPointLabelsIpe());
                            texts.addAll(setDataTextIpe());
                            layers.add(new Layer(paths, uses, texts));

                            pointsQueue.remove(pointsQueue.size()-2);

                            // Layer
                            paths = new ArrayList<>(setPolygonIpe());
                            paths.addAll(setDataFrameIpe());
                            paths.addAll(setLineSegmentsIpe());
                            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                            uses = new ArrayList<>(setPointsIpe());
                            texts = new ArrayList<>(setPointLabelsIpe());
                            texts.addAll(setDataTextIpe());
                            layers.add(new Layer(paths, uses, texts));
                        }
                        // If concave
                        else {
                            break;
                        }
                    }
                }
            }
        }

        // If only left chain remains
        if (leftChainTmp.size() > 0) {
            pointsQueue.add(leftChainTmp.peek());
            leftChainTmp.poll();

            // Layer
            paths = new ArrayList<>(setPolygonIpe());
            paths.addAll(setDataFrameIpe());
            paths.addAll(setLineSegmentsIpe());
            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
            uses = new ArrayList<>(setPointsIpe());
            texts = new ArrayList<>(setPointLabelsIpe());
            texts.addAll(setDataTextIpe());
            layers.add(new Layer(paths, uses, texts));

            if (leftChain.contains(pointsQueue.get(pointsQueue.size()-2))) {
                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                paths.addAll(setHighlightsQueueIpe(1));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                pointsQueue.remove(pointsQueue.size()-2);

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }
            else {
                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-1)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                paths.addAll(setHighlightsQueueIpe(0));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

//                pointsQueue.remove(pointsQueue.size()-3);
//
//                // Layer
//                paths = new ArrayList<>(setPolygonIpe());
//                paths.addAll(setDataFrameIpe());
//                paths.addAll(setLineSegmentsIpe());
//                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
//                uses = new ArrayList<>(setPointsIpe());
//                texts = new ArrayList<>(setPointLabelsIpe());
//                texts.addAll(setDataTextIpe());
//                layers.add(new Layer(paths, uses, texts));
            }

            // Draw all the line segments with each point on the points queue
            while (pointsQueue.size() > 2) {
                if (!rightChain.contains(pointsQueue.get(pointsQueue.size()-3))) {
                    pointsQueue.remove(pointsQueue.size()-3);

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                    paths.addAll(setHighlightsQueueIpe(0));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    break;
                }

                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-3)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                paths.addAll(setHighlightsQueueIpe(0));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                pointsQueue.remove(pointsQueue.size()-3);

                if (pointsQueue.size() == 3) {
                    pointsQueue.remove(0);
                }

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                paths.addAll(setHighlightsQueueIpe(0));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }

            // Draw all the line segments with each point on the left chain
            while (leftChainTmp.size() > 0) {
                pointsQueue.add(leftChainTmp.peek());
                leftChainTmp.poll();

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                LineSegment ls = new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-2));
                // If convex
                if (ls.crossProductToPoint(pointsQueue.get(pointsQueue.size()-1)) > 0) {
                    lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                    paths.addAll(setHighlightsQueueIpe(1));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    pointsQueue.remove(pointsQueue.size()-2);
                }
                else {
                    // pointsQueue.remove(pointsQueue.size()-3);
                }

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }

            if (pointsQueue.size() > 2) {
                LineSegment ls = new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-2));
                // If convex
                if (ls.crossProductToPoint(pointsQueue.get(pointsQueue.size()-1)) > 0) {
                    lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                    paths.addAll(setHighlightsQueueIpe(1));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    pointsQueue.remove(pointsQueue.size()-2);

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));
                }
            }

            if (leftChain.contains(pointsQueue.get(0))) {
                pointsQueue.add(bottom);
                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                paths.addAll(setHighlightsQueueIpe(1));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                pointsQueue.remove(pointsQueue.size()-2);

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }
        }
        // If only right chain remains
        else if (rightChainTmp.size() > 0) {
            pointsQueue.add(rightChainTmp.peek());
            rightChainTmp.poll();

            // Layer
            paths = new ArrayList<>(setPolygonIpe());
            paths.addAll(setDataFrameIpe());
            paths.addAll(setLineSegmentsIpe());
            paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
            uses = new ArrayList<>(setPointsIpe());
            texts = new ArrayList<>(setPointLabelsIpe());
            texts.addAll(setDataTextIpe());
            layers.add(new Layer(paths, uses, texts));

            if (rightChain.contains(pointsQueue.get(pointsQueue.size()-2))) {
                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                paths.addAll(setHighlightsQueueIpe(0));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                pointsQueue.remove(pointsQueue.size()-2);

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }
            else {
                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-1)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                paths.addAll(setHighlightsQueueIpe(0));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

//                pointsQueue.remove(pointsQueue.size()-3);
//
//                // Layer
//                paths = new ArrayList<>(setPolygonIpe());
//                paths.addAll(setDataFrameIpe());
//                paths.addAll(setLineSegmentsIpe());
//                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
//                uses = new ArrayList<>(setPointsIpe());
//                texts = new ArrayList<>(setPointLabelsIpe());
//                texts.addAll(setDataTextIpe());
//                layers.add(new Layer(paths, uses, texts));
            }

            // Draw all the line segments with each point on the points queue
            while (pointsQueue.size() > 2) {
                if (!leftChain.contains(pointsQueue.get(pointsQueue.size()-3))) {
                    pointsQueue.remove(pointsQueue.size()-3);

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                    paths.addAll(setHighlightsQueueIpe(0));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    break;
                }

                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-3)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                paths.addAll(setHighlightsQueueIpe(0));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                pointsQueue.remove(pointsQueue.size()-3);

                if (pointsQueue.size() == 3) {
                    pointsQueue.remove(0);
                }

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                paths.addAll(setHighlightsQueueIpe(0));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }

            // Draw all the line segments with each point on the right chain
            while (rightChainTmp.size() > 0) {
                pointsQueue.add(rightChainTmp.peek());
                rightChainTmp.poll();

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                LineSegment ls = new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-2));
                // If convex
                if (ls.crossProductToPoint(pointsQueue.get(pointsQueue.size()-1)) < 0) {
                    lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                    paths.addAll(setHighlightsQueueIpe(1));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    pointsQueue.remove(pointsQueue.size()-2);
                }
                else {
                    // pointsQueue.remove(pointsQueue.size()-3);
                }

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }

            if (pointsQueue.size() > 2) {
                LineSegment ls = new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-2));
                // If convex
                if (ls.crossProductToPoint(pointsQueue.get(pointsQueue.size()-1)) < 0) {
                    lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                    paths.addAll(setHighlightsQueueIpe(1));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));

                    pointsQueue.remove(pointsQueue.size()-2);

                    // Layer
                    paths = new ArrayList<>(setPolygonIpe());
                    paths.addAll(setDataFrameIpe());
                    paths.addAll(setLineSegmentsIpe());
                    paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                    uses = new ArrayList<>(setPointsIpe());
                    texts = new ArrayList<>(setPointLabelsIpe());
                    texts.addAll(setDataTextIpe());
                    layers.add(new Layer(paths, uses, texts));
                }
            }

            if (rightChain.contains(pointsQueue.get(0))) {
                pointsQueue.add(bottom);
                lineSegments.add(new LineSegment(pointsQueue.get(pointsQueue.size()-3), pointsQueue.get(pointsQueue.size()-1)));

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), pointsQueue.get(pointsQueue.size()-3)));
                paths.addAll(setHighlightsQueueIpe(1));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));

                pointsQueue.remove(pointsQueue.size()-2);

                // Layer
                paths = new ArrayList<>(setPolygonIpe());
                paths.addAll(setDataFrameIpe());
                paths.addAll(setLineSegmentsIpe());
                paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
                uses = new ArrayList<>(setPointsIpe());
                texts = new ArrayList<>(setPointLabelsIpe());
                texts.addAll(setDataTextIpe());
                layers.add(new Layer(paths, uses, texts));
            }
        }

        // Layer
        paths = new ArrayList<>(setPolygonIpe());
        paths.addAll(setDataFrameIpe());
        paths.addAll(setLineSegmentsIpe());
        paths.addAll(setHighlightsQueueIpe(2));
        paths.addAll(setHighlightsPointIpe(pointsQueue.get(pointsQueue.size()-1), pointsQueue.get(pointsQueue.size()-2), null));
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setPointLabelsIpe());
        texts.addAll(setDataTextIpe());
        layers.add(new Layer(paths, uses, texts));

        // Layer
        paths = new ArrayList<>(setPolygonIpe());
        paths.addAll(setDataFrameIpe());
        paths.addAll(setLineSegmentsIpe());
        uses = new ArrayList<>(setPointsIpe());
        texts = new ArrayList<>(setPointLabelsIpe());
        layers.add(new Layer(paths, uses, texts));
    }

    public void sortPoints() {
        double maxY = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;

        for (Point p : points) {
            if (p.y > maxY) {
                maxY = p.y;
                top = new Point(p.x, p.y);
            }
            if (p.y < minY) {
                minY = p.y;
                bottom = new Point(p.x, p.y);
            }
        }

        LineSegment vertical = new LineSegment(bottom, top);
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).y == maxY) {
                if (i == 0) {
                    if (vertical.crossProductToPoint(points.get(points.size()-1)) > 0) {
                        for (int j = points.size()-1; j > 0; j--) {
                            if (points.get(j).y != bottom.y) {
                                leftChain.add(points.get(j));
                            }
                            else {
                                break;
                            }
                        }
                        for (int j = 1; j < points.size(); j++) {
                            if (points.get(j).y != bottom.y) {
                                rightChain.add(points.get(j));
                            }
                            else {
                                break;
                            }
                        }
                    }
                    else {
                        for (int j = points.size()-1; j > 0; j--) {
                            if (points.get(j).y != bottom.y) {
                                rightChain.add(points.get(j));
                            }
                            else {
                                break;
                            }
                        }
                        for (int j = 1; j < points.size(); j++) {
                            if (points.get(j).y != bottom.y) {
                                leftChain.add(points.get(j));
                            }
                            else {
                                break;
                            }
                        }
                    }
                }
                if (i == points.size()-1) {
                    if (vertical.crossProductToPoint(points.get(0)) > 0) {
                        for (Point point : points) {
                            if (point.y != bottom.y) {
                                leftChain.add(point);
                            } else {
                                break;
                            }
                        }
                        for (int j = points.size()-2; j >= 0; j--) {
                            if (points.get(j).y != bottom.y) {
                                rightChain.add(points.get(j));
                            }
                            else {
                                break;
                            }
                        }
                    }
                    else {
                        for (Point point : points) {
                            if (point.y != bottom.y) {
                                rightChain.add(point);
                            } else {
                                break;
                            }
                        }
                        for (int j = points.size()-2; j >= 0; j--) {
                            if (points.get(j).y != bottom.y) {
                                leftChain.add(points.get(j));
                            }
                            else {
                                break;
                            }
                        }
                    }
                }
                else {
                    if (vertical.crossProductToPoint(points.get(i-1)) > 0) { // bug
                        boolean leftEnd = false;
                        boolean rightEnd = false;
                        for (int j = i-1; j >= 0; j--) {
                            if (points.get(j).y != bottom.y) {
                                leftChain.add(points.get(j));
                            }
                            else {
                                leftEnd = true;
                                break;
                            }
                        }
                        if (!leftEnd) {
                            for (int j = points.size()-1; j >= 0; j--) {
                                if (points.get(j).y != bottom.y) {
                                    leftChain.add(points.get(j));
                                }
                                else {
                                    break;
                                }
                            }
                        }
                        for (int j = i+1; j < points.size(); j++) {
                            if (points.get(j).y != bottom.y) {
                                rightChain.add(points.get(j));
                            }
                            else {
                                rightEnd = true;
                                break;
                            }
                        }
                        if (!rightEnd) {
                            for (Point point : points) {
                                if (point.y != bottom.y) {
                                    rightChain.add(point);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        boolean leftEnd = false;
                        boolean rightEnd = false;
                        for (int j = i-1; j >= 0; j--) {
                            if (points.get(j).y != bottom.y) {
                                rightChain.add(points.get(j));
                            }
                            else {
                                rightEnd = true;
                                break;
                            }
                        }
                        if (!rightEnd) {
                            for (int j = points.size()-1; j >= 0; j--) {
                                if (points.get(j).y != bottom.y) {
                                    rightChain.add(points.get(j));
                                }
                                else {
                                    break;
                                }
                            }
                        }
                        for (int j = i+1; j < points.size(); j++) {
                            if (points.get(j).y != bottom.y) {
                                leftChain.add(points.get(j));
                            }
                            else {
                                leftEnd = true;
                                break;
                            }
                        }
                        if (!leftEnd) {
                            for (Point point : points) {
                                if (point.y != bottom.y) {
                                    leftChain.add(point);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        Queue<Point> leftChainTmp = new LinkedList<>(leftChain);
        Queue<Point> rightChainTmp = new LinkedList<>(rightChain);
        sortedPoints.add(top);
        while (leftChainTmp.size() > 0 && rightChainTmp.size() > 0) {
            if (leftChainTmp.peek().y >= rightChainTmp.peek().y) {
                sortedPoints.add(leftChainTmp.peek());
                leftChainTmp.poll();
            }
            else {
                sortedPoints.add(rightChainTmp.peek());
                rightChainTmp.poll();
            }
        }
        if (leftChainTmp.size() > 0) {
            while (leftChainTmp.size() > 0) {
                sortedPoints.add(leftChainTmp.peek());
                leftChainTmp.poll();
            }
        }
        else if (rightChainTmp.size() > 0) {
            while (rightChainTmp.size() > 0) {
                sortedPoints.add(rightChainTmp.peek());
                rightChainTmp.poll();
            }
        }
        sortedPoints.add(bottom);

    }

    public void setPoints(Path path) {
        for (Ipe.Object.Point point : path.points) {
            //strPolygons.add(point);
            if (point.type.equals("m") || point.type.equals("l")) {
                try {
                    points.add(new Point(Double.parseDouble(point.x), Double.parseDouble(point.y)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
