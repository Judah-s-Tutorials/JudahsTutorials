package util;

import java.awt.Color;

public class LinePropertyTester
{
    /** Grid width; puts y-axis at x = 100. */
    private static final int    testGridWidth   = 201;
    /** Center of y-axis */
    private static final int    yAxisXco        = testGridWidth / 2;
    /** Grid width; puts x-axis at y = 200. */
    private static final int    testGridHeight  = 401;
    /** Center of x-axis */
    private static final int    xAxisYco        = testGridHeight / 2;
    /** 
     * Pixels per unit. Locates points at:
     * <ul>
     * <li>
     *      x-axis: x = 216, 232, 248, 264
     * <li>
     * <li>
     *      y-axis: y = 116, 132, 148, 164
     * <li>
     * </ul>
     */
    private static final int    gridUnit        = 16;
    /** 
     * Minor tic marks-per-unit.
     * Puts a minor tic mark at:
     * <ul>
     * <li>
     *      x-axis: x = 204, 208, 212
     * <li>
     * <li>
     *      y-axis: y = 104, 108, 112
     * <li>
     * </ul>
     */
    private static final int    ticMinorMPU     = 4;
    /** 
     * Major tic marks-per-unit.
     * Puts a major tic mark at:
     * <ul>
     * <li>
     *      x-axis: x = 204, 208, 212
     * <li>
     * <li>
     *      y-axis: y = 104, 108, 112
     * <li>
     * </ul>
     */
    private static final int    ticMajorMPU     = 6;
    /** 
     * Grid-line lines-per-unit.
     * Puts a grid line at:
     * <ul>
     * <li>
     *      x-axis: x = 208, 216, 224
     * <li>
     * <li>
     *      y-axis: y = 108, 116, 124
     * <li>
     * </ul>
     */
    private static final int    gridLPU         = 8;
    
    /** X-coordinate of first vertical minor tic mark. */
    private static final int    ticMinorVXco    = yAxisXco + ticMinorMPU;
    /** Y-coordinate of first vertical minor tic mark. */
    private static final int    ticMinorVYco    = xAxisYco;
    
    /** X-coordinate of first horizontal minor tic mark. */
    private static final int    ticMinorHXco    = xAxisYco;
    /** Y-coordinate of first horizontal minor tic mark. */
    private static final int    ticMinorHYco    = yAxisXco + ticMinorMPU;
    
    /** X-coordinate of first vertical major tic mark. */
    private static final int    ticMajorVXco    = yAxisXco + ticMajorMPU;
    /** Y-coordinate of first vertical major tic mark. */
    private static final int    ticMajorVYco    = xAxisYco;
    
    /** X-coordinate of first vertical grid line. */
    private static final int    gridLineVXco    = yAxisXco + gridLPU;
    /** Y-coordinate of first vertical minor tic mark. */
    private static final int    gridLineVYco    = xAxisYco;
    
    /** X-coordinate of first horizontal grid line. */
    private static final int    gridLineHXco    = xAxisYco;
    /** Y-coordinate of first horizontal grid line. */
    private static final int    gridLineHYco    = yAxisXco + gridLPU;
    
    /** Minor tic mark width. Initially 1, may change during testing. */
    private int                 ticMinorWidth   = 1;
    /** Minor tic mark length. Initially 3, may change during testing. */
    private int                 ticMinorLength  = 3;
    /** Major tic mark width. Initially 7, may change during testing. */
    private int                 ticMajorLength  = 2 * ticMinorLength + 1;
    /** Major tic mark length. Initially 1, may change during testing. */
    private int                 ticMajorWidth   = 1;
    /** Grid line width. Initially 1, may change during testing. */
    private int                 gridLineWidth   = 1;
    
    /** Grid background color. May change during testing. */
    private Color               gridBGColor     = Color.WHITE;    
    /** Minor tic mark color. May change during testing. */
    private Color               ticMinorColor   = Color.RED;    
    /** Major tic mark color. May change during testing. */
    private Color               ticMajorColor   = Color.GREEN;    
    /** Grid line color. May change during testing. */
    private Color               gridLineColor   = Color.BLUE;
}
