package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class GetTitledPanelsDemo
{
    private static final String     appTitle    =
        "Get All Titled Panels Demo";
    private static final int        allColors   = (int)Math.pow( 2, 48 );
    private static final Dimension  panelSize   = new Dimension( 100, 100 );
    private static final Border     emptyBorder =
        BorderFactory.createEmptyBorder( 5, 5, 5, 5 );

    private final Set<String>   expTitles   = new HashSet<>();
    private final Set<String>   actTitles   = new HashSet<>();
    private final Random        randy       = new Random( 0 );
    private int                 titleNum    = 0;
    
    public static void main(String[] args)
    {
        GetTitledPanelsDemo demo    = new GetTitledPanelsDemo();
        GUIUtils.schedEDTAndWait( demo::buildAndShow );
        demo.execDemo();
    }

    private GetTitledPanelsDemo()
    {
    }
    
    private void buildAndShow()
    {
        JFrame      appFrame        = new JFrame( appTitle );
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        JPanel      masterPanel     = new JPanel( new GridLayout( 1, 2 ) );
        masterPanel.add( getLeftPanel() );
        masterPanel.add( getRightPanel() );
        
        String  lastText    = "Last Title " + titleNum;
        contentPane.add( new JLabel( lastText ), BorderLayout.SOUTH );
        contentPane.add( masterPanel, BorderLayout.CENTER );
        appFrame.setContentPane( contentPane );
        appFrame.setLocation( 200, 100 );
        appFrame.pack();
        appFrame.setVisible( true );
        getAllTitledPanels( contentPane );
    }
    
    private void execDemo()
    {
        actTitles.forEach( System.out::println );
        String  message = "Exp Titles == Act Titles? ";
        System.out.println( message + expTitles.equals( actTitles ) );
        expTitles.forEach( System.out::println );
    }
    
    private JPanel getLeftPanel()
    {
        JPanel      leftPanel   = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( leftPanel, BoxLayout.Y_AXIS );
        leftPanel.setLayout( layout );
        
        IntStream.range( 0, 3 )
            .mapToObj( i -> getPlainTitledPanel() )
            .forEach( leftPanel::add );
        
        leftPanel.add( 
            getCompoundTitledPanel( false )
        );
        
        leftPanel.add( 
            getCompoundTitledPanel( false )
        );
        return leftPanel;
    }
    
    private JPanel getRightPanel()
    {
        JPanel  rightPanel  = new JPanel( new BorderLayout() );
        rightPanel.setBorder( emptyBorder );
        rightPanel.setBackground( new Color( randy.nextInt( allColors ) ) );
        
        JPanel  panel1          = new JPanel( new BorderLayout() );
        Border  border  = getCompoundBorder( true );
        panel1.setBorder( border );
        panel1.setBackground( new Color( randy.nextInt( allColors ) ) );
        rightPanel.add( panel1, BorderLayout.CENTER );
        
        JPanel  panel2          = new JPanel( new BorderLayout() );
        panel2.setBorder( emptyBorder );
        panel2.setBackground( new Color( randy.nextInt( allColors ) ) );
        panel1.add( panel2, BorderLayout.CENTER );
        
        JPanel  panel3          = new JPanel( new BorderLayout() );
        border  = getCompoundBorder( true );
        panel3.setBorder( border );
        panel3.setBackground( new Color( randy.nextInt( allColors ) ) );
        panel2.add( panel3, BorderLayout.CENTER );
        
        JPanel  panel4          = new JPanel( new BorderLayout() );
        panel4.setBorder( emptyBorder );
        panel4.setBackground( new Color( randy.nextInt( allColors ) ) );
        panel3.add( panel4, BorderLayout.CENTER );
        
        JPanel  panel5          = new JPanel();
        border = getTitledBorder();
        panel5.setBorder( border );
        panel5.setPreferredSize( panelSize );
        panel5.setBackground( new Color( randy.nextInt( allColors ) ) );
        panel4.add( panel5, BorderLayout.CENTER );
        
        return rightPanel;
    }
    
    private JPanel getPlainTitledPanel()
    {
        JPanel  panel   = new JPanel();
        Border  border  = getTitledBorder();
        panel.setBorder( border );
        panel.setPreferredSize( panelSize );
        panel.setBackground( new Color( randy.nextInt( allColors ) ) );
        return panel;
    }
    
    private JPanel getCompoundTitledPanel( boolean outside )
    {
        Border  border  = getCompoundBorder( outside );
        JPanel  panel   = new JPanel();

        panel.setBorder( border );
        panel.setPreferredSize( panelSize );
        panel.setBackground( new Color( randy.nextInt( allColors ) ) );
        return panel;
    }
    
    private void getAllTitledPanels( Container source )
    {
        if ( source instanceof JPanel )
        {
            JPanel  panel   = (JPanel)source;
            String  title   = getBorderTitle( panel.getBorder() );
            if ( title != null )
                actTitles.add( title );
        }
        Arrays.stream( source.getComponents() )
            .filter( c -> c instanceof Container )
            .map( c -> (Container)c )
            .forEach( c -> getAllTitledPanels( c ) );
    }
    
    private String getBorderTitle( Border border )
    {
        String  title   = null;
        if ( border instanceof TitledBorder )
        {
            title = ((TitledBorder)border).getTitle();
        }
        else if ( border instanceof CompoundBorder )
        {
            CompoundBorder  compBorder  = (CompoundBorder)border;
            Border          inside      = compBorder.getInsideBorder();
            Border          outside     = compBorder.getOutsideBorder();
            if ( (title = getBorderTitle( inside )) == null )
                title = getBorderTitle( outside );
        }
        return title;
    }
    
    private Border getCompoundBorder( boolean outside )
    {
        Border  titledBorder    = getTitledBorder();
        Border  border          = outside ?
            BorderFactory.createCompoundBorder( titledBorder, emptyBorder ) :
            BorderFactory.createCompoundBorder( emptyBorder, titledBorder );
        return border;
    }
    
    private Border getTitledBorder()
    {
        String  title   = "Title " + ++titleNum;
        Border  border  = BorderFactory.createTitledBorder( title );
        expTitles.add( title );
        return border;
    }
}
