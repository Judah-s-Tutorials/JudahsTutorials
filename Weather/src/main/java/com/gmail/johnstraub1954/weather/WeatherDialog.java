package com.gmail.johnstraub1954.weather;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class WeatherDialog
{
    private static final char   degree  = '\u00b0';
    
    private final  JDialog                  dialog;
    private final  WeatherData              data;
    
    private static transient WeatherDialog  weatherDialog;
    public static WeatherDialog 
    of( Dialog owner, String title, WeatherData data )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            weatherDialog = new WeatherDialog( owner, title, data );
        else
        {
            try
            {
                SwingUtilities.invokeAndWait( () ->
                    weatherDialog = new WeatherDialog( owner, title, data )
                );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
        }
        return weatherDialog;
    }
    
    private WeatherDialog( Dialog owner, String title, WeatherData data )
    {
        this.data = data;
        dialog = new JDialog( owner, title, true );
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.setContentPane( getRoot() );
        dialog.pack();
    }
    
    public JDialog getJDialog()
    {
        return dialog;
    }
    
    public void show()
    {
        dialog.setVisible( true );
    }
    
    private JPanel getRoot()
    {
        JPanel  panel   = new JPanel( new BorderLayout() );
        Border  border  = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        panel.setBorder( border );
        panel.add( centerPanel(), BorderLayout.CENTER );
        panel.add( southPanel(), BorderLayout.SOUTH );
        panel.add( northPanel(), BorderLayout.NORTH );
        
        return panel;
    }
    
    private JPanel centerPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.add( getCurrentPanel() );
        panel.add( getForecastPanel() );
        return panel;
    }
    
    private JPanel northPanel()
    {
        String  label0Text  =
            "<html>"
            + "<span style='font-size: 150%;'>"
            + data.getCity() + " "
            + data.getRegion() + ", "
            + data.getCountry() + " "
            + data.getCurrentTime()
            + "</span>";
        JLabel  label0      = new JLabel( label0Text );
        
        JPanel  panel   = new JPanel();
        panel.add( label0 );
        return panel;
    }
    
    private JPanel getCurrentPanel()
    {
        String      label0Text  = 
            "<html>"
            + data.getCurrentConditionText() + "<br>" 
            + data.getCurrentTempC() + degree + "C, "
            + data.getCurrentTempF() + degree + "F<br>"
            + "Sunrise: " + data.getSunrise() + "<br>"
            + "Sunset: " + data.getSunset() + "<br>"
            + "Precip: " + data.getCurrentPrecipMillis() + "mm, "
            + data.getCurrentPrecipInches() + "in";
        Icon    label0Icon      = data.getCurrentIcon();
        JLabel  label0          = new JLabel( label0Text );
        label0.setIcon( label0Icon );
        
        String      phase           = data.getMoonPhase();
        int         illumination    = data.getMoonIllumination();
        Icon        label1Icon      = MoonIcon.of( illumination, phase );
        String      label1Text      = 
            "<html>"
            + phase + "<br>" 
            + "Moonrise: " + data.getMoonrise() + "<br>"
            + "Moonset: " + data.getMoonset() + "<br>"
            + "Illumination: " + illumination + "%";
        JLabel  label1          = new JLabel( label1Text ); 
        label1.setIcon( label1Icon );
        
        String  label3Text      =
            "<html>"
            + "Chance of rain: " + data.getCurrentRainChance() + "%<br>"
            + "Chance of snow: " + data.getCurrentSnowChance() + "%<br>"
            + "Expected high: " + data.getCurrentMaxTempC() + "C, "
            + data.getCurrentMaxTempF() + "F<br>"
            + "Expected low: " + data.getCurrentMinTempC() + "C, "
            + data.getCurrentMinTempF() + "F<br>";
        JLabel  label3          = new JLabel( label3Text );
        
        String  label4Text      =
            "<html>"
            + "Latitude: " + data.getLatitude() + "<br>"
            + "Longitude: " + data.getLongitude() + "<br>"
            + "Time zone: " + data.getTimeZone();
        TimeZone    timeZone    = TimeZone.of( data );
        if ( timeZone != null )
        {
            int seconds = timeZone.getOffsetSeconds();
            int minutes = seconds / 60;
            int hours   = minutes / 60;
            minutes %= 60;
            label4Text += 
                "<br>UTC: " + hours + ":" + minutes + ", "
                    + "DST: " + timeZone.isDst();
        }
        JLabel      label4      = new JLabel( label4Text );
        
        JPanel  panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.LINE_AXIS );
        panel.setLayout( layout );
        
        Border  outterBorder    = 
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        Border  innerBorder     =
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border  border          =
            BorderFactory.createCompoundBorder( outterBorder, innerBorder );
        panel.setBorder( border );
        
        Dimension   dim = new Dimension( 7, 0 );
        panel.add( label0 );
        panel.add( Box.createRigidArea( dim ) );
        panel.add( label1 );
        panel.add( Box.createRigidArea( dim ) );
        panel.add( label3 );
        panel.add( Box.createRigidArea( dim ) );
        panel.add( label4 );
        return panel;
    }
    
    private JPanel southPanel()
    {
        JPanel  panel   = new JPanel();
        JButton okay    = new JButton( "OK" );
        dialog.getRootPane().setDefaultButton( okay );
        okay.addActionListener( e -> dialog.setVisible( false ) );
        panel.add( okay );
        return panel;
    }
    
    private JPanel getForecastPanel()
    {
        int         last    = data.getForecastDays().size();
        int         start   = 1;
        int         end     = last < 6 ? last : 6;
        int         numDays = end - start;
        GridLayout  layout  = new GridLayout( 1, numDays, 5, 0 );
        JPanel      panel   = new JPanel( layout );
        for ( int inx = start ; inx < end ; ++inx )
        {
            JComponent  day = getForecastFor( inx );
            panel.add( day );
        }
        
        return panel;
    }
    
    private JComponent getForecastFor( int index )
        throws IndexOutOfBoundsException
    {   
        Border      innerBorder =
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border      outerBorder =
            BorderFactory.createLineBorder( Color.LIGHT_GRAY, 1 );
        Border      border      =
            BorderFactory.createCompoundBorder( outerBorder, innerBorder );
        ForecastDay forecastDay = data.getForecastDay( index );
        Day         day         = forecastDay.getDay();
        double      highF       = day.getMaxtempF();
        double      highC       = day.getMaxtempC();
        double      lowF        = day.getMintempF();
        double      lowC        = day.getMintempC();
        String  labelText   = 
            "<html>"
            + forecastDay.getDate() + "<br>"
            + "High " + highF + degree + "F, " + highC + degree + "C<br>"
            + "low " + lowF + degree + "F, " + lowC + degree + "C<br>"
            + "Chance of rain: " + day.getDailyChanceOfRain() + "%<br>"
            + "Chance of snow: " + day.getDailyChanceOfSnow() + "%<br>";
        Icon    labelIcon   = day.getCondition().getIcon();
        JLabel  label       = new JLabel( labelText );
        label.setIcon( labelIcon );
        label.setBorder( border );
        return label;
    }
}
