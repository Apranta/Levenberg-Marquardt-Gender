/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

import java.awt.Color;
 

/**
 *
 * @author TOSHIBA
 */
public class Preprocessor {
   private int height;
   private int width;
    
    public Preprocessor(int height,int width)
    {
        this.height = height;
        this.width = width;
        
    }
    
    
    private int[] imageHistogram(FingerPrintImage fpi) 
    {
        int[] histogram = new int[256];
        int width = fpi.getWidth();
        int height = fpi.getHeight();
        int i,j,red;
        
        for(i=0; i<histogram.length; i++) histogram[i] = 0;
        
        for(i=0; i<width; i++) {
            for(j=0; j<height; j++) {
                red = fpi.getPixelOutput(j, i);
                histogram[red]++;
            }
        } 
        return histogram;
    }
    
    private int otsuThreshold(FingerPrintImage original) 
    {
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();
 
        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
 
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        } 
        return threshold; 
    }
    
    public FingerPrintImage binarize(FingerPrintImage fpimage)
    {
        Normalize(fpimage, 50, 50, 50, 50);
      
      //  GradientFinder gf = new GradientFinder();
      //  gf.FindingGradient(fpimage);
         
        int threshold = otsuThreshold(fpimage);
         
        for(int baris=0;baris<height;baris++)
        {
           for(int kolom=0;kolom<width;kolom++)
           {
                    int gray =  fpimage.getPixelOutput(baris, kolom);                                       
                    if (gray>threshold)
                    {
                       fpimage.setPixelOutput(baris, kolom, 255);
                    }else{
                       fpimage.setPixelOutput(baris,kolom,0);
                    }                         

           }
       } 
        
        return fpimage;
    }
    
    private  double CountMean(FingerPrintImage fpimage)
      {
        int mn = width*height;
        int sum=0;
        for(int i=0; i<height; i++)
         {
           for(int j=0; j<width; j++)
            {
                   sum +=  fpimage.getPixel(i, j);
            }
         }
         double mean=(double) sum/mn;
         return mean;
      }
      
    private double  CountVariant(FingerPrintImage fpimage ,double mean)
    {
         int mn = width*height;
         int sum=0;
         for(int i=0; i<height; i++)
         {
           for(int j=0; j<width; j++)
            {
                   sum += Math.pow(fpimage.getPixel(i, j) -mean,2);
            }
         }
         double variant=(double) sum/mn;
         return variant;
    }
    
    private FingerPrintImage Normalize(FingerPrintImage fPImage,int m0,int v0,double mean, double variant)
    {        
        for(int i=0; i<height; i++)
         {
           for(int j=0; j<width; j++)
            {
               int pixel =  fPImage.getPixel(i, j);
               int normpixel =0;

               if (pixel > mean) {
                   normpixel = m0 + (int) Math.sqrt((v0 * Math.pow(pixel-mean,2))/variant);
               }else{
                   normpixel = m0 - (int) Math.sqrt((v0 * Math.pow(pixel-mean,2))/variant);
               }
               if (normpixel > 255)
               {
                  normpixel=255;
               }else if (normpixel < 0) {
                  normpixel=0;
               }

          //  pixels[i][j] = normpixel;
             fPImage.setPixelOutput(i, j, normpixel);

            }
         }
         return fPImage;
}
 

}
