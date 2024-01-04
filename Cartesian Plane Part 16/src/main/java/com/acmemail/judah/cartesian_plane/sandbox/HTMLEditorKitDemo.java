package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class HTMLEditorKitDemo
{
    private static final String rule1   = ".test1{ color: #0001;";
    private static final String rule2   = ".test2{ color: #0002;";
    private static final String rule3   = ".test3{ color: #0003;";
    private static final String rule4   = ".test4{ color: #0004;";
    private static final String rule5   = ".test5{ color: #0005;";
    private static final String rule6   = ".test6{ color: #0006;";
    private static final String rule7   = ".test7{ color: #0007;";
    private static final String rule8   = ".test8{ color: #0008;";
    private static final String rule9   = ".test9{ color: #0009;";
    
    public static void main( String[] args )
    {
        HTMLEditorKit   kitA    = new HTMLEditorKit();
        StyleSheet      sheet1  = kitA.getStyleSheet();
        sheet1.addRule( rule1 );
        
        HTMLEditorKit   kitB    = new HTMLEditorKit();
        System.out.println( "kitA == kitB? " + (sheet1 == kitB.getStyleSheet()) );
        System.out.println( "===================" );
        
        StyleSheet      sheet2  = new StyleSheet();
        sheet2.addRule( rule2 );
        HTMLEditorKit   kitC    = new HTMLEditorKit();
        kitC.setStyleSheet( sheet2 );
        System.out.println( "kitA == kitB? " + (sheet1 == kitB.getStyleSheet()) );
        System.out.println( "kitA == kitC? " + (sheet1 == kitC.getStyleSheet()) );
        System.out.println( "kitB == kitC? " + (sheet2 == kitC.getStyleSheet()) );
        System.out.println( "===================" );
        
    }
}
