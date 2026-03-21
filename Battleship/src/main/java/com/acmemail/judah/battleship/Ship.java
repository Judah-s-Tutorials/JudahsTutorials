package com.acmemail.judah.battleship;
import static com.acmemail.judah.battleship.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship.Orientation.VERTICAL;

import java.awt.Rectangle;
public class Ship
{
    private static final String DEF_NAME    = "Default Name";
    
    private final ShipType      type;
    private final String        name;
    private final GridCoords    firstSquare;
    private final Orientation   orientation;
    
    public Ship( 
        ShipType type, 
        GridCoords first, 
        Orientation orientation
    )
    {
        this( type, DEF_NAME, first, orientation );
    }
    
    public Ship( 
        ShipType type, 
        String name, 
        GridCoords first, 
        Orientation orientation
    )
    {
        super();
        this.type = type;
        this.name = name;
        this.firstSquare = first;
        this.orientation = orientation;
    }
    
    public boolean contains( int xco, int yco )
    {
        boolean contains    =
            xco >= getMinX() && xco < getMaxX()
            && yco >= getMinY() && yco < getMaxY();
        return contains;
    }
    
    public boolean contains( GridCoords pair )
    {
        int     xco         = pair.getXco();
        int     yco         = pair.getYco();
        boolean contains    = contains( xco, yco );
        return contains;
    }
    
    public boolean intersects( Rectangle rect )
    {
        Rectangle   thisRect    = getRect();
        boolean     result      = rect.intersects( thisRect );
        return result;
    }
    
    public boolean intersects( Ship that )
    {
        Rectangle   rect    = that.getRect();
        boolean     result  = intersects( rect );
        return result;
    }
    
    public Rectangle getRect()
    {
        Rectangle   rect    =
            new Rectangle( getMinX(), getMinY(), getWidth(), getHeight() );
        return rect;
    }
    
    public int getWidth()
    {
        int width   = orientation == HORIZONTAL ? type.getLength() : 1;
        return width;
    }
    
    public int getHeight()
    {
        int height  = orientation == VERTICAL ? type.getLength() : 1;
        return height;
    }
    
    public int getMinX()
    {
        return firstSquare.getXco();
    }
    
    public int getMaxX()
    {
        int maxXco  = getMinX() + getWidth();
        return maxXco;
    }
    
    public int getMinY()
    {
        return firstSquare.getYco();
    }
    
    public int getMaxY()
    {
        int maxY    = getMinY() + getHeight();
        return maxY;
    }

    /**
     * Gets the ship type.
     * 
     * @return the ship type
     */
    public ShipType getType()
    {
        return type;
    }

    /**
     * Gets the name of the ship type.
     * 
     * @return the ship type name
     */
    public String getTypeName()
    {
        return type.getTypeName();
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the firstSquare
     */
    public GridCoords getFirstSquare()
    {
        return firstSquare;
    }

    /**
     * @return the length
     */
    public int getLength()
    {
        int len = type.getLength();
        return len;
    }

    /**
     * @return the orientation
     */
    public Orientation getOrientation()
    {
        return orientation;
    }
    
    @Override
    public String toString()
    {
        int             len     = getLength();
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "name=" ).append( name )
            .append( ",type=" ).append( type )
            .append( ",start=" ).append( firstSquare )
            .append( ",orient=" ).append( orientation )
            .append( ",length=" ).append( len );
        
        return bldr.toString();
    }
}
