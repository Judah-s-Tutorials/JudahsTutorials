package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.Color;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.DoubleUnaryOperator;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.CircleShape;
import com.acmemail.judah.cartesian_plane.PlotColorCommand;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotCoordinatesCommand;
import com.acmemail.judah.cartesian_plane.PlotShapeCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.sandbox.lines.Polynomial;

public class PolyIteratorDemo
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
    public static void main(String[] args)
    {
        PropertyManager pmgr    = PropertyManager.INSTANCE;
        
        pmgr.setProperty( CPConstants.TIC_MAJOR_LEN_PN, 21 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_MPU_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_LEN_PN, 11 );
        pmgr.setProperty( CPConstants.TIC_MINOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_MPU_PN, 5 );
        Root    root    = new Root( plane );
        root.start();
        
        Polynomial  poly    = new Polynomial( -2, 4, 0, -1 );
        plane.setIterator( new CommandIterator( poly, -2, 3.001f, .005f ) );
    }

    private static class CommandIterator implements Iterator<PlotCommand>
    {
        private final PlotCommand   shapeCmd        = 
            new PlotShapeCommand( plane, new CircleShape( 3 ) );
        private final PlotCommand   negColorCmd     = 
            new PlotColorCommand( plane, Color.RED );
        private final PlotCommand   posColorCmd     = 
            new PlotColorCommand( plane, Color.BLUE );
        
        private final DoubleUnaryOperator   funk;
        private final float                 first;
        private final float                 last;
        private final float                 incr;
        private float                       xco;
        private PlotCommand                 pendingCmd  = null;
        private int                         lastSign    = 1;
        private boolean                     setColor    = false;
        private boolean                     setShape    = false;
        
        public CommandIterator( 
            DoubleUnaryOperator oper, 
            float first, 
            float last, 
            float incr 
        )
        {
            this.funk = oper;
            this.first = first;
            this.last = last;
            this.incr = incr;
            this.xco = first;
        }
        
        @Override
        public boolean hasNext()
        {
            return xco < last;
        }
        
        @Override
        public PlotCommand next()
        {
            PlotCommand cmd = null;
            if ( !setColor )
            {
                cmd = posColorCmd;
                lastSign = 1;
                setColor = true;
            }
            else if ( pendingCmd != null )
            {
                cmd = pendingCmd;
                pendingCmd = null;
            }
            else if ( xco > last )
            {
                throw new NoSuchElementException();
            }
            else
            {
                float   yco         = (float)funk.applyAsDouble( xco );
                int     currSign    = (int)Math.signum( yco );
                cmd = new PlotCoordinatesCommand( plane, xco, yco );
                if ( currSign != lastSign )
                {
                    lastSign = currSign;
                    pendingCmd = cmd;
                    cmd = currSign < 0 ? negColorCmd : posColorCmd;
                }
                else
                    xco += incr;
            }
            System.out.println( cmd );
            return cmd;
        }   
    }
}
