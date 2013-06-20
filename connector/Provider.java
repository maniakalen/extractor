package connector;
import org.jdom2.Element;

public class Provider
{
    private String _name;
    private String _listQuery;
    private String _detailsQuery;
    public Provider(Element config) {
       _name = config.getChild("name").getText();
       _listQuery = config.getChild("listQuery").getText();
       _detailsQuery = config.getChild("detailsQuery").getText();
    }
    public String getName() {
        return _name;
    }
    public String getListQuery() {
        return _listQuery;
    }
    public String getDetailsQuery() {
        return _detailsQuery;
    }
    @Override
    public String toString() {
        return _name;
    }
}
