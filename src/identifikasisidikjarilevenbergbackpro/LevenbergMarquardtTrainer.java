/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

import java.io.IOException;
import javax.swing.JOptionPane;
import ui.FormTraining;

 
public class LevenbergMarquardtTrainer implements Runnable
{
    private  FormTraining frmTrain;
    private LevenbergMarquardt lm;
    private TrainingSamples ts;
    private NetworkSetting ns;
    
    public LevenbergMarquardtTrainer(TrainingSamples tset,FormTraining frmTrainANN,NetworkSetting ns )
    {
        this.ts = tset;
        this.frmTrain = frmTrainANN;
        this.ns = ns;
    }   
     
    
    
    private double[][] createInputs()
    {         
        
        double[][] temp = new double[ts.getTotalFingerPrint()][3];
        //System.out.println("Panjang Fitur:" + ts.getFingerPrintImage(0).toOneDimensionalPixelReal().length);   
        System.out.println("Ekstraksi Fitur Dimulai");        
        for(int i=0;i<temp.length;i++)
        { 
            FingerPrintImage fpimg = ts.getFingerPrintImage(i);
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
    
     private double[][] createTargets()
     {
        double t[][] = new double[ts.getTotalFingerPrint()][1];
        for(int i=0;i<ts.getTotalFingerPrint();i++ )
        {           
            int target =ts.getTarget(i);
            t[i] = toTargetUnits(target, 1);         
        }
        return t;
    }
       
    private double[] toTargetUnits(int target,int nOutput)
    {
        double unittarget [] = new double[nOutput]; 
        
         String biner = Integer.toBinaryString(target);
         
       // String bintarget 
         int akhir = nOutput-biner.length();
         for(int i=0;i<akhir;i++)
         {
             unittarget[i] = 0;
         }
         
         for(int i=akhir;i<akhir+biner.length();i++)
         {
              unittarget[i] = Integer.parseInt(biner.substring(i-akhir,(i-akhir)+1));                                    //biner.toCharArray()[i-akhir];
         }
        
        return unittarget;
        
    }
  
     public void train()
    { 
        new Thread(this).start(); 
       
    }

    @Override
    public void run() 
    {       
        double err = ns.getErrTolerance();
        double maxepoch = ns.getMaxEpoch();     
        
        int epoch = 0;
        double MSE =1.0;
        
        double[][] input = createInputs();
        double[][] target = createTargets();
       
      
       
       LevenbergMarquardt lm = new LevenbergMarquardt(input, target, ns.getNNeuronHidden(), ns.getNNeuronOutput(), ns.getLearningRate());
       lm.initBobot();
       
        do{
            lm.resetPattern();
            MSE = 1;
             while(!lm.isLastPattern())
             {   
              // System.out.println("Pattern " + pattern );
               lm.feedforward();
               lm.countError();
               lm.backPropagate();     
               
               MSE = MSE + lm.getErrorMean();
               
               lm.nextPattern();
             
             }
           // }
           // MSE = MSE / ts.getTotalFingerPrint();
            MSE = MSE / input.length;
          
           
         
           // frmTrain.setStatus(String.valueOf(MSE),String.valueOf(epoch));
            epoch++;
        }while (epoch<5);
        
       
        
        
        System.out.println("Masuk Levenber-Marquart ----------------------------");
        
        epoch = 0;
        
        while(MSE > err) 
        {			
	    if ( epoch >= maxepoch ) break;			
	
            MSE = lm.RunEpoch(input, target);            
            System.out.println("MSE EPOCH -" + String.valueOf(epoch+1)  + ":" + MSE);            
            epoch++;	
            
            frmTrain.setStatus(String.valueOf(MSE),String.valueOf(epoch));
			
	}
		
       System.out.println("Number of epochs: "+epoch);
        
        
        
        
        try {
            NetworkWriter.writeBobotBias("bobot", "bobotbiashidden_lm", lm.getBobotBiasV() );
            NetworkWriter.writeBobotBias("bobot", "bobotbiasoutput_lm", lm.getBobotBiasW() );
            NetworkWriter.writeBobotHidden("bobot", "bobothidden_lm", lm.getBobotWxi());
            NetworkWriter.writeBobotOutput("bobot", "bobotoutput_lm", lm.getBobotWyi());
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Training Selesai Dilakukan!");
        System.out.println("Bobot Berhasil Disimpan " + epoch);
        
    }
}
