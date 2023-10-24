package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class EnumToListDemo
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ButtonGroup group   = new ButtonGroup();
        Stream.of( "A", "B", "C", "D" )
            .map( JRadioButton::new )
            .forEach( group::add );
        List<AbstractButton>    list    = Collections.list( group.getElements() );
        list.forEach( b -> System.out.println( b.getText() ) );
    }
}
