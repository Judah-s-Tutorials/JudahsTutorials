package com.acmemail.judah.battleship.ui.text;

import java.util.List;

import com.acmemail.judah.battleship.Provisioner;

public record RoundTripResult( 
    Provisioner     provisioner,
    TextPhase       phase,
    List<String>    input,
    List<String>    feedback
)
{
}