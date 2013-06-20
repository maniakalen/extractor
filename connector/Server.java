package connector;

import org.jdom2.Element;

public class Server
{
    private String _name;
    private String _host;
    private String _port;
    private String _database;
    private String _user;
    private String _pass;
    public Server() {
    }
    public Server(Element config) {
       _name = config.getChild("name").getText();
       _host = config.getChild("host").getText();
       _port = config.getChild("port").getText();
       _database = config.getChild("database").getText();
       _user = config.getChild("user").getText();
       _pass = config.getChild("password").getText();
    }
    public String getMysqlConnectionString() {
        String port = null;
        if ( _port != null ) port = ":" + _port;
        return "jdbc:mysql://" + _host + port + "/" + _database + "?user=" + _user + "&password=" + _pass;   
    }
    public String getName() {
        return _name;
    }
    @Override
    public String toString() {
        return _name;
    }
}
