package com.acmemail.judah.cartesian_plane.sandbox;

public class SplitDemo1
{

    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        "the walrus and the carpenter".split( "\\s", 3 );
        split( "the walrus and the carpenter", "\\s+", 3 );
//        split( "Manny Moe Jack", " ", -1 );
//        split( "Manny    Moe    Jack", " ", -1 );
//        split( "Manny\tMoe\tJack", "\\s", -1 );
//        split( "Manny\t\tMoe\t\tJack", "\\s", -1 );
//        split( "Manny\t\tMoe\t\tJack", "\\s+", -1 );
//        split( "Manny\t\tMoe\t\tJack", "\\s+", 2 );
//        split( "Manny\t\tMoe\t\tJack", "\\s+", 1 );
//        split( "Manny\t\tMoe\t\tJack", "\\s+", 0 );
//        split( "MannyMoeJack", "\\s+", -1 );
    }

    private static void split( String toSplit, String regex, int num )
    {
        String[]    input       = getStrings( toSplit );
        String[]    splitter    = getStrings( regex );
        String      line1       =
            "||" + input[0] + "||" + "   ||" + splitter[0] + "||";
        String      line2       =
            "||" + input[1] + "||" + "   ||" + splitter[1] + "||";
        System.out.println( "*****" );
        System.out.println( line1 );
        System.out.println( line2 );
        
        String[]    result      = null;
        if ( num < 0 )
        {
            System.out.println( "toSplit.split(\"" + regex + "\");" );
            result = toSplit.split( regex );
        }
        else
        {
            System.out.println( "toSplit.split(" + regex + ", " + num + ");" );
            result = toSplit.split( regex, num );
        }
        System.out.println( "Array length: " + result.length );
        for ( String str : result )
        {
            System.out.print( "[>>" + str + "<<]   ");
        }
        System.out.println();

    }
    
    private static String[] getStrings( String toParse )
    {
        StringBuilder   pointers    = new StringBuilder();
        StringBuilder   input       = new StringBuilder();
        for ( char ccc : toParse.toCharArray() )
        {
            if ( ccc == ' ' )
            { 
                pointers.append( 'v' );
                input.append( ' ' );
            }
            else if ( ccc == '\t' )
            { 
                pointers.append( "v " );
                input.append( "\\t" );
            }
            else 
            { 
                pointers.append( " " );
                input.append( ccc );
            }
        }
        String[]    result  = { pointers.toString(), input.toString() };
        return result;
    }
}
