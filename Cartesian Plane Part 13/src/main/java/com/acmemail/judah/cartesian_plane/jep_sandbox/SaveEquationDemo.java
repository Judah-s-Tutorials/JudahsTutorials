package com.acmemail.judah.cartesian_plane.jep_sandbox;

import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.input.FileManager;

public class SaveEquationDemo
{    
    public static void main(String[] args)
    {
        Equation    equation    = new Exp4jEquation();
        FileManager.save( equation );
    }
}
