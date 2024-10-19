package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.ProfileFileManager;
import com.acmemail.judah.cartesian_plane.components.ProfileEditor;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class ProfileFileManagerDemo1
{
    private static final String title       = "ProfileFileManager Demo1";
    private final JFrame        frame       = new JFrame( title );
    private final JTextField    currFile    = new JTextField( 10 );
    private final JTextField    lastResult  = new JTextField( 5 );
    private final Profile       profile     = new Profile();
    private final ProfileEditor editor      = new ProfileEditor( profile );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( ProfileFileManagerDemo1::new );
    }
    
    public ProfileFileManagerDemo1()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        Border      border      = 
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        contentPane.setBorder( border );
        contentPane.add( editor, BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.WEST );
        contentPane.add( getFeedbackPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.pack();
        GUIUtils.center( frame );
        frame.setVisible( true );
    }
    
    private JPanel getFeedbackPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( new JLabel( "Curr File: " ) );
        panel.add( currFile );
        panel.add( new JLabel( "Last Result: " ) );
        panel.add( lastResult );
        
        currFile.setEditable( false );
        lastResult.setEditable( false );
        return panel;
    }
    
    private JPanel getButtonPanel()
    {
        JPanel      buttons     = 
            new JPanel( new GridLayout( 6, 1, 0, 3 ) );
        
        JButton     open        = new JButton( "Open" );
        JButton     save        = new JButton( "Save" );
        JButton     saveAs      = new JButton( "Save As" );
        JButton     close       = new JButton( "Close" );
        
        JButton     exit        = new JButton( "Exit" );
        
        open.addActionListener( e -> {
            execCommand( ProfileFileManager::open );
            editor.reset();
        });

        save.addActionListener( e -> {
            editor.apply();
            execCommand( () -> ProfileFileManager.save( profile ) );
        });
        
        saveAs.addActionListener( e -> {
            editor.apply();
            execCommand( ProfileFileManager::saveAs );
        });
        
        close.addActionListener(
            e -> execCommand( ProfileFileManager::close )
        );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        buttons.add( open );
        buttons.add( save );
        buttons.add( saveAs );
        buttons.add( close );
        buttons.add( new JLabel( "" ) );
        buttons.add( exit );

        JPanel  panel   = new JPanel();
        panel.add( buttons );
        return panel;
    }
    
    private void execCommand( Runnable runner )
    {
        runner.run();
        String  fileName    = "";
        boolean result      = ProfileFileManager.getLastResult();
        File    file        = ProfileFileManager.getCurrFile();
        if ( file != null )
            fileName = file.getName();
        currFile.setText( fileName );
        lastResult.setText( String.valueOf( result ) );
    }
}
