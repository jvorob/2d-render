/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;

/**
 *
 * @author George
 */
public class Line {
    public Vector2 end1, end2;
    
    public Line(Vector2 a, Vector2 b)
    {end1 = a; end2 = b;}
    public static Line fromVector(Vector2 lin, Vector2 start)//makes line from pos to pos+lin
    {
        return new Line(start, Vector2.vecAdd(lin, start));
    }
}
