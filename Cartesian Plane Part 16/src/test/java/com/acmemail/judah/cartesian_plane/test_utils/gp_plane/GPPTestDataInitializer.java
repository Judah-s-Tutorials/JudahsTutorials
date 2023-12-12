package com.acmemail.judah.cartesian_plane.test_utils.gp_plane;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetBM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetLM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetMW;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetRM;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySetTM;

public class GPPTestDataInitializer
{
    /** List of valid font names for initializing property sets */
    private final List<String>  fontNames   =
        Arrays.asList(
            Font.DIALOG, 
            Font.DIALOG_INPUT, 
            Font.MONOSPACED, 
            Font.SERIF,
            Font.SANS_SERIF
        );
    /** Length of file names list. */
    private int             fontNamesLen    = fontNames.size();
    /** Index into file names list. */
    private int             fontNamesInx    = 0;
    private boolean         isBold          = true;
    private boolean         isItalic        = false;
    private int             fontSize        = 4;
    private int             fgColor         = 100;
    private int             width           = 500;
    private boolean         isFontDraw      = true;
    private int             bgColor         = 1000;
    
    private final List<GraphPropertySet>    origSets    = 
        new ArrayList<>();
    private final List<GraphPropertySet>    newSets     = 
        new ArrayList<>();
    
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
    
    public GraphPropertySet getOrigValues( Class<?> clazz )
    {
        GraphPropertySet    set = 
            origSets.stream()
                .filter( s -> s.getClass() == clazz )
                .findFirst().orElse( null );
        return set;
    }
    
    public GraphPropertySet getNewValues( Class<?> clazz )
    {
        GraphPropertySet    set = 
            newSets.stream()
                .filter( s -> s.getClass() == clazz )
                .findFirst().orElse( null );
        return set;
    }
    
    private void initSet( GraphPropertySet set )
    {
        set.setFontName( fontNames.get( fontNamesInx % fontNamesLen ) );
        fontNamesInx += 2;
        set.setBold( isBold = !isBold );
        set.setItalic( isItalic = !isItalic );
        set.setFontSize( fontSize += 4 );
        set.setFGColor( new Color( fgColor += 100 ) );
        set.setWidth( width += 100 );
        set.setFontDraw( isFontDraw = !isFontDraw );
        set.setBGColor( new Color( bgColor += 100 ) );
        set.apply();
        origSets.add( set );
    }
    
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
        dest.setFontSize( source.getFontSize() + 2 );
        dest.setFGColor( fgColor );
        dest.setWidth( width += 100 );
        dest.setFontDraw( isFontDraw = !isFontDraw );
        dest.setBGColor( bgColor );
        newSets.add( dest );
    }
}
