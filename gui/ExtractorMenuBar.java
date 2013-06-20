package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import java.util.*;
import javax.swing.*;
import org.jdom2.*;
import org.jdom2.input.*;
public class ExtractorMenuBar extends JMenuBar
{
    public ExtractorMenuBar(ArrayList<ExtractorMenuBar.ExtractorMenuBarLafItem> list)
    {
        super();
        JMenu menu = new JMenu("File");
        JMenu lookAndFeel = new JMenu("Look and feel");
        for ( int i = 0; i < list.size(); i++) {
            
            lookAndFeel.add(list.get(i));
        }

        menu.add(lookAndFeel);
        menu.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(exit);
        this.add(menu);
    }
    static class ExtractorMenuBarLafItem extends JMenuItem {
        private Element _element;
        
        public ExtractorMenuBarLafItem(Element config) {
            super(config.getAttribute("name").getValue());
            _element = config;
            
        }
        
        public Attribute getItemAttr(String prop) {
            return _element.getAttribute(prop);
        }
    }
}
