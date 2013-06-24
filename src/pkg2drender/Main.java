/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.event.AncestorEvent;

/**
 *
 * @author George
 */
public class Main implements TimerListener, MouseEventListener {

    Screen screen,sview;
    Mouse mouse;
    Keyboard keys;
    Timer timer;
    //ArrayList<Vector2> points;
    ArrayList<Line> lines;
    Vector2 lastMouse;
    
    static final float pi = 3.14159f;
    static final float fovangle = 35 * pi / 180;//actually half the fov
    static final float fov = (float)Math.tan(fovangle);
    static final float turnSpeed = 2f;
    
    static final boolean DEBUG = false;
    static final boolean DISPLAY = true;
    
    Vector2 pos;
    float heading;
    public static void main(String[] args) 
    {
        Main m = new Main();
    }
    
    public Main()
    {
        //points = new ArrayList<Vector2>();
        lines = new ArrayList<Line>();
        pos = new Vector2(250, 250);
        screen = new Screen(500, 500);
        sview = new Screen(500,20);
        timer = new Timer(50);
        timer.addListener(this);
        keys = new Keyboard();
        mouse = new Mouse();
        screen.c.addKeyListener(keys);
        screen.c.addMouseListener(mouse);
        screen.c.addMouseMotionListener(mouse);
        mouse.addListener(this);
        timer.start();
    }
    
    public void timerEvent()
    {
        Vector2 m = Vector2.fromPoint(mouse.get());
        //heading = Angles.getAngle(Vector2.vecSubt(m, pos));
        if(keys.getKey(KeyEvent.VK_Q))
            heading -= turnSpeed * timer.interval /1000;
        if(keys.getKey(KeyEvent.VK_E))
            heading += turnSpeed * timer.interval /1000;
        heading = Angles.fixAngle(heading);
        Vector2 headingVect = new Vector2(10, 0);
        headingVect.setAngle(heading);
        if(keys.getKey(KeyEvent.VK_W))
            pos.add(Vector2.vecMult(4 * (float)timer.interval/1000,headingVect));
        if(keys.getKey(KeyEvent.VK_S))
            pos.add(Vector2.vecMult(-4 * (float)timer.interval/1000,headingVect));
        if(keys.getKey(KeyEvent.VK_SPACE))
            ;//can be used for miscellaneous debuggery
        headingVect.add(pos);
        Graphics g = screen.buffer.getGraphics();
        g.setColor(Color.black);
        g.drawOval((int)pos.x - 5, (int)pos.y - 5, 10, 10);
        g.drawLine((int)pos.x, (int)pos.y, (int)headingVect.x, (int)headingVect.y);
        
        Iterator<Line> i = lines.iterator();
        while(i.hasNext())
        {
            Line l = i.next();
            g.drawLine((int)l.end1.x, (int)l.end1.y, (int)l.end2.x, (int)l.end2.y);
        }
        
        //#DEBUG, draws the two lines bounding your fov
        if(DEBUG || DISPLAY)
        {
            int x,y;
            x = (int)pos.x;
            y = (int)pos.y;
            g.drawLine(x,y,
                    x + (int)(200 *Math.cos( heading + fovangle )),
                    y + (int)(200* Math.sin( heading + fovangle ))
                    );
            g.drawLine(x,y,
                    x + (int)(200 *Math.cos( heading - fovangle )),
                    y + (int)(200* Math.sin( heading - fovangle ))
                    );
            if(lines.size() == 1)
            {
                //lines.get(0).rotateAbout(pos, 0.1f);
                /*Line line = lines.get(0);
                float angle1 = relAngle(line.end1, pos, heading);
                float angle2 = relAngle(line.end2, pos, heading);
                //Misc.prln(String.valueOf(angle1) + ' ' + String.valueOf(angle2));
                float diff = Angles.fixAngle(angle1 - angle2);
                if(diff > pi)
                    diff -= 2 * pi;
                float avgangle = Angles.fixAngle(diff / 2+ angle2);
                drawRay(avgangle + heading, 300);*/
            }
        }
        screen.flushBuffer();
        screen.clear();
        
        //Rendering code |
        g = sview.buffer.getGraphics();
        g.setColor(Color.red);
        i = lines.iterator();
        while(i.hasNext())
        {
            Line l = i.next();
            Line temp = clipLine(l, pos, heading);
            if(temp != null)
            {
                float a = mapPoint(temp.end1, pos, heading);
                float b = mapPoint(temp.end2, pos, heading);
                if(a > b)
                {
                    float swaptemp = a;
                    a = b;
                    b = swaptemp;
                }
                int h = sview.c.getHeight();
                int w = sview.c.getWidth();
                g.fillRect((int)((a * w) + w)/2, 0, (int)Math.abs(((b * w) + w)/2 - ((a * w) + w)/2), h);
            }
        }
        sview.flushBuffer();
        sview.clear();
    }

    @Override
    public boolean mouseClicked(int x, int y, boolean left, boolean down) {
        if(down && !left)
        {
            lastMouse = new Vector2(x,y);
            //points.add(new Vector2(x,y));
        }
        else if(!down && !left)
        {
            if(lastMouse != null)
            {
                lines.add(new Line(lastMouse,new Vector2(x,y)));
            }
            lastMouse = null;
        }
        return false;
    }
    
