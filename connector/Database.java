package connector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
/**
 * Write a description of class Database here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Database
{
    private Connection _connect;
    /**
     * Constructor for objects of class Database
     */
    public Database(Server server)
    {
        this._prepareConnection(server);   
    }
    private void _prepareConnection(Server server) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
                // Setup the connection with the DB
            this._connect = DriverManager
                .getConnection(server.getMysqlConnectionString());
            
        }catch (Exception ex) {
            System.err.println("Ex: " + ex.getMessage());
            System.out.println(server.getMysqlConnectionString());
        }
    }
    public Connection getConnection() {
        return _connect;
    }
    
    public static class Connector {
        private Database _db;
        private Provider _p;
        public Connector(Server server, Provider provider) {
            _db = new Database(server);
            _p = provider;
        }
        public Database.Row getRowData(Integer id) {
            Database.Row result = new Database.Row();
            try {
                Statement statement = this._db.getConnection().createStatement();
                statement.setQueryTimeout(300);
                ResultSet dbresult = statement.executeQuery(String.format(_p.getDetailsQuery(),id));
                
                while (dbresult.next()) {
                   
                   result.setRequest(dbresult.getString("request"));
                   result.setResponse(dbresult.getString("response"));    
                }
                //return true;
                
            } catch (Exception ex) {
                System.err.println("Exeption: " + ex.getMessage() );
            }
           return result;
        }
        public Vector<Vector> getTableRows() {
            Vector<Vector> val = new Vector<Vector>();
            
            
            
            try {
                Statement statement = this._db.getConnection().createStatement();
                statement.setQueryTimeout(300);
                ResultSet dbresult = statement.executeQuery(_p.getListQuery());
                
                while (dbresult.next()) {
                   Vector<String> row = new Vector<String>();
                   row.addElement(dbresult.getString("id"));
                    row.addElement(dbresult.getString("date"));
                    
                    val.addElement(row);  
                }
                //return true;
                
            } catch (Exception ex) {
                System.err.println("Exeption: " + ex.getMessage() );
            }
            
            
            return val;
        }
    }
    public static class Row {
        private String _request;
        private String _response;
        public String getResponse() {
            
            return this._response;
        }
        public String getRequest() {
            return this._request;
        }
        public void setRequest(String request) {
            this._request = request;
        }
        public void setResponse(String response) {
            this._response = response;
        }
    }
}
