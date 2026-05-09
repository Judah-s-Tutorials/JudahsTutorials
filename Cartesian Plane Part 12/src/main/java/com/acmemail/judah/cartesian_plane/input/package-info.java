/**
 * Reads, writes, and interprets equation-configuration files.
 * <p>
 * A configuration file is plain text, one directive per line, intended
 * to be human-readable and human-editable. Operators may copy, edit,
 * and create files by hand using any text editor.
 * </p>
 *
 * <h2>File format</h2>
 *
 * <p>
 * Each non-blank, non-comment line consists of a command optionally
 * followed by an argument:
 * </p>
 * <pre>    COMMAND argument</pre>
 * <p>
 * <code>COMMAND</code> matches a constant in
 * {@link com.acmemail.judah.cartesian_plane.input.Command} and is
 * case-insensitive. The argument syntax depends on the command and is
 * documented on the corresponding {@code Command} value.
 * </p>
 *
 * <h3>Comments and blank lines</h3>
 * <ul>
 *   <li>Lines beginning with {@code #} are comments and are ignored.</li>
 *   <li>Blank lines are ignored.</li>
 *   <li>Leading and trailing whitespace on a line is stripped.</li>
 * </ul>
 *
 * <h3>Shortcuts</h3>
 * <p>
 * The following case-insensitive prefixes are short forms for the
 * indicated commands:
 * </p>
 * <ul>
 *   <li>{@code x=expr} &rarr; {@code XEQUALS expr}</li>
 *   <li>{@code y=expr} &rarr; {@code YEQUALS expr}</li>
 *   <li>{@code r=expr} &rarr; {@code REQUALS expr}</li>
 *   <li>{@code t=expr} &rarr; {@code TEQUALS expr}</li>
 * </ul>
 *
 * <h3>Example</h3>
 * <pre>
 * # Parametric equation: a Lissajous curve
 * EQUATION lissajous
 * SET a=2, b=3
 * x= 3 * cos(a * t)
 * y= 3 * sin(b * t)
 * START 0
 * END   2 * pi
 * STEP  pi / 200
 * XYPLOT
 * </pre>
 *
 * <h2>Reading and writing</h2>
 * <p>
 * Configuration files are read via
 * {@link com.acmemail.judah.cartesian_plane.input.CommandReader}, which
 * yields a stream of
 * {@link com.acmemail.judah.cartesian_plane.input.ParsedCommand} objects.
 * Each command can then be applied to an
 * {@link com.acmemail.judah.cartesian_plane.input.Equation} via
 * {@link com.acmemail.judah.cartesian_plane.input.CommandProcessor}.
 * </p>
 *
 * @author Jack Straub
 *
 * @see com.acmemail.judah.cartesian_plane.input.Command
 * @see com.acmemail.judah.cartesian_plane.input.CommandReader
 * @see com.acmemail.judah.cartesian_plane.input.CommandProcessor
 */
package com.acmemail.judah.cartesian_plane.input;
