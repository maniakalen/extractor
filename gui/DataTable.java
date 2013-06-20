package gui;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;
import connector.*;
public class DataTable extends JTable
{
    private DataTable.DataTableModel _model;
    private JFrame _frame;
    public DataTable(DataTable.DataTableModel model, JFrame root)
    {
        super(model);
        _model = model;
        _frame = root;
        setRowSelectionAllowed(true);
        //setSelectionBackground(Color.GREEN);
        addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                 JTable target = (JTable)e.getSource();
                 int row = target.getSelectedRow();
                 int column = target.getSelectedColumn();
                 (new RowDialog( _frame, new Integer((String)_model.getValueAt(row,0)),_model.getDatabaseProvider() )).goToFullScreen();
              }
           }
        });
        //DataTable.DataTableModel model = new DataTable.DataTableModel(data);
    }
    
    
    public static class DataTableModel extends AbstractTableModel {
        private String[] _columnNames = {"Id","Date"};
        public Class[] m_colTypes = { Integer.class, String.class };
        private Vector<Vector> _data;
        private Database.Connector _lc;
        public DataTableModel(Database.Connector c) {
            
            super();            
            _lc = c;
            _data = c.getTableRows();
        }
        public int getColumnCount() {
            return _columnNames.length;
        }
        public Database.Connector getDatabaseProvider() {
            return _lc;
        }
        public int getRowCount() {
            return _data.size();
        }
    
        public String getColumnName(int col) { 
            return _columnNames[col];
        }
    
        public Object getValueAt(int row, int col) {
            return _data.elementAt(row).elementAt(col);
        }
    
        public Class getColumnClass(int c) {
            return m_colTypes[c];
        }
    
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }
        public String toString() {
            return _data.toString();
        }
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            //_data.elementAt(row).set(col, value);
            //fireTableCellUpdated(row, col);
        }
    }
    
    
}
