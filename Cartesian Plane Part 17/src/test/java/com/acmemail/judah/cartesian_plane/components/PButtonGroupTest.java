package com.acmemail.judah.cartesian_plane.components;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

class PButtonGroupTest
{
    private static final String prop1   = "Property 1";
    private static final String prop2   = "Property 2";
    private static final String prop3   = "Property 3";
    
    private PRadioButton<String>    button1;
    private PRadioButton<String>    button2;
    private PRadioButton<String>    button3;
    private PButtonGroup<String>    buttonGroup;
    private Object                  selectedButton;
    
    @BeforeEach
    void setUp() throws Exception
    {
        button1 = new PRadioButton<>( prop1 );
        button2 = new PRadioButton<>( prop2 );
        button3 = new PRadioButton<>( prop3 );
        
        button1.addActionListener( this::actionPerformed );
        button2.addActionListener( this::actionPerformed );
        button3.addActionListener( this::actionPerformed );
        
        buttonGroup = new PButtonGroup<>();
        buttonGroup.add( button1 );
        buttonGroup.add( button2 );
        buttonGroup.add( button3 );
    }

    @Test
    public void testAddPRadioButtonOfT()
    {
        GUIUtils.schedEDTAndWait( this::testAddPRadioButtonOfTEDT );
    }

    private void testAddPRadioButtonOfTEDT()
    {
        String                  propNew     = "Property new";
        PRadioButton<String>    buttonNew   = new PRadioButton<>( propNew );
        buttonGroup.add( buttonNew );
        List<PRadioButton<String>>  list    = buttonGroup.getButtons();
        assertEquals( 4, list.size() );
        assertEquals( 4, buttonGroup.getButtonCount() );
        Stream.of( button1, button2, button3, buttonNew )
            .forEach( b -> assertTrue( list.contains( b ) ) );
    }

    @Test
    public void testGetSelectedButton()
    {
        GUIUtils.schedEDTAndWait( this::testGetSelectedButtonEDT );
    }

    private void testGetSelectedButtonEDT()
    {
        assertFalse( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertNull( buttonGroup.getSelectedButton() );
        
        button1.doClick();
        assertTrue( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( button1, buttonGroup.getSelectedButton() );
        assertEquals( button1, selectedButton );
        
        button2.doClick();
        assertFalse( button1.isSelected() );
        assertTrue( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( button2, buttonGroup.getSelectedButton() );
        assertEquals( button2, selectedButton );
        
        button3.doClick();
        assertFalse( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertTrue( button3.isSelected() );
        assertEquals( button3, buttonGroup.getSelectedButton() );
        assertEquals( button3, selectedButton );
    }

    @Test
    void testGetSelectedProperty()
    {
        GUIUtils.schedEDTAndWait( this::testGetSelectedPropertytEDT );
    }

    void testGetSelectedPropertytEDT()
    {
        assertFalse( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertNull( buttonGroup.getSelectedProperty() );
        
        button1.doClick();
        assertTrue( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( prop1, buttonGroup.getSelectedProperty() );
        assertEquals( button1, selectedButton );
        
        button2.doClick();
        assertFalse( button1.isSelected() );
        assertTrue( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( prop2, buttonGroup.getSelectedProperty() );
        assertEquals( button2, selectedButton );
        
        button3.doClick();
        assertFalse( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertTrue( button3.isSelected() );
        assertEquals( prop3, buttonGroup.getSelectedProperty() );
        assertEquals( button3, selectedButton );
    }

    @Test
    void testSelectButtonT()
    {
        GUIUtils.schedEDTAndWait( this::testSelectButtonTEDT );
    }

    void testSelectButtonTEDT()
    {
        assertFalse( buttonGroup.selectButton( "Not a property" ) );
        assertFalse( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertNull( buttonGroup.getSelectedProperty() );
        
        assertTrue( buttonGroup.selectButton( prop1 ) );
        assertTrue( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( prop1, buttonGroup.getSelectedProperty() );
        assertEquals( button1, selectedButton );
        
        assertTrue( buttonGroup.selectButton( prop2 ) );
        assertFalse( button1.isSelected() );
        assertTrue( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( prop2, buttonGroup.getSelectedProperty() );
        assertEquals( button2, selectedButton );
        
        assertTrue( buttonGroup.selectButton( prop3 ) );
        assertFalse( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertTrue( button3.isSelected() );
        assertEquals( prop3, buttonGroup.getSelectedProperty() );
        assertEquals( button3, selectedButton );
    }

    @Test
    void testSelectIndex()
    {
        GUIUtils.schedEDTAndWait( this::testSelectIndexEDT );
    }

    void testSelectIndexEDT()
    {
        buttonGroup.selectIndex( 0 );
        assertTrue( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( button1, selectedButton );
        
        buttonGroup.selectIndex( 1 );
        assertFalse( button1.isSelected() );
        assertTrue( button2.isSelected() );
        assertFalse( button3.isSelected() );
        assertEquals( button2, selectedButton );
        
        buttonGroup.selectIndex( 2 );
        assertFalse( button1.isSelected() );
        assertFalse( button2.isSelected() );
        assertTrue( button3.isSelected() );
        assertEquals( button3, selectedButton );
    }
    
    @Test
    public void selectButtonTNeg()
    {
        // Not running this test on the EDT. It's not necessary,
        // and it gets complicated; when the desired exception
        // is thrown it triggers a TargetInvocationException
        // on the EDT.
        Class<IllegalArgumentException> clazz   =
            IllegalArgumentException.class;
        assertThrows( clazz, () -> buttonGroup.selectButton( null ) );
    }

    private void actionPerformed( ActionEvent evt )
    {
        selectedButton = evt.getSource();
    }
}
