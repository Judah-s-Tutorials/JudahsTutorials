package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class EDTDemo
{
    public static boolean   isRunning   = true;
    
    public static void main( String[] args )
        throws InterruptedException
    {
        Thread  thread  = 
            new Thread( () -> auxThreadRunner(), "Aux Thread:" );
        thread.start();
        int count   = 0;
        while ( isRunning )
        {
            String      tName       = Thread.currentThread().getName();
            String      msg         = "Main method Thread: " + tName;
            if ( count++ % 5 == 0 )
                System.out.println( msg );
            Thread.sleep( 1000 );
        }
        System.exit( 0 );
    }
    
    public static void auxThreadRunner()
    {
        TestDialog  testDialog  = new TestDialog();
        String      tName       = Thread.currentThread().getName();
        String      msg         = "Aux Thread " + tName;
        System.out.println( msg + ": starting dialog" );
        testDialog.show();
        System.out.println( msg + ": dialog ended" );
        isRunning = false;
    }

    private static class TestDialog
    {
        private final JDialog   dialog  = 
            new JDialog( (Window)null, "Test Dialog" );
        
        public TestDialog()
        {
            dialog.setModal( true );
            JPanel  pane    = new JPanel( new GridLayout( 1, 2 ) );
            JButton okay    = new JButton( "OK" );
            JButton cancel  = new JButton( "Cancel" );
            Border  border  = 
                BorderFactory.createEmptyBorder( 15, 15, 15, 15 );
            pane.setBorder( border );
            pane.add( okay );
            pane.add( cancel );
            dialog.setContentPane( pane );
            dialog.pack();
            
            okay.addActionListener( e -> {
                String  tName   = Thread.currentThread().getName();
                String  msg     = "OK actionPerformed Thread: " + tName;
                System.out.println( msg );
            });
            
            cancel.addActionListener( e -> {
                String  tName   = Thread.currentThread().getName();
                String  msg     = "Cancel actionPerformed: Thread: " + tName;
                System.out.println( msg );
                dialog.setVisible( false );
            });
        }
        
        public void show()
        {
            dialog.setVisible( true );
        }
    }
}
