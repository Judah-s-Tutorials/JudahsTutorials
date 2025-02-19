package temp;

import static com.gmail.johnstraub1954.penrose2.PShape.D108;
import static com.gmail.johnstraub1954.penrose2.PShape.D36;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.gmail.johnstraub1954.penrose2.Vertex;
import com.judahstutorials.javaintro.tools.Turtle;

public class VertexTurtleDemo extends JPanel
{
    private static final double     longSide    = 200;
    private static final double     shortSide   = 
        longSide * (Math.sin( D36 ) / Math.sin( D108 ));
    private static final Point2D    begin       = 
        new Point2D.Double( 0, 0 );
    
    private int             type    = BufferedImage.TYPE_INT_ARGB;
    private BufferedImage   image   = 
        new BufferedImage( (int)longSide, (int)longSide, type );
    
    public static void main(String[] args)
    {
        Turtle  tutt    = new Turtle();
        tutt.paint( -36, longSide );
        tutt.paint( 72 - 180, longSide );
        tutt.paint( 36 - 180, shortSide );
        tutt.paint( 216 - 180, shortSide );
    }
}
