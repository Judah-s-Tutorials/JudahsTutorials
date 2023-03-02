package com.acmemail.judah.anonymous_classes.lambdas;

/**
 * This class encapsulates
 * the properties of a show dog.
 * It is used purely
 * for demonstrative purposes.
 * 
 * @author Jack Straub
 */
public class ShowDog
{
    /** The ShowDog's name. */
    private String  name;
    /** The ShowDog's age. */
    private int     age;
    /** The ShowDog's breed. */
    private String  breed;
    /** The ShowDog's owner ID. */
    private int     ownerID;
    
    /**
     * Default constructor.
     */
    public ShowDog()
    {
    }
    
    /**
     * Constructor.
     * Sets the properties of this ShowDog.
     * 
     * @param name      the name of this ShowDog
     * @param age       the age of this ShowDog
     * @param breed     the breed of this ShowDog
     * @param ownerID   the owner ID of this ShowDog
     */
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

    /**
     * Gets the name of this ShowDog.
     * 
     * @return the name of this ShowDog
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this ShowDog
     * and returns <em>this.</em>
     * 
     * @param name the name to set
     * 
     * @return this
     */
    public ShowDog putName(String name)
    {
        this.name = name;
        return this;
    }

    /**
     * Gets the age of this ShowDog.
     * 
     * @return the age of this ShowDog
     */
    public int getAge()
    {
        return age;
    }

    /**
     * Sets the age of this ShowDog
     * and returns <em>this.</em>
     * 
     * @param age the age to set
     * 
     * @return this
     */
    public ShowDog putAge(int age)
    {
        this.age = age;
        return this;
    }

    /**
     * Gets the breed of this ShowDog.
     * 
     * @return the breed of this ShowDog
     */
    public String getBreed()
    {
        return breed;
    }

    /**
     * Sets the breed of this ShowDog
     * and returns <em>this.</em>
     * 
     * @param breed the breed to set
     * 
     * @return this
     */
    public ShowDog putBreed(String breed)
    {
        this.breed = breed;
        return this;
    }

    /**
     * Gets the owner ID of this ShowDog.
     * 
     * @return the owner ID of this ShowDog
     */
    public int getOwnerID()
    {
        return ownerID;
    }

    /**
     * Sets the owner ID of this ShowDog
     * and returns <em>this.</em>
     * 
     * @param ownerID the owner ID to set
     * 
     * @return this
     */
    public ShowDog putOwnerID(int ownerID)
    {
        this.ownerID = ownerID;
        return this;
    }
    
    /**
     * This method is suitable for use
     * in a Comparator that sorts
     * a collection of ShowDogs by name.
     * Example:<br>
     * <pre>    list.sort( ShowDog::sortByName )</pre>.
     * 
     * @param dog1  the first ShowDog to compare
     * @param dog2  the second ShowDog to compare
     * 
     * @return 
     *      a negative number if dog1 is less than dog2;
     *      a positive number if dog1 is greater than dog); and
     *      0 if dog1 and dog2 are equal
     */
    public static int sortByName( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.name.compareTo( dog2.name );
    }
    
    /**
     * This method is suitable for use
     * in a Comparator that sorts
     * a collection of ShowDogs by breed.
     * Example:<br>
     * <pre>    list.sort( ShowDog::sortByBreed )</pre>.
     * 
     * @param dog1  the first ShowDog to compare
     * @param dog2  the second ShowDog to compare
     * 
     * @return 
     *      a negative number if dog1 is less than dog2;
     *      a positive number if dog1 is greater than dog); and
     *      0 if dog1 and dog2 are equal
     */
    public static int sortByBreed( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.breed.compareTo( dog2.breed );
    }
    
    /**
     * This method is suitable for use
     * in a Comparator that sorts
     * a collection of ShowDogs by age.
     * Example:<br>
     * <pre>    list.sort( ShowDog::sortByAge )</pre>.
     * 
     * @param dog1  the first ShowDog to compare
     * @param dog2  the second ShowDog to compare
     * 
     * @return 
     *      a negative number if dog1 is less than dog2;
     *      a positive number if dog1 is greater than dog); and
     *      0 if dog1 and dog2 are equal
     */
    public static int sortByAge( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.age - dog2.age;
    }
    
    /**
     * This method is suitable for use
     * in a Comparator that sorts
     * a collection of ShowDogs by owner ID.
     * Example:<br>
     * <pre>    list.sort( ShowDog::sortByOwnerID )</pre>.
     * 
     * @param dog1  the first ShowDog to compare
     * @param dog2  the second ShowDog to compare
     * 
     * @return 
     *      a negative number if dog1 is less than dog2;
     *      a positive number if dog1 is greater than dog); and
     *      0 if dog1 and dog2 are equal
     */
    public static int sortByOwnerID( ShowDog dog1, ShowDog dog2 )
    {
        return dog1.ownerID - dog2.ownerID;
    }
}
