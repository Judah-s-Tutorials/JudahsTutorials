package com.acmemail.judah.color_primer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for SpectrumDial's plain property accessors: hueMin,
 * hueMax, saturation, and brightness. These properties have no
 * Swing side effects (they are read/written directly by DialFrame's
 * listeners, never touched by paintComponent or the mouse/key
 * monitors' own logic), so the SpectrumDial under test does not
 * need to be realized, shown, or driven from the EDT.
 */
class SpectrumDialTest
{
    /**
     * Pairs a property's setter/getter with a descriptive name.
     * Unbound method references so each test can supply its own
     * fresh SpectrumDial rather than sharing one across cases.
     */
    private record PropertyConfig(
        String                              name,
        BiConsumer<SpectrumDial, Integer>   setter,
        ToIntFunction<SpectrumDial>         getter
    )
    {
        @Override
        public String toString()
        {
            return name;
        }
    }

    private static Stream<PropertyConfig> propertyConfigs()
    {
        return Stream.of(
            new PropertyConfig(
                "hueMin", SpectrumDial::setHueMin, SpectrumDial::getHueMin ),
            new PropertyConfig(
                "hueMax", SpectrumDial::setHueMax, SpectrumDial::getHueMax ),
            new PropertyConfig(
                "saturation",
                SpectrumDial::setSaturation,
                SpectrumDial::getSaturation ),
            new PropertyConfig(
                "brightness",
                SpectrumDial::setBrightness,
                SpectrumDial::getBrightness ),
            new PropertyConfig(
                "barAngle",
                SpectrumDial::setBarAngle,
                SpectrumDial::getBarAngle )
        );
    }

    @ParameterizedTest
    @MethodSource( "propertyConfigs" )
    void propertyDefaultsToZero( PropertyConfig cfg )
    {
        SpectrumDial    dial    = new SpectrumDial( 300 );
        assertEquals( 0, cfg.getter().applyAsInt( dial ) );
    }

    @ParameterizedTest
    @MethodSource( "propertyConfigs" )
    void setterUpdatesGetter( PropertyConfig cfg )
    {
        SpectrumDial    dial    = new SpectrumDial( 300 );
        cfg.setter().accept( dial, 42 );
        assertEquals( 42, cfg.getter().applyAsInt( dial ) );
    }

    @ParameterizedTest
    @MethodSource( "propertyConfigs" )
    void secondSetterCallOverwritesFirst( PropertyConfig cfg )
    {
        SpectrumDial    dial    = new SpectrumDial( 300 );
        cfg.setter().accept( dial, 42 );
        cfg.setter().accept( dial, 17 );
        assertEquals( 17, cfg.getter().applyAsInt( dial ) );
    }

    /**
     * Guards against a copy/paste slip among the four near-identical
     * setter bodies (e.g. setHueMax accidentally assigning hueMin).
     */
    @Test
    void settingOnePropertyDoesNotAffectTheOthers()
    {
        SpectrumDial    dial    = new SpectrumDial( 300 );
        dial.setHueMin( 42 );
        assertEquals( 0, dial.getHueMax() );
        assertEquals( 0, dial.getSaturation() );
        assertEquals( 0, dial.getBrightness() );
    }
}
