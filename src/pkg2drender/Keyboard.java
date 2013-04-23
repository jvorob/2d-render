


package pkg2drender;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author George
 */
public class Keyboard extends KeyAdapter {
    private boolean[] keys;
    private ArrayList<KeyEventListener> listeners;
    
    public Keyboard(){
        listeners = new ArrayList<KeyEventListener>();
        keys=new boolean[66000];
    }
    
    public void addListener(KeyEventListener e){listeners.add(e);}    
    public void removeListener(KeyEventListener e){listeners.remove(e);}
    
    public void keyPressed(KeyEvent e){
        keys[e.getKeyCode()]=true;
        inform(e.getKeyCode(), true);
    }
    
    public void keyReleased(KeyEvent e){
        keys[e.getKeyCode()]=false;
        inform(e.getKeyCode(), false);
    }
    
    public boolean getKey(int k){
        return keys[k];
    }
    
    private void inform(int index, boolean down)
    {
         ListIterator<KeyEventListener> i = listeners.listIterator();
         while(i.hasNext())
         {
             i.next().KeyChange(index, down);
         }
    }
}
