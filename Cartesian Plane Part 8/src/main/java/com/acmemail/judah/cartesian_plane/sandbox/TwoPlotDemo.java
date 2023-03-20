package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotColorCommand;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class TwoPlotDemo
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
        
        ToPlotCommand           toPlotCmd   = new ToPlotCommand( plane );
        
        Polynomial              poly        = new Polynomial( 3.5f, -5, 0, 1 );
        FunctionIterator        iter        = 
            new FunctionIterator( poly, -2, 2.5, .005 );
        Spliterator<Point2D>    splitter    =
            Spliterators.spliteratorUnknownSize( iter, 0 );
        Stream<PlotCommand>     polyStream  = 
            StreamSupport.stream( splitter, false )
                .map( toPlotCmd::toPlotPointCommand );

        DoubleFunction<Point2D> circle  = t ->
            new Point2D.Double(
                Math.cos( t ) + .5,
                Math.sin( t ) - .5
            );
        ParametricCoordinates   coords          =
            new ParametricCoordinates( circle, 0, 2 * Math.PI, .005 );
        Stream<PlotCommand>     circleStream    =
            coords.stream().map( toPlotCmd::toPlotPointCommand );

        PlotCommand         redCmd      = new PlotColorCommand( plane, Color.RED );
        PlotCommand         blueCmd     = new PlotColorCommand( plane, Color.BLUE );
        Stream<PlotCommand> streamA = 
            Stream.concat( Stream.of( redCmd ), polyStream );
        Stream<PlotCommand> streamB = 
            Stream.concat( streamA, Stream.of( blueCmd ) );
        Stream<PlotCommand> streamC = 
            Stream.concat( streamB, circleStream );
        plane.setStreamSupplier( () -> streamC );
    }
}
