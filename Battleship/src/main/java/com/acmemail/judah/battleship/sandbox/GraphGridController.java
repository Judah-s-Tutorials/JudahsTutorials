package com.acmemail.judah.battleship.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship2D.Cell2DView;
import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Orientation;
import com.acmemail.judah.battleship2D.Ship2D;
import com.acmemail.judah.battleship2D.ShipTypes;
import com.acmemail.judah.battleship2D.default_ship_types.Battleship;
import com.acmemail.judah.battleship2D.default_ship_types.Destroyer;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

public class GraphGridController
{
    private static GridFrame    gridFrame;
    private static Parent       controller; 

    public static void main(String[] args)
    {
        Grid2D  grid    = new Grid2D();
        Grid2D  grid2   = new Grid2D( "Opponent 1" );
        Grid2D  grid3   = new Grid2D( "Opponent 2" );
        Grid2D  grid4   = new Grid2D( "Opponent 3" );
        Grid2D  grid5   = new Grid2D( "Opponent 4" );
        Grid2D  grid6   = new Grid2D( "Opponent 5" );
        Grid2D  grid7   = new Grid2D( "Opponent 6" );
        gridFrame = GridFrame.getFrame( () -> new Parent() );
        controller = (Parent)gridFrame.getClient();

        ShipTypes.registerDefaultTypes();
        Fleet   fleet   = new Fleet();
        fleet.addToBeDeployed( Battleship.getType(), "ship 1" );
        fleet.addToBeDeployed( Destroyer.getType(), "ship 2" );
        fleet.addToBeDeployed( Submarine.getType(), "ship 3" );
        
        Configurator.nextState();
        
        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployed();
        Fleet.Proto         proto           = toBeDeployed.get( 0 );
        GridCoords          coords          = new GridCoords( 0, 0 );
        Ship2D              ship            =
            fleet.getShip( coords, "", Orientation.HORIZONTAL, proto );
        Result              result          = fleet.deploy( ship, proto );
        if ( !result.getStatus() )
            showResult( result );
        proto = toBeDeployed.get( 0 );
        coords = new GridCoords( 1, 2 );
        ship = fleet.getShip( coords, "", Orientation.VERTICAL, proto );
        result = fleet.deploy( ship, proto );
        if ( !result.getStatus() )
            showResult( result );
        proto = toBeDeployed.get( 0 );
        coords = new GridCoords( 2, 4 );
        ship = fleet.getShip( coords, "", Orientation.HORIZONTAL, proto );
        result = fleet.deploy( ship, proto );
        if ( !result.getStatus() )
            showResult( result );
        
        Configurator.nextState();
        
        grid.attack( new GridCoords( 1, 1 ) );
        grid.attack( new GridCoords( 1, 2 ) );
        grid.attack( new GridCoords( 5, 3 ) );
        grid.attack( new GridCoords( 2, 8 ) );
        controller.repaint();
    }
    
    private static void showResult( Result result )
    {
        String  messages    = String.join( "\n", result.getMessages() );
        JOptionPane.showMessageDialog( null, messages );
        System.exit( 1 );
    }
    
    public static class Parent extends JPanel
    {
        private final int       border1Width    = 5;
        private final int       border2Width    = 5;
        private final Color     border2Color    = Color.BLUE;
        
        public Parent()
        {
            Grid2D  home    = Grid2D.getHomeGrid();
            setGUILayout();
            setMaxDimensions();
            add( getTitleComponent( home ) );
            for ( Grid2D grid : Grid2D.getAllGrids() )
            {
                if ( grid != home )
                    add( getTitleComponent( grid ) );
            }
        }
        
