package com.acmemail.judah.anonymous_classes.lambdas;

public class ShowDog
{
    private String  name;
    private int     age;
    private String  breed;
    private int     ownerID;
    
    public ShowDog()
    {
    }
    
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
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public ShowDog putName(String name)
    {
        this.name = name;
        return this;
    }

    /**
     * @return the age
     */
    public int getAge()
    {
        return age;
    }

    /**
     * @param age the age to set
     */
    public ShowDog putAge(int age)
    {
        this.age = age;
        return this;
    }

    /**
     * @return the breed
     */
    public String getBreed()
    {
        return breed;
    }

    /**
     * @param breed the breed to set
     */
    public ShowDog putBreed(String breed)
    {
        this.breed = breed;
        return this;
    }

    /**
     * @return the ownerID
     */
    public int getOwnerID()
    {
        return ownerID;
    }

    /**
     * @param ownerID the ownerID to set
     */
    public ShowDog putOwnerID(int ownerID)
    {
        this.ownerID = ownerID;
        return this;
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
