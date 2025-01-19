package Controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.Definition;
import com.judahstutorials.glossary.SeeAlso;

public class MainFrame
{
    private final JTextField        ident       = new JTextField( 10 );
    private final JTextField        term        = new JTextField( 20 );
    private final JTextField        seqNum      = new JTextField( 20 );
    private final JTextArea         def         = new JTextArea( 24, 60 );
    private final JTextField        addField    = new JTextField( 30 );
    private final DefaultListModel<String>  seeAlsoModel   =
        new DefaultListModel<>();
    private final JList<String>     seeAlso     = 
        new JList<>( seeAlsoModel );
    
    private Definition  currDef;
    
    public MainFrame()
    {
        JFrame      frame       = new JFrame( "Glossary Editor" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getMainPanel(), BorderLayout.CENTER );
        contentPane.add( getSeeAlsoPanel(), BorderLayout.EAST );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setLocation( 200, 200 );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }

    private JPanel getMainPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( getTextFieldPanel( term, "Term: " ) );
        panel.add( getTextFieldPanel( seqNum, "Sequence: " ) );
        panel.add( new JLabel( "Definition" ) );
        panel.add( def );
        
        ident.setEditable( false );
        return panel;
    }
    
    private JPanel getSeeAlsoPanel()
    {
        JPanel      panel       = new JPanel( new BorderLayout() );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        
        String  seeAlsoText = "<html><strong>See Also:</strong></html>";
        panel.add( new JLabel( seeAlsoText ), BorderLayout.NORTH );
        panel.add( getAddPanel(), BorderLayout.SOUTH );
        panel.add( getScrolledList(), BorderLayout.CENTER );
        return panel;
    }
    
    private JPanel getTextFieldPanel( JTextField textField, String label )
    {
        JPanel  panel   = new JPanel();
        panel.add( new JLabel( label ) );
        panel.add( textField );
        return panel;
    }
    
    private JScrollPane getScrolledList()
    {
        JScrollPane pane = new JScrollPane();
        pane.setViewportView( seeAlso );
        
        return pane;
    }
    
    private JPanel getAddPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setLayout( layout );
        panel.setBorder( border );

        JButton     addButton   = new JButton( "Add" );
        addButton.addActionListener( this::addSeeAlso );
        addField.addActionListener( this::addSeeAlso );
        
        panel.add( new JLabel( "New link: " ) );
        panel.add( addField );
        panel.add( addButton );
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel       = new JPanel();
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
//        panel.setLayout( layout );
        panel.setBorder( border );
        
        JButton     newButton       = new JButton( "New" );
        JButton     insertButton    = new JButton( "Insert" );
        JButton     updateButton    = new JButton( "Update" );
        JButton     exitButton      = new JButton( "Exit" );
        newButton.addActionListener( this::newDef );
        exitButton.addActionListener( e -> {
            ConnectionMgr.closeConnection();
            System.exit( 0 );
        });
        
        panel.add( newButton );
        panel.add( insertButton );
        panel.add( updateButton );
        panel.add( exitButton );
        
        return panel;
    }
    
    private void newDef( ActionEvent evt )
    {
        currDef = new Definition();
        term.setText( "" );
        ident.setText( "" );
        def.setText( "" );
        seeAlsoModel.removeAllElements();
        addField.setText( "" );
    }
    
    private void addSeeAlso( ActionEvent evt )
    {
        String  text    = addField.getText();
        Integer ident   = null;
        if ( currDef == null )
            ;
        else if ( text == null )
            ;
        else if ( text.isEmpty() )
            ;
        else if ( (ident = currDef.getID()) == null )
            ;
        else
        {
            Integer rval    = SeeAlso.insert( ident, text );
            if ( rval != null )
            {
                seeAlsoModel.addElement( text );
                int rowCount    = seeAlsoModel.getSize();
                seeAlso.setSelectedIndex( rowCount - 1 );
                addField.setText( "" );
            }
        }
    }
}