        private void setGUILayout()
        {
            List<Grid2D>    allGrids    = Grid2D.getAllGrids();
            int             gridCount   = allGrids.size();
            int             guiLayoutNumRows;
            int             guiLayoutNumCols;
            if ( gridCount <= 3 )
            {
                guiLayoutNumRows = 1;
                guiLayoutNumCols = gridCount;
            }
            else if ( gridCount == 4 )
            {
                guiLayoutNumRows = 2;
                guiLayoutNumCols = 2;
            }
            else
            {
                guiLayoutNumRows = (int)Math.ceil( gridCount  / 3.);
                guiLayoutNumCols = 3;
            }

            setLayout( new GridLayout( guiLayoutNumRows, guiLayoutNumCols ) );
        }
        
        private void setMaxDimensions()
        {
            GraphicsConfiguration config    = 
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
            Insets      insets = 
                Toolkit.getDefaultToolkit().getScreenInsets( config );
            Rectangle   bounds = config.getBounds();
            
            int maxWidth    = bounds.width - insets.left - insets.right;
            int maxHeight   = bounds.height - insets.top - insets.bottom;
            setMaximumSize( new Dimension( maxWidth, maxHeight ) );
        }
    
        private JPanel getBorderPanel()
        {
            JPanel  panel   = new JPanel();
            Border  outerBorder = 
                BorderFactory.createEmptyBorder( 
                    border1Width, 
                    border1Width, 
                    border1Width, 
                    border1Width 
                );
            Border  innerBorder =
                BorderFactory.createLineBorder( border2Color, border2Width );
            Border  border      =
                BorderFactory.createCompoundBorder( innerBorder, outerBorder );
            panel.setBorder( border );
            return panel;
        }

        private JComponent getTitleComponent( Grid2D grid)
        {
            // Title and controller should live inside the border
            // The border should be inside a scroll pane
            // The border component should never be resized
            JPanel  borderPanel = getBorderPanel();
            BoxLayout   layout = new BoxLayout( borderPanel, BoxLayout.Y_AXIS );
            borderPanel.setLayout( layout );

            borderPanel.add( getTitle( grid.getName() ) );
            borderPanel.add( new Controller( grid ) );
            
            // The dummy panel is to prevent the border panel from being resized
            JPanel  dummyPanel  = new JPanel();
            dummyPanel.add( borderPanel );
            JScrollPane pane    = new JScrollPane( dummyPanel );

            return pane;
        }
    }

    private static JLabel   getTitle( String name )
    {
        String  titleText   =
            "<HTML><BODY style='font-size: 150%;'>"
            + name 
            + "</BODY></HTML>";
        JLabel  title   = new JLabel( titleText );
        return title;
    }

    public static class Controller extends JPanel
    {
        private final Font      labelFont;
        private final Color     bgColor     = Color.LIGHT_GRAY;
        private final Color     shipColor   = Color.DARK_GRAY;
        private final Color     gridColor   = Color.BLACK;
        private final Color     splatColor  = Color.RED;
        private final Color     labelColor  = Color.BLACK;
        private final int       cellSide    = 20;
        private final Grid2D    grid;
        private final int       numRows;
        private final int       numCols;
        private final Rectangle gridBounds;
        
        private final Rectangle shipRect    = new Rectangle();
        private Dimension       labelBounds = null;
        
        public Controller( Grid2D grid )
        {
            this.grid = grid;
            
            labelFont   = new Font( Font.MONOSPACED, Font.BOLD, 12 );
            labelBounds = getMaxLabelBounds( labelFont, getFontMetrics( labelFont ) );
            
            numRows = Grid2D.getNumRows();
            numCols = Grid2D.getNumCols();
            int     gridWidth   = numCols * cellSide;
            int     gridHeight  = numRows * cellSide;
            int     winWidth    = gridWidth + labelBounds.width;
            int     winHeight   = gridHeight + labelBounds.height;
            gridBounds = new Rectangle( 0, 0, gridWidth, gridHeight );

            Dimension   dim     = new Dimension( winWidth, winHeight );
            setPreferredSize( dim );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            Graphics2D  gtx         = (Graphics2D)graphics;
            int         winWidth    = getWidth();
            int         winHeight   = getHeight();
            gtx.setColor( bgColor );
            gtx.fillRect( 0, 0, winWidth, winHeight );
            gtx.setFont( labelFont );
            
            paintShips( gtx );
            paintGridLines( gtx );
            paintSplats( gtx );
            paintLabels( gtx );
        }

