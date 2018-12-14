/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

 
 
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import javax.swing.ImageIcon;
 


public class PixelsReader 
{
    FingerPrintImage img;
    public PixelsReader() {
        img = new FingerPrintImage();
    }
    
    public void readPixelsFrom(ImageIcon imgic)
    {
      img = new FingerPrintImage(imgic.getIconWidth(), imgic.getIconHeight()); 
      PixelGrabber pxlgrabber = new PixelGrabber(imgic.getImage(),0,0,img.getWidth(), img.getHeight(),false);
      pxlgrabber.startGrabbing();
      int pixels[];
      int pixelasli[][];
       
      try{
        if(pxlgrabber.grabPixels())
        {
            pixels = (int[])pxlgrabber.getPixels();
           
            
            BufferedImage image = new BufferedImage(imgic.getIconWidth() , imgic.getIconHeight() , BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, imgic.getIconWidth() , imgic.getIconHeight() ,pixels, 0, imgic.getIconWidth());  
            
            ImageIcon imgIcon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_DEFAULT));          
            img = new FingerPrintImage(imgIcon.getIconWidth(), imgIcon.getIconHeight());             
            
            pxlgrabber = new PixelGrabber(imgIcon.getImage(),0,0,img.getWidth(), img.getHeight(),false);      
            
            pxlgrabber.startGrabbing();           
            
          if(pxlgrabber.grabPixels())
           {
            pixels = (int[])pxlgrabber.getPixels();            
              pixelasli  = new int [img.getHeight()][img.getWidth()];
              int wpx = 0;
              int hpx = 0;
              for(int i =0;i<pixels.length;i++)
              {    
                 int pixel = pixels[i]; 
                 Color c = new Color(pixel);
                 int merah = c.getRed();
                 int hijau = c.getGreen();
                 int biru = c.getBlue();
                 int gray = (merah+hijau+biru)/3 ;           
                 
                 if (gray>128)
                 {
                    // gray =255;
                 }else{
                     // gray=0;
                 } 
                 
                 pixelasli[hpx][wpx] = gray; 
                 img.setPixelOutput(hpx, wpx, gray);
             //   System.out.println(gray);
                 wpx++;
                 if (wpx==img.getWidth())
                 {
                    wpx=0;
                    hpx++;
                 }
               }          
               img.setPixelAsli(pixelasli);
            }
           
            System.out.println("Sukses");
        }
    }catch(InterruptedException ex){}
    }

    public FingerPrintImage getFPImage() {
        return img;
    }   
    
}
