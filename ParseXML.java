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

    /**
     * 
     * @param board xml file for game board
     * @param cards xml file for game cards
     */
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
     * Generate GameBoard from Board document, distribute scene cards into scenes after parsing
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
                    CastingOffice office = getCastingOffice((Element) nodes.item(i));
                    locations[j] = office;
                    break;
            }
            ++j;
        }
        return locations;
    }

    /**
     * Return array of scene cards from cards XML
     * @return
     */
    public Card[] parseCards()
    {
        NodeList nodes = cards.getDocumentElement().getChildNodes();
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
        String image = attributes.getNamedItem("img").getNodeValue();
        int budget = Integer.parseInt(attributes.getNamedItem("budget").getNodeValue());
        //parse Scene
        Node scene = ((Element) node).getElementsByTagName("scene").item(0);
        int sceneNum = Integer.parseInt(scene.getAttributes().getNamedItem("number").getNodeValue());
        String desc = scene.getChildNodes().item(0).getNodeValue();
        //parse Roles
        NodeList partList = ((Element) node).getElementsByTagName("part");
        Part[] parts = new Part[partList.getLength()];

        int j = 0;
        for (int i = 0; i < partList.getLength(); i+=1)
        {
            Node part = partList.item(i);
            int[] area = getArea(part);
            String partName = part.getAttributes().getNamedItem("name").getNodeValue();
            int rank = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
            String line = part.getChildNodes().item(3).getTextContent();
            parts[j] = new Part(partName, area, line, rank, true);
            ++j;
        }
        return new Card(name, image, sceneNum, budget, desc, parts);
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
        NodeList takes = node.getElementsByTagName("take");
        int[][] shots = new int[takes.getLength()][4];
        for (int i = takes.getLength()-1; i >= 0; --i)
        {
            int[] area = getArea(takes.item(i));
            shots[i] = area;
        }
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
            int[] area = getArea(roleList.item(i));
            Part role = new Part(roleName, area, line, rank, false);
            roles[j] = role;
            ++j;
        }
        int[] area = getArea(node);
        return new MovieSet(name, shots, neighbors, area, roles);
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
                
                String neighbor = neighborList.item(i).getNodeName();
                neighbors[j] = neighbor;
            }
            else
            {
                String neighbor = neighborList.item(i).getAttributes().getNamedItem("name").getNodeValue();
                neighbors[j] = neighbor;
            }
            ++j;
        }
        int[] area = getArea(node);
        return new Location(name, area, neighbors);
    }  

    private CastingOffice getCastingOffice(Node node)
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
                
                String neighbor = neighborList.item(i).getNodeName();
                neighbors[j] = neighbor; 
            }
            else
            {
                String neighbor = neighborList.item(i).getAttributes().getNamedItem("name").getNodeValue();
                neighbors[j] = neighbor;
            }
            ++j;
        }

        NodeList costs = ((Element) node).getElementsByTagName("upgrade");
        int[] moneyCost = new int[costs.getLength()/2];
        int[] creditCost = new int[costs.getLength()/2];
        for (int i = 0; i < costs.getLength(); i += 1)
        {
            NamedNodeMap attributes = costs.item(i).getAttributes();
            int level = Integer.parseInt(attributes.getNamedItem("level").getTextContent());
            int amt = Integer.parseInt(attributes.getNamedItem("amt").getTextContent());

            if (attributes.getNamedItem("currency").getNodeValue().equals("dollar"))
            {
                moneyCost[level-2] = amt; 
            }
            else
            {
                creditCost[level-2] = amt;
            }
        }

        return new CastingOffice(name, getArea(node), neighbors, moneyCost, creditCost);
    }

    private int[] getArea(Node node)
    {
        NodeList areas = ((Element) node).getElementsByTagName("area");
        NamedNodeMap areaNode = areas.item(0).getAttributes();
        int[] area = new int[4];
        area[0] = Integer.valueOf(areaNode.getNamedItem("x").getNodeValue());
        area[1] = Integer.valueOf(areaNode.getNamedItem("y").getNodeValue());
        area[2] = Integer.valueOf(areaNode.getNamedItem("w").getNodeValue());
        area[3] = Integer.valueOf(areaNode.getNamedItem("h").getNodeValue());
        return area;
    }

}
