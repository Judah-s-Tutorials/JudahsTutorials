package com.acmemail.judah.battleship;
import static com.acmemail.judah.battleship.Orientation.*;
public class Ship
{
    private static final String DEF_NAME    = "Default Name";
    
    private final ShipType      type;
    private final String        name;
    private final OrderedPair   firstSquare;
//    private final OrderedPair   lastSquare;
//    private final int           length;
//    private final int           width;
//    private final int           height;
    private final Orientation   orientation;
    
    public Ship( 
        ShipType type, 
        OrderedPair first, 
        Orientation orientation
    )
    {
        this( type, DEF_NAME, first, orientation );
    }
    
    public Ship( 
        ShipType type, 
        String name, 
        OrderedPair first, 
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
    
    public boolean contains( OrderedPair pair )
    {
        int     xco         = pair.getXco();
        int     yco         = pair.getYco();
        boolean contains    = contains( xco, yco );
        return contains;
    }
    
    public boolean contains( Square square )
    {
        int     xco         = square.getXco();
        int     yco         = square.getYco();
        boolean contains    = contains( xco, yco );
        return contains;
    }
    
    public boolean intersects( Ship that )
    {
        boolean result      = false;
        int     thisMinX    = getMinX();
        int     thisMaxX    = getMaxX();
        int     thatMinX    = that.getMinX();
        int     thatMaxX    = that.getMaxX();
        int     thisMinY    = getMinY();
        int     thisMaxY    = getMaxY();
        int     thatMinY    = that.getMinY();
        int     thatMaxY    = that.getMaxY();
        if ( thisMinY >= thatMaxY )
            result = false;
        else if ( thisMaxY <= thatMinY )
            result = false;
        else if ( thisMinX >= thatMaxX )
            result = false;
        else if ( thisMaxX <= thatMinX )
            result = false;
        else
            result = true;
        
//        if ( thisMaxX >= thatMinX && thisMinX < thatMaxX )
//            if ( thisMaxY >= thatMinY && thisMinY < thatMaxY )
//                result = true;
        return result;
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
     * @return the type
     */
    public ShipType getType()
    {
        return type;
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
    public OrderedPair getFirstSquare()
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
