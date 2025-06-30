package com.gmail.johnstraub1954.penrose.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.gmail.johnstraub1954.penrose.PCanvas;
import com.gmail.johnstraub1954.penrose.PShape;

public class FileManager
{
    private static final List<PropertyChangeListener>   changeListeners =
        new ArrayList<>();
    private static final String         chooserTitle    = "Choose File";
    private static final PCanvas        canvas          = 
        PCanvas.getDefaultCanvas();
    private static final JFileChooser   chooser;
    
    private static File     currFile        = null;

    static
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
        chooser.setDialogTitle( chooserTitle );
    }
    
    public static synchronized void 
    addPropertyChangeListener( PropertyChangeListener listener )
    {
        changeListeners.add( listener );
    }
    
    public static synchronized void 
    removePropertyChangeListener( PropertyChangeListener listener )
    {
        changeListeners.remove( listener );
    }
   
    public static synchronized void open()
    {
        int choice  = chooser.showOpenDialog( canvas );
        if ( choice == JFileChooser.APPROVE_OPTION )
        {
            File    file    = chooser.getSelectedFile();
            open( file );
        }
    }
    
    public static synchronized void save()
    {
        File    file    = currFile;
        if ( file == null )
        {
            int choice  = chooser.showSaveDialog( canvas );
            if ( choice == JFileChooser.APPROVE_OPTION )
                file    = chooser.getSelectedFile();
        }
        if ( file != null )
            save( file );
    }
    
    public static synchronized void saveAs()
    {
        File    file    = currFile;
        int     choice  = chooser.showSaveDialog( canvas );
        if ( choice == JFileChooser.APPROVE_OPTION )
            file    = chooser.getSelectedFile();
        if ( file != null )
            save( file );
    }
    
    public static File getCurrFile()
    {
        return currFile;
    }

    private static void save( File file )
    {
        try ( 
            FileOutputStream fileStream = new FileOutputStream( file );
            ObjectOutputStream outStream = 
                new ObjectOutputStream( fileStream );
        )
        {
            SelectionManager    mgr     = canvas.getSelectionManager();
            List<PShape>        shapes  = mgr.getShapes();
            outStream.writeObject( shapes );
            setCurrFile( file );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            String  path    = file.getPath();
            JOptionPane.showMessageDialog(
                canvas, 
                path + " Save Failed", 
                "IO Error", 
                JOptionPane.ERROR_MESSAGE
            );
            setCurrFile( file );
        }
    }
    
    private static void open( File file )
    {
        try ( 
            FileInputStream fileStream = new FileInputStream( file );
            ObjectInputStream inStream = 
                new ObjectInputStream( fileStream );
        )
        {
            Object input    = inStream.readObject();
            if ( !(input instanceof List<?>) )
                throw new Malfunction( "Invalid object read" );
            @SuppressWarnings("unchecked")
            List<PShape>        shapes  = (List<PShape>)input;
            SelectionManager    mgr     = canvas.getSelectionManager();
            mgr.setShapes( shapes );
            canvas.repaint();
        }
        catch ( ClassNotFoundException | IOException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                canvas, 
                "Open Failed: " + exc.getMessage(), 
                "IO Error", 
                JOptionPane.ERROR_MESSAGE
            );
            setCurrFile( file );
        }
    }
    
    private static void setCurrFile( File file )
    {
        File    oldValue    = currFile;
        currFile = file;
        fireChangeListeners( oldValue );
    }

    private static void fireChangeListeners( Object oldValue )
    {
        PropertyChangeEvent evt = 
            new PropertyChangeEvent( canvas, "CurrFile", oldValue, currFile );
        changeListeners.forEach( l -> l.propertyChange( evt ) );
    }
}
