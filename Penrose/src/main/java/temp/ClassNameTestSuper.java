package temp;

public abstract class ClassNameTestSuper
{
    public ClassNameTestSuper()
    {
        String  name    = getClass().getSimpleName();
        System.out.println( name );
    }
}
