package Ipe.Handler;

import CG.Algorithm.AreaOfPolygon;
import CG.Algorithm.BentleyOttmann;
import CG.Algorithm.GrahamScan;
import CG.Algorithm.TriangulationMonotonePolygon;
import Ipe.Object.Layer;
import Ipe.Object.Path;
import Ipe.Object.Point;
import Ipe.Object.Use;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
        if (doc != null) {
            Document updatedDoc = updateDocument(doc);
            new OutputHandler(updatedDoc);
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
            System.out.println(e);
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
                                HashMap<String, String> attributes = new HashMap<>();
                                attributes.put("layer", "0");
                                attributes.put("name", "mark/disk(sx)");
                                attributes.put("pos", thirdNodeElement.getAttribute("pos"));
                                attributes.put("size", "normal");
                                attributes.put("stroke", "black");
                                firstLayer.addUse(new Use(new Point(pos[0], pos[1]), attributes));
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
                                HashMap<String, String> attributes = new HashMap<>();
                                attributes.put("layer", "0");
                                attributes.put("stroke", "black");
                                firstLayer.addPath(new Path(pts, attributes));
                            }
                            else {
                                //
                            }
                        }
                    }

                    // remove initial layer
                    while (secondNodeElement.hasChildNodes()) {
                        secondNodeElement.removeChild(secondNodeElement.getFirstChild());
                    }
                }
            }
        }

        return firstLayer;
    }

    public Document updateDocument(Document doc) {
        Node firstNode = doc.getElementsByTagName("ipe").item(0);
        NodeList firstNodeList = firstNode.getChildNodes();
        for (int i = 0; i < firstNodeList.getLength(); i++) {
            Node secondNode = firstNodeList.item(i);
            if (secondNode.getNodeType() == Node.ELEMENT_NODE) {
                Element secondNodeElement = (Element) secondNode;
                if (secondNodeElement.getTagName().equals("page")) {

                    // set layers
                    for (int j = 0; j < layers.size(); j++) {
                        Element element = secondNodeElement.getOwnerDocument().createElement("layer");
                        secondNodeElement.appendChild(element);
                        Attr attr = secondNodeElement.getOwnerDocument().createAttribute("name");
                        attr.setValue(String.valueOf(j));
                        element.setAttributeNode(attr);
                    }

                    // set views
                    for (int j = 0; j < layers.size(); j++) {
                        if (j == 0) {
                            Element element = secondNodeElement.getOwnerDocument().createElement("view");
                            secondNodeElement.appendChild(element);
                            Attr attr = secondNodeElement.getOwnerDocument().createAttribute("layers");
                            attr.setValue("0");
                            element.setAttributeNode(attr);
                        }
                        else {
                            Element element = secondNodeElement.getOwnerDocument().createElement("view");
                            secondNodeElement.appendChild(element);
                            Attr attr = secondNodeElement.getOwnerDocument().createAttribute("layers");
                            attr.setValue("0 " + j);
                            element.setAttributeNode(attr);
                        }
                    }

                    // set paths
                    for (Layer layer : layers) {
                        Element element;
                        Attr attr;
                        Text text;

                        // path
                        if (layer.paths != null) {
                            for (int j = 0; j < layer.paths.size(); j++) {
                                element = secondNodeElement.getOwnerDocument().createElement("path");
                                secondNodeElement.appendChild(element);

                                Set<String> keys = layer.paths.get(j).attributes.keySet();
                                for (String key : keys) {
                                    attr = element.getOwnerDocument().createAttribute(key);
                                    attr.setValue(layer.paths.get(j).attributes.get(key));
                                    element.setAttributeNode(attr);
                                }

                                for (Point p : layer.paths.get(j).points) {
                                    text = doc.createTextNode("\n" + p.toString());
                                    element.appendChild(text);
                                }
                                text = doc.createTextNode("\n");
                                element.appendChild(text);
                            }
                        }
                    }

                    // set uses
                    for (Layer layer : layers) {
                        Element element;
                        Attr attr;

                        // use
                        if (layer.uses != null) {
                            for (int j = 0; j < layer.uses.size(); j++) {
                                element = secondNodeElement.getOwnerDocument().createElement("use");
                                secondNodeElement.appendChild(element);

                                Set<String> keys = layer.uses.get(j).attributes.keySet();
                                for (String key : keys) {
                                    attr = element.getOwnerDocument().createAttribute(key);
                                    attr.setValue(layer.uses.get(j).attributes.get(key));
                                    element.setAttributeNode(attr);
                                }
                            }
                        }
                    }

                }
            }
        }

        return doc;
    }
}
