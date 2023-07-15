package com.acmemail.judah.sandbox.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.sandbox.ComponentException;
import com.acmemail.judah.sandbox.ComponentFinder;

public class JColorChooserDemo
{
    private static final Color  START_COLOR = Color.RED;
    private static final Color  NEW_COLOR   = Color.BLUE;
    private static final String TITLE       = "Color Chooser Demo";
    
    private final Driver            driver  = new Driver();
    private final ComponentFinder   finder  = 
        new ComponentFinder( true, false, false );
    private JFrame  frame;
    private JPanel  contentPane;

    private JColorChooser   chooser;
    private JButton         okButton;
    private JButton         cancelButton;
    private Color           colorChoice;
    
    public static void main(String[] args) throws InterruptedException
    {
        new JColorChooserDemo().execute();
    }
    
    public void execute() throws InterruptedException
    {
        invokeAndWait( this::showFeedback );
        
        while ( true )
        {
            Thread  thread      = startChooserThread();
//            String  selection   = driver.showDialog().toLowerCase();
            JButton button      = okButton;
//                selection.equals( "ok" ) ? okButton : cancelButton;
            SandboxUtils.pause( 3000 );
            invokeAndWait( () -> button.doClick() );
            SandboxUtils.join( thread );
            
            if ( colorChoice != null )
            {
                invokeAndWait( () -> {
                    contentPane.setBackground( colorChoice );
                    contentPane.repaint();
                });
            }
        }
    }
    
    public void showFeedback()
    {
        contentPane = new JPanel();
        contentPane.setBackground(START_COLOR);
        contentPane.setPreferredSize( new Dimension( 200, 75 ) );

        frame = new JFrame( "JColorChooser Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void startChooser()
    {
        colorChoice = 
            JColorChooser.showDialog( null, TITLE, START_COLOR );
    }
    
    private Thread startChooserThread() 
    {
        Thread  thread  = new Thread( () -> startChooser() );
        thread.start();
        SandboxUtils.pause( 500 );
        
        chooser = getChooser();
        okButton = getButton( "OK" );
        cancelButton = getButton( "Cancel" );
        
        return thread;
    }
    
    private JColorChooser getChooser()
    {
        JComponent  comp    = 
            finder.find( c -> (c instanceof JColorChooser) );
        if ( comp == null || !(comp instanceof JColorChooser) ) 
            throw new ComponentException( "JColorChooser not found" );
        return (JColorChooser)comp;
    }
    
    private JButton getButton( String text )
    {
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( text );
        JComponent  comp    = 
            finder.find( pred );
        if ( comp == null || !(comp instanceof JButton) )
            throw new ComponentException( text + " button not found" );
        return (JButton)comp;
    }
    
    private void invokeAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( runner );
        } 
        catch ( InterruptedException | InvocationTargetException exc )
        {
            throw new ComponentException( exc );
        }
    }
    
    private class Instructions extends JLabel
    {
        private static final String text   =
            "<html>"
            + "Instructions:<br>"
            + "<ul>"
            + "<li>\"Color\" to change selected color</li>"
            + "<li>OK to select color dialog OK button</li>"
            + "<li>Cancel to select color dialog Cancel button</li>"
            + "<li>Exit to terminate demo</li>"
            + "</ul>"
            + "</html>";
        
        public Instructions()
        {
            super( text );
        }
    }
    
    @SuppressWarnings("serial")
    private class Driver extends JDialog
        implements ActionListener
    {
        private String  selection   = null;
        
        public Driver()
        {
            setModal( true );
            setDefaultCloseOperation( DISPOSE_ON_CLOSE );
            setLocation( 300, 300 );
            addWindowListener( new WindowAdapter() {
                public void windowClosed( WindowEvent evt )
                {
                    System.exit( 0 );
                }
            });
            JPanel  contentPane = new JPanel( new BorderLayout() );
            contentPane.add( new Instructions(), BorderLayout.CENTER );
            contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
            setContentPane( contentPane );
            pack();
        }
        
        public String showDialog()
        {
            setVisible( true );
            return selection;
        }
        
        private void setAndClose( String selection )
        {
            this.selection = selection;
            setVisible( false );
        }
        
        private JPanel getButtonPanel()
        {
            JPanel  panel   = new JPanel( new GridLayout( 2, 3 ) );
            panel.setLayout( new GridLayout( 2, 3 ) );
            Stream.of( Color.BLUE, Color.ORANGE, Color.YELLOW )
                .map( c -> {
                    JButton button = new JButton( "" );
                    button.setBackground( c );
                    return button;
                })
                .peek( b -> b.addActionListener( this ) )
                .forEach( panel::add );    
            
            Stream.of( "OK", "Cancel", "Exit" )
                .map( JButton::new )
                .peek( b -> b.addActionListener( this ) )
                .forEach( panel::add );
            
            return panel;
        }
        
        public void actionPerformed( ActionEvent evt )
        {
            if ( chooser != null && chooser.isVisible() )
            {
                JButton button  = getSourceButton( evt );
                String  text    = button.getText().toLowerCase();
                if ( text.equals( "exit" ) )
                    System.exit( 1 );
                else if ( text.equals( "ok" ))
                    setAndClose( "OK" );
                else if ( text.equals( "cancel" ) )
                    setAndClose( "Cancel" );
                else
                {
                    Color   color   = button.getBackground();
                    chooser.setColor( color );
                }
            }
        }
        
        private JButton getSourceButton( ActionEvent evt )
        {
            Object  source  = evt.getSource();
            if ( !(source instanceof JButton) )
            {
                String  message =
                    "Unexpected ActionEvent source: "
                    + source.getClass().getName();
                throw new ComponentException( message );
            }
            return (JButton)source;
        }
    }
}
    