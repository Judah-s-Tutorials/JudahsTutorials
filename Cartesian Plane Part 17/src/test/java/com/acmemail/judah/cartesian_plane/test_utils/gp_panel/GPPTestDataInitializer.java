package com.acmemail.judah.cartesian_plane.test_utils.gp_panel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetBM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetLM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetRM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetTM;

/**
 * Initializes the properties
 * associated with the {@linkplain GraphPropertySet}
 * to predictable and
 * (insofar as possible)
 * unique values.
 * Two sets of data are generated
 * and stored:
 * one set,
 * for each subclass,
 * containing the initial values
 * (the "original set")
 * and another set
 * containing properties that are
 * guaranteed different from 
 * the initial values
 * (the "new set").
 * <p>
 * The initial value of the font size property
 * is incremented by two
 * each time it is used;
 * this way the initial font size
 * for each subclass 
 * will be different.
 * When generating a "new" property set
 * from an "original" property set
 * the new size is set
 * to the initial size + 1.
 * In this way
 * all font sizes
 * in all property sets
 * will be different.
 * <p>
 * Regarding issue #??????:
 * you can force all initial font sizes
 * to be a fixed value;
 * see {@linkplain #setFixedFontSize(Double)};
 * 
 * @author Jack Straub
 * 
 * @see #setFixedFontSize(Double)
 */
public class GPPTestDataInitializer
{
    /** List of valid font names for initializing property sets. */
    private final List<String>  fontNames   =
        Arrays.asList(
            Font.DIALOG, 
            Font.DIALOG_INPUT, 
            Font.MONOSPACED, 
            Font.SERIF,
            Font.SANS_SERIF
        );
    
    /**
     * Workaround to bug #?????????:
     * "Automated tests don't work
     * when font size changes."
     * When this value is present
     * it will be used to set all font sizes
     * in the "original" set of values.
     */
    private static OptionalDouble   fixedFontSize   = 
        OptionalDouble.empty();
    
    /** Length of file names list. */
    private int             fontNamesLen    = fontNames.size();
    /** Index into file names list. */
    private int             fontNamesInx    = 0;
    /** Determines the initial value of the "is bold" property. */
    private boolean         isBold          = true;
    /** Determines the initial value of the "is italic" property. */
    private boolean         isItalic        = false;
    /** Determines the initial value of the font size property. */
    private int             fontSize        = 4;
    /** Determines the initial value of the foreground color property. */
    private int             fgColor         = 100;
    /** Determines the initial value of the width property. */
    private int             width           = 500;
    /** Determines the initial value of the "draw" property. */
    private boolean         isFontDraw      = true;
    /** Determines the initial value of the background color property. */
    private int             bgColor         = 1000;
    
    /** Contains copies of initial values for all property sets. */
    private final List<GraphPropertySet>    origSets    = 
        new ArrayList<>();
    /**
     * For each set of initial properties,
     * contains a corresponding set of properties
     * with values that are distinct
     * from the initial properties.
     */
    private final List<GraphPropertySet>    newSets     = 
        new ArrayList<>();
    
    /**
     * Constructor.
     * Calculates the values 
     * of all "original" and "new" propertysets.
     */
    public GPPTestDataInitializer()
    {
        GraphPropertySet    mwOrig  = new GraphPropertySetMW();
        GraphPropertySet    mwNew   = new GraphPropertySetMW();
        initSet( mwOrig );
        newSet( mwNew, mwOrig );
        
        GraphPropertySet    tmOrig  = new GraphPropertySetTM();
        GraphPropertySet    tmNew   = new GraphPropertySetTM();
        initSet( tmOrig );
        newSet( tmNew, tmOrig );
        
        GraphPropertySet    rmOrig  = new GraphPropertySetRM();
        GraphPropertySet    rmNew   = new GraphPropertySetRM();
        initSet( rmOrig );
        newSet( rmNew, rmOrig );
        
        GraphPropertySet    lmOrig  = new GraphPropertySetLM();
        GraphPropertySet    lmNew   = new GraphPropertySetLM();
        initSet( lmOrig );
        newSet( lmNew, lmOrig );
        
        GraphPropertySet    bmOrig  = new GraphPropertySetBM();
        GraphPropertySet    bmNew   = new GraphPropertySetBM();
        initSet( bmOrig );
        newSet( bmNew, bmOrig );
    }
    
    /**
     * Sets the initial font size
     * for all property sets
     * to a fixed value.
     * See issue #??????
     * 
     * @param fontSize  
     *      the fixed value to use for the initial font size property.
     */
    public static void setFixedFontSize( Double fontSize )
    {
        fixedFontSize = fontSize == null ? 
            OptionalDouble.empty() : OptionalDouble.of( fontSize );
    }
    
    /**
     * Returns the set of initial properties
     * for GraphPropertySet subclasses
     * of the given class.
     * The given class could be,
     * for example,
     * GraphPropertySetMW.class or
     * GraphPropertySetTM.class.
     * If the class can't be found
     * null is returned.
     * 
     * @param clazz the given class
     * 
     * @return  
     *      the set of original values for the given class,
     *      or null if the class can't be found.
     */
    public GraphPropertySet getOrigValues( Class<?> clazz )
    {
        GraphPropertySet    set = 
            origSets.stream()
                .filter( s -> s.getClass() == clazz )
                .findFirst().orElse( null );
        return set;
    }
    
    /**
     * Returns the set of new properties
     * for GraphPropertySet subclasses
     * of the given class.
     * The given class could be,
     * for example,
     * GraphPropertySetMW.class or
     * GraphPropertySetTM.class.
     * If the class can't be found
     * null is returned.
     * 
     * @param clazz the given class
     * 
     * @return  
     *      the set of new values for the given class,
     *      or null if the class can't be found.
     */
    public GraphPropertySet getNewValues( Class<?> clazz )
    {
        GraphPropertySet    set = 
            newSets.stream()
                .filter( s -> s.getClass() == clazz )
                .findFirst().orElse( null );
        return set;
    }
    
    /**
     * Sets the initial values
     * for a given property set.
     * As much as possible,
     * the values given to a property
     * will be distinct from the property value
     * given to any other set.
     * After all values are calculated
     * the PropertyManager is updated
     * with the values,
     * and the set is added to
     * the map of "original value sets"
     * {@linkplain #origSets}.
     * 
     * @param set   the given property set.
     */
    private void initSet( GraphPropertySet set )
    {
        set.setFontName( fontNames.get( fontNamesInx % fontNamesLen ) );
        fontNamesInx += 2;
        set.setBold( isBold = !isBold );
        set.setItalic( isItalic = !isItalic );
        set.setFontSize( fontSize += 2 );
        if ( fixedFontSize.isPresent() )
            set.setFontSize( (float)fixedFontSize.getAsDouble() );
        set.setFGColor( new Color( fgColor += 100 ) );
        set.setWidth( width += 100 );
        set.setFontDraw( isFontDraw = !isFontDraw );
        set.setBGColor( new Color( bgColor += 100 ) );
        set.apply();
        origSets.add( set );
    }
    
    /**
     * For two given property sets,
     * "source" and "destination",
     * the destination set
     * is initialized with property values
     * that are distinct
     * from the source set.
     * After initialization is complete
     * the destination set
     * is added to  
     * the map of "new value sets"
     * {@linkplain #newSets}.
     * 
     * @param dest      the given destination property set
     * @param source    the given source property set
     */
    private void newSet( GraphPropertySet dest, GraphPropertySet source )
    {
        int     namesInx    = 
            fontNames.indexOf( source.getFontName() ) + 1;
        namesInx %= fontNamesLen;
        int     iFGColor    = source.getFGColor().getRGB() + 50;
        Color   fgColor     = new Color( iFGColor );
        int     iBGColor    = source.getBGColor().getRGB() + 50;
        Color   bgColor     = new Color( iBGColor );

        dest.setFontName( fontNames.get( namesInx ) );
        dest.setBold( !source.isBold() );
        dest.setItalic( !source.isItalic() );
        dest.setFontSize( source.getFontSize() + 1 );
        dest.setFGColor( fgColor );
        dest.setWidth( width += 100 );
        dest.setFontDraw( isFontDraw = !isFontDraw );
        dest.setBGColor( bgColor );
        newSets.add( dest );
    }
}
