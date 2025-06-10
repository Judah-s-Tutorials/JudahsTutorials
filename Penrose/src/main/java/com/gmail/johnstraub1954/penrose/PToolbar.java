package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.gmail.johnstraub1954.penrose.utils.FileManager;

public class PToolbar
{
    private static final ClassLoader classLoader    = PToolbar.class.getClassLoader();
    private static final String title       = "Penrose Tiles Toolbar";
    private static final String chooserTitle    = "Choose File";
    private final JToolBar      toolbar     = 
        new JToolBar( title, JToolBar.HORIZONTAL );
    private final JFileChooser  chooser;

    private static final int    iconSize  = 16;

    /** Unicode for an up-arrow. */
    private static final String         upArrow     = "\u2191";
    /** Unicode for a down-arrow. */
    private static final String         downArrow   = "\u2193";
    /** Unicode for a left-arrow. */
    private static final String         leftArrow   = "\u2190";
    /** Unicode for a right-arrow. */
    private static final String         rightArrow  = "\u2192";
   
    private static final String redLEDToolTip       = 
        "No compatible shapes selected";
    private static final String yellowLEDToolTip    = 
        "Compatible shapes selected";
    private static final String greenLEDToolTip     = "Ready to snap";
    
    private static final String openToolTip     = "Open file (Ctrl+O)";
    private static final String saveToolTip     = "Save file (Ctrl+S)";
    private static final String saveAsToolTip   = "Save as (Ctrl+Shift+S)";

    private static final String doubleLeftArrowToolTip    = 
        "Shift selected shapes left (" + leftArrow + ")";
    private static final String doubleRightArrowToolTip    = 
        "Shift selected shapes right (" + rightArrow + ")";
    private static final String doubleUpArrowToolTip    = 
        "Shift selected shapes up (" + upArrow + ")";
    private static final String doubleDownArrowToolTip    = 
        "Shift selected shapes down (" + downArrow + ")";
    private static final String rotateRightToolTip    = 
        "Rotate right (selected) (Ctrl+" + rightArrow + ")";
    private static final String rotateLeftToolTip    = 
        "Rotate left (selected) (Ctrl+" + leftArrow + ")";
    
    private static final String sourceSelectLeftToolTip    = 
        "Source select previous side (Ctrl+Alt+N)";
    private static final String sourceSelectRightToolTip    = 
        "Source select next side (Ctrl+Alt+O)";
    private static final String destinationSelectLeftToolTip    = 
        "Destination select previous side(Ctrl+Alt+P)";
    private static final String destinationSelectRightToolTip    = 
        "Destination select next side(Ctrl+Alt+Q)";
    private static final String snapToolTip    = 
        "Snap selected shapes together (Ctrl+Alt+S)";
    
    private static final String selectAllToolTip    = 
        "Select all (Ctrl+A)";
    private static final String newDartToolTip    = 
        "New dart (Ctrl+D)";
    private static final String newKiteToolTip    = 
        "New kite (Ctrl+K)";
    private static final String deleteSelectedToolTip   = 
        "Delete selected (del)";
    
    private static final String exitToolTip    = "Exit";
    
    private final PCanvas       canvas      = PCanvas.getDefaultCanvas();
    private final ButtonDesc[]  buttonDescs =
    {
        new ButtonDesc( 
            "redLED.png", 
            redLEDToolTip, 
            () -> new JLabel(), 
            null
        ),
        new ButtonDesc( 
            "yellowLED.png", 
            yellowLEDToolTip, 
            () -> new JLabel(), 
            null 
        ),
        new ButtonDesc( 
            "greenLED.png", 
            greenLEDToolTip, 
            () -> new JLabel(), 
            null 
        ),
        new Separator(),
        new ButtonDesc( 
            "OpenFile.png", 
            openToolTip, 
            () -> new JButton(), 
            e -> FileManager.open()
        ),
        new ButtonDesc( 
            "SaveFile.png", 
            saveToolTip, 
            () -> new JButton(), 
            e -> FileManager.save()
        ),
        new ButtonDesc( 
            "SaveAs.png", 
            saveAsToolTip, 
            () -> new JButton(), 
            e -> FileManager.saveAs()
        ),
        new Separator(),
        new ButtonDesc( 
            "DoubleLeftArrow.png", 
            doubleLeftArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( -4, 0 ) ) 
        ),
        new ButtonDesc( 
            "DoubleRightArrow.png", 
            doubleRightArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( 4, 0 ) ) 
        ),
        new ButtonDesc( 
            "DoubleUpArrow.png",
            doubleUpArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( 0, -4 ) ) 
        ),
        new ButtonDesc( 
            "DoubleDownArrow.png",
            doubleDownArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( 0, 4 ) ) 
        ),
        new ButtonDesc( 
            "RotateRight16.png", 
            rotateRightToolTip, 
            () -> new JButton(), 
            e -> canvas.rotate( PShape.D18 )
        ),
        new ButtonDesc( 
            "RotateLeft16.png", 
            rotateLeftToolTip, 
            () -> new JButton(), 
            e -> canvas.rotate( -PShape.D18 )
        ),
        new Separator(),
        new ButtonDesc( 
            "SourceSelectLeft.png", 
            sourceSelectLeftToolTip, 
            () -> new JButton(), 
            e -> canvas.selectSource( -1 ) 
        ),
        new ButtonDesc( 
            "SourceSelectRight.png", 
            sourceSelectRightToolTip, 
            () -> new JButton(), 
            e -> canvas.selectSource( 1 ) 
        ),
        new ButtonDesc( 
            "DestinationSelectLeft.png", 
            destinationSelectLeftToolTip, 
            () -> new JButton(), 
            e -> canvas.selectDestination( -1 ) 
        ),
        new ButtonDesc( 
            "DestinationSelectRight.png", 
            destinationSelectRightToolTip, 
            () -> new JButton(), 
            e -> canvas.selectDestination( 1 ) 
        ),
        new ButtonDesc( 
            "Snap.png", 
            snapToolTip, 
            () -> new JButton(), 
            e -> canvas.consumeSnap() 
        ),
        new Separator(),
        new ButtonDesc( 
            "SelectAll.png", 
            selectAllToolTip, 
            () -> new JButton(), 
            null
        ),
        new ButtonDesc( 
            "KiteIcon.png", 
            newKiteToolTip, 
            () -> new JButton(), 
            e -> canvas.addShape( new PKite(), true, true )
        ),
        new ButtonDesc( 
            "DartIcon.png", 
            newDartToolTip, 
            () -> new JButton(), 
            e -> canvas.addShape( new PDart(), true, true )
        ),
        new ButtonDesc( 
            "Delete.png", 
            deleteSelectedToolTip, 
            () -> new JButton(), 
            e -> canvas.deleteSelected()
        ),
        new Separator(),
        new ButtonDesc( 
            "Exit.png", 
            exitToolTip, 
            () -> new JButton(), 
            e -> System.exit( 0 ) 
        ),
    };
    private JComponent saveButton  = null;

    public JToolBar getJToolbar()
    {
        return toolbar;
    }

    public PToolbar()
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
        chooser.setDialogTitle( chooserTitle );
        FileManager.addPropertyChangeListener( e -> {
            if ( saveButton != null )
                saveButton.setEnabled( e.getNewValue() != null );
        });
        makeToolbar();
    }
    
    private void makeToolbar()
    {
        for ( ButtonDesc desc : buttonDescs )
        {
            if ( desc instanceof Separator )
                toolbar.addSeparator();
            else
            {
                JComponent  comp    = desc.getComponent();
                String      toolTip = comp.getToolTipText();
                if ( saveToolTip.equals( toolTip ) )
                    saveButton = comp;
                toolbar.add( comp );   
            }
        }
    }

    private static ImageIcon getIcon( String path )
    {
        ImageIcon   icon    = null;
        try
        {
            URL         url     = classLoader.getResource( path );
            if ( url == null )
                throw new FileNotFoundException();
            Image       image   = ImageIO.read( url );
            image = image.getScaledInstance( iconSize, iconSize, Image.SCALE_SMOOTH );
            icon = new ImageIcon( image );
        }
        catch ( IOException | IllegalArgumentException exc )
        {
            System.err.println( path );
            exc.printStackTrace();
            System.exit( 1 );
        }
        return icon;
    }
    
    private void action( Consumer<PShape> consumer )
    {
        List<PShape>    list    = canvas.getSelected();
        list.forEach( consumer );
        canvas.repaint();
    }
    
    private static class ButtonDesc
    {
        public final JComponent component;
        
        /**
         * Default constructor.
         * Placed here to support the Separator class.
         * 
         * @see Separator
         */
        public ButtonDesc()
        {
            component = null;
        }
        
        public ButtonDesc( 
            String path, 
            String toolTip, 
            Supplier<JComponent> getter,
            ActionListener action
        )
        {
            component = getter.get();
            Icon    icon    = getIcon( path );
            component.setToolTipText( toolTip );
            if ( component instanceof AbstractButton )
            {
                AbstractButton  button  = (AbstractButton)component;
                button.addActionListener( action );
                button.setIcon( icon );
            }
            else if ( component instanceof JLabel )
            {
                JLabel  label   = (JLabel)component;
                label.setIcon( icon );
            }
            else
                ;
        }
        
        public JComponent getComponent()
        {
            return component;
        }
    }
    
    private static class Separator extends ButtonDesc
    {
    }
}
