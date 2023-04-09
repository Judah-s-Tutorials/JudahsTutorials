package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.stream.Stream;

import net.objecthunter.exp4j.ValidationResult;

public interface Equation
{

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
     * Gets the value of the variable
     * with the given name.
     * If the name is not found
     * null is returned.
     * 
     * @param name  the given name
     * 
     * @return  the value of the variable with the given name
     */
    Double getVar(String name);

    ValidationResult parseFunction(String funk);

    /**
     * Parses the expression used to derive
     * the x-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise ValidationResult.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    ValidationResult setXExpression(String exprStr);

    /**
     * Parses the expression used to derive
     * the y-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise ValidationResult.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    ValidationResult setYExpression(String exprStr);

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation of the form <em>y=f(x)</em>.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     * 
     * @throws InvalidExpressionException if the equation is invalid
     */
    Stream<Point2D> streamY();

    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from a parametric equation.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     * 
     * @throws InvalidExpressionException if the equation is invalid
     */
    Stream<Point2D> streamXY();

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
     * 
     * @param valStr  the given string
     * 
     * @return  true if the given string is a valid double value
     */
    boolean isValidValue( String valStr );
}