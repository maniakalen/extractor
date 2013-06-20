package gui;


/**
 * Write a description of class MainFrame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.*;
import java.io.File;
import connector.*;
public class MainFrame extends JFrame implements ActionListener
{
    private JTextArea _textarea;
    private ArrayList<Server> _servers;
    private ArrayList<Provider> _providers;
    private ArrayList<ExtractorMenuBar.ExtractorMenuBarLafItem> _menuItems;
    private JPanel _toolbar;
    private JPanel _mainContent;
    private JComboBox _serversCombo;
    private JComboBox _providersCombo;
    public static JFrame mainFrame;
    //private List<Provider> _providers;
    public MainFrame() {
        
        super("Extractor");
        try {
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Dimension d = new Dimension(500,550);
            setPreferredSize(d);
            setMinimumSize(d);
            getContentPane().setLayout(new BorderLayout());
            
            _mainContent = new JPanel();
            
            getContentPane().add(_mainContent,BorderLayout.CENTER);
            
            
            _toolbar = new JPanel();
            _toolbar.setLayout(new BoxLayout(_toolbar,BoxLayout.X_AXIS));
            _readConfiguration();
            getContentPane().add(_toolbar, BorderLayout.NORTH);
            //_toolbar.add(new JLabel("North part"));
            _prepareServerConnectForm();
            setJMenuBar(new ExtractorMenuBar(_menuItems));
            pack();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public void actionPerformed(ActionEvent e) {
        
        
        Server selectedServer = (Server)_serversCombo.getItemAt(_serversCombo.getSelectedIndex());
        Provider selectedProvider = (Provider)_providersCombo.getItemAt(_providersCombo.getSelectedIndex());
        _buildTable(selectedServer,selectedProvider);
        pack();
    }
    private void _buildTable(Server server,Provider provider) {
        Database.Connector connector = new Database.Connector(server, provider);
            
        DataTable.DataTableModel model = new DataTable.DataTableModel(connector);
        
        DataTable table = new DataTable(model, this);
        JScrollPane p = new JScrollPane(table);
        _mainContent.removeAll();
        //p.add(table);
        _mainContent.add(p);
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                MainFrame.mainFrame = frame;
                //frame.prepareConnection();
                //if ( frame.getResponse() )
                frame.setVisible(true);
            }
        });
    }
    
    private void _readConfiguration() {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document doc = saxBuilder.build(new File("config.xml"));
            
            Element servers = doc.getRootElement().getChild("servers");
            Element providers = doc.getRootElement().getChild("providers");
            Element ui = doc.getRootElement().getChild("ui");
            if ( servers != null ) {
                _readServerConfiguration( servers );
                
            }
            if ( providers != null ) {
                _readProviderConfiguration( providers );
            }
            if ( ui != null ) {
                _prepareUiLaf( ui );
            }
            JButton doSearch = new JButton("Search");
            doSearch.addActionListener(this);
            _toolbar.add(doSearch);
        } catch (Exception e) { System.err.println("Error: " + e); }
    }
    private void _readServerConfiguration(Element servers) {
        try {
            _servers = new ArrayList<Server>();
            java.util.List<Element> servList = servers.getChildren("server");
            for ( int i = 0; i < servList.size(); i++) {
                _servers.add(new Server(servList.get(i)));
            }
        } catch (Exception e) {}
    }
    private void _prepareServerConnectForm() {
        Vector<Server> _serverNames = new Vector<Server>(_servers);
        _serversCombo = new JComboBox(_serverNames);
        //_serversCombo.addActionListener(this);
        _toolbar.add(_serversCombo);
        
        Vector<Provider> _providersNames = new Vector<Provider>(_providers);
        _providersCombo = new JComboBox(_providersNames);
        _toolbar.add(_providersCombo);
    }
    private void _prepareUiLaf(Element ui) {
        _menuItems = new ArrayList<ExtractorMenuBar.ExtractorMenuBarLafItem>();
        java.util.List<Element> lafList = ui.getChildren("lookAndFeelList").get(0).getChildren("lookAndFeel");
        for ( int i = 0; i < lafList.size(); i++) {
            UIManager.installLookAndFeel(lafList.get(i).getAttribute("name").getValue(),lafList.get(i).getAttribute("class").getValue());
            ExtractorMenuBar.ExtractorMenuBarLafItem itm = new ExtractorMenuBar.ExtractorMenuBarLafItem(lafList.get(i));
            
            itm.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        ExtractorMenuBar.ExtractorMenuBarLafItem item = (ExtractorMenuBar.ExtractorMenuBarLafItem)e.getSource();
                        UIManager.setLookAndFeel(item.getItemAttr("class").getValue()); 
                        
                        SwingUtilities.updateComponentTreeUI(MainFrame.mainFrame);
                        MainFrame.mainFrame.pack();
                        
                        
                    } catch (Exception ex) { System.err.println(ex); }
                }
            });
             _menuItems.add(itm);
        }
    }
    private void _readProviderConfiguration( Element providers ) {
        try {
            _providers = new ArrayList<Provider>();
            java.util.List<Element> provList = providers.getChildren("provider");
            for ( int i = 0; i < provList.size(); i++) {
                _providers.add(new Provider(provList.get(i)));
            }
        } catch (Exception e) {}
    }
}
