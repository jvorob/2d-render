/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

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
    
    
    static final float fovangle = 35 * 3.14159f / 180;//actually half the fov
    static final float fov = (float)Math.tan(fovangle);
    static final float turnSpeed = 2f;
    
    static final boolean DEBUG = true;
    
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
        screen.clear();
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
        if(keys.getKey(KeyEvent.VK_SPACE))
            clipLine(lines.get(0), pos, heading);
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
        if(DEBUG)
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
        }
        screen.flushBuffer();
        
        //Rendering code |
        sview.clear();
        g = sview.buffer.getGraphics();
        g.setColor(Color.red);
        i = lines.iterator();
        while(i.hasNext())
        {
            Line l = i.next();
            float a = mapPoint(l.end1, pos, heading);
            float b = mapPoint(l.end2, pos, heading);
            int h = sview.c.getHeight();
            int w = sview.c.getWidth();
            g.fillRect((int)((a * w) + w)/2, 0, (int)Math.abs(((b * w) + w)/2 - ((a * w) + w)/2), h);
        }
        sview.flushBuffer();
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
        float a = mapPoint(line.end1,viewPos, viewHeading);
        float b = mapPoint(line.end2,viewPos, viewHeading);
        
        //Case: Entirely within fov, all good
        if(Math.abs(a) < 2 && Math.abs(b) < 2)
        {
            Misc.prln("good!");
            return line;
        }
        
        //Case: Entirely outside fov
        if(true)
        {
            ...
            float angle1 = relAngle(line.end1, viewPos, viewHeading);
        }
        Misc.prln("bad :(");
        return null;
    }
    
    public float relAngle(Vector2 point, Vector2 viewPos, float viewHeading)
    {
        Vector2 n = Vector2.vecSubt(point,viewPos);
        return Angles.fixAngle(viewHeading - Angles.getAngle(n));
    }
    public float mapPoint(Vector2 point, Vector2 viewPos, float viewHeading)
    {
        float angle = relAngle(point, viewPos, viewHeading);
        if(angle < 3.14159 / 2 || angle > 3.14159 * 3 / 2)
        {
            float x = -1 * (float)Math.tan(angle);
            if (Math.abs(x) <= fov)
            {
                x/=fov;
                return x;
            }
        }
        return angle < 3.14159?-2:2;//If point is not in view
    }
    
    @Override
    public boolean mouseMoved(int oldX, int oldY, int x, int y, boolean left, boolean right) {return false;}
}
