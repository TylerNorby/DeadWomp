import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;

public class ParseDoc {
    private Document document;
    private HashMap<String, Location> locations;

    public ParseDoc(Document document)
    {
        this.document = document;
        
    }

    public HashMap<String, Location> parseLocations()
    {
        HashMap<String, Location> locations = new HashMap<String, Location>();

        return locations;
    }
    
}
