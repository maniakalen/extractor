package gui;
import javax.swing.*;
import java.awt.*;
import connector.*;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import java.io.StringReader;
import java.io.IOException;
import java.util.List;
import java.awt.event.*;
//
import org.jdom2.*;
import org.jdom2.input.*;
public class RowDialog extends JDialog
{
    private Database.Connector _lc;
    private JXTreeTable _request;
    private JXTreeTable _response;
    public RowDialog(JFrame frame, Integer id, Database.Connector lc)
    {
        super(frame, "Id: " + id.toString());
        Dimension d = new Dimension(1200,960);
        //setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        _lc = lc;
        setPreferredSize(d);
        //setMinimumSize(d);
        //setSize(d);
        Database.Row row = _lc.getRowData(id);
        _response = _parseXml(row.getResponse());
        _request = _parseXml(row.getRequest());
        JScrollPane paneResponse = new JScrollPane(_response);
        JScrollPane paneRequest = new JScrollPane(_request);
        
        JPanel requestPanel = new JPanel(new BorderLayout());
        JPanel toolbarRequest = new JPanel();
        toolbarRequest.setLayout(new BoxLayout(toolbarRequest,BoxLayout.X_AXIS));
        toolbarRequest.add(new JLabel("Request"));
        JButton reqExpand = new JButton("Expand all");
        reqExpand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _request.expandAll();
            }
        });
        toolbarRequest.add(reqExpand);
        JButton reqCollapse = new JButton("Collapse all");
        reqCollapse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _request.collapseAll();
            }
        });
        toolbarRequest.add(reqCollapse);
        
        Dimension ps = new Dimension(560,960);
        
        requestPanel.add(toolbarRequest,BorderLayout.NORTH);
        
        
        //requestPanel.add(reqExpand,BorderLayout.NORTH);
        requestPanel.add(paneRequest,BorderLayout.CENTER);

        JPanel respPanel = new JPanel(new BorderLayout());
        
        JPanel toolbarResp = new JPanel();
        toolbarResp.setLayout(new BoxLayout(toolbarResp,BoxLayout.X_AXIS));
        toolbarResp.add(new JLabel("Response"));
        JButton respExpand = new JButton("Expand all");
        respExpand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _response.expandAll();
            }
        });
        toolbarResp.add(respExpand);
        
        JButton respCollapse = new JButton("Collapse all");
        respCollapse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _response.collapseAll();
            }
        });
        toolbarResp.add(respCollapse);
        respPanel.add(toolbarResp,BorderLayout.NORTH);
        respPanel.add(paneResponse,BorderLayout.CENTER);
        
        JTabbedPane reqTabbed = new JTabbedPane( JTabbedPane.BOTTOM );
        reqTabbed.setPreferredSize(ps);
        reqTabbed.addTab("Request table", requestPanel);
        reqTabbed.addTab("Request plain", new JScrollPane(new JTextArea(row.getRequest())));
        JPanel panel = new JPanel();
        
        JTabbedPane respTabbed = new JTabbedPane( JTabbedPane.BOTTOM );
        respTabbed.setPreferredSize(ps);
        respTabbed.addTab("Response table", respPanel);
        respTabbed.addTab("Response plain", new JScrollPane(new JTextArea(row.getResponse())));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(reqTabbed);
        panel.add(respTabbed);
        getContentPane().add(panel,BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
    /*public JTree build(String pathToXml) throws Exception {
         SAXReader reader = new SAXReader();
         Document doc = reader.read(pathToXml);
         return new JTree(build(doc.getRootElement()));
    }
    
    public DefaultMutableTreeNode build(Element e) {
       DefaultMutableTreeNode result = new DefaultMutableTreeNode(e.getText());
       for(Object o : e.elements()) {
          Element child = (Element) o;
          result.add(build(child));
       }
    
       return result;         
    }*/
    

    public void goToFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        
        
        try {
            if ( gs.isFullScreenSupported() ) {
                gs.setFullScreenWindow(this);
            } else {
            
            }
        } catch (Exception e) {
            gs.setFullScreenWindow(null);
        }finally {
            //
        }
    }
    private JXTreeTable _parseXml(String xml) {
        
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document doc = saxBuilder.build(new StringReader(xml));
            String name = doc.getRootElement().getName();
            ArrayNode root = new ArrayNode(new Object[] {name, ""});
            this._parseXmlChildren(doc.getRootElement(), root);
            return new JXTreeTable(new DefaultTreeTableModel(root) {
                public String getColumnName(int col) { 
                    String[] cols = {"Name","Value"};
                    return cols[col];
                }
                public Class getColumnClass(int c) {
                    return String.class;
                }
            });
        } catch (JDOMException e) {
            // handle JDOMException
        } catch (IOException e) {
            // handle IOException
        } catch (NullPointerException e) {
            
        }
        return new JXTreeTable();
    }
    private void _parseXmlChildren(Element parentElement, ArrayNode parentNode) {
        List<Element> children = parentElement.getChildren();
        for (int i = 0; i < children.size(); i++ ) {
            Element item = children.get(i);
            String name = item.getName();
            String value = item.getText();
            ArrayNode itemNode = new ArrayNode(new Object[] {name, value});
            
            List<Attribute> attrs = item.getAttributes();
            for (int j = 0; j < attrs.size(); j++) {
                Attribute at = attrs.get(j);
                String atName = "@" + at.getName();
                String atValue = at.getValue();
                ArrayNode attNode = new ArrayNode(new Object[] {atName, atValue});
                itemNode.add(attNode);
            }
            this._parseXmlChildren(item,itemNode);
            parentNode.add(itemNode);
        }
            
    }
}
