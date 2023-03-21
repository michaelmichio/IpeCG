package Ipe.Handler;

import CG.Algorithm.AreaOfPolygon;
import CG.Algorithm.BentleyOttmann;
import CG.Algorithm.GrahamScan;
import CG.Algorithm.TriangulationMonotonePolygon;
import Ipe.Object.Layer;
import Ipe.Object.Path;
import Ipe.Object.Point;
import Ipe.Object.Use;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class DocumentHandler {
    ArrayList<Layer> layers = new ArrayList<>();

    public DocumentHandler(Document doc, String alg) {
        Layer firstLayer = new Layer();
        if (doc != null) {
            firstLayer = getFirstLayer(doc);
        }
        else {
            System.out.println("invalid document");
        }
        if(alg != null) {
            setLayersResult(firstLayer, alg);
        }
        else {
            System.out.println("invalid function");
        }

    }

    public void setLayersResult(Layer layer, String alg) {
        ArrayList<Path> lineSegments = new ArrayList<>();
        ArrayList<Path> polygons = new ArrayList<>();
        for (Path pth : layer.paths) {
            if (pth.points.get(pth.points.size() - 1).type.equals("h")) {
                polygons.add(pth);
            } else if (pth.points.size() == 2) {
                lineSegments.add(pth);
            }
            else {
                //
            }
        }
        layers.add(layer);
        try {
            switch (alg) {
                case "area_of_polygon" -> layers.addAll(new AreaOfPolygon(polygons.get(0)).layers);
                case "bentley_ottmann" -> layers.addAll(new BentleyOttmann(lineSegments).layers);
                case "graham_scan" -> layers.addAll(new GrahamScan(layer.uses).layers);
                case "triangulation_monotone_polygon" -> layers.addAll(new TriangulationMonotonePolygon(polygons.get(0)).layers);
                default -> {
                }
            }
        } catch (Exception e) {
            System.out.println("object not found");
        }
    }

    public Layer getFirstLayer(Document doc) {

        Layer firstLayer = new Layer();
        Node firstNode = doc.getElementsByTagName("ipe").item(0);
        NodeList firstNodeList = firstNode.getChildNodes();
        for (int i = 0; i < firstNodeList.getLength(); i++) {
            Node secondNode = firstNodeList.item(i);
            if (secondNode.getNodeType() == Node.ELEMENT_NODE) {
                Element secondNodeElement = (Element) secondNode;
                if (secondNodeElement.getTagName().equals("page")) {
                    NodeList secondNodeList = secondNodeElement.getChildNodes();
                    for (int j = 0; j < secondNodeList.getLength(); j++) {
                        Node thirdNode = secondNodeList.item(j);
                        if (thirdNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element thirdNodeElement = (Element) thirdNode;

                            if (thirdNodeElement.getTagName().equals("use")
                                    && !thirdNodeElement.getAttribute("layer").equals("alpha")
                                    && !thirdNodeElement.getAttribute("layer").equals("")) {
                                //
                            }
                            else if (thirdNodeElement.getTagName().equals("path")
                                    && !thirdNodeElement.getAttribute("layer").equals("alpha")
                                    && !thirdNodeElement.getAttribute("layer").equals("")) {
                                //
                            }
                            else if (thirdNodeElement.getTagName().equals("use")) {
                                String[] pos = thirdNodeElement.getAttribute("pos").split(" ");
                                firstLayer.addUse(new Use(new Point(pos[0], pos[1])));
                            }
                            else if (thirdNodeElement.getTagName().equals("path")) {
                                String[] paths = thirdNodeElement.getFirstChild().getNodeValue().split("\n");
                                ArrayList<Point> pts = new ArrayList<>();
                                for (String strPath : paths) {
                                    if (strPath.length() > 1) {
                                        try {
                                            String[] splitPath = strPath.split(" ");
                                            pts.add(new Point(splitPath[0], splitPath[1], splitPath[2]));
                                        } catch (Exception e) {
                                            break;
                                        }
                                    } else if (strPath.equals("h")) {
                                        pts.add(new Point("h"));
                                    }
                                }
                                firstLayer.addPath(new Path(pts));
                            }
                            else {
                                //
                            }
                        }
                    }
                }
            }
        }

        return firstLayer;
    }

    public void updateDocument() {

    }
}
