package com.acmemail.judah.sandbox.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.acmemail.judah.sandbox.LinePropertySet;
import com.acmemail.judah.sandbox.PropertyManager;

/**
 * Maintains test data
 * for unit testing
 * of the LinePanel class
 * and and related facilities.
 * Data is organized by major category.
 * Contains methods for 
 * generating a unique set of data
 * from existing values
 * ({@ink #getUniqueData()}
 * and for validating
 * expected data
 * against actual data
 * ({@link #assertMapsTo(JComponent)}, {@link #assertMapsTo(LinePropertySet)_.
 * 
 * @author Jack Straub
 * 
 * @see #getUniqueData()
 * @see #assertMapsTo(JComponent)
 * @see #assertMapsTo(LinePropertySet)
 */
public class LineTestData
{
    private final PropertyManager   pMgr    = PropertyManager.instanceOf();
    private final String            majorCategory;
    
    private OptionalDouble  stroke;
    private OptionalDouble  length;
    private OptionalDouble  spacing;
    private Optional<Color> color;
    
    private LineTestData()
    {
        majorCategory = null;
    }
    
    public LineTestData( String cat )
    {
        this.majorCategory = cat;
        stroke = pMgr.getAsDouble( cat, PropertyManager.STROKE );
        length = pMgr.getAsDouble( cat, PropertyManager.LENGTH );
        spacing = pMgr.getAsDouble( cat, PropertyManager.SPACING );
        color = pMgr.getAsColor( cat, PropertyManager.COLOR );
    }
    
    public LineTestData(
        JSpinner strokeSpinner,
        JSpinner lengthSpinner,
        JSpinner spacingSpinner,
        JTextField colorField
    )
    {
        majorCategory = null;
        stroke = getOptional( strokeSpinner );
        length = getOptional( lengthSpinner );
        spacing = getOptional( spacingSpinner );
        color = getOptionalColor( colorField );
    }
    
    public static LineTestData of( JComponent button )
    {
        LinePropertySet set = LinePropertySet.getPropertySet( button );
        String          cat = set.getMajorCategory();
        
        LineTestData data = new LineTestData( cat );
        return data;
    }
    
    public LineTestData getUniqueData()
    {
        LineTestData  data    = new LineTestData();
        data.stroke = getUniqueData( stroke );
        data.length = getUniqueData( length );
        data.spacing = getUniqueData( spacing );
        data.color = getUniqueData( color );
        
        return data;
    }
    
    public String getMajorCategory()
    {
        return majorCategory;
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
    
    public void assertMapsTo( JComponent comp )
    {
        LinePropertySet set = LinePropertySet.getPropertySet( comp );
        System.out.println( set.getMajorCategory() );
        assertMapsTo( set );
    }
    
    public void assertMapsTo( LinePropertySet set )
    {
        assertEquals( hasStroke(), set.hasStroke() );
        assertEquals( hasLength(), set.hasLength() );
        assertEquals( hasSpacing(), set.hasSpacing() );
        assertEquals( hasColor(), set.hasColor() );
        
        if ( hasStroke() )
            assertEquals( getStroke(), set.getStroke() );
        if ( hasLength() )
            assertEquals( getLength(), set.getLength() );
        if ( hasSpacing() )
            assertEquals( getSpacing(), set.getSpacing() );
        if ( hasColor() )
            assertEquals( getColor(), set.getColor() );
    }
    
    @Override
    public int hashCode()
    {
        int hash    = 
            Objects.hash(
                majorCategory,
                stroke,
                length,
                spacing,
                color
            );
        return hash;
    }
   
    @Override
    public boolean equals( Object other )
    {
        boolean result = false;
        if  ( other != null && other instanceof LineTestData )
        {
            LineTestData    that    = (LineTestData)other;
            result = Objects.equals( this.majorCategory, that.majorCategory );
            result = result
                && this.stroke.equals( that.stroke )
                && this.length.equals( that.length )
                && this.spacing.equals( that.spacing )
                && this.color.equals( that.color );
        }
        return result;
    }
    
    private OptionalDouble getOptional( JSpinner spinner )
    {
        OptionalDouble  optDouble   = OptionalDouble.empty();
        if ( spinner.isEnabled() )
        {
            try
            {
                spinner.commitEdit();
                optDouble = OptionalDouble.of( (double)spinner.getValue() );
            }
            catch ( ParseException exc )
            {
                optDouble = OptionalDouble.of( -1. );
            }
        }
            
        return optDouble;
    }
    
    private Optional<Color> getOptionalColor( JTextField field )
    {
        Optional<Color> optColor    = Optional.empty();
        if ( field.isEnabled() )
        {
            try
            {
                int intColor    = Integer.decode( field.getText() );
                optColor = Optional.of( new Color( intColor ) );
            }
            catch ( NumberFormatException exc )
            {
                optColor = Optional.of( Color.WHITE );
            }
        }
        return optColor;
    }
    
    private OptionalDouble getUniqueData( OptionalDouble prop )
    {
        OptionalDouble  result  = OptionalDouble.empty();
        if ( prop.isPresent() )
        {
            double  oldData = prop.getAsDouble();
            double  newData = oldData * 2 + 1;
            result = OptionalDouble.of( newData );
        }
        return result;
    }
    
    private Optional<Color> getUniqueData( Optional<Color> prop )
    {
        Optional<Color> result  = Optional.empty();
        if ( prop.isPresent() )
        {
            Color   oldColor    = prop.get();
            int     oldInt      = oldColor.getRGB();
            int     newInt      = (oldInt & 0x000000ff) + 1;
            Color   newColor    = new Color( newInt );
            result = Optional.of( newColor );
        }
        return result;
    }
}
