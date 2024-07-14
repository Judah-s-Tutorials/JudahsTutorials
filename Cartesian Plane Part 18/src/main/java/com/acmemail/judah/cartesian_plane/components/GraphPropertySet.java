package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

import com.acmemail.judah.cartesian_plane.PropertyManager;

/**
 * An object of this type
 * is used to manage the properties
 * of an area of the Cartesian plane GUI:
 * the main area (the graph itself),
 * and the top-, right-, bottom- and left-margins.
 * Properties include
 * the width of the area,
 * the background and foreground colors,
 * and the name, size and style (bold/italic)
 * of the font.
 * All properties are managed by
 * the {@linkplain PropertyManager} class,
 * and named in the 
 * {@linkplain com.acmemail.judah.cartesian_plane.CPConstants} class.
 * For example,
 * the background color property
 * may refer to
 * the main window background color (CPConstants.MW_BG_COLOR_PN)
 * or the background color
 * of one of the margins
 * (Constants.MARGIN_TOP_BG_COLOR_PN,Constants.MARGIN_RIGHT_BG_COLOR_PN, 
 * Constants.MARGIN_BOTTOM_BG_COLOR_PN, Constants.MARGIN_LEFT_BG_COLOR_PN). 
 * <p>
 * The correspondence between encapsulated properties
 * (background color, foreground color, etc.)
 * and PropertyManager properties
 * (e.g. CPConstants.MARGIN_TOP_BG_COLOR_PN)
 * is established by subclasses
 * via the class constructor.
 * See, for example, {@linkplain GraphPropertySetMW}.
 * </p>
 * 
 * @author Jack Straub
 */
