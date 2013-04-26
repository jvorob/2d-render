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
    ArrayList<Vector2> points;
    
    static final float fov = 2f;
    static final float turnSpeed = 2f;
    
    Vector2 pos;
    float heading;
    public static void main(String[] args) 
    {
        Main m = new Main();
    }
    
    public Main()
    {
        points = new ArrayList<Vector2>();
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
        headingVect.add(pos);
        Graphics g = screen.buffer.getGraphics();
        g.setColor(Color.black);
        g.drawOval((int)pos.x - 5, (int)pos.y - 5, 10, 10);
        g.drawLine((int)pos.x, (int)pos.y, (int)headingVect.x, (int)headingVect.y);
        
        Iterator<Vector2> i = points.iterator();
        while(i.hasNext())
        {
            Vector2 n = i.next();
            g.drawOval((int)n.x - 1, (int)n.y - 1, 3, 3);
        }
        /*float a = Angles.getAngle(Vector2.vecSubt(m, new Vector2(250, 250)));
        
        g.drawOval(249, 249, 2, 2);
        g.drawLine(250, 250, 400, 250);
        g.drawLine(250,250,(int)m.x,500 -(int)m.y);
        g.drawArc(240, 240, 20, 20, 0, (int)(Angles.pik * a));
        g.drawString(String.valueOf(m.x - 250) + " " + String.valueOf(m.y - 250) +
                "; " +
                String.valueOf(Angles.pik * a)
                , 0, 25);*/
        screen.flushBuffer();
        
        //Rendering code |
        sview.clear();
        g = sview.buffer.getGraphics();
        g.setColor(Color.red);
        i = points.iterator();
        while(i.hasNext())
        {
            float x = mapPoint(i.next(), pos, heading);
            int h = sview.c.getHeight();
            int w = sview.c.getWidth();
            g.drawLine((int)((x * w) + w)/2, 0, (int)((x * w) + w)/2, h);
        }
        sview.flushBuffer();
    }

    @Override
    public boolean mouseClicked(int x, int y, boolean left, boolean down) {
        if(down && !left)
        {
            points.add(new Vector2(x,y));
        }
        return false;
    }
    
    public float mapPoint(Vector2 point, Vector2 viewPos, float viewHeading)
    {
        Vector2 n = Vector2.vecSubt(point,viewPos);
            float angle = Angles.fixAngle(viewHeading - Angles.getAngle(n));
            if(angle < 3.14159 / 2 || angle > 3.14159 * 3 / 2)
            {
                float x = -1 * (float)Math.tan(angle);
                if (Math.abs(x) <= fov)
                {
                    x/=fov;
                    return x;
                }
            }
        return -2;//If point is not in view
    }
    
    @Override
    public boolean mouseMoved(int oldX, int oldY, int x, int y, boolean left, boolean right) {return false;}
}
