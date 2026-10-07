package com.acmemail.judah.battleship.sandbox;

import com.acmemail.judah.battleship.ui.text.SetupPhase;

public class SetupPhaseDemo
{
    public static void main( String[] args )
    {
        SetupPhase  setup   = new SetupPhase();
        setup.exec();
        if ( setup.isQuit() )
            System.out.println( "quitting" );
        else
            System.out.println( "not quitting" );
    }
}
