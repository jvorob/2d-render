/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2drender;

import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 *
 * @author George
 */
public class StaticImageObserver_1 implements ImageObserver {
    
    
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }
    
}   
