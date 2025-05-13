import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseDoc {
    private Document document;

    public ParseDoc(Document document)
    {
        this.document = document;
    }

    public Location[] parseLocations()
    {
        NodeList nodes = document.getDocumentElement().getChildNodes();
        Location[] locations = new Location[nodes.getLength()];

        for (int i = 0; i < nodes.getLength(); ++i)
        {
            switch (nodes.item(i).getNodeName())
            {
                case "set":
                    break;
                case "trailer":
                    break;
                case "office":
                    break;
            }
        }


        return locations;
    }

    public Scene[] parseScenes()
    {
        NodeList nodes = document.getDocumentElement().getChildNodes();
        Scene[] scenes = new Scene[nodes.getLength()];

        return scenes;
    }
}
