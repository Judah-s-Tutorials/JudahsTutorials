package util;

/**
 * This class collects the parameters
 * that describe the <em>base case</em> line generation example
 * in a single place.
 * 
 * @author Jack Straub
 */
public class BaseCaseParameters
{
    /** Width of the rectangle bounding the base case grid. */
    public static final float   BASE_GRID_WIDTH     = 511;
    /** Height of the rectangle bounding the base case grid. */
    public static final float   BASE_GRID_HEIGHT    = 211;
    /** Grid unit of the base case grid. */
    public static final float   BASE_GRID_UNIT      = 50;
    /** LPU of the base case grid. */
    public static final float   BASE_LINES_PER_UNIT = 2;
    
    /**
     * Makes the default constructor private
     * in order to prevent instantiation.
     */
    private BaseCaseParameters()
    {
    }
}
