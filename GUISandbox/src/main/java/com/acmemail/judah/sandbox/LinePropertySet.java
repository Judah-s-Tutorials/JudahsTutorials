package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.util.Optional;
import java.util.OptionalDouble;

public class LinePropertySet
{
	private final PropertyManager	pMgr	= PropertyManager.instanceOf();
    private final String    major;
    private OptionalDouble  stroke;
    private OptionalDouble  length;
    private OptionalDouble  spacing;
    private Optional<Color> color;
    
    public LinePropertySet( String major )
    {
        this.major = major;
        reset();
    }
    
    public void apply()
    {
        if ( hasStroke() )
            pMgr.put( major, PropertyManager.STROKE, stroke.getAsDouble() );
        if ( hasLength() )
            pMgr.put( major, PropertyManager.LENGTH, length.getAsDouble() );
        if ( hasSpacing() )
            pMgr.put( major, PropertyManager.SPACING, spacing.getAsDouble() );
        if ( hasColor() )
            pMgr.put( major, PropertyManager.COLOR, color.get() );
    }
    
    public void reset()
    {
        stroke = pMgr.getAsDouble( major, PropertyManager.STROKE );
        length = pMgr.getAsDouble( major, PropertyManager.LENGTH );
        spacing = pMgr.getAsDouble( major, PropertyManager.SPACING );
        color = pMgr.getAsColor( major, PropertyManager.COLOR );
    }
    
    public String getMajorCategory()
    {
        return major;
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
    
    public double getStroke()
    {
        return stroke.orElse( -1 );
    }
    
    public double getLength()
    {
        return length.orElse( -1 );
    }
    
    public double getSpacing()
    {
        return spacing.orElse( -1 );
    }
    
    public Color getColor()
    {
        return color.orElse( null );
    }
    
    public void setStroke( double stroke )
    {
        if ( hasStroke() )
            this.stroke = OptionalDouble.of( stroke );
    }
    
    public void setLength( double length )
    {
        if ( hasLength() )
            this.length = OptionalDouble.of( length );
    }
    
    public void setSpacing( double spacing )
    {
        if ( hasSpacing() )
            this.spacing = OptionalDouble.of( spacing );
    }
    
    public void setColor( Color color )
    {
        if ( hasColor() )
            this.color = Optional.of( color );
    }
}
