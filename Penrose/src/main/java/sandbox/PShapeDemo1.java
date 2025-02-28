package sandbox;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.penrose.PCanvas;
import com.gmail.johnstraub1954.penrose.PDart;
import com.gmail.johnstraub1954.penrose.PKite;
import com.gmail.johnstraub1954.penrose.PShape;

public class PShapeDemo1
{
    private PCanvas canvas;
    
    public static void main(String[] args)
    {
        String      kiteName    = PKite.class.getSimpleName();
        PShape.setDefaultFillColor( kiteName,  new Color( 0xe63900 ) );
        String      dartName    = PDart.class.getSimpleName();
        PShape.setDefaultFillColor( dartName, new Color( 0x0073e6 ) );

        PShapeDemo1   demo        = new PShapeDemo1();
        SwingUtilities.invokeLater( () -> {
            demo.build(); 
            demo.canvas.addShape( new PKite( 50, 25, 0 ) );
            demo.canvas.addShape( new PDart( 50, 85, 0 ) );
            demo.canvas.addShape( new PKite( 50, 25, 100 ) );
            demo.canvas.addShape( new PDart( 50, 85, 100 ) );
        });
    }

    private void build()
    {
        JFrame  frame   = new JFrame( "PShape Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        canvas = PCanvas.getDefaultCanvas();
        frame.setContentPane( canvas );
        frame.pack();
        frame.setVisible( true );
    }
}
