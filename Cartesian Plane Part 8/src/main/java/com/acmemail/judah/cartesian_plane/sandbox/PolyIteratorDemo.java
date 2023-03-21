package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * This is a simple application
 * to demonstrate the use of 
 * functional interfaces, iterators and streams.
 * <ol>
 * <li>
 *      First it uses {@linkplain Polynomial} 
 *      to implement a functional interface,
 *      <em>DoubleUnaryOperator.</em>
 * </li>
 * <li>
 *      The functional interface
 *      is used in conjunction with {@inkplain FunctionIterator}
 *      to create an <em>Iterator&lt;Point2D&gt;</em>.
 * </li>
 * <li>
 *      The iterator is turned into a spliterator
 *      using the <em>Spliterators</em> utility class.
 * </li>
 * <li>
 *      The spliterator is turned into a stream
 *      using the <em>StreamSupport</em> utility class.
 *      The stream is ultimately delivered 
 *      to the CartesianPlane class
 * </li>
 * </ol>
 * 
 * @author Jack Straub
 */
public class PolyIteratorDemo
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
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
        
        ToPlotPointCommand      toPlotPointCmd   = 
            FIUtils.toPlotPointCommand( plane );
        
        Polynomial              poly        = new Polynomial( 3.5f, -5, 0, 1 );
        FunctionIterator        iter        = 
            new FunctionIterator( poly, -2, 2.5, .005 );
        Spliterator<Point2D>    splitter    =
            Spliterators.spliteratorUnknownSize( iter, 0 );
        plane.setStreamSupplier(
            () -> StreamSupport.stream( splitter, false )
                .map( toPlotPointCmd::of )
        );
    }
}
