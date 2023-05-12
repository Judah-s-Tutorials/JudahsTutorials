package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This interface describes the facilities necessary
 * to manage an <em>equation</em>.
 * An <em>equation</em> is a set of resources
 * that define a simple function (<code>y = f(x)</code>)
 * or a parametric equation (<code>(x,y) = f(t)</code>).
 * These resources include:
 * <ul>
 *      <li>
 *          Expressions for the calculation 
 *          of y values in a simple function,
 *          or the calculation of (x,y) values
 *          an a parametric equation;
 *      </li>
 *      <li>The declaration of variables used in the expression(s); and</li>
 *      <li>A range for producing a plot.</li>
 * </ul>
 * <p>
 * For archival purposes
 * and equation can optionally have a name.
 * </p>
 *
 * @author Jack Straub
 */
public interface Equation
{
    /**
     * Sets the name of this equation.
     * 
     * @param name  the name of this equation
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
     * @param name  the name of the variable
     * @param val   the given value
     */
    void setVar(String name, double val);

    /**
     * Removes from the set of variables
     * the variable with the given name.
     * If the name is not found
     * the operation is ignored.
     * 
     * @param name  the given name
     */
    void removeVar(String name);

    /**
     * Gets an Optional object
     * containing the value of the variable
     * with the given name.
     * If the name is not found
     * an empty Optional is returned.
     * 
     * @param name  the given name
     * 
     * @return  
     *      an Optional object
     *      containing the value of the variable
     *      with the given name
     */
    Optional<Double> getVar(String name);

    /**
     * Returns an unmodifiable map
     * describing all declared variables 
     * and their values.
     * 
     * @return an unmodifiable map describing all declared variables
     */
    Map<String,Double> getVars();

    /**
     * Parses the expression used to derive
     * the x-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise Result.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    Result setXExpression(String exprStr);

    /**
     * Parses the expression used to derive
     * the y-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise Result.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    Result setYExpression(String exprStr);

    /**
     * Parses the expression used to derive
     * the theta-coordinate of a point 
     * in polar coordinates
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise Result.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    Result setTExpression(String exprStr);

    /**
     * Parses the expression used to derive
     * the theta-coordinate of a point 
     * in polar coordinates
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise Result.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    Result setRExpression(String exprStr);

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
     * derived from an equation of the form <em>y=f(x)</em>.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    Stream<Point2D> yPlot();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from a parametric equation.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    Stream<Point2D> xyPlot();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation
     * expressed in polar coordinates,
     * r = f(t).
     * Theta is used
     * to traverse the iteration range.
     * 
     * @return the (x,y) coordinates derived from an equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    Stream<Point2D> rPlot();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation
     * expressed in polar coordinates,
     * t = f(r).
     * Radius is used
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
    String getParam();

    /**
     * Sets the name of the parameter
     * in a parametric equation.
     * 
     * @param param the name of the parameter
     */
    void setParam(String param);

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
    void setRadiusName(String radius);

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
     * @param theta the name of the parameter
     */
    void setThetaName(String theta);

    /**
     * Establishes the iteration range for this Equation.
     * 
     * @param start the start of the iteration range
     * @param end   the end of the iteration range
     * @param step  the increment to use when traversing the iteration range
     */
    void setRange(double start, double end, double step);

    /**
     * Sets the start of the iteration range.
     * 
     * @param rangeStart   iteration range start
     */
    void setRangeStart(double rangeStart);

    /**
     * Returns the start of the iteration range.
     * 
     * @return the start of the iteration range
     */
    double getRangeStart();

    /**
     * Returns the end of the iteration range.
     * 
     * @return the end of the iteration range
     */
    double getRangeEnd();

    /**
     * Sets the end of the iteration range.
     * 
     * @param rangeEnd  iteration range end
     */
    void setRangeEnd(double rangeEnd);

    /**
     * Returns the increment used
     * to iterate over the encapsulated range.
     * 
     * @return the start of the iteration range
     */
    double getRangeStep();

    /**
     * Sets the increment used
     * to iterate over the encapsulated range.
     * 
     * @param rangeStep   iteration range increment
     */
    void setRangeStep(double rangeStep);
    
    /**
     * Determines if a given string
     * is a valid variable name.
     * Given that underscore is an alphabetic character,
     * a valid variable name is one that
     * begins with an alphabetic character,
     * and whose remaining are characters alphanumeric.
     * 
     * @param name  the given string
     * 
     * @return  true if the given string is a valid variable name
     */
    boolean isValidName( String name );

    
    /**
     * Determines if a given string
     * is a valid double value.
     * Consider preferring {@linkplain #evaluate(String)}
     * to this method.
     * 
     * @param valStr  the given string
     * 
     * @return  true if the given string is a valid double value
     * 
     * @see #evaluate(String)
     */
    boolean isValidValue( String valStr );
    
    /**
     * Parses and evaluates an expression in the context
     * of the current equation.
     * Declared variables and functions are recognized;
     * use of undeclared variables or functions
     * will result in evaluation failure.
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
     * 
     * </p>
     * 
     * @param exprStr   the expression to evaluate
     * 
     * @return  
     *      an Optional containing the result of the evaluation,
     *      or an empty Optional if an error occurred
     */
    Optional<Double> evaluate( String exprStr );
}