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
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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
        Location[] locations = new Location[nodes.getLength()/2];

        int j = 0;
        for (int i = 1; i < nodes.getLength() - 1; i+=2)
        {
            switch (nodes.item(i).getNodeName())
            {
                case "set":
                    MovieSet set = getMovieSet((Element) nodes.item(i));
                    locations[j] = set;
                    break;
                case "trailer":
                    Location location = getLocation((Element) nodes.item(i));
                    locations[j] = location;
                    break;
                case "office":
                    Location office = getLocation((Element) nodes.item(i));
                    locations[j] = office;
                    //parse payment info for bank 
                    break;
            }
            ++j;
        }
        return locations;
    }

    /**
     * Return array of scenes from scenes XML
     * @return
     */
    public Card[] parseCards()
    {
        NodeList nodes = cards.getDocumentElement().getChildNodes();
        System.out.println(nodes.item(2));
        Card[] cards = new Card[nodes.getLength()/2];

        int j = 0;
        for (int i = 1; i < nodes.getLength() - 1; i+=2)
        {
            cards[j] = getCard(nodes.item(i)); 
            ++j;
        }
        return cards;
    }

    /**
     * Parse Scene object from Card node.
     * @param node
     * @return
     */
    private Card getCard(Node node)
    {
        //parse Card attributes
        NamedNodeMap attributes = node.getAttributes();
        String name = attributes.getNamedItem("name").getNodeValue();
        int budget = Integer.parseInt(attributes.getNamedItem("budget").getNodeValue());
        //parse Scene
        Node scene = ((Element) node).getElementsByTagName("scene").item(0);
        int sceneNum = Integer.parseInt(scene.getAttributes().getNamedItem("number").getNodeValue());
        String desc = scene.getChildNodes().item(0).getNodeValue();
        //parse Roles
        NodeList partList = ((Element) node).getElementsByTagName("part");
        Part[] parts = new Part[partList.getLength()/2];

        int j = 0;
        for (int i = 1; i < partList.getLength(); i+=2)
        {
            Node part = partList.item(i);
            String partName = part.getAttributes().getNamedItem("name").getNodeValue();
            int rank = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
            String line = part.getChildNodes().item(1).getNodeValue();
            parts[j] = new Part(partName, line, rank, true);
            ++j;
        }
        return new Card(name, sceneNum, budget, desc, parts);
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
        NodeList neighborList = node.getElementsByTagName("neighbor");
        String[] neighbors = new String[neighborList.getLength()];
        int len = neighborList.getLength();

        for (int i = 0; i < len; ++i)
        {
            neighbors[i] = neighborList.item(i).getAttributes().getNamedItem("name").getNodeValue();
        }
        NodeList takes = node.getElementsByTagName("takes").item(0).getChildNodes();
        int shots = takes.getLength();
        //parse off-card roles
        NodeList roleList = node.getElementsByTagName("part");
        Part[] roles = new Part[roleList.getLength()];
        len = roleList.getLength();

        int j = 0;
        for (int i = 0; i < len; i += 1)
        {
            NamedNodeMap attributes = roleList.item(i).getAttributes();
            String roleName = attributes.getNamedItem("name").getNodeValue();
            int rank = Integer.parseInt(attributes.getNamedItem("level").getNodeValue());
            String line = ((Element) roleList.item(i)).getElementsByTagName("line").item(0).getTextContent();
            Part role = new Part(roleName, line, rank, false);
            roles[j] = role;
            ++j;
        }
        return new MovieSet(name, neighbors, shots, roles);
    }

    /**
     * Parse Location object from Location node.
     * @param node
     * @return
     */
    private Location getLocation(Node node)
    {
        String name = node.getNodeName();

        //parse neighbors
        NodeList neighborList = ((Element) node).getElementsByTagName("neighbors").item(0).getChildNodes();
        String[] neighbors = new String[neighborList.getLength()/2];
        int len = neighborList.getLength();

        int j = 0;
        for (int i = 1; i < len; i += 2) //parser outputs weird gaps in array
        {
            NamedNodeMap attributes = neighborList.item(i).getAttributes();
            if (attributes == null)
            {
                neighbors[j] = neighborList.item(i).getNodeName();
            }
            else
            {
                neighbors[j] = neighborList.item(i).getAttributes().getNamedItem("name").getNodeValue();
            }
            ++j;
        }
        return new Location(name, neighbors);
    }  

}