        private void paintShips( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = labelBounds.width;
            gtx.translate( translate, translate );
            
            gtx.setColor( shipColor );
            for ( Ship2D ship : grid.getAllShips() )
            {
                Rectangle   bounds  = ship.getBounds();
                shipRect.x = cellSide * bounds.x;
                shipRect.y = cellSide * bounds.y;
                shipRect.width = cellSide * bounds.width;
                shipRect.height = cellSide * bounds.height;
                gtx.fill( shipRect );
            }

            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }

        private void paintGridLines( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = labelBounds.width;
            gtx.translate( translate, translate );
            gtx.setColor( gridColor );

            int             gridWidth       = gridBounds.width;
            int             gridHeight      = gridBounds.height;
            IntStream.iterate( 0, i -> i <= gridWidth , i -> i += cellSide )
                .mapToObj( i -> new Line2D.Double( i, 0, i, gridHeight ) )
                .forEach( gtx::draw );
            IntStream.iterate( 0, i -> i <= gridHeight , i -> i += cellSide )
                .mapToObj( i -> new Line2D.Double( 0, i, gridWidth, i ) )
                .forEach( gtx::draw );

            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }
        
        private void paintSplats( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = labelBounds.width;
            gtx.translate( translate, translate );
            gtx.setColor( splatColor );

            grid.getCells()
            .filter( Cell2DView::isSplatted )
                .map ( c -> 
                    new Rectangle(
                        c.getCoords().xco() * cellSide + 3,
                        c.getCoords().yco() * cellSide + 3,
                        cellSide - 6,
                        cellSide - 6
                    )
                )
                .forEach( gtx::fill );

            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }

        private void paintLabels( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = labelBounds.width;
            gtx.translate( translate, translate );
            gtx.setColor( labelColor );

            FontRenderContext   context     = gtx.getFontRenderContext();
            Font                font        = gtx.getFont();
            
            for ( int row = 0 ; row < numRows ; ++row )
            {
                Label       label       = new Label( 0, row );
                String      strLabel    = label.getRowStr();
                Rectangle2D strBounds   = 
                    font.getStringBounds( strLabel, context );
                int         iWidth      = (int)strBounds.getWidth();
                int         iHeight     = (int)strBounds.getHeight();
                int         xco         = -iWidth - 5;
                int         yco         = 
                    cellSide * (row + 1) - cellSide / 2 + iHeight / 2 - 1;
                gtx.drawString( strLabel, xco, yco );
            }
            
            // The labels on the columns require another modification
            // to the transform, so displaying the column labels should
            // be the last thing we do before restoring the original
            // transform (saveTransform).
            gtx.rotate( Math.PI / 2 );
            for ( int col = 0 ; col < numCols ; ++col )
            {
                Label       label       = new Label( col, 0 );
                String      strLabel    = label.getColStr();
                Rectangle2D strBounds   = 
                    font.getStringBounds( strLabel, context );
                int         iWidth      = (int)strBounds.getWidth();
                int         iHeight     = (int)strBounds.getHeight();
                
                // drawing the column labels in the       .
                // rotated context is nearly the same     .
                // as drawing the row labels, except      4 _____
                // the first label (1) has to drawn at    3 _____
                // the bottom of the figure, and later    2 _____
                // positions extend toward the top        1 _____
                int         xco         = -iWidth - 5;
                int         yco         = 
                    -cellSide * col - cellSide / 2 + iHeight / 4;
                gtx.drawString( strLabel, xco, yco );
            }
            
            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }
        
        private static Dimension getMaxLabelBounds( 
            Font font, 
            FontMetrics fontMetrics 
        )
        {
            int         height      = 
                fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
            int         width       = 2 * fontMetrics.getMaxAdvance();
            Dimension   dim         = new Dimension( width, height );
            return dim;
        }
    }
}
