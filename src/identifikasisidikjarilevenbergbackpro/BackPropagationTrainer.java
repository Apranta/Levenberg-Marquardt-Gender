/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

import java.io.IOException;
import javax.swing.JOptionPane;
import ui.FormTraining;


public class BackPropagationTrainer implements Runnable
{
     private FormTraining frmTrain;
     private BackPropagation bp;
     private TrainingSamples ts;
     private NetworkSetting ns;
    
      public BackPropagationTrainer(TrainingSamples tset,FormTraining frmTrainANN,NetworkSetting ns )
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
               // System.out.println(inp.getValue(j));
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
        bp = new BackPropagation( createInputs(), createTargets(),  ns.getNNeuronHidden(), ns.getNNeuronOutput(), ns.getLearningRate());
        int epoch =1;
        double MSE=0;
        bp.initBobot();
        System.out.println("==========INISIALISASI BOBOT==========");
        do{
            bp.resetPattern();
            MSE = 0;
             while(!bp.isLastPattern())
             {   
              // System.out.println("Pattern " + pattern );
               bp.feedforward();
               bp.countError();
               bp.backPropagate();     
               
                MSE = MSE + bp.getErrorMean();
               
               bp.nextPattern();
             
             }
           // }
            MSE = MSE / ts.getTotalFingerPrint();
          
            System.out.println("************************* EPOCH Ke-" + String.valueOf(epoch) + " **********************");   
            System.out.println("MSE = " + MSE);
         
            frmTrain.setStatus(String.valueOf(MSE),String.valueOf(epoch));
            epoch++;
        }while (epoch<maxepoch && MSE>err);
        System.out.println("Berhenti Pada Epoch " + epoch);
        try {
            NetworkWriter.writeBobotBias("bobot", "bobotbiashidden_bp", bp.getBobotBiasV() );
            NetworkWriter.writeBobotBias("bobot", "bobotbiasoutput_bp", bp.getBobotBiasW() );
            NetworkWriter.writeBobotHidden("bobot", "bobothidden_bp", bp.getBobotWxi());
            NetworkWriter.writeBobotOutput("bobot", "bobotoutput_bp", bp.getBobotWyi());
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Training Selesai Dilakukan!");
        System.out.println("Bobot Berhasil Disimpan " + epoch);
    }
    
}
