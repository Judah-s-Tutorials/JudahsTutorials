package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.util.Optional;
import java.util.OptionalInt;

public class LinePropertySet
{
	private final PropertyManager	pMgr	= new PropertyManager();
    private final String    major;
    private OptionalInt     stroke;
    private OptionalInt     length;
    private OptionalInt     spacing;
    private Optional<Color> color;
    
    public LinePropertySet( String major )
    {
        this.major = major;
        stroke = pMgr.getAsInt( major, PropertyManager.STROKE );
        length = pMgr.getAsInt( major, PropertyManager.LENGTH );
        spacing = pMgr.getAsInt( major, PropertyManager.SPACING );
        color = pMgr.getAsColor( major, PropertyManager.COLOR );
    }
    
    public void apply()
    {
        if ( hasStroke() )
            pMgr.put( major, PropertyManager.STROKE, stroke.getAsInt() );
        if ( hasLength() )
            pMgr.put( major, PropertyManager.LENGTH, length.getAsInt() );
        if ( hasSpacing() )
            pMgr.put( major, PropertyManager.SPACING, spacing.getAsInt() );
        if ( hasColor() )
            pMgr.put( major, PropertyManager.COLOR, color.get() );
    }
    
    public boolean hasStroke()
    {
        return stroke.isPresent();
    }
    
    public boolean hasLength()
    {
        return length.isPresent();
    }
    
    public boolean hasSpacing()
    {
        return spacing.isPresent();
    }
    
    public boolean hasColor()
    {
        return color.isPresent();
    }
    
    public int getStroke()
    {
        return stroke.orElse( -1 );
    }
    
    public int getLength()
    {
        return stroke.orElse( -1 );
    }
    
    public int getSpacing()
    {
        return stroke.orElse( -1 );
    }
    
    public Color getColor()
    {
        return color.orElse( null );
    }
    
    public void setStroke( int stroke )
    {
        if ( hasStroke() )
            this.stroke = OptionalInt.of( stroke );
    }
    
    public void setLength( int length )
    {
        if ( hasLength() )
            this.length = OptionalInt.of( length );
    }
    
    public void setSpacing( int spacing )
    {
        if ( hasSpacing() )
            this.spacing = OptionalInt.of( spacing );
    }
    
    public void setColor( Color color )
    {
        if ( hasColor() )
            this.color = Optional.of( color );
    }
}
