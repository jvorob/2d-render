/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;

import java.lang.reflect.Array;

/**
 *
 * @author George
 */
public class Timer 
{
    
    int interval;
    TimerListener[] listenerList;
    long lastTime;
    boolean running;
    
    public Timer(int x)
    {
        interval = x;        
        listenerList = new TimerListener[0];
        running = false;
    }
    
    public void start()
    {
        running = true;
        lastTime=System.currentTimeMillis();
        while (running)
        {
            if(System.currentTimeMillis()-lastTime > interval)
            {
                update();
                lastTime+=interval;
            }
        }
    }
    
    public void stop()
    {
        running = false;
    }
    
    public void addListener(TimerListener tl)
    {
        TimerListener[] tempList = new TimerListener[Array.getLength(listenerList)+1];
        
        for(int i=0;i<Array.getLength(listenerList);i++)
        {
            tempList[i]=listenerList[i];
        }
        tempList[Array.getLength(tempList)-1]=tl;
        listenerList = tempList;
    }
    
    public void update()
    {
        for(int i = 0;i<Array.getLength(listenerList);i++)
        {
            listenerList[i].timerEvent();
        }
    }
    
}