public abstract class GraphPropertySet
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
    private final String        widthProperty;
    private final String        bgColorProperty;
    private final String        fgColorProperty;
    private final String        fontNameProperty;
    private final String        fontSizeProperty;
    private final String        fontStyleProperty;
    private final String        fontDrawProperty;
    
    private float   width;
    private Color   bgColor;
    private Color   fgColor;
    private String  fontName;
    private float   fontSize;
    private String  fontStyle;
    private boolean fontDraw;
    
    /**
     * Constructor.
     * Establishes the specific property names
     * base on the category encapsulated
     * by the subclass.
     * 
     * @param widthProperty     the name of the width property
     * @param bgColorProperty   the name of the background color property
     * @param fgColorProperty   the name of the foreground color property
     * @param fontNameProperty  the name of the font name property
     * @param fontSizeProperty  the name of the font size property
     * @param fontStyleProperty the name of the font style property
     * @param fontDrawProperty  the name of the font draw property
     */
    public GraphPropertySet(
        String widthProperty, 
        String bgColorProperty,
        String fgColorProperty,
        String fontNameProperty, 
        String fontSizeProperty, 
        String fontStyleProperty,
        String fontDrawProperty
    )
    {
        super();
        this.widthProperty = widthProperty;
        this.bgColorProperty = bgColorProperty;
        this.fgColorProperty = fgColorProperty;
        this.fontNameProperty = fontNameProperty;
        this.fontSizeProperty = fontSizeProperty;
        this.fontStyleProperty = fontStyleProperty;
        this.fontDrawProperty = fontDrawProperty;
        reset();
    }
    
    /**
     * Sets all encapsulate properties
     * from values provided by
     * the PropertyManager.
     * Changes that have been made
     * but not applied,
     * are discarded.
     * 
     * @see #apply()
     */
    public void reset()
    {
        width = pMgr.asFloat( widthProperty );
        bgColor = pMgr.asColor( bgColorProperty );
        fgColor = pMgr.asColor( fgColorProperty );
        fontName = pMgr.asString( fontNameProperty );
        fontSize = pMgr.asFloat( fontSizeProperty );
        fontStyle = pMgr.asString( fontStyleProperty );
        fontDraw = pMgr.asBoolean( fontDrawProperty );
    }
    
    /**
     * Sets the encapsulated properties
     * via the PropertyManager.
     * 
     * @see #reset()
     */
    public void apply()
    {
        pMgr.setProperty( widthProperty, width );
        pMgr.setProperty( bgColorProperty, bgColor );
        pMgr.setProperty( fgColorProperty, fgColor );
        pMgr.setProperty( fontNameProperty, fontName );
        pMgr.setProperty( fontSizeProperty, fontSize );
        pMgr.setProperty( fontStyleProperty, fontStyle );
        pMgr.setProperty( fontDrawProperty, fontDraw );
    }
    
    /**
     * Copy the property values from this GraphPropertySet
     * to another GraphPropertySet.
     * 
     * @param toSet the destination GraphPropertySet
     */
    public void copyTo( GraphPropertySet toSet )
    {
        toSet.setBGColor( this.getBGColor() );
        toSet.setBold( this.isBold() );
        toSet.setFGColor( this.getFGColor() );
        toSet.setFontDraw( this.isFontDraw() );
        toSet.setFontName( this.getFontName() );
        toSet.setFontSize( this.getFontSize() );
        toSet.setItalic( this.isItalic() );
        toSet.setWidth( this.getWidth() );
    }

    /**
     * Gets the value of the width property.
     * 
     * @return the width
     */
    public float getWidth()
    {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param width the width to set
     */
    public void setWidth(float width)
    {
        this.width = width;
    }

    /**
     * Gets the value of the background color property.
     * 
     * @return the background color
     */
    public Color getBGColor()
    {
        return bgColor;
    }

    /**
     * Sets the value of the background color property.
     * 
     * @param bgColor the background color to set
     */
    public void setBGColor(Color bgColor)
    {
        this.bgColor = bgColor;
    }

    /**
     * Gets the value of the foreground property.
     * 
     * @return the foreground color
     */
    public Color getFGColor()
    {
        return fgColor;
    }

    /**
     * Sets the value of the foreground color property.
     * 
     * @param fgColor the foreground color to set
     */
    public void setFGColor(Color fgColor)
    {
        this.fgColor = fgColor;
    }

    /**
     * Gets the value of the font name property.
     * 
     * @return the font name
     */
    public String getFontName()
    {
        return fontName;
    }

    /**
     * Sets the value of the font name property.
     * 
     * @param fontName the font name to set
     */
    public void setFontName(String fontName)
    {
        this.fontName = fontName;
    }

    /**
     * Sets the font style to the given value.
     * 
     * @param style the given value
     */
    public void setFontStyle( String style )
    {
        fontStyle = style;
    }
    
    /**
     * Gets the value of the font size property.
     * 
     * @return the font size
     */
    public float getFontSize()
    {
        return fontSize;
    }
    
    /**
     * Returns the font style.
     * The style will be be get bitwise 'or'
     * of PLAIN, BOLD, and ITALIC.
     * 
     * @return  the font style
     */
    public int getFontStyle()
    {
        int style   = Font.PLAIN;
        if ( isBold() )
            style |= Font.BOLD;
        if ( isItalic() )
            style |= Font.ITALIC;
        return style;
    }

    /**
     * Sets the value of the font size property.
     * 
     * @param fontSize the font size to set
     */
    public void setFontSize(float fontSize)
    {
        this.fontSize = fontSize;
    }
    
    /**
     * Gets the value of the font draw property.
     * 
     * @return the value of the font draw property
     */
    public boolean isFontDraw()
    {
        return fontDraw;
    }
    
    /**
     * Sets the value of the font draw property.
     * 
     * @param fontDraw  the font draw value to set
     */
    public void setFontDraw( boolean fontDraw )
    {
        this.fontDraw = fontDraw;
    }

    /**
     * Gets the value of the isItalic property.
     * 
     * @return the isItalic property
     */
    public boolean isItalic()
    {
        if ( fontStyle == null )
            fontStyle = "PLAIN";
        String  ucStyle = fontStyle.toUpperCase();
        return ucStyle.contains( "ITALIC" );
    }

    /**
     * Sets the value of the isItalic property.
     * 
     * @param isItalic true, to indicate the font is italic.
     */
    public void setItalic( boolean isItalic )
    {
        setStyle( isItalic, isBold() );
    }

    /**
     * Gets the value of the isBold property.
     * 
     * @return true if the font is bold
     */
    public boolean isBold()
    {
        if ( fontStyle == null )
            fontStyle = "PLAIN";
        String  ucStyle = fontStyle.toUpperCase();
        return ucStyle.contains( "BOLD" );
    }

    /**
     * Sets the value of the isBold property.
     * 
     * @param isBold true, to indicate the font is bold.
     */
    public void setBold(boolean isBold)
    {
        setStyle( isItalic(), isBold );
    }

    @Override
    public int hashCode()
    {
        int hashCode    = Objects.hash(
            widthProperty ,
            bgColorProperty,
            fgColorProperty,
            fontNameProperty,
            fontSizeProperty,
            fontStyleProperty,
            fontDrawProperty,
            width,
            bgColor,
            fgColor,
            fontName,
            fontSize,
            fontStyle,
            fontDraw
        );
        return hashCode;
    }
    
    @Override
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( other == null )
            result = false;
        else if ( this == other )
            result = true;
        else if ( this.getClass() != other.getClass() )
            result = false;
        else
        {
            GraphPropertySet    that    = (GraphPropertySet)other;
            result = 
                this.width == that.width
                && this.fgColor.equals( that.fgColor )
                && this.bgColor.equals( that.bgColor )
                && this.fontName.equals( that.fontName )
                && this.isBold() == that.isBold()
                && this.isItalic() == that.isItalic()
                && this.fontSize == that.fontSize;
        }
        return result;
    }

    /**
     * Set the font style.
     * Result will be "PLAIN" (not bold or italic),
     * "ITALIC", "BOLD" or "ITALIC BOLD".
     * 
     * @param isItalic  true if style includes italic
     * @param isBold    true if style includes bold
     */
    private void setStyle( boolean isItalic, boolean isBold )
    {
        StringBuilder   bldr    = new StringBuilder();
        if ( !isItalic && !isBold )
            bldr.append( "PLAIN" );
        else
        {
            if ( isItalic )
            {
                bldr.append( "ITALIC" );
                if ( isBold )
                    bldr.append( " " );
            }
            if ( isBold )
                bldr.append( "BOLD" );
        }
        fontStyle = bldr.toString();
    }
}
