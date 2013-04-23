/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;

import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George
 */
public class Screen implements ImageObserver
{
    public Frame f;
    public Canvas c;
    public BufferedImage buffer;
    
    public Screen(int x, int y)
    {
        f = new Frame("Hello");
        c = new Canvas();
        c.setSize(x, y);
        f.add(c);
        /*f.setCursor(
                Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB), 
                new Point(), 
                null));*/
        buffer = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
        f.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {/*f.dispose();*/System.exit(0);}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });
        f.setSize(x, y);
        f.validate();
        f.setVisible(true);
        Insets i = f.getInsets();
        f.setSize(x + i.left + i.right, y + i.top + i.bottom);
        c.setVisible(true);
    }
    
    public void clear()
    {
        Graphics g = buffer.getGraphics();
        g.fillRect(0, 0,buffer.getWidth(), buffer.getHeight());
    }
    
    public void draw(Image b)
    {
        try{
        Graphics g = c.getGraphics();
        
        g.drawImage(b,0,0,this);
        }
        catch (Exception e){System.out.println("screendrawException");}
    }

    public void flushBuffer()
    {
        try{
        Graphics g = c.getGraphics();
        
        g.drawImage(buffer,0,0,this);
        }
        catch (Exception e){System.out.println("screendrawException");}
    }
    
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }
}
