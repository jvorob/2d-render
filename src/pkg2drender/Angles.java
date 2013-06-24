/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;


/**
 *
 * @author George
 */
public class Angles {
    static final float pi = 3.141592f;
    static final float pik = 180/pi;
    static float getAngle(float relx, float rely, float dist)//Angle CCW from +X
    {
        if(rely == 0)
        {
            if(relx == 0)
                System.out.println("Segment has length 0,0");
            else if(relx > 0)
                return (float)0;
            else
                return pi;
        }
        if(Math.abs(relx/rely)>1)
        {
            if(relx > 0)
                return (float)Math.asin((float)(rely/dist));
            else
                return pi - (float)Math.asin((float)(rely/dist));
        }
        else
        {
            if(rely > 0)
                return (float)Math.acos((float)(relx/dist));
            else
                return 2 * pi - (float)Math.acos((float)(relx/dist));
        }
    }
    
    public static float getAngle(float relx, float rely)
    {
        return fixAngle(getAngle(relx,rely,getDist(relx,rely)));
    }
    
    public static float getAngle(Vector2 v)
    {
        return fixAngle(getAngle(v.x,v.y,v.length()));
    }
    
    public static float fixAngle(float angle)//moves angle into 0-2pi range
    {
        int x = (int)Math.floor(angle / 2 / pi);
        x = Math.abs(x);
        if(angle < 0)
            return angle + x * 2 * pi;
        else
            return angle - x * 2 * pi;
    }
    
    public static float getDist(float relx, float rely)
    {
        return (float)Math.sqrt(relx * relx + rely * rely);
    }
    
    public static float abs(float angle)//returns the 'absolute value' of an angle
    {
        return Angles.fixAngle(angle>pi?pi + pi-angle:angle);
    }
}
