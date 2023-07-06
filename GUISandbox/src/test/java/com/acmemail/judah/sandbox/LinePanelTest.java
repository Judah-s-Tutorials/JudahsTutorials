package com.acmemail.judah.sandbox;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

class LinePanelTest
{
    private JFrame      frame;
    private LinePanel   linePanel;
    
    private JRadioButton    axisRB;
    private JRadioButton    majorRB;
    private JRadioButton    minorRB;
    private JRadioButton    gridRB;
    
    private JLabel          strokeLabel;
    private JLabel          lengthLabel;
    private JLabel          spacingLabel;
    private JButton         colorButton;
    
    private StrokeFeedback  strokeFB;
    private LengthFeedback  lengthFB;
    private SpacingFeedback spacingFB;
    private ColorFeedback   colorFB;
    
    @Test
    void testLinePanel() throws InterruptedException
    {
        postFrame();
        assertTrue( axisRB.isSelected() );
        assertFalse( majorRB.isSelected() );
        assertFalse( minorRB.isSelected() );
        assertFalse( gridRB.isSelected() );
    }

    private void postFrame()
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> buildFrame() );
        }
        catch ( InvocationTargetException | InterruptedException exc )
        {
            exc.printStackTrace();
            fail( "unexpected exception" );
        }
        axisRB = getRadioButton( "Axes" );
        majorRB = getRadioButton( "Major Tics" );
        minorRB = getRadioButton( "Minor Tics" );
        gridRB = getRadioButton( "Grid Lines" );
        
        strokeLabel = getLabel( "Stroke" );
        lengthLabel = getLabel( "Length" );
        spacingLabel = getLabel( "Spacing" );
        colorButton = getButton( "Color" );
        
        strokeFB = getByType( StrokeFeedback.class );
        lengthFB = getByType( LengthFeedback.class );
        spacingFB = getByType( SpacingFeedback.class );
        colorFB = getByType( ColorFeedback.class );
    }
    
    private void buildFrame()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        linePanel = new LinePanel();
        contentPane.add( linePanel, BorderLayout.CENTER );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JRadioButton getRadioButton( String label )
    {
        final Predicate<JComponent> isRadioButton   = 
            jc -> (jc instanceof JRadioButton);
        Predicate<JComponent>   hasText             =
            jc -> hasText( (AbstractButton)jc, label );
        Predicate<JComponent>   pred                =
            isRadioButton.and( hasText );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JRadioButton );
        return (JRadioButton)comp;
    }
    
    private JLabel getLabel( String label )
    {
        final Predicate<JComponent> isLabel   = 
            jc -> (jc instanceof JLabel);
        Predicate<JComponent>   hasText             =
            jc -> hasText( (JLabel)jc, label );
        Predicate<JComponent>   pred                =
            isLabel.and( hasText );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JLabel );
        return (JLabel)comp;
    }
    
    private JButton getButton( String label )
    {
        final Predicate<JComponent> isLabel   = 
            jc -> (jc instanceof JButton);
        Predicate<JComponent>   hasText             =
            jc -> hasText( (JButton)jc, label );
        Predicate<JComponent>   pred                =
            isLabel.and( hasText );
        JComponent  comp    = ComponentFinder.find( linePanel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
    
    private <T extends JComponent> T getByType( Class<T> clazz )
    {
        Predicate<JComponent> isType   = jc -> (jc.getClass() == clazz);
        JComponent  comp    = ComponentFinder.find( linePanel, isType );
        assertNotNull( comp );
        return (T)comp;
    }
    
    private boolean hasText( JComponent comp, String text )
    {
        String  compText    = null;
        if ( comp instanceof AbstractButton)
            compText = ((AbstractButton)comp).getText();
        else if ( comp instanceof JLabel )
            compText = ((JLabel)comp).getText();
        else
            fail ( "Invalid component: " + comp.getClass().getName() );
        
        String  expText     = text.toUpperCase();
        if ( compText != null )
            compText = compText.toUpperCase();
        boolean result  = expText.equals( compText );
        return result;
    }
}
