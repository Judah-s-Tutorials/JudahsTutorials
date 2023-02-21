package lambdas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LambdaFramePart4
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }
    
    private static void buildGUI()
    {
        JFrame  frame   = new JFrame();
        frame.setContentPane( new Canvas() );
        frame.pack();
        frame.setVisible( true );
        
        frame.addPropertyChangeListener( new PropertyMonitor() );
    }
    
    private static class PropertyMonitor implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            System.out.println( 
                evt.getPropertyName() + 
                " changed from " + evt.getOldValue() +
                " to " + evt.getNewValue()
            );
            
        }
    }

    @SuppressWarnings("serial")
    private static class Canvas extends JPanel
    {
        public Canvas()
        {
            setPreferredSize( new Dimension( 500, 600 ) );
        }
        
        public void paintComponent( Graphics graphics )
        {
            Graphics2D  gtx = (Graphics2D)graphics.create();
            gtx.setColor( Color.blue );
            gtx.fillRect( 0,  0, getWidth(), getHeight() );
        }
    }
}
