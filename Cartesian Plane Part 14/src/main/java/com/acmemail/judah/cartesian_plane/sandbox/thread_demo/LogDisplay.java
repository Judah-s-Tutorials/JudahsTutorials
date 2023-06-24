package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.awt.Dimension;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class LogDisplay
{
    private static final String             lineSep     = System.lineSeparator();
    private static final DateTimeFormatter  timeFMT     = 
        DateTimeFormatter.ofPattern( "HH:mm:ss.SSS" );
    
    private final JEditorPane   textArea    = new JEditorPane();
    private final JFrame        frame       = new JFrame( "Thread Log" );
    private final JScrollPane   scroller    = new JScrollPane( textArea );

    
    public void build()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel();
        
        contentPane.add( scroller );
        frame.setContentPane( contentPane );
        
        textArea.setContentType( "text/html" );
        textArea.setPreferredSize( new Dimension( 500, 250 ) );
        
        update();
        frame.pack();
        frame.setVisible( true );       
    }
    
    public void update()
    {
        List<ExecLog>   log     = ExecLog.getLog();
        StringBuilder   bldr    = new StringBuilder();
        head( bldr );
        log.forEach( l -> row( bldr, l ) );
        tail( bldr );
        textArea.setText( bldr.toString() );
        
        JScrollBar  vertBar = scroller.getVerticalScrollBar();
        vertBar.setValue( 2 * vertBar.getMaximum() );
        frame.repaint();
    }
    
    private static StringBuilder head( StringBuilder bldr )
    {
        bldr.append( "<html>" ).append( lineSep )
            .append( "<table>" ).append( lineSep )
//            .append( "<thead>" ).append( lineSep )
            .append( "<tr>" ).append( lineSep )
            .append( "    " )
                .append( getTH( "Time" ) )
                .append( getTH( "Class" ) )
                .append( getTH( "Method" ) )
                .append( getTH( "Param" ) )
                .append( getTH( "Thread" ) )
                .append( lineSep )
            .append( "</tr>").append( lineSep )
//            .append( "</thead>" ).append( lineSep )
            .append( "<tbody>" ).append( lineSep );
        return bldr;
    }
    
    private static StringBuilder row( StringBuilder bldr, ExecLog data )
    {
        String  timeStr = timeFMT.format( data.getTime() );
        bldr.append( "<tr>" ).append( lineSep )
            .append( "    " )
            .append( getTD( timeStr ) )
            .append( getTD( data.getClassName() ) )
            .append( getTD( data.getMethodName() ) )
            .append( getTD( data.getParamName() ) )
            .append( getTD( data.getThreadName() ) )
        .append( lineSep )
        .append( "</tr>" )
        .append( lineSep );
        return bldr;
    }
    
    private static StringBuilder tail( StringBuilder bldr )
    {
        bldr.append( "</tbody>" ).append( lineSep )
             .append( "</table>" ).append( lineSep )
             .append( "</html>" ).append( lineSep );
        return bldr;
    }
    
    private static String getTH( String text )
    {
        String  cell    = "<th>" + text + "</th>";
        return cell;
    }
    
    private static String getTD( String text )
    {
        String  cell    = "<td>" + text + "</td>";
        return cell;
    }
}
