package com.acmemail.judah.battleship;

public class Ship
{
    public enum Orientation
    {
        HORIZONTAL,
        VERTICAL
    }
    
    public enum Type
    {
        BATTLESHIP( 4),
        CARRIER( 5 ),
        CRUISER( 3 ),
        DESTROYER( 3 ),
        SUBMARINE( 2 );
        
        private final int   length;
        
        Type( int length )
        {
            this.length = length;
        }
        
        public int getLength()
        {
            return length;
        }
    }
    
    private static final String DEF_NAME    = "Default Name";
    
    private final String        type;
    private final String        name;
    private final OrderedPair   firstSquare;
    private final OrderedPair   lastSquare;
    private final int           length;
    private final Orientation   orientation;
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
            lastYco = first.getYco() + length;
        }
        else
        {
            lastXco = first.getXco() + length;
            lastYco = first.getYco();
        }
        lastSquare = new OrderedPair( lastXco, lastYco );
    }
    
    public boolean contains( int xco, int yco )
    {
        boolean contains    =
            xco >= firstSquare.getXco() && xco <= lastSquare.getXco()
            && yco >= firstSquare.getYco() && xco <= lastSquare.getYco();
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
    
    
}
