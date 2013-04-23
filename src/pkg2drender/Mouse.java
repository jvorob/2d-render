/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;


import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.event.MouseInputAdapter;


/**
 *
 * @author George
 */
public class Mouse extends MouseInputAdapter{
    private ArrayList<MouseEventListener> listeners;
    private int x;
    private int y;
    private boolean l;
    private boolean r;
    
    public Mouse()
    {
        listeners = new ArrayList<MouseEventListener>();
    }
    
    public void addListener(MouseEventListener e){listeners.add(e);}    
    public void removeListener(MouseEventListener e){listeners.remove(e);}
    
    public void mouseMoved(MouseEvent e){
        informMoved(x,y,e.getX(),e.getY(),l,r);
        x=e.getX();
        y=e.getY();
    }
    
    public void mouseDragged(MouseEvent e){
        mouseMoved(e);
    }
    
    public void mousePressed(MouseEvent e){
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            l=true;
            informClicked(x,y,true,true);
        }
        if(e.getButton()==MouseEvent.BUTTON3)
        {
            r=true;
            informClicked(x,y,false,true);
        }
    }
    
    public void mouseReleased(MouseEvent e){
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            l=false;
            informClicked(x,y,true,false);
        }
        if(e.getButton()==MouseEvent.BUTTON3)
        {
            r=false;
            informClicked(x,y,false,false);
        }
    }
    
    public void informClicked(int x, int y, boolean left, boolean down)
    {
        ListIterator<MouseEventListener> i = listeners.listIterator();
         while(i.hasNext())
         {
             i.next().mouseClicked(x, y, left, down);
         }
    }
    
    public void informMoved(int oldX, int oldY, int x, int y, boolean left, boolean right)
    {
        ListIterator<MouseEventListener> i = listeners.listIterator();
         while(i.hasNext())
         {
             i.next().mouseMoved(oldX, oldY, x, y, left, right);
         }
    }
    
    public int getX()    {return x;}
    public int getY()    {return y;}
    public Point get()   {return new Point(x,y);}
    public boolean getL(){return l;}
    public boolean getR(){return r;}
}
