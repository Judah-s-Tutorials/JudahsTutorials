package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.Font;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class GraphPropertySetTest
{
    private final GraphPropertySet              origValues;
    private final GraphPropertySet              newValues;
    private final Supplier<GraphPropertySet>    setSupplier;
    
    private GraphPropertySet    testSet;
    
    public GraphPropertySetTest(
        Supplier<GraphPropertySet> setSupplier
    )
    {
        origValues = setSupplier.get();
        newValues = setSupplier.get();
        this.setSupplier = setSupplier;
        setOrigValues();
        calcNewValues();
    }
    
    @BeforeEach
    void beforeEach() throws Exception
    {
        origValues.apply();
        testSet = setSupplier.get();
        assertSetsEqual( origValues, testSet );

    }

    @Test
    void testReset()
    {
        copy( testSet, newValues );
        assertSetsEqual( newValues, testSet );
        testSet.reset();
        assertSetsEqual( origValues, testSet );
    }

    @Test
    void testApply()
    {
        newValues.apply();
        GraphPropertySet    testSet = setSupplier.get();
        assertSetsEqual( newValues, testSet );
    }

    @Test
    void testGetWidth()
    {
        assertEquals( origValues.getWidth(), testSet.getWidth() );
        testSet.setWidth( newValues.getWidth() );
        assertEquals( newValues.getWidth(), testSet.getWidth() );
    }

    @Test
    void testGetBGColor()
    {
        assertEquals( origValues.getBGColor(), testSet.getBGColor() );
        testSet.setBGColor( newValues.getBGColor() );
        assertEquals( newValues.getBGColor(), testSet.getBGColor() );
    }

    @Test
    void testGetFGColor()
    {
        assertEquals( origValues.getFGColor(), testSet.getFGColor() );
        testSet.setFGColor( newValues.getFGColor() );
        assertEquals( newValues.getFGColor(), testSet.getFGColor() );
    }

    @Test
    void testGetFontName()
    {
        assertEquals( origValues.getFontName(), testSet.getFontName() );
        testSet.setFontName( newValues.getFontName() );
        assertEquals( newValues.getFontName(), testSet.getFontName() );
    }

    @Test
    void testGetFontSize()
    {
        assertEquals( origValues.getFontSize(), testSet.getFontSize() );
        testSet.setFontSize( newValues.getFontSize() );
        assertEquals( newValues.getFontSize(), testSet.getFontSize() );
    }

    @Test
    void testIsFontDraw()
    {
        assertEquals( origValues.isFontDraw(), testSet.isFontDraw() );
        testSet.setFontDraw( newValues.isFontDraw() );
        assertEquals( newValues.isFontDraw(), testSet.isFontDraw() );
    }

    @Test
    void testIsItalic()
    {
        assertEquals( origValues.isItalic(), testSet.isItalic() );
        testSet.setItalic( newValues.isItalic() );
        assertEquals( newValues.isItalic(), testSet.isItalic() );
    }

    @Test
    void testIsBold()
    {
        assertEquals( origValues.isBold(), testSet.isBold() );
        testSet.setBold( newValues.isBold() );
        assertEquals( newValues.isBold(), testSet.isBold() );
    }
    
    private static void 
    copy( GraphPropertySet dest, GraphPropertySet source )
    {
        dest.setFontName( source.getFontName() );
        dest.setBold( source.isBold() );
        dest.setItalic( source.isItalic() );
        dest.setFontSize( source.getFontSize() );
        dest.setFGColor( source.getFGColor() );
        dest.setFontDraw( source.isFontDraw() );
        dest.setWidth( source.getWidth() );
        dest.setBGColor( source.getBGColor() );
    }
    
    private static void 
    assertSetsEqual( GraphPropertySet expSet, GraphPropertySet actSet )
    {
        assertEquals( expSet.getFontName(), actSet.getFontName() );
        assertEquals( expSet.isBold(), actSet.isBold() );
        assertEquals( expSet.isFontDraw(), actSet.isFontDraw() );
        assertEquals( expSet.getFontSize(), actSet.getFontSize() );
        assertEquals( expSet.getFGColor(), actSet.getFGColor() );
        assertEquals( expSet.isFontDraw(), actSet.isFontDraw() );
        assertEquals( expSet.getWidth(), actSet.getWidth() );
        assertEquals( expSet.getBGColor(), actSet.getBGColor() );
    }

    private void setOrigValues()
    {
        origValues.setFontName( Font.SANS_SERIF );
        origValues.setBold( true );
        origValues.setItalic( false );
        origValues.setFontSize( 10 );
        origValues.setFGColor( Color.BLUE );
        origValues.setFontDraw( true );
        origValues.setWidth( 1000 );
        origValues.setBGColor( Color.RED );
    }

    private void calcNewValues()
    {
        int     iOrigBGColor    = origValues.getBGColor().getRGB();
        int     iOrigFGColor    = origValues.getFGColor().getRGB();
        Color   newBGColor      = new Color( iOrigBGColor + 128 );
        Color   newFGColor      = new Color( iOrigFGColor + 128 );
        String  newFontName     = 
            origValues.getFontName().equals( Font.DIALOG ) ?
                Font.MONOSPACED : Font.DIALOG;

        newValues.setFontName( newFontName );
        newValues.setBold( !origValues.isBold() );
        newValues.setItalic( !origValues.isItalic() );
        newValues.setFontSize( origValues.getFontSize() + 100 );
        newValues.setFGColor( newFGColor );
        newValues.setFontDraw( !origValues.isFontDraw() );
        newValues.setWidth( origValues.getWidth() + 100);;
        newValues.setBGColor( newBGColor );
    }
}
