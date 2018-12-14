/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

 
import java.util.List;
 

/**
 *
 * @author TOSHIBA
 */
public class FeatureExtractor 
{
    FingerPrintImage fpi[] ;
    Input inputs[];

    public FeatureExtractor() {
    }
    
    
    
    public FeatureExtractor(FingerPrintImage fpi[]) 
    {
        this.fpi = fpi;
        this.inputs = new Input[fpi.length];
    }
    
     private int countRidgeThickness(FingerPrintImage fpiblock)
    {
        int result=0;
      
        for(int h=0;h<fpiblock.getHeight();h++)
        {
            for(int w=0;w<fpiblock.getWidth();w++)
            {
                if (fpiblock.getPixelOutput(h, w)==0) 
                {
                  result++;
                }
            }
        }
        return result;
    }
     
     private int countRidgeDensity(FingerPrintImage fpiblock) 
     {
         int dense=0;
         int center = fpiblock.getWidth()/2;
         int hmax = (int)(fpiblock.getHeight() * 0.3);
         for(int h=0;h<hmax;h++)
         {
            if (fpiblock.getPixelOutput(h,center)==0) 
                {
                    dense++;
                }
         }
         
         return dense;
         
     }
    
     private int countValleyThickness(FingerPrintImage fpiblock)
    {
        int result=0;
        
        for(int h=0;h<fpiblock.getHeight();h++)
        {
            for(int w=0;w<fpiblock.getWidth();w++)
            {
                if (fpiblock.getPixelOutput(h, w)==255) 
                {
                  result++;
                }
            }
        }
        return result;
    }

    
    public Input extract(FingerPrintImage fpi)
    {
         Input inp = new Input(3);
         // Ketebalan Ridge Dan Valley
         BlockProcessor bp = new BlockProcessor();
         bp.CreateImageBlock(fpi, 20);
         FingerPrintImage fpiblocks[] = bp.GetImageBlock();
         double sum=0.0;double sumrt=0.0;
         for(int i=0;i<fpiblocks.length;i++)
         {
           double rt = countRidgeThickness(fpiblocks[i]);
           double vt = countValleyThickness(fpiblocks[i]);
           double rtvtr = (double) rt/vt;
           sumrt += rt;
           if (!Double.isNaN(rtvtr) && vt!=0) 
           {
              
             sum +=rtvtr;
           }
         //  System.out.println(sum);
         }
         double avg = sum / fpiblocks.length;
         sumrt = sumrt /fpiblocks.length;
         int rd = countRidgeDensity(fpi);
         double max = 100;// Math.max(Math.max(avg, sumrt),rd);
         //Normalisasi
         avg = avg / max;
         sumrt = sumrt / max;
         double drd = rd / max;
         
         inp.setValue(0,avg );  
         inp.setValue(1, sumrt);  
         inp.setValue(2, drd);  
         return inp;
    }
    
    public void extractFeature() 
    {    
        for(int i=0;i<fpi.length;i++)
        {  
           Preprocessor prep = new Preprocessor(fpi[i].getHeight(), fpi[i].getWidth());
           fpi[i] = prep.binarize(fpi[i]);
           
            inputs[i] = extract(fpi[i]);   
            inputs[i].setTarget(fpi[i].getTarget());
        }
    }

    public Input[] getInputs() {
        return inputs;
    }
    
    
    
    
}
