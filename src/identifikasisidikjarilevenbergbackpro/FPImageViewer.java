/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

 
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
 


public class FPImageViewer {
    FingerPrintImage myimg;
    JLabel jblViewer;
    public FPImageViewer() 
    {
        
    }
    
    public void setImage(FingerPrintImage myimg)
    {
        this.myimg = myimg;
    }
    
    public void setViewer(JLabel lblviewer)
    {
        this.jblViewer = lblviewer;
                
    }
    
    public void viewImageDefault()
    {
        int pix[] =  myimg.toOneDimensionalPixelReal();
        int w = myimg.getWidth();
        int h = myimg.getHeight();        
        BufferedImage image = new BufferedImage(w , h , BufferedImage.TYPE_INT_RGB);     
        image.setRGB(0, 0, w , h ,pix, 0, w);
        jblViewer.setText("");     
        jblViewer.setIcon(new ImageIcon(image.getScaledInstance(jblViewer.getWidth(), jblViewer.getHeight(), Image.SCALE_DEFAULT)));
    }
    
     public void viewImageOutput()
    {
        int pix[] =  myimg.toOneDimensionalPixelOutput();
        int w = myimg.getWidth();
        int h = myimg.getHeight();        
        BufferedImage image = new BufferedImage(w , h , BufferedImage.TYPE_INT_RGB);     
         
        image.setRGB(0, 0, w , h ,pix, 0, w);
        jblViewer.setText("");     
        jblViewer.setIcon(new ImageIcon(image.getScaledInstance(jblViewer.getWidth(), jblViewer.getHeight(), Image.SCALE_DEFAULT)));
    }
    
}
