import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
            NodeList wpt = doc.getDocumentElement().getElementsByTagName("wpt");
            NodeList desc = doc.getDocumentElement().getElementsByTagName("desc");
            NodeList link = doc.getDocumentElement().getElementsByTagName("link");
            String lon, lat, text, attribute;
            Element kml = newdoc.createElement("kml");
            Element document = newdoc.createElement("Document");
            newdoc.appendChild(kml);
            kml.appendChild(document);
            for (int i = 0; i < wpt.getLength();  i++) {
                text = desc.item(i).getTextContent();
                attribute = link.item(i).getAttributes().item(0).getTextContent();
                lat = wpt.item(i).getAttributes().item(0).getTextContent();
                lon = wpt.item(i).getAttributes().item(1).getTextContent();
                Element placemark = newdoc.createElement("Placemark");
                Element name = newdoc.createElement("name");
                document.appendChild(placemark);
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
                Element coord = newdoc.createElement("coordinates");
                placemark.appendChild(point);
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