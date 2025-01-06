package com.acmemail.judah.anonymous_classes.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This application is one of a pair
 * illustrating the difference between
 * the map and flatMap methods
 * found in the Stream class.
 * In this application we will stream a two-dimensonal array
 * and use the map method
 * producing a sequence of one-dimensional arrays
 * which can then be streamed.
 * 
 * @author Jack Straub
 */
public class MapVsFlatmapDemo3
{
    private static final String names[] =
    {
        "fido",
        "spot",
        "sky",
        "miracle",
        "bozo",
        "browie",
        "blackie",
        "foxy",
        "wolf",
        "woofer"
    };
    
    private static final List<String>   listA   = getNameList( 0 );
    private static final List<String>   listB   = getNameList( 3 );
    private static final List<String>   listC   = getNameList( 6 );
    private static final List<String>   listD   = getNameList( 9 );

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {        
        List<String>    finalList   = 
            Stream.of( listA, listB, listC, listD )
            .flatMap( l -> l.stream() )
            .distinct()
            .collect( Collectors.toList() );
        finalList.forEach( System.out::println );
    }
    
    /**
     * Make a list of up to five Strings
     * from the names array,
     * beginning from a given starting index.
     * If the starting index
     * is less than or equal to
     * the length of the names array
     * an empty list is returned.
     * 
     * @param from  the given starting index
     * 
     * @return  the new list of Strings.
     */
    private static List<String> getNameList( int from )
    {
        List<String>    list    = new ArrayList<>();
        int             count   = 5;
        int             limit   = from + count;
        if ( limit >= names.length )
            limit = names.length;
        for ( int inx = from ; inx < limit ;++inx )
            list.add( names[inx] );
        return list;
    }
}
