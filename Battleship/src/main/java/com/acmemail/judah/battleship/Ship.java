package com.acmemail.judah.battleship;
public class Ship
{
    private static final String DEF_NAME    = "Default Name";
    
    private final String        type;
    private final String        name;
    private final OrderedPair   firstSquare;
    private final OrderedPair   lastSquare;
    private final int           length;
    private final Orientation   orientation;
    public Ship( 
        String type, 
        OrderedPair first, 
        int length, 
        Orientation orientation
    )
    {
        this( type, DEF_NAME, first, length, orientation );
    }
    
    public Ship( 
        String type, 
        String name, 
        OrderedPair first, 
        int length, 
        Orientation orientation
    )
    {
        super();
        this.type = type;
        this.name = name;
        this.firstSquare = first;
        this.length = length;
        this.orientation = orientation;
        
        int lastXco;
        int lastYco;
        if ( orientation == Orientation.VERTICAL )
        {
            lastXco = first.getXco();
            lastYco = first.getYco() + length - 1;
        }
        else
        {
            lastXco = first.getXco() + length - 1;
            lastYco = first.getYco();
        }
        lastSquare = new OrderedPair( lastXco, lastYco );
    }
    
    public boolean contains( int xco, int yco )
    {
        boolean contains    =
            xco >= firstSquare.getXco() && xco <= lastSquare.getXco()
            && yco >= firstSquare.getYco() && yco <= lastSquare.getYco();
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
        boolean result  = false;
        if ( orientation != that.orientation )
        {
            if ( orientation == Orientation.VERTICAL )
                result = testIntersectDiff( this, that );
            else
                result = testIntersectDiff( that, this );
        }
        else if ( orientation == Orientation.VERTICAL )
            result = testIntersectVertical( this, that );
        else
            result = testIntersectnHorizontal( this, that );
        return result;
    }
    
    public int getMinX()
    {
        return firstSquare.getXco();
    }
    
    public int getMaxX()
    {
        return lastSquare.getXco();
    }
    
    public int getMinY()
    {
        return firstSquare.getYco();
    }
    
    public int getMaxY()
    {
        return lastSquare.getYco();
    }

    /**
     * @return the type
     */
    public String getType()
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
     * @return the lastSquare
     */
    public OrderedPair getLastSquare()
    {
        return lastSquare;
    }

    /**
     * @return the length
     */
    public int getLength()
    {
        return length;
    }

    /**
     * @return the orientation
     */
    public Orientation getOrientation()
    {
        return orientation;
    }
    
    private static boolean 
    testIntersectDiff( Ship vertical, Ship horizontal )
    {
        int     vXco    = vertical.getMaxX();
        int     vMinYco = vertical.getMinY();
        int     vMaxYco = vertical.getMaxY();
        int     hYco    = horizontal.getMaxY();
        int     hMinXco = horizontal.getMinX();
        int     hMaxXco = horizontal.getMaxX();
        
        boolean result  = 
            vXco >= hMinXco && vXco <= hMaxXco &&
            vMinYco <= hYco && vMaxYco >= hYco;
        return result;
    }
    
    private static boolean 
    testIntersectVertical( Ship ship1, Ship ship2 )
    {
        int     ship1Xco        = ship1.getMaxX();
        int     ship2Xco        = ship2.getMaxX();
        int     ship1MinYco     = ship1.getMinY();
        int     ship1MaxYco     = ship1.getMaxY();
        int     ship2MinYco     = ship2.getMinY();
        int     ship2MaxYco     = ship2.getMaxY();
        
        boolean result  = false;
        if ( ship1Xco != ship2Xco )
            result = false;
        else if ( ship1MinYco <= ship2MinYco )
            result = ship1MaxYco >= ship2MinYco;
        else 
            result = ship2MaxYco >= ship1MinYco;
        return result;
    }
    
    private static boolean 
    testIntersectnHorizontal( Ship ship1, Ship ship2 )
    {
        int     ship1Yco        = ship1.getMaxY();
        int     ship2Yco        = ship2.getMaxY();
        int     ship1MinXco     = ship1.getMinX();
        int     ship1MaxXco     = ship1.getMaxX();
        int     ship2MinXco     = ship2.getMinX();
        int     ship2MaxXco     = ship2.getMinX();
        
        boolean result  = false;
        if ( ship1Yco != ship2Yco )
            result = false;
        else if ( ship1MinXco <= ship2MinXco )
            result = ship1MaxXco >= ship2MinXco;
        else 
            result = ship2MaxXco >= ship1MinXco;
        return result;
    }
}
