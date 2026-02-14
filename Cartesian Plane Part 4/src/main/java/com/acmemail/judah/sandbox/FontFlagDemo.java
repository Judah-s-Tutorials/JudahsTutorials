package com.acmemail.judah.sandbox;

import java.awt.Font;

public class FontFlagDemo
{
    public static void main(String[] args)
    {
        int     bold    = Font.BOLD;
        int     italic  = Font.ITALIC;
        int     boldAndItalic   = Font.BOLD | Font.ITALIC;
        System.out.printf( 
            "bold=0x%x, italic=0x%x, both=0x%s%n",
            bold,
            italic,
            boldAndItalic
        );
    }

}
