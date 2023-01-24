package com.acmemail.judah.cartesian_plane.sandbox;

/**
 * This class represents a set of field names
 * and associated getters
 * for the purpose of demonstrating
 * various aspects of reflection.
 * Each field is one of three types
 * (int, String or float)
 * and is declared with some mixture of the modifiers
 * public, private, static and final.
 * All fields are explicitly initialized
 * in their declarations
 * except for iVar, sVar and fVar,
 * which are initialized from parameters
 * in the sole constructor.
 * 
 * @author Jack Straub
 * 
 * @see #ReflectionDemoObject(int, String, float)
 */
public class ReflectionDemoObject
{
    public int                  pimiVar1    = 10;
    public String               pimsVar1    = "Alfa";
    public float                pimfVar1    = 3.14f;
    
    private int                 rimiVar1    = 20;
    private String              rimsVar1    = "Baker";
    private float               rimfVar1    = 4.14f;
    
    public static int           pcmiVar1    = 30;
    public static String        pcmsVar1    = "Charlie";
    public static float         pcmfVar1    = 5.14f;
    
    private static int          rcmiVar1    = 40;
    private static String       rcmsVar1    = "Delta";
    private static float        rcmfVar1    = 6.14f;
    
    public static final int     pcfiVar1    = 50;
    public static final String  pcfsVar1    = "Easy";
    public static final float   pcffVar1    = 7.14f;
    
    private static final int    rcfiVar1    = 60;
    private static final String rcfsVar1    = "Foxtrot";
    private static final float  rcffVar1    = 8.14f;
    
    private final int           iVar1;
    private final String        sVar1;
    private final float         fVar1;
    
    public ReflectionDemoObject( int iParam, String sParam, float fParam )
    {
        iVar1 = iParam;
        sVar1 = sParam;
        fVar1 = fParam;
    }

    /**
     * @return the pimiVar1
     */
    public int getPimiVar1()
    {
        return pimiVar1;
    }

    /**
     * @return the pimsVar1
     */
    public String getPimsVar1()
    {
        return pimsVar1;
    }

    /**
     * @return the pimfVar1
     */
    public float getPimfVar1()
    {
        return pimfVar1;
    }

    /**
     * @return the rimiVar1
     */
    public int getRimiVar1()
    {
        return rimiVar1;
    }

    /**
     * @return the rimsVar1
     */
    public String getRimsVar1()
    {
        return rimsVar1;
    }

    /**
     * @return the rimfVar1
     */
    public float getRimfVar1()
    {
        return rimfVar1;
    }

    /**
     * @return the pcmiVar1
     */
    public static int getPcmiVar1()
    {
        return pcmiVar1;
    }

    /**
     * @return the pcmsVar1
     */
    public static String getPcmsVar1()
    {
        return pcmsVar1;
    }

    /**
     * @return the pcmfVar1
     */
    public static float getPcmfVar1()
    {
        return pcmfVar1;
    }

    /**
     * @return the rcmiVar1
     */
    public static int getRcmiVar1()
    {
        return rcmiVar1;
    }

    /**
     * @return the rcmsVar1
     */
    public static String getRcmsVar1()
    {
        return rcmsVar1;
    }

    /**
     * @return the rcmfVar1
     */
    public static float getRcmfVar1()
    {
        return rcmfVar1;
    }

    /**
     * @return the pcfiVar1
     */
    public static int getPcfiVar1()
    {
        return pcfiVar1;
    }

    /**
     * @return the pcfsVar1
     */
    public static String getPcfsVar1()
    {
        return pcfsVar1;
    }

    /**
     * @return the pcffVar1
     */
    public static float getPcffVar1()
    {
        return pcffVar1;
    }

    /**
     * @return the rcfiVar1
     */
    public static int getRcfiVar1()
    {
        return rcfiVar1;
    }

    /**
     * @return the rcfsVar1
     */
    public static String getRcfsVar1()
    {
        return rcfsVar1;
    }

    /**
     * @return the rcffVar1
     */
    public static float getRcffVar1()
    {
        return rcffVar1;
    }

    /**
     * @return the iVar1
     */
    public int getIVar1()
    {
        return iVar1;
    }

    /**
     * @return the sVar1
     */
    public String getSVar1()
    {
        return sVar1;
    }

    /**
     * @return the fVar1
     */
    public float getFVar1()
    {
        return fVar1;
    }
}
