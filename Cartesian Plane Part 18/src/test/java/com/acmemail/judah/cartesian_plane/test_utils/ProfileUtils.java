package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.Color;
import java.awt.Font;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;

/**
 * Utility class containing facilities
 * for testing with Profiles.
 * 
 * @author Jack Straub
 */
public class ProfileUtils
{
    /**
     * Create a profile with property values
     * that are different from a given Profile.
     *  
     * @param profile   the given Profile
     * 
     * @return
     *      a profile with property values
     *      that are different from a given Profile
     */
    public static Profile getDistinctProfile( Profile srcProfile )
    {
        final String[] propSetNames =
        {
            LinePropertySetAxes.class.getSimpleName(),
            LinePropertySetGridLines.class.getSimpleName(),
            LinePropertySetTicMajor.class.getSimpleName(),
            LinePropertySetTicMinor.class.getSimpleName(),
        };
        
        Profile destProfile = new Profile();
        destProfile.setGridUnit( srcProfile.getGridUnit() + 1 );
        getDistinctProperties( destProfile, srcProfile.getMainWindow() );
        
        Stream.of( propSetNames )
            .map( srcProfile::getLinePropertySet )
            .forEach( set -> getDistinctProperties( destProfile, set ) );
        return destProfile;
    }

    /**
     * Assigns unique values to
     * the properties of a given GraphPropertySet
     * that are guaranteed to be distinct
     * from the original values.
     * 
     * @param src   the given GraphPropertySet
     */
    private static void 
    getDistinctProperties( Profile destProfile, GraphPropertySet src )
    {
        GraphPropertySet    dest    =   destProfile.getMainWindow();
        
        Color   newBGColor  = getDistinctColor( src.getBGColor() );
        Color   newFGColor  = getDistinctColor( src.getFGColor() );
        String  newName     = getDistinctFontName( src.getFontName() );
        String  newStyle    = getDistinctFontStyle( src.getFontStyle() );
        float   newSize     = (int)src.getFontSize() + 10;
        boolean newDraw     = !src.isFontDraw();
        
        dest.setBGColor( newBGColor );
        dest.setFGColor( newFGColor );
        dest.setFontName( newName );
        dest.setFontSize( newSize );
        dest.setFontStyle( newStyle );
        dest.setFontDraw( newDraw );
    }
    
    
    /**
     * Assigns to a given Profile
     * property values that are distinct from
     * a given LinePropertySet.
     * 
     * @param destProfile   the given Profile
     * @param src           the given LinePropertySet
     */
    private static void
    getDistinctProperties( Profile destProfile, LinePropertySet src )
    {
        String          setName = src.getClass().getSimpleName();
        LinePropertySet dest    = destProfile.getLinePropertySet( setName );
        Color   newColor    = getDistinctColor( src.getColor() );
        boolean newDraw     = !src.getDraw();
        float   newLength   = src.getLength() + 10;
        float   newSpacing  = src.getSpacing() + 10;
        float   newStroke   = src.getStroke() + 10;
        
        dest.setColor( newColor );
        dest.setDraw( newDraw );
        dest.setLength( newLength );
        dest.setSpacing( newSpacing );
        dest.setStroke( newStroke );
    }
    
    /**
     * From a given font style value,
     * derive a new value
     * that is distinct from the given value.
     * 
     * @param styleIn   the given font style value
     * 
     * @return  a font style value distinct from styleIn
     */
    private static String getDistinctFontStyle( int styleIn )
    {
        String  styleOut    = styleIn == Font.PLAIN ? "ITALIC" : "PLAIN";
        return styleOut;
    }
    
    /**
     * From a given color,
     * derive a new color
     * that is distinct from the given color.
     * 
     * @param colorIn   the given color
     * 
     * @return  a Color distinct from colorIn
     */
    private static Color getDistinctColor( Color colorIn )
    {
        int     iColorIn    = colorIn.getRGB() & 0xFFFFFF;
        int     iColorOut   = ~iColorIn;
        Color   colorOut    = new Color( iColorOut );
        return colorOut;
    }
    
    /**
     * From a font name,
     * derive a new font name
     * that is distinct from the given font name.
     * 
     * @param nameIn   the given color
     * 
     * @return  a Color distinct from colorIn
     */
    private static String getDistinctFontName( String nameIn )
    {
        String  nameOut = nameIn.equals( Font.MONOSPACED ) ? 
            Font.DIALOG : Font.MONOSPACED;
        return nameOut;
    }

}