    public Line clipLine(Line line, Vector2 viewPos, float viewHeading)
    {
        //test for different cases
        float a = relAngle(line.end1,viewPos,viewHeading);// viewPos, viewHeading)mapPoint(line.end1,viewPos, viewHeading);
        float b = relAngle(line.end2,viewPos,viewHeading);//mapPoint(line.end2,viewPos, viewHeading);
        boolean aleft = Angles.fixAngle(a) > pi;//reversed, left(ccw) is clockwise because coordinates are from topleft
        boolean bleft = Angles.fixAngle(b) > pi;
        boolean ain = Angles.abs(a) < fovangle;
        boolean bin = Angles.abs(b) < fovangle;
        float angle1 = relAngle(line.end1, viewPos, viewHeading);
        float angle2 = relAngle(line.end2, viewPos, viewHeading);
        float diff = Angles.fixAngle(angle1 - angle2);
        if(diff > pi)
            diff -= 2 * pi;
        float avgangle = Angles.fixAngle(diff / 2+ angle2);
        boolean lineInFront = avgangle < pi / 2 || avgangle > Angles.fixAngle((float)(-pi / 2));
        //Misc.prln(ain);
        //Misc.prln(bin);
        //Misc.prln("---");
        Line temp = line.clone();
        //Case: Entirely within fov, all good
        if(ain && bin)
        {
            if(DEBUG)Misc.prln("inside");
        }
        
        //testing case: if a is outside and b is in
        else if(!ain && bin)
        {
            if(aleft)
            {
                temp.end1 = intersectWithFov(true, line);
            }
            else
            {
                temp.end1 = intersectWithFov(false, line);
            }
        }
        else if(ain && !bin)
        {
            if(bleft)
            {
                temp.end2 = intersectWithFov(true, line);
            }
            else
            {
                temp.end2 = intersectWithFov(false, line);
            }
        }
        //Case: Both outside fov on opposite sides
        else if(!ain && !bin && ((bleft && !aleft) || (!bleft && aleft)) && lineInFront)
        {
            if(DEBUG)Misc.prln("infront but fully outside");
            if(aleft)
            {
                temp.end1 = intersectWithFov(true, line);
                temp.end2 = intersectWithFov(false, line);
            }
            else
            {
                temp.end1 = intersectWithFov(false, line);
                temp.end2 = intersectWithFov(true, line);
            }
        }
        else
        {
            if(DEBUG)Misc.prln("outside");
            return null;
        }
        if(DEBUG || DISPLAY)drawLine(temp);
        return temp;//after clipping has finished
    }
    
    public float xintercept(Line l)//given a line, returns its x-interept
    {
        Vector2 mag = l.magnitude();
        if(mag.x == 0)
        {
            return l.end1.x;
        }
        else
        {
            //drawLine(Line.fromVector(mag,new Vector2(0,0)));
            float slope = -1 * mag.y / mag.x;
            //Misc.prln( l.end1.x + (l.end1.y / slope));
            return l.end1.x + (l.end1.y / slope);
        }
    }
    
    public Vector2 intersectWithFov(boolean left, Line l)
    {
        float angle = heading + (fovangle * (left?-1:1));
        angle = Angles.fixAngle(angle);
        Line temp = l.clone();
        temp.rotateAbout(pos,-1 * angle);
        temp.move(Vector2.inverse(pos));
        float dist = xintercept(temp);//dist is the distance along the fov edge from the viewer
        Vector2 inter = Vector2.vecAdd(pos,Vector2.fromAngle(heading + (fovangle * (left?-1:1)), dist));
        //Misc.prln(Vector2.fromAngle(heading + (fovangle * (left?-1:1)), dist).length());
        if(DEBUG || DISPLAY)drawCircle(inter);
        return inter;
    }
    
    public float relAngle(Vector2 point, Vector2 viewPos, float viewHeading)
    {
        Vector2 n = Vector2.vecSubt(point,viewPos);
        return Angles.fixAngle(Angles.getAngle(n) - viewHeading);
    }
    public float mapPoint(Vector2 point, Vector2 viewPos, float viewHeading)
    {
        float angle = relAngle(point, viewPos, viewHeading);
        if(angle < fovangle || angle > pi + pi - fovangle)
        {
            float x = (float)Math.tan(angle);
            if (Math.abs(x) <= fov)
            {
                x/=fov;
                return x;
            }
        }
        return angle < pi?1:-1;//If point is not in view
    }
    
    public void drawRay(float angle, int l)//draws a ray of length l from the player 
            //                              at the specifided angle relative TO +X
    {
        Graphics g = screen.buffer.getGraphics();
        g.setColor(Color.red);
        g.drawLine((int)pos.x,(int)pos.y,
                    (int)pos.x + (int)(l * Math.cos(angle)),
                    (int)pos.y + (int)(l * Math.sin(angle))
                    );
    }
    
    public void drawLine(Line l)//draws a ray of length l from the player 
            //                              at the specifided angle relative TO +X
    {
        Graphics2D g = screen.buffer.createGraphics();
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(3));
        g.drawLine((int)l.end1.x,(int)l.end1.y,
                    (int)l.end2.x,(int)l.end2.y);
    }
    public void drawCircle(Vector2 v)//draws a circle around v
    {
        Graphics g = screen.buffer.getGraphics();
        g.setColor(Color.red);
        g.drawOval((int)(v.x-5),(int)(v.y-5),10,10);
    }
    @Override
    public boolean mouseMoved(int oldX, int oldY, int x, int y, boolean left, boolean right) {return false;}
}
