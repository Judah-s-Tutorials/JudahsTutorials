package Controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.Definition;
import com.judahstutorials.glossary.SeeAlso;

public class MainFrame
{
    private final JFrame            frame       =
        new JFrame( "Glossary Editor" );
    
    private final JTextField            ident           =
        new JTextField( 20 );
    private final JFormattedTextField   term            =
        new JFormattedTextField();
    private final JFormattedTextField   seqNum          = 
        new JFormattedTextField();
    private final JFormattedTextField   slug            = 
        new JFormattedTextField();
    private final JTextArea             description     =
        new JTextArea( 24, 40 );
    private final QueryDialog           queryDialog     = 
        new QueryDialog( frame );
    private final SeeAlsoPanel          seeAlsoPanel    =
        new SeeAlsoPanel();
    
    private Definition  currDef;
    
    public MainFrame()
    {
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getMainPanel(), BorderLayout.CENTER );
        contentPane.add( seeAlsoPanel, BorderLayout.EAST );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setLocation( 200, 100 );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        frame.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing( WindowEvent evt )
                {
                    ConnectionMgr.closeConnection();
                }
        });
    }

    private JPanel getMainPanel()
    {
        JPanel      panel   = new JPanel( new BorderLayout() );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        panel.add( getHeaderPanel(), BorderLayout.NORTH );
        panel.add( description, BorderLayout.CENTER );
        
        description.addKeyListener( new KeyAdapter() {
            @Override
            public void keyTyped( KeyEvent evt )
            {
                currDef.setDescription( description.getText() );
            }
        });
        ident.setEditable( false );
        return panel;
    }

    private JPanel getHeaderPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border      =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        JLabel      defLabel    = new JLabel( "Definition" );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( getTextFieldPanel( ident, "Term ID:" ) );
        panel.add( getTextFieldPanel( term, "Term:     " ) );
        panel.add( getTextFieldPanel( seqNum, "Seq #:    " ) );
        panel.add( getTextFieldPanel( slug, "Slug:      " ) );
        panel.add( defLabel );
        
        ident.setEditable( false );
        term.addPropertyChangeListener( "value", e ->
            currDef.setSlug( (String)slug.getValue() ) 
        );
        term.addPropertyChangeListener( "value", e ->
            currDef.setTerm( (String)seqNum.getValue() ) 
        );
        seqNum.addPropertyChangeListener( "value", e ->
            currDef.setSeqNum( (Integer)seqNum.getValue() ) 
        );
        return panel;
    }
    
    private JPanel getTextFieldPanel( JTextField textField, String label )
    {
        JPanel  panel   = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        panel.add( new JLabel( label ) );
        panel.add( textField );
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel       = new JPanel();
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        
        JButton     newButton       = new JButton( "New" );
        JButton     queryButton     = new JButton( "Query" );
        JButton     commitButton    = new JButton( "Commit" );
        JButton     updateButton    = new JButton( "Update" );
        JButton     exitButton      = new JButton( "Exit" );
        newButton.addActionListener( this::newDef );
//        insertButton.addActionListener( this::insertTerm );
        queryButton.addActionListener( this::query );
        exitButton.addActionListener( e -> {
            ConnectionMgr.closeConnection();
            System.exit( 0 );
        });
        
        panel.add( newButton );
        panel.add( queryButton );
        panel.add( commitButton );
        panel.add( updateButton );
        panel.add( exitButton );
        
        return panel;
    }
    
    private void commit( ActionEvent evt )
    {
        currDef.commit();
    }
    
    private void newDef( ActionEvent evt )
    {
        currDef = new Definition();
        term.setText( "" );
        ident.setText( "" );
        description.setText( "" );
        slug.setText( "" );
        seeAlsoPanel.setDefinition( currDef );
    }
    
    private void query( ActionEvent evt )
    {
        Definition  def     = null;
        int choice  = queryDialog.showDialog();
        if ( choice == JOptionPane.OK_OPTION )
            def = queryDialog.getSelection();
        if ( def != null )
        {
            currDef = def;
            int termID  = def.getID();
            ident.setText( String.valueOf( termID ) );
            term.setText( def.getTerm() );
            slug.setText( def.getSlug() );
            seqNum.setValue( def.getSeqNum() );
            description.setText( def.getDescription() );
            
            seeAlsoPanel.setDefinition( currDef );
            List<SeeAlso>   list    = SeeAlso.getAllFor( termID );
            System.out.println( termID );
            System.out.println( list.size() );
        }
    }
}
