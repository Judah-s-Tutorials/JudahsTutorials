package com.acmemail.judah.cartesian_plane.input;

import java.util.Optional;

public interface IEquationFileChooser
{
    /**
     * Allow the operator to choose an equation file;
     * convert the file to an equation
     * and return the equation in an Optional.
     * If the operator cancels the operation
     * an empty Optional is returned.
     * If an I/O error occurs,
     * or if a parse error occurs,
     * an error message is posted to the {@link MessageConsumer}
     * and an empty Optional is returned.
     * 
     * @return  
     *      If the operation completes successfully,
     *      an Optional containing the fetched equation,
     *      otherwise an empty Optional
     *      
     * @see MessageConsumer
     * @see #setMessageConsumer
     */
    Optional<Equation> openDialog();

    /**
     * Allow the operator to save an equation
     * in a selected location.
     * Returns true if the operation completes successfully.
     * If the operator cancels the operation
     * false is returned.
     * If an error occurs,
     * an error message is posted to the 
     * {@link MessageConsumer}
     * and false is returned.
     * 
     * @param equation  the equation to save; must not be null
     *
     * @return  true, if the operation completes successfully
     *
     * @throws NullPointerException if {@code equation} is null
     *
     * @see MessageConsumer
     * @see #setMessageConsumer
     */
    boolean saveDialog(Equation equation);

    /**
     * Sets the MessageConsumer for this instance.
     * 
     * @param consumer  the target MessageConsumer
     */
    void setMessageConsumer( MessageConsumer consumer );
}