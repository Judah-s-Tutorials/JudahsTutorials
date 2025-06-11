package com.gmail.johnstraub1954.penrose.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Serializable;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.penrose.PCanvas;
import com.gmail.johnstraub1954.penrose.PDart;
import com.gmail.johnstraub1954.penrose.PKite;
import com.gmail.johnstraub1954.penrose.PShape;
import com.gmail.johnstraub1954.penrose.PToolbar;
import com.gmail.johnstraub1954.penrose.utils.FileManager;
import com.gmail.johnstraub1954.penrose.utils.SelectionEvent;
import com.gmail.johnstraub1954.penrose.utils.SelectionListener;

public class PShapeMain implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 3168160266742270027L;

    /** Scale factor for traffic light icons. */
    private static final double iconSize    = .25;
    
    private final ImageIcon canBeMapped = getIcon( "CanBeMapped.png" );
    private final ImageIcon isMapped    = getIcon( "IsMapped.png" );
    private final ImageIcon noMapping   = getIcon( "NoMapping.png" );
    
    private static double       longSide    = 50;
    private static final String chooserTitle    = "Choose File";
    private static final String appTitle        = "Penrose Tiling";
    private final JFrame        frame           = new JFrame( appTitle );
    private final PCanvas       canvas          = PCanvas.getDefaultCanvas();
    private final JLabel        trafficLight    = new JLabel( noMapping );
    private final JFileChooser  chooser;
    
    public static void main(String[] args)
    {
        PShapeMain demo2   = new PShapeMain();
        PShape.setLongSide( longSide );

        SwingUtilities.invokeLater( () -> {
            demo2.build();
            demo2.canvas.addShape( new PKite( 0, 0 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 0 ) );
            demo2.canvas.addShape( new PKite( longSide, 0, 100 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 100 ) );
        });
    }
    
    public PShapeMain()
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
        chooser.setDialogTitle( chooserTitle );
    }
    
    public void build()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setJMenuBar( new PMenuBar() );
        JPanel  pane    = new JPanel( new BorderLayout() );
        canvas.addSelectionListener( new TrafficLightMgr() );
        pane.add( canvas, BorderLayout.CENTER );
        PToolbar toolbar         = new PToolbar();
        pane.add( toolbar.getJToolbar(), BorderLayout.NORTH );
        
        frame.setContentPane( pane );
        frame.setLocation( 350, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private static ImageIcon getIcon( String path )
    {
        ClassLoader loader  = PCanvas.class.getClassLoader();
        URL         url     = loader.getResource( path );
        ImageIcon   icon    = new ImageIcon( url );
        Image       image   = icon.getImage();
        int         width   = (int)(image.getWidth( null ) * iconSize);
        int         height  = (int)(image.getHeight( null ) * iconSize);
        image = image.getScaledInstance( width, height, Image.SCALE_SMOOTH );
        icon = new ImageIcon( image );
        return icon;
    }
    
    private class TrafficLightMgr 
        implements Serializable, SelectionListener
    {
        /**
         * 
         */
        private static final long serialVersionUID = -5696886398499246877L;

        public void select( SelectionEvent event )
        {
            int mapping = event.getMapState();
            if ( mapping == SelectionEvent.CAN_MAP )
                trafficLight.setIcon( canBeMapped );
            else if ( mapping == SelectionEvent.IS_MAPPED )
                trafficLight.setIcon( isMapped );
            else
                trafficLight.setIcon( noMapping );
        }
    }

    private class PMenuBar extends JMenuBar
    {
        private static final long serialVersionUID = -6941152071574388193L;

        public PMenuBar()
        {
            add( getFileMenu() );
            add( getEditMenu() );
        }
        
        private JMenu getFileMenu()
        {
            JMenu   menu    = new JMenu( "File" );
            menu.setMnemonic( KeyEvent.VK_F );
            
            JMenuItem   save    = new JMenuItem( "Save", KeyEvent.VK_S );
            JMenuItem   open    = new JMenuItem( "Open", KeyEvent.VK_O );
            menu.add( save );
            menu.add( open );
            
            save.addActionListener( _ -> FileManager.save() );
            open.addActionListener( _ -> FileManager.open() );
            
            return menu;
        }
        
        private JMenu getEditMenu()
        {
            JMenu   menu    = new JMenu( "Edit" );
            menu.setMnemonic( KeyEvent.VK_E );
            
            JMenuItem   select      = 
                new JMenuItem( "Select All", KeyEvent.VK_A );
            JMenuItem   deselect    = 
                new JMenuItem( "Deselect All" );
            JMenuItem   delete      = new JMenuItem( "Delete Selected" );
            JMenuItem   color       = new JMenuItem( "Color" );
            JMenuItem   edgeColor   = new JMenuItem( "Edge Color" );
            JMenuItem   kiteColor   = new JMenuItem( "Kite Color" );
            JMenuItem   kiteEColor  = new JMenuItem( "Kite Edge Color" );
            JMenuItem   dartColor   = new JMenuItem( "Dart Color" );
            JMenuItem   dartEColor  = new JMenuItem( "Dart Edge Color" );
            
            select.addActionListener( _ -> {
                canvas.getShapes().forEach( s -> canvas.select( s,  0  ) );
                canvas.repaint();
            });
            
            deselect.addActionListener( _ -> {
                canvas.getSelected().forEach( canvas::deselect );
                canvas.repaint();
            });
            
            delete.addActionListener( _ -> canvas.deleteSelected() );
            
            color.addActionListener( _ -> {
                String  title       = "Choose a Fill Color";
                Color   shapeColor  = 
                    JColorChooser.showDialog( null, title, null );
                if ( shapeColor != null )
                {
//                    canvas.getSelected()
//                        .forEach( s -> s.putColor( PShape.FILL_COLOR, shapeColor ) );
                    canvas.repaint();
                }
            });
            
            menu.add( select );
            menu.add( deselect );
            menu.add( delete );
            menu.add( color );
            menu.add( edgeColor );
            menu.add( kiteColor );
            menu.add( kiteEColor );
            menu.add( dartColor );
            menu.add( dartEColor );
            
            return menu;
        }
    }
}
