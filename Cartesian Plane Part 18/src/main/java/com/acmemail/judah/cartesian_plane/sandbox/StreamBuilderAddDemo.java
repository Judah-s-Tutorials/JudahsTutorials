package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.stream.Stream;

/**
 * Simple application to build a Steam dynamically
 * with Stream.builder.
 * Strings are added to the builder sequentially
 * by method chaining using the add method.
 * 
 * @author Jack Straub
 * 
 * StreamBuilderAddDemo
 */
public class StreamBuilderAddDemo
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main( String[] args )
    {
        Stream.Builder<String>  bldr    = Stream.<String>builder()
            .add( "Alabama" )
            .add( "Alaska" )
            .add( "Arizona" )
            .add( "Arkansas" )
            .add( "California" )
            .add( "Colorado" )
            .add( "Connecticut" )
            .add( "Delaware" )
            .add( "Florida" )
            .add( "Georgia" )
            .add( "Hawaii" )
            .add( "Idaho" )
            .add( "Illinois" )
            .add( "Indiana" )
            .add( "Iowa" )
            .add( "Kansas" )
            .add( "Kentucky" )
            .add( "Louisiana" )
            .add( "Maine" )
            .add( "Maryland" )
            .add( "Massachusetts" )
            .add( "Michigan" )
            .add( "Minnesota" )
            .add( "Mississippi" )
            .add( "Missouri" )
            .add( "Montana" )
            .add( "Nebraska" )
            .add( "Nevada" )
            .add( "NewHampshire" )
            .add( "NewJersey" )
            .add( "NewMexico" )
            .add( "NewYork" )
            .add( "NorthCarolina" )
            .add( "NorthDakota" )
            .add( "Ohio" )
            .add( "Oklahoma" )
            .add( "Oregon" )
            .add( "Pennsylvania" )
            .add( "RhodeIsland" )
            .add( "SouthCarolina" )
            .add( "SouthDakota" )
            .add( "Tennessee" )
            .add( "Texas" )
            .add( "Utah" )
            .add( "Vermont" )
            .add( "Virginia" )
            .add( "Washington" )
            .add( "WestVirginia" )
            .add( "Wisconsin" )
            .add( "Wyoming" );
        Stream<String>  stream  = bldr.build();
        stream.forEach( System.out::println );
    }
}
