package lambdas;

import java.util.ArrayList;
import java.util.List;

public class MethodReferenceExample2
{    
    public static void main(String[] args)
    {
        List<DogShowEntrant>    list    = getList();
        list.sort( (d1, d2) -> DogShowEntrant.sortByAge( d1, d2 ) );
        list.forEach( System.out::println );
        
        list.sort( DogShowEntrant::sortByOwnerID );
        list.forEach( System.out::println );
    }

    private static List<DogShowEntrant> getList()
    {
        List<DogShowEntrant>    list    = new ArrayList<>();
        
        list.add( new DogShowEntrant( "Fido", 3, "Collie", 55555 ) );
        list.add( new DogShowEntrant( "Shep", 2, "Collie", 22222 ) );
        list.add( new DogShowEntrant( "Tipsy", 4, "Poodle", 33333 ) );
        list.add( new DogShowEntrant( "Doodles", 5, "Shepherd", 77777 ) );
        list.add( new DogShowEntrant( "Iggy", 2, "Poodle", 33333 ) );
        
        return list;
    }
}
