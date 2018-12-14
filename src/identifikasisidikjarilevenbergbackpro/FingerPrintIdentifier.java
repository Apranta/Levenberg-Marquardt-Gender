/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

import java.io.FileNotFoundException;
import java.io.IOException;
import utils.Util;


public class FingerPrintIdentifier 
{
      public static int BACKPROPAGATION_WEIGHTS = 0;
      public static int LEVENBERGMARQUARDT_WEIGHTS = 1;
   
      private BackPropagation bp;
      private FingerPrintImage fpimageuji;
      private NetworkSetting ns;
      
      private int typeofweights;
     
    
      public FingerPrintIdentifier(FingerPrintImage fpimageuji,int typeofweights )
      {       
        this.fpimageuji = fpimageuji;   
        this.typeofweights = typeofweights;
        initSetting();
      }   
    
     
    
    
    
    private double[][] createInputs()
    {         
        
       double[][] temp = new double[1][3];
        //System.out.println("Panjang Fitur:" + ts.getFingerPrintImage(0).toOneDimensionalPixelReal().length);   
        System.out.println("Ekstraksi Fitur Dimulai");        
        for(int i=0;i<temp.length;i++)
        { 
            FingerPrintImage fpimg = fpimageuji;
            Preprocessor prep = new Preprocessor(fpimg.getHeight(), fpimg.getWidth());
            fpimg = prep.binarize(fpimg);

            FeatureExtractor fe = new  FeatureExtractor();
            Input inp =fe.extract(fpimg);
            
            for(int j=0;j<inp.getInputLength();j++)
            {
                System.out.println(inp.getValue(j));
                temp[i][j] = inp.getValue(j);
            }
           
        }       
        System.out.println("Ekstraksi Fitur Selesai ....");
        return temp;
    }
    
    
     private void initSetting()
    {
         if (Util.checkFile("setting", "network", "txt")){
            try {
                ns = Util.readNetworkSetting("setting", "network");
             
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
   public String GetID()
    {
        String id= "";
        BackPropagation bp = null;     
        int nhidden = ns.getNNeuronHidden();
        int nOutput = ns.getNNeuronOutput();
        System.out.println("Jumlah Neuron Hidden :" + nhidden);
        bp = new BackPropagation(createInputs(),  nhidden, 1,typeofweights);        
        bp.feedforward();
        id = bp.compete();
        System.out.println(id);
        return id;     
    }
  
}
