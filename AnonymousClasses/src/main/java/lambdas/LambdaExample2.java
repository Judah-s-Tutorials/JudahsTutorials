package lambdas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class LambdaExample2
{
    private static final Comparator<Integer> evensFirstCmp   =
        (i1, i2) ->
        {
            boolean isEven1 = i1 % 2 == 0;
            boolean isEven2 = i2 % 2 == 0;
            int     rcode   = 0;
            
            if ( isEven1 == isEven2 )
                rcode = i1 - i2;
            else if ( isEven1 )
                rcode = 1;
            else
                rcode = -1;
            return rcode;
        };
        
    private static final Comparator<String> insensitiveCmp1 =
        (s1, s2) -> { return s1.compareToIgnoreCase( s2 ); };
        
    private static final Comparator<String> insensitiveCmp2 =
        (s1, s2) -> s1.compareToIgnoreCase( s2 );
        
    public static void main( String[] args )
    {
        List<String>    randomList  = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 100 ; ++inx )
        {
            int     rInt    = randy.nextInt( 100 );
            String  str     = rInt % 2 == 0 ? "ITEM" : "item";
            String  item    = String.format( "%s: %03d", str, rInt );
            randomList.add( item );
        }
        
//        randomList.sort( insensitiveCmp2 );
        
        randomList.sort( (s1, s2) -> s1.compareToIgnoreCase( s2 ) );
        
        for ( String str : randomList )
            System.out.println( str );
    }
}
