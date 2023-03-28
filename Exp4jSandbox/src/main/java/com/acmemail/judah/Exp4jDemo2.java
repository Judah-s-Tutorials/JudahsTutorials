package com.acmemail.judah;

import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Exp4jDemo2
{
    private static final    CartesianPlane    plane   = new CartesianPlane();
    
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        
        List<PlotCommand>   plot    = new ArrayList<>();
        Expression e = new ExpressionBuilder("4  x^2 + 3 * x - 2" )
            .variables("x")
            .build();
        for ( double xco = -1 ; xco <= .5 ; xco += .005 )
        {
            e.setVariable("x", xco);
            double yco = e.evaluate();
            plot.add( new PlotPointCommand( plane, (float)xco, (float)yco ) );
        }
        plane.setStreamSupplier( () -> plot.stream() );
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }

}
