import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parse XML file into respective objects
 */
public class ParseXML {
    private Document board;
    private Document cards;

    public ParseXML(String board, String cards)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.board = db.parse(board);
            this.cards = db.parse(cards);
        }
        catch (Exception e)
        {
            System.out.println("XML Parse Failure. ");
            e.printStackTrace();
        }
    }

    /**
     * Get list of Location objects from Board document 
     */
    public Location[] parseBoard()
    {
        NodeList nodes = board.getDocumentElement().getChildNodes();
        Location[] locations = new Location[nodes.getLength()];

        for (int i = 0; i < nodes.getLength(); ++i)
        {
            switch (nodes.item(i).getNodeName())
            {
                case "set":
                    MovieSet set = getMovieSet((Element) nodes.item(i));
                    break;
                case "trailer":
                    Location location = getLocation((Element) nodes.item(i));
                    break;
                case "office":
                    Location office = getLocation((Element) nodes.item(i));
                    //parse payment info for bank 
                    break;
            }
        }
        return locations;
    }

    /**
     * Return array of scenes from scenes XML
     * @return
     */
    public Scene[] parseScenes()
    {
        NodeList nodes = cards.getDocumentElement().getChildNodes();
        Scene[] scenes = new Scene[nodes.getLength()];
        return scenes;
    }

    /**
     * Parse MovieSet object from MovieSet node.
     * @param node
     * @return
     */
    private MovieSet getMovieSet(Element node)
    {
        String name = node.getAttributes().getNamedItem("name").getNodeValue();

        //parse neighbors
        NodeList neighborList = node.getElementsByTagName("neighbors").item(0).getChildNodes();
        String[] neighbors = new String[neighborList.getLength()];
        int len = neighbors.length;

        for (int i = 0; i < len; ++i)
        {
            neighbors[i] = neighborList.item(i).getAttributes().getNamedItem("name").getNodeValue();
        }
        NodeList takes = node.getElementsByTagName("takes").item(0).getChildNodes();
        int shotCounter = takes.getLength();
        //parse off-card roles
        NodeList roleList = node.getElementsByTagName("parts").item(0).getChildNodes();
        Role[] roles = new Role[roleList.getLength()];
        len = roles.length;

        for (int i = 0; i < len; ++i)
        {
            NamedNodeMap attributes = roleList.item(i).getAttributes();
            String roleName = attributes.getNamedItem("name").getNodeValue();
            int rank = Integer.parseInt(attributes.getNamedItem("level").getNodeValue());
            String line = ((Element) roleList.item(i)).getChildNodes().item(1).getNodeValue();
            Role role = new Role(roleName, line, rank, false);
            roles[i] = role;
        }
        return new MovieSet(name, neighbors, roles);
    }

    /**
     * Parse Location object from Location node.
     * @param node
     * @return
     */
    private Location getLocation(Node node)
    {
        String name = node.getAttributes().getNamedItem("name").getNodeValue();

        //parse neighbors
        NodeList neighborList = ((Element) node).getElementsByTagName("neighbors").item(0).getChildNodes();
        String[] neighbors = new String[neighborList.getLength()];
        int len = neighbors.length;

        for (int i = 0; i < len; ++i)
        {
            neighbors[i] = neighborList.item(i).getAttributes().getNamedItem("name").getNodeValue();
        }
        return new Location(name, neighbors);
    }  

    /**
     * Parse Scene object from Scene node.
     * @param node
     * @return
     */
    private Scene getScene(Node node)
    {
        return null;
    }

    public static void main(String[] args)
    {

    }
}
