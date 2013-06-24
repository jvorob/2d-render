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
    
    public float getAngle()//gets the angle if end1 is at the origin
    {
        return Angles.getAngle(Vector2.vecSubt(end2, end1));
    }
    
    public void rotateAbout(Vector2 rotPoint, float angle)//rotates the line ccw about rotPoint
    {
        Vector2 temp1 = Vector2.vecSubt(end1, rotPoint);
        Vector2 temp2 = Vector2.vecSubt(end2, rotPoint);
        temp1.rotate(angle);
        temp2.rotate(angle);
        end1 = Vector2.vecAdd(temp1,rotPoint);
        end2 = Vector2.vecAdd(temp2,rotPoint);
    }
    
    public void rotate(float angle)//rotates the line ccw about end1
    {
        Vector2 temp2 = Vector2.vecSubt(end2, end1);
        temp2.rotate(angle);
        end2 = Vector2.vecAdd(temp2,end1);
    }
    
    public void move(Vector2 v)//moves both ends by v
    {
        end1.add(v);
        end2.add(v);
    }
    
    public Line clone()
    {
        return new Line(end1.clone(),end2.clone());
    }
    
    public Vector2 magnitude()
    {
        return Vector2.vecSubt(end2, end1);
    }
   
}
