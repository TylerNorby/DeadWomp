import javax.xml.parsers.*;

import org.w3c.dom.Document;
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
                    MovieSet set = getMovieSet(nodes.item(i));
                    break;
                case "trailer":
                    Location location = getLocation(nodes.item(i));
                    break;
                case "office":
                    Location office = getLocation(nodes.item(i));
                    //parse payment info for bank 
                    break;
            }
        }
        return locations;
    }

    /**
     * Parse MovieSet object from MovieSet node.
     * @param node
     * @return
     */
    private MovieSet getMovieSet(Node node)
    {
        return null;
    }

    /**
     * Parse Location object from Location node.
     * @param node
     * @return
     */
    private Location getLocation(Node node)
    {
        return null;
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
}
