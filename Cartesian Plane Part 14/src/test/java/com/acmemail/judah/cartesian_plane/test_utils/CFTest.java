package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CFTest
{
    @AfterEach
    void tearDown() throws Exception
    {
    }

    @Test
    void test()
    {
        TestDialog  dialog  = new TestDialog( "AA" );
        dialog.show();
    }

    private class TestDialog
    {
        private final JDialog           dialog;
        private final JPanel            contentPane;
        private final JPanel            panel1;
        private final JPanel            panel2;
        private final JButton           okButton;
        private final JButton           cancelButton;
        private final JButton           exitButton;
        private final JButton           abortButton;
        
        public TestDialog( String prefix )
        {
            String  title   = prefix + "title";
            dialog = new JDialog( (Window)null, title );
            dialog.setModal( true );
            contentPane = new JPanel( new GridLayout( 2, 1 ) );
            dialog.setContentPane( contentPane );
            
            panel1 = new JPanel( new GridLayout( 1, 2 ) );
            contentPane.add( panel1 );
            okButton = new JButton( prefix + "OK" );
            cancelButton = new JButton( prefix + "Cancel" );
            panel1.add( okButton );
            panel1.add( cancelButton );
            
            panel2 = new JPanel( new GridLayout( 1, 2 ) );
            contentPane.add( panel2 );
            exitButton = new JButton( prefix + "Exit" );
            abortButton = new JButton( prefix + "Abort" );
            panel2.add( exitButton );
            panel2.add( abortButton );
            
            ActionListener  closeDialog = 
                e -> dialog.setVisible( false );
            okButton.addActionListener( closeDialog );
            cancelButton.addActionListener( closeDialog );
            exitButton.addActionListener( closeDialog );
            abortButton.addActionListener( closeDialog );
            
            dialog.pack();
        }
    
        public void show()
        {
            dialog.setVisible( true );
        }
    }    
}
