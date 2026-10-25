package com.acmemail.judah.battleship.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.battleship.artwork.text.TextGridParent;
import com.acmemail.judah.battleship.model.Grid2D;

/**
 * This is a simple application
 * that creates a {@link TextGridParent},
 * and prints the table it generates to stdout.
 */
public class TextGridParentDemo
{
    public static void main( String[] args )
    {
        List<String>   names            =
            List.of( "dick", "jane", "sally", "spot", "mom" );
        Grid2D.reset( 11, 13 );
        
        List<Grid2D>    virtualGrids    = new ArrayList<>();
        virtualGrids.add( Grid2D.getHomeGrid() );
        Grid2D.getHomeGrid();
        names.stream()
            .map( Grid2D::new )
            .forEach( g -> System.out.println( g.getName() ) );
        TextGridParent  gridMaker   = new TextGridParent();
        List<String>    gridLines   = gridMaker.exec();
        gridLines.forEach( System.out::println );
    }
}
