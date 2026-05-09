package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This interface describes the facilities necessary
 * to manage an <em>equation</em>.
 * An <em>equation</em> is a set of resources
 * that define a simple function (<code>y = f(x)</code>),
 * a parametric equation (<code>(x,y) = f(t)</code>),
 * or a polar equation (<code>r = acos(n&Theta;)</code>, 
 * <code>&Theta; = atan( x/y ))</code>.
 * These resources include:
 * <ul>
 *      <li>
 *          Expressions for the calculation 
 *          of y values in a simple function,
 *          (x,y) values in a parametric equation,
 *          and radius values in a polar equation;
 *      </li>
 *      <li>The declaration of variables used in the expression(s); and</li>
 *      <li>A range for producing a plot.</li>
 * </ul>
 * <p>
 * For archival purposes,
 * an equation can optionally have a name.
 * </p>
 * <p>
 * Any element of the equation
 * can be validated before trying to plot it.
 * Validation facilities include
 * {@linkplain #validateRange()},
 * {@linkplain #validateValue(String)},
 * {@linkplain #validateName(String)}.
 * If any of these methods fail,
 * an unsuccessful result
 * with one or more error messages is returned.
 * </p>
 * <p>
 * The {@linkplain #evaluate(String)} method,
 * which attempts to derive a value from an expression,
 * returns a non-empty Optional if the expression
 * evaluates successfully,
 * an empty Optional if evaluation fails.
 * </p>
 * <p>
 * Methods that establish expressions,
 * {@linkplain #setYExpression(String)},
 * {@linkplain #setXExpression(String)},
 * {@linkplain #setRExpression(String)},
 * {@linkplain #setTExpression(String)},
 * validate the target expression 
 * before setting it.
 * If the expression is invalid
 * the currently set expression is not overwritten,
 * and an unsuccessful Result
 * with one or more error messages is returned.
 * </p>
 * <p>
 * Methods that generate streams from equations, 
 * {@linkplain #yPlot()},
 * {@linkplain #xyPlot()},
 * {@linkplain #rPlot()},
 * {@linkplain #tPlot()},
 * expect equations to be pre-validated,
 * and throw a ValidationException
 * if an invalid component of the equation
 * is detected.
 * </p>
 *
 * @author Jack Straub
 */
public interface Equation
{
    /** 
     * A map of all Command enum constants that have default
     * string values. Includes all expressions,
     * such as {@code XEQUALS 1},
     * and special names,
     * such as {@code PARAM t}, the name of the parameter variable
     * for use in parametric equations.
     */
    Map<Command,String> DEF_STRINGS = Map.of( 
        Command.XEQUALS, "1",
        Command.YEQUALS, "1",
        Command.REQUALS, "1",
        Command.TEQUALS, "1",
        Command.PARAM, "t",
        Command.RADIUS, "r",
        Command.THETA, "t" 
    );
    
    /**  
     * A map of all available intrinsic variables and their default
     * values. An Equation implementation may have more intrinsic
     * variables, but they must support these.
     */
    Map<String,Double>  INTRINSIC_VARIABLES = Map.of(
        "x", 0d,
        "y", 0d,
        "a", 0d,
        "b", 0d,
        "c", 0d,
        "r", 0d,
        "t", 0d
    );
    
    /**  
     * A map of all names and their default. 
     * "Special names" are the names of variables
     * used in parametric and polar equations.
     * An Equation implementation may have more special names,
     * but they must support these.
     */
    Map<Command,String>  SPECIAL_NAMES   = Map.of(
        Command.PARAM, "t",
        Command.RADIUS, "r",
        Command.THETA, "t" 
    );
    
    /**
     * Sets the name of this equation.
     * 
     * @param name  the name of this equation;
     *              must be non-null
     */
    void setName( String name );
    
    /**
     * Gets the name of this equation
     * 
     * @return  the name of this equation
     */
    String getName();
    
    /**
     * Returns a newly initialized Equation.
     * 
     * @return  a newly initialized Equation
     */
    Equation newEquation();
    
    /**
     * Sets the value of a variable to a given value.
     * 
     * @param name  the name of the variable; must be non-null
     * @param val   the given value
     * 
     * @throws NullPointerException if name is null
     */
    void setVar( String name, double val );

    /**
     * Removes from the set of variables
     * the variable with the given name.
     * If the name is not found
     * the operation is ignored.
     * 
     * @param name  the given name; must be non-null
     * 
     * @throws NullPointerException if name is null
     */
    void removeVar( String name );

    /**
     * Gets an Optional object
     * containing the value of the variable
     * with the given name.
     * If the name is not found
     * an empty Optional is returned.
     * 
     * @param name  given name; must be non-null
     * 
     * @return  
     *      an Optional object
     *      containing the value of the variable
     *      with the given name
     * 
     * @throws NullPointerException if name is null
     */
    Optional<Double> getVar( String name );

    /**
     * Returns an unmodifiable map
     * describing all declared variables 
     * and their values.
     * 
     * @return an unmodifiable map describing all declared variables
     */
    Map<String,Double> getVars();

    /**
     * Parses the given expression used to derive
     * the x-coordinate of a point.
     * If a parsing error occurs,
     * a Result object is returned
     * containing a description of the error,
     * and a <em>success</em> value of false.
     * Otherwise the returned Result object
     * will have a <em>success</em> value of true.
     * 
     * @param exprStr   the given expression, must be non-null
     * 
     * @return  the status of the operation
     * 
     * @throws  NullPointerException if exprStr is null
     */
    Result setXExpression( String exprStr );

    /**
     * Parses the given expression used to derive
     * the y-coordinate of a point.
     * If a parsing error occurs,
     * a Result object is returned
     * containing a description of the error,
     * and a <em>success</em> value of false.
     * Otherwise the returned Result object
     * will have a <em>success</em> value of true.
     * 
     * @param exprStr   the given expression, must be non-null
     * 
     * @return  the status of the operation
     * 
     * @throws  NullPointerException if exprStr is null
     */
    Result setYExpression( String exprStr );

    /**
     * Parses the given expression used to derive
     * the theta-coordinate of a point 
     * in polar coordinates.
     * If a parsing error occurs,
     * a Result object is returned
     * containing a description of the error,
     * and a <em>success</em> value of false.
     * Otherwise the returned Result object
     * will have a <em>success</em> value of true.
     * 
     * @param exprStr   the given expression, must be non-null
     * 
     * @return  the status of the operation
     * 
     * @throws  NullPointerException if exprStr is null
     */
    Result setTExpression( String exprStr );

    /**
     * Parses the given expression used to derive
     * the radius-coordinate of a point 
     * in polar coordinates.
     * If a parsing error occurs,
     * a Result object is returned
     * containing a description of the error,
     * and a <em>success</em> value of false.
     * Otherwise the returned Result object
     * will have a <em>success</em> value of true.
     * 
     * @param exprStr   the given expression, must be non-null
     * 
     * @return  the status of the operation
     * 
     * @throws  NullPointerException if exprStr is null
     */
    Result setRExpression( String exprStr );

    /**
     * Gets the currently set x-expression.
     * 
     * @return  the currently set x-expression
     */
    String getXExpression();

    /**
     * Gets the currently set y-expression.
     * 
     * @return  the currently set y-expression
     */
    String getYExpression();

    /**
     * Gets the currently set t-expression.
     * 
     * @return  the currently set t-expression
     */
    String getTExpression();

    /**
     * Gets the currently set r-expression.
     * 
     * @return  the currently set r-expression
     */
    String getRExpression();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation of the form <code>y=f(x)</code>.
     * 
     * @return a stream of (x,y) coordinates derived from an equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    Stream<Point2D> yPlot();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from a parametric equation.
     * 
     * @return 
     *      a stream of (x,y) coordinates derived 
     *      from a parametric equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    Stream<Point2D> xyPlot();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation
     * expressed in polar coordinates,
     * <code>r = f(t)</code>.
     * <em>Theta</em> is used
     * to traverse the iteration range.
     * 
     * @return a stream of (x,y) coordinates derived from an equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    Stream<Point2D> rPlot();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation
     * expressed in polar coordinates,
     * <code>t = f(r)</code>.
     * <em>Radius</em> is used
     * to traverse the iteration range.
     * 
     * @return the (x,y) coordinates derived from an equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    Stream<Point2D> tPlot();

    /**
     * Gets the name of the parameter
     * in a parametric equation.
     * 
     * @return the name of the parameter
     */
    String getParamName();

    /**
     * Sets the name of the parameter
     * in a parametric equation.
     * 
     * @param param the name of the parameter
     */
    void setParamName( String param );

    /**
     * Gets the name of the radius variable
     * used in a polar equation.
     * 
     * @return the name of the radius
     */
    String getRadiusName();

    /**
     * Sets the name of the radius variable
     * used in a polar equation.
     * 
     * @param radius the name of the radius
     */
    void setRadiusName( String radius );

    /**
     * Gets the name of the angle variable
     * used in a polar equation.
     * 
     * @return the name of the angle variable
     */
    String getThetaName();

    /**
     * Sets the name of the angle variable
     * in a polar equation.
     * 
     * @param theta the name of the angle variable
     */
    void setThetaName( String theta );

    /**
     * Establishes the iteration range for this Equation.
     * Validation of the range properties does not take place
     * at the time this method is called.
     * 
     * @param start the start of the iteration range
     * @param end   the end of the iteration range
     * @param step  the increment to use when traversing the iteration range
     * 
     * @see #validateRange()
     */
    void setRange( double start, double end, double step );

    /**
     * Sets the start of the iteration range.
     * Validation of the range properties does not take place
     * at the time this method is called.
     * 
     * @param rangeStart   iteration range start
     * 
     * @see #validateRange()
     */
    void setRangeStart( double rangeStart );

    /**
     * Returns the start of the iteration range.
     * 
     * @return the start of the iteration range
     */
    double getRangeStart();

    /**
     * Sets the end of the iteration range.
     * Validation of the range properties does not take place
     * at the time this method is called.
     * 
     * @param rangeEnd  iteration range end
     * 
     * @see #validateRange()
     */
    void setRangeEnd( double rangeEnd );

    /**
     * Returns the end of the iteration range.
     * 
     * @return the end of the iteration range
     */
    double getRangeEnd();

    /**
     * Sets the increment used
     * to iterate over the encapsulated range.
     * Validation of the range properties does not take place
     * at the time this method is called.
     * 
     * @param rangeStep   iteration range increment
     * 
     * @see #validateRange()
     */
    void setRangeStep( double rangeStep );

    /**
     * Returns the increment used
     * to iterate over the encapsulated range.
     * 
     * @return the increment used to iterate over the encapsulated range
     */
    double getRangeStep();
    
    /**
     * Validate the range properties.
     * If the range is valid
     * a Result object is returned
     * with a success status of true.
     * If it's invalid,
     * the Result object will contain an error message,
     * and will have a success status of false.
     * <ol>
     * <li>Step may never be 0.</li>
     * <li>If step is positive, start must be less than or equal to end.</li>
     * <li>If step is negative, start must be greater than end.</li>
     * </ol>
     * 
     * @return	
     * 		successful Result if the range is valid,
     * 		otherwise an unsuccessful Result containing an error message
     */
    Result validateRange();
    
    /**
     * Determines if a given string
     * is a valid variable name.
     * A valid variable name is one 
     * that is non-null and non-empty,
     * begins with an underscore or alphabetic character,
     * and whose remaining characters
     * may be underscore or alphanumeric.
     * Returns a successful result if valid,
     * otherwise an unsuccessful result and error message.
     * 
     * @param name  the given string
     * 
     * @return  successful result if valid,
     *          otherwise an unsuccessful result
     *          
     * @see #validateValue(String)
     */
    Result validateName( String name );

    
    /**
     * Determines if a given string
     * is a valid double value.
     * Null and empty strings are considered invalid.
     * 
     * @param valStr  the given string
     * 
     * @return  successful result if valid,
     *          otherwise an unsuccessful result
     * 
     * @see #evaluate(String)
     * @see #validateName(String)
     */
    Result validateValue( String valStr );
    
    /**
     * Parses and evaluates an expression in the context
     * of the current equation.
     * Declared variables and functions are recognized;
     * use of undeclared variables or functions
     * will result in evaluation failure.
     * Null input will result in a NullPointerException.
     * If <em>a</em>, <em>b</em> and <em>theta</em>
     * are declared variables,
     * the following are all considered valid expressions.
     * <ul>
     *     <li>3</li>
     *     <li>.5^3</li>
     *     <li>e^2</li>
     *     <li>3 * cos( pi / 7 )</li>
     *     <li>3 * a * b</li>
     *     <li>a * sin(theta)</li>
     * </ul>
     * <p>
     * If evaluation of the expression is successful
     * an Optional object containing the result
     * is returned.
     * If evaluation is not successful
     * an empty Optional is returned.
     * </p>
     * 
     * @param exprStr   the expression to evaluate; must be non-null
     * 
     * @return  
     *      an Optional containing the result of the evaluation,
     *      or an empty Optional if an error occurred
     *      
     *  @throws NullPointerException if expStr is null
     */
    Optional<Double> evaluate( String exprStr );
}