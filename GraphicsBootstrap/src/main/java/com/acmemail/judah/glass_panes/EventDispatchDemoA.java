package com.acmemail.judah.glass_panes;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.ItemSelectable;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.acmemail.judah.glass_panes.util.Display;

public class EventDispatchDemoA
{
    private final Display   logger  = Display.getDisplay();
    
    public EventDispatchDemoA()
    {
    }

    public static void main(String[] args)
    {
        EventDispatchDemoA  demo    = new EventDispatchDemoA();
        demo.buildShow();
    }

    private void buildShow()
    {
        JFrame      frame   = new JFrame( "Event Dispatch Demo A" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( getContentPane() );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getContentPane()
    {
        Border      lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK, 5 );
        Border      emptyBorder =
            BorderFactory.createEmptyBorder( 15, 15, 15, 15 );
        Border      border      =
            BorderFactory.createCompoundBorder( lineBorder, emptyBorder );
        JPanel      panel       = new JPanel();
        JCheckBox   checkBox    = new JCheckBox( "Check Me" );
        panel.setBorder( border );
        panel.add( checkBox );
        
        panel.addMouseListener( new MouseMonitor( checkBox ) );
        checkBox.addItemListener( new CheckBoxMonitor() );
        
        return panel;
    }
    
    private class CheckBoxMonitor implements ItemListener
    {
        public void itemStateChanged( ItemEvent evt )
        {
            String  source  = evt.getSource().getClass().getSimpleName();
            boolean state   = evt.getStateChange() == ItemEvent.SELECTED;
            int     ident   = evt.getID();
            String  format  = "State changed; id: %d source: %s, selected: %s";
            String  message = String.format( format, ident, source, state );
            logger.println( message );
        }
    }
    
    private class Selectable implements ItemSelectable
    {
        @Override
        public Object[] getSelectedObjects()
        {
            return null;
        }

        @Override
        public void addItemListener(ItemListener l)
        {
        }
        
        @Override
        public void removeItemListener(ItemListener l)
        {
        }
    }
    
    private class MouseMonitor extends MouseAdapter
    {
        private final JCheckBox checkBox;
        
        public MouseMonitor( JCheckBox checkBox )
        {
            this.checkBox = checkBox;
        }
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            String      source  = evt.getSource().getClass().getSimpleName();
            int         xco     = evt.getX();
            int         yco     = evt.getY();
            String      format  = "%s: mouse clicked at (%d,%d)";
            String      message = String.format( format, source, xco, yco );
            Selectable  select  = new Selectable();
            logger.println( message );
            boolean     newState    = !checkBox.isSelected();
            checkBox.setSelected( newState );
        }
    }
}
