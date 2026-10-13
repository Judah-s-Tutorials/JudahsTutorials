package com.acmemail.judah.battleship.ui.text;

import java.io.PrintStream;
import java.io.Reader;
import java.util.function.BiFunction;

/**
 * Function interface to simplify 
 * declaration of BiFunction<Reader, PrintStream, TextPhase>.
 * The apply method is {@code apply(Reader,PrintStream}.
 * A TextPhase object,
 * whose input and output streams
 * are tied to the Reader and PrinstStream,
 * is returned.
 */
@FunctionalInterface
public interface PhaseGetter extends BiFunction<Reader, PrintStream, TextPhase>
{
}
