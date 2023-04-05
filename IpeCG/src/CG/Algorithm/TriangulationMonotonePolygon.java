package CG.Algorithm;

import CG.Object.LineSegment;
import CG.Object.Point;
import Ipe.Object.Layer;
import Ipe.Object.Path;

import java.util.*;

public class TriangulationMonotonePolygon {
    public ArrayList<Layer> layers = new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();
    public Point top = new Point();
    public Point bottom = new Point();
    public Queue<Point> leftChain = new LinkedList<>();
    public Queue<Point> rightChain = new LinkedList<>();

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

    public void addLayer(Point p1, Point p2) {
        HashMap<String, String> attributes = new HashMap<>();
        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Ipe.Object.Point> strPoints = new ArrayList<>();

        strPoints.add(new Ipe.Object.Point(String.valueOf(p1.x), String.valueOf(p1.y), "m"));
        strPoints.add(new Ipe.Object.Point(String.valueOf(p2.x), String.valueOf(p2.y), "l"));
        attributes.put("layer", String.valueOf(layers.size()+1));
        attributes.put("stroke", "black");
        paths.add(new Path(strPoints, attributes));
        layers.add(new Layer(paths, null, null));
    }

    public void generateLayers() {

        // System.out.println("top " + top);
        // System.out.println("bottom " + bottom);
        // System.out.println(leftChain);
        // System.out.println(rightChain);

        LinkedList<Point> listQ = new LinkedList<>();
        boolean isLeftSide = false;

        listQ.add(top);
        if (leftChain.peek().y >= rightChain.peek().y) {
            listQ.add(leftChain.peek());
            leftChain.poll();
            isLeftSide = true;
        }
        else {
            listQ.add(rightChain.peek());
            rightChain.poll();
        }

        while (leftChain.size() > 0 || rightChain.size() > 0) {

            LineSegment ls = new LineSegment(listQ.get(listQ.size()-2), listQ.getLast());

            if (leftChain.size() == 0) {
                while (listQ.size() >= 2) {
                    addLayer(listQ.get(1), rightChain.peek());
                    listQ.removeFirst();
                }
                rightChain.poll();
                while (rightChain.size() > 0) {
                    addLayer(listQ.getFirst(), rightChain.peek());
                    rightChain.poll();
                }
            }
            else if (rightChain.size() == 0) {
                while (listQ.size() >= 2) {
                    addLayer(listQ.get(1), leftChain.peek());
                    listQ.removeFirst();
                }
                leftChain.poll();
                while (leftChain.size() > 0) {
                    addLayer(listQ.getFirst(), leftChain.peek());
                    leftChain.poll();
                }
            }
            else if (leftChain.peek().y >= rightChain.peek().y) {
                if (isLeftSide) {
                    if (ls.crossProductToPoint(leftChain.peek()) > 0) { // in
                        listQ.removeLast();
                        addLayer(listQ.getLast(), leftChain.peek());
                    }
                }
                else {
                    for (int i = listQ.size()-1; i > 0; i--) {
                        addLayer(listQ.getLast(), leftChain.peek());
                        listQ.removeLast();
                    }
                }
                listQ.add(leftChain.peek());
                leftChain.poll();
            }
            else {
                if (isLeftSide) {
                    for (int i = listQ.size()-1; i > 0; i--) {
                        addLayer(listQ.getLast(), rightChain.peek());
                        listQ.removeLast();
                    }
                }
                else {
                    if (ls.crossProductToPoint(rightChain.peek()) < 0) { // in
                        listQ.removeLast();
                        addLayer(listQ.getLast(), rightChain.peek());
                    }
                }
                listQ.add(rightChain.peek());
                rightChain.poll();
            }

        }

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
                    if (vertical.crossProductToPoint(points.get(i-1)) > 0) {
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
    }

    public void setPoints(Path path) {
        for (Ipe.Object.Point point : path.points) {
            if (point.type.equals("m") || point.type.equals("l")) {
                try {
                    points.add(new Point(Double.parseDouble(point.x), Double.parseDouble(point.y)));
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

}
