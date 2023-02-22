package lambdas;

public class ShowDog
{
    private final String    name;
    private final int       age;
    private final String    breed;
    private final int       ownerID;

    public ShowDog(String name, int age, String breed, int ownerID)
    {
        super();
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.ownerID = ownerID;
    }

    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "Name: " ).append( name ).append( "," )
            .append( "age: " ).append( age ).append( "," )
            .append( "breed:" ).append( breed ).append( "," )
            .append( "owner: " ).append( ownerID );
        return bldr.toString();
    }
    
    public static int sortByName( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.name.compareTo( dog2.name );
    }
    
    public static int sortByBreed( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.breed.compareTo( dog2.breed );
    }
    
    public static int sortByAge( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.age - dog2.age;
    }
    
    public static int sortByOwnerID( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.ownerID - dog2.ownerID;
    }
}
