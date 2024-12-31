package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PShapeDemo1
{
    /** Unicode for an up-arrow. */
    private static final String         upArrow     = "\u21e7";
    /** Unicode for a down-arrow. */
    private static final String         downArrow   = "\u21e9";
    /** Unicode for a left-arrow. */
    private static final String         leftArrow   = "\u21e6";
    /** Unicode for a right-arrow. */
    private static final String         rightArrow  = "\u21e8";
    /** Unicode for a rotate-left arrow. */
    private static final String         rotateLeft  = "\u21B6";
    /** Unicode for a rotate-right arrow. */
    private static final String         rotateRight = "\u21B7";
    
    private static double       longSide    = 50;
    private final JFileChooser  chooser;
    private static final String chooserTitle    = "Choose File";
    private JFrame        frame;
    private PCanvas canvas;
    
    public static void main(String[] args)
    {
        PShapeDemo1 demo2   = new PShapeDemo1();
        String      clazz   = PKite.class.getSimpleName();
        PShape.setDefaultFillColor( clazz, new Color( 0x900C3F ) );
        clazz = PDart.class.getSimpleName();
        PShape.setDefaultFillColor( clazz, new Color( 0x663399 ) );

        SwingUtilities.invokeLater( () -> {
            demo2.build();
            demo2.canvas.addShape( new PKite( longSide, 0, 0 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 0 ) );
            demo2.canvas.addShape( new PKite( longSide, 0, 100 ) );
            demo2.canvas.addShape( new PDart( longSide, 100, 100 ) );
        });
    }
    
    public PShapeDemo1()
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
        chooser.setDialogTitle( chooserTitle );
    }
    
    public void build()
    {
        frame   = new JFrame( "Dart Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        canvas = new PCanvas();
        pane.add( canvas, BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        pane.add( new PMenuBar(), BorderLayout.NORTH );
        frame.setContentPane( pane );
        
        frame.setLocation( 350, 100 );
        frame.pack();
        
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        final Dimension rigidDim    = new Dimension( 5, 0 );
        JPanel  panel   = new JPanel();
        panel.add( getTranslatePanel() );
        panel.add( Box.createRigidArea( rigidDim ) );
        panel.add( getAddRotatePanel() );
        panel.add( Box.createRigidArea( rigidDim ) );
        
        JButton select  = new JButton( "<html>Select<br>All</html>" );
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        select.addActionListener( e -> {
            canvas.getShapes().forEach( canvas::select );
            canvas.repaint();
        });
        panel.add( select );
        panel.add( exit );
        return panel;
    }
    
    private JPanel getTranslatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 2, 3 ) );
        JButton upButton    = new JButton( upArrow );
        JButton downButton  = new JButton( downArrow );
        JButton leftButton  = new JButton( leftArrow );
        JButton rightButton = new JButton( rightArrow );
        
        upButton.addActionListener( e -> 
            action( p -> p.move( 0, -4 ) )
        );
        downButton.addActionListener( e -> 
            action( p -> p.move( 0, 4 ) )
        );
        leftButton.addActionListener( e -> 
            action( p -> p.move( -4, 0 ) )
        );
        rightButton.addActionListener( e -> 
            action( p -> p.move( 4,0 ) )
        );
        
        panel.add( new JLabel( "" ) );
        panel.add( upButton );
        panel.add( new JLabel( "" ) );
        panel.add( leftButton );
        panel.add( downButton );
        panel.add( rightButton );
        return panel;
    }
    
    private JPanel getAddRotatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 2, 2 ) );
        JButton leftButton  = new JButton( rotateLeft );
        JButton rightButton = new JButton( rotateRight );
        JButton kiteButton  = new JButton( "Kite" );
        JButton dartButton  = new JButton( "Dart" );
        
        leftButton.addActionListener( e -> 
            action( p -> p.rotate( -PShape.D18 ) )
        );
        rightButton.addActionListener( e -> 
            action( p -> p.rotate(  PShape.D18 ) )
        );
        kiteButton.addActionListener( e -> 
            addShape( new PKite( longSide, 0, 0 ) ) 
        );
        dartButton.addActionListener( e -> 
            addShape( new PDart( longSide, 0, 0 ) ) 
        );
        
        panel.add( leftButton );
        panel.add( rightButton );
        panel.add( kiteButton );
        panel.add( dartButton );
        return panel;
    }
    
    private void action( Consumer<PShape> consumer )
    {
        List<PShape>    list    = canvas.getSelected();
        list.forEach( consumer );
        canvas.repaint();
    }
    
    private void addShape( PShape shape )
    {
        int         width   = canvas.getWidth();
        int         height  = canvas.getHeight();
        Rectangle2D rect    = shape.getBounds();
        double      xco     = width / 2 - rect.getWidth() / 2;
        double      yco     = height / 2 - rect.getHeight() / 2;
        canvas.addShape( shape );
        shape.moveTo( xco, yco );
        canvas.deselect();
        canvas.select( shape );
        canvas.repaint();
    }
    
    private void save()
    {
        int choice  = chooser.showSaveDialog( canvas );
        if ( choice == JFileChooser.APPROVE_OPTION )
        {
            File    file    = chooser.getSelectedFile();
            try ( 
                FileOutputStream fileStream = new FileOutputStream( file );
                ObjectOutputStream outStream = 
                    new ObjectOutputStream( fileStream );
            )
            {
                outStream.writeObject( canvas );
            }
            catch ( IOException exc )
            {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(
                    canvas, 
                    "Save Failed", 
                    "IO Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void open()
    {
        int choice  = chooser.showSaveDialog( canvas );
        if ( choice == JFileChooser.APPROVE_OPTION )
        {
            File    file    = chooser.getSelectedFile();
            try ( 
                FileInputStream fileStream = new FileInputStream( file );
                ObjectInputStream inStream = 
                    new ObjectInputStream( fileStream );
            )
            {
                Object input    = inStream.readObject();
                if ( !(input instanceof PCanvas ) )
                    throw new ClassNotFoundException( "Class not found" );
                JPanel  pane    = (JPanel)frame.getContentPane();
                pane.remove( canvas );
                canvas = (PCanvas)input;
                pane.add( canvas, BorderLayout.CENTER );
                frame.pack();
                canvas.repaint();
            }
            catch ( ClassNotFoundException | IOException exc )
            {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(
                    canvas, 
                    "Save Failed", 
                    "IO Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
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
            
            save.addActionListener( e -> save() );
            open.addActionListener( e -> open() );
            
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
            
            select.addActionListener( e -> {
                canvas.getShapes().forEach( canvas::select );
                canvas.repaint();
            });
            
            deselect.addActionListener( e -> {
                canvas.getSelected().forEach( canvas::deselect );
                canvas.repaint();
            });
            
            delete.addActionListener( e -> {
                List<PShape>    selected    = canvas.getSelected();
                while ( !selected.isEmpty() )
                    canvas.delete( selected.get( 0 ) );
                canvas.repaint();
            });
            
            color.addActionListener( e -> {
                String  title       = "Choose a Fill Color";
                Color   shapeColor  = 
                    JColorChooser.showDialog( null, title, null );
                if ( shapeColor != null )
                {
                    canvas.getSelected()
                        .forEach( s -> s.setColor( shapeColor ) );
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
