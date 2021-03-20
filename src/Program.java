import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.io.File;
import org.w3c.dom.*;

public class Program {
    public static void main(String[] args) {
        try {
            File inputFile = new File("D:\\Studying\\Java\\lab4\\L-35-003-points.gpx");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            Document newdoc = dBuilder.newDocument();
            doc.getDocumentElement().normalize();

            NodeList desc = doc.getDocumentElement().getElementsByTagName("desc");
            NodeList link = doc.getDocumentElement().getElementsByTagName("link");
            NodeList wpt = doc.getDocumentElement().getElementsByTagName("wpt");
            String lon, lat, text, attribute;
            Element kml = newdoc.createElement("kml");
            newdoc.appendChild(kml);
            Element document = newdoc.createElement("Document");
            kml.appendChild(document);
            //int listlenght = nDesc.getLength();
           // NodeList nList = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < desc.getLength();  i++) {
                /*
                Node nNode = nDesc.item(i);
                text = nNode.getTextContent();
                 */
                text = desc.item(i).getTextContent();
                attribute = link.item(i).getAttributes().item(0).getTextContent();
/*
                Node link = nLink.item(i);
                NamedNodeMap attributes = link.getAttributes();
                Node attribute = attributes.item(0);
                decription = attribute.getTextContent();
                decription = nLink.item(i).getAttributes().item(0).getTextContent();
                Node wpt = nWpt.item(i);
                NamedNodeMap wptattributes = wpt.getAttributes();
                Node wptLat = wptattributes.item(0);
                Node wptLon = wptattributes.item(1);
                lat = wptLat.getTextContent();
                lon = wptLon.getTextContent();
                 */
                lat = wpt.item(i).getAttributes().item(0).getTextContent();
                lon = wpt.item(i).getAttributes().item(1).getTextContent();

                Element placemark = newdoc.createElement("Placemark");
                document.appendChild(placemark);

                Element name = newdoc.createElement("name");
                placemark.appendChild(name);
                if (text != null) {
                    name.appendChild(newdoc.createTextNode(text));
                }

                Element descript = newdoc.createElement("description");
                placemark.appendChild(descript);
                if (attribute != null) {
                    descript.appendChild(newdoc.createTextNode(attribute));
                }

                Element point = newdoc.createElement("Point");
                placemark.appendChild(point);

                Element coord = newdoc.createElement("coordinates");
                point.appendChild(coord);
                if (lat != null && lon != null) {
                    Text textNode = newdoc.createTextNode(lat + "," + lon);
                    coord.appendChild(textNode);
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(newdoc);
            StreamResult result = new StreamResult(new File("output.kml"));
            transformer.transform(source, result);
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (ParserConfigurationException | SAXException | IOException e ) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}