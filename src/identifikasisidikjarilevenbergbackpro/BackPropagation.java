/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

 
public class BackPropagation
{   
    protected int Xinput = 0; 
    protected int Yhidden = 0;
    protected int Zoutput = 0;
    
    protected double InputLayer[][]; 
    protected double HiddenLayer[]; 
    protected double OutputLayer[];// 
    protected double    t[][];//           = new int [26][5];    
    
    protected double BobotWxi[][];  
    protected double BobotBiasV[];
    protected double BobotWyi[][];  
    protected double BobotBiasW[];
    
    protected double DeltaWxi[][];  
    protected double DeltaBiasV[];   
    protected double DeltaWyi[][]; 
    protected double DeltaBiasW[]; 
    
    
    private Random random = new Random();
    
    private double ErrorOutputLayer[];  
    private double ErrorHiddenLayer[];  
    private double ErrorFactor[];      
    private double Error[];            
    
    //private double MS[]               = new double[5];
    
    private double DK[] = new double[Zoutput];
    private double DK_net[] = new double[Yhidden];    
    private double DS[] = new double[Yhidden];
            
    protected double errormean;
    protected double lambda;
    private long epoch;
    private int currentpattern;
 

    public BackPropagation(double InputLayer[][],double t[][],int nHidden,int nOutput,double learningrate) 
    {
        this.InputLayer = InputLayer;
        
        HiddenLayer = new double[nHidden];
        OutputLayer = new double[nOutput];
        Xinput = InputLayer[0].length;
       // InputLayer = new double[4][5];
        Yhidden = nHidden;
        Zoutput = nOutput;
       
        BobotWxi = new double[Xinput][Yhidden];
        BobotWyi = new double[Yhidden][Zoutput];  
        BobotBiasV = new double[Yhidden];
        BobotBiasW = new double[Zoutput];
        DeltaWxi  = new double[Xinput][Yhidden];
        DeltaWyi = new double[Yhidden][Zoutput];
        DeltaBiasV = new double[Yhidden];
        DeltaBiasW = new double[Zoutput];
        
        ErrorOutputLayer = new double[Zoutput];
        ErrorHiddenLayer = new double[Yhidden];
        ErrorFactor      = new double[Yhidden];    
        Error            = new double[Zoutput];
        
        DK  = new double[Zoutput];
        DK_net  = new double[Yhidden];    
        DS  = new double[Yhidden];
        
        this.t =t;
                
        lambda = learningrate;
        currentpattern=0;
    }
    
    
    
    
      public BackPropagation(double InputLayer[][],int nHidden,int nOutput,int typeofweights) 
    {
        this.InputLayer = InputLayer;
        HiddenLayer = new double[nHidden];
        OutputLayer = new double[nOutput];
        Xinput = InputLayer[0].length;
        Yhidden = nHidden;
        Zoutput = nOutput;
       
        BobotWxi = new double[Xinput][Yhidden];
        BobotWyi = new double[Yhidden][Zoutput];    
        DeltaWxi  = new double[Xinput][Yhidden];
        DeltaWyi = new double[Yhidden][Zoutput];
        BobotBiasV = new double[Yhidden];
        BobotBiasW = new double[Zoutput];
        ErrorOutputLayer = new double[Zoutput];
        ErrorHiddenLayer = new double[Yhidden];
        ErrorFactor      = new double[Yhidden];    
        Error            = new double[Zoutput];
        
        DK  = new double[Zoutput];
        DK_net  = new double[Yhidden];    
        DS  = new double[Yhidden];
        
        loadBobot(typeofweights);
                
      
        currentpattern=0;
    }

      
    private void loadBobot(int typeofweights)
    {
        int i,j;
        
        try
        {
            String suffix="";
            if (typeofweights==FingerPrintIdentifier.BACKPROPAGATION_WEIGHTS)
            {
                suffix = "bp";
            }else if (typeofweights==FingerPrintIdentifier.LEVENBERGMARQUARDT_WEIGHTS)
            {
                suffix = "lm";
            }
            
            
            DataInputStream dist1 = new DataInputStream(new BufferedInputStream(new FileInputStream("bobot/bobothidden_" + suffix + ".txt")));
            DataInputStream dist2 = new DataInputStream(new BufferedInputStream(new FileInputStream("bobot/bobotoutput_" + suffix + ".txt")));
            DataInputStream dist3 = new DataInputStream(new BufferedInputStream(new FileInputStream("bobot/bobotbiashidden_" + suffix + ".txt")));
            DataInputStream dist4 = new DataInputStream(new BufferedInputStream(new FileInputStream("bobot/bobotbiasoutput_" + suffix + ".txt")));
             
            //DataInputStream : membaca inputan user
            // BufferedInputStream : menangkap inputan dari keayboard
            //FileInputStream  : membaca file
            //available : membaca byte
            //try catch : untuk menangkap error
            
            while(dist1.available()!=0 && dist2.available()!=0)
            {
                for(i=0;i<Xinput;i++)
                {
                    for(j=0;j<Yhidden;j++)
                    {
                        BobotWxi[i][j] = Double.parseDouble(dist1.readLine());
                        //readline : method membaca inputan user
                        //readline bertipe string dan dirubah dalam double
                    }
                }
                
                for(i=0;i<Yhidden;i++)
                {
                    for(j=0;j<Zoutput;j++)
                    {
                        BobotWyi[i][j] = Double.parseDouble(dist2.readLine());
                    }
                }
                
                for(i=0;i<Yhidden;i++)
                {
                    BobotBiasV[i] = Double.parseDouble(dist3.readLine());
                }
                
                  for(i=0;i<Zoutput;i++)
                {
                    BobotBiasW[i] = Double.parseDouble(dist4.readLine());
                }
                
            }
        }
        
        catch(IOException ex)
        {
        }
    }
      

    public double[][] getBobotWxi() {
        return BobotWxi;
    }

    public double[][] getBobotWyi() {
        return BobotWyi;
    }
    
    
     public void initPositiveBobot()
    {
        int i,j,k;
        
        for(i=0;i<Xinput;i++)
            {
                for(j=0;j<Yhidden;j++)
                {
                    if (i==0){
                        BobotBiasV[j]=1;
                    }
                    
                      int r = random.nextInt(2);
                   
                             BobotWxi[i][j]=1;
                      
              
                  //  BobotWxi[i][j] = random.nextDouble();
             //  System.out.println("BobotWXi"+i+","+j+":"+BobotWxi[i][j]);
                }
            }  
           // System.out.println("+++++++++++++++++++++++++++++");
            for(j=0;j<Yhidden;j++)
            {
                for(k=0;k<Zoutput;k++)
                {
                    
                     if (j==0){
                        BobotBiasW[k]=1;
                    }
                    
                   
                             BobotWyi[j][k]=1;
                      
                  //  BobotWyi[j][k] = random.nextDouble();
           //     System.out.println("BobotWXi"+i+","+j+":"+BobotWyi[j][k]);
                }
            }
                
    }
    
    
     public void initBobot()
    {
        int i,j,k;
        
        for(i=0;i<Xinput;i++)
            {
                for(j=0;j<Yhidden;j++)
                {
                    if (i==0){
                        BobotBiasV[j]=1;
                    }
                    
                      int r = random.nextInt(2);
                      if (r==0)
                      {
                             BobotWxi[i][j]=random.nextDouble()/2;
                      }else{
                             BobotWxi[i][j]=-1* random.nextDouble()/2;
                      }
              
                  //  BobotWxi[i][j] = random.nextDouble();
             //  System.out.println("BobotWXi"+i+","+j+":"+BobotWxi[i][j]);
                }
            }  
           // System.out.println("+++++++++++++++++++++++++++++");
            for(j=0;j<Yhidden;j++)
            {
                for(k=0;k<Zoutput;k++)
                {
                    
                     if (j==0){
                        BobotBiasW[k]=1;
                    }
                    
                     int r = random.nextInt(2);
                      if (r==0)
                      {
                             BobotWyi[j][k]=random.nextDouble()/2;
                      }else{
                            BobotWyi[j][k]=-1* random.nextDouble()/2;
                      }
                  //  BobotWyi[j][k] = random.nextDouble();
           //     System.out.println("BobotWXi"+i+","+j+":"+BobotWyi[j][k]);
                }
            }
                
    }
     
     public double getErrorMean()
     {
         return errormean/(InputLayer.length);
     }
     
     public void resetPattern(){
         errormean=0;
         currentpattern=0;
     }
     
     public void nextPattern()
     {
         currentpattern++;
     }
     
     public boolean isLastPattern(){
         return (currentpattern==InputLayer.length);
     }
     
    public void feedforward()
    {
        int i,j,k,l,u;       
        double FEHidden;
        double SigmaXiWi =0;
        double SigmaHiWi =0;
                   
        for(k=0;k<Yhidden;k++)
        {
            SigmaXiWi = 0;
            for(l=0;l<Xinput;l++)
            {
                    SigmaXiWi = SigmaXiWi + (InputLayer[currentpattern][l] * BobotWxi[l][k]);   
                 
            }
            SigmaXiWi = SigmaXiWi + BobotBiasV[k];
            HiddenLayer[k] = sigmoid(SigmaXiWi);
           // System.out.println("HiddenLayer" + i + ": " +HiddenLayer[k]);
        }
     
        for(i=0;i<Zoutput;i++)
        {
            SigmaHiWi = 0;
            for(j=0;j<Yhidden;j++)
            {
                    SigmaHiWi = SigmaHiWi + (HiddenLayer[j] * BobotWyi[j][i]);            
            }
               SigmaHiWi = SigmaHiWi + BobotBiasW[i];
               OutputLayer[i] = sigmoid(SigmaHiWi);            
               
//               System.out.println("OutputLayer:" + i + " : " +OutputLayer[i] + " Vs " + t[currentpattern][i]);
        }
         System.out.println("-----------------------------------");    
      }
    
    public void feedforwardBy(int iInput)
    {
        int i,j,k,l,u;       
        double FEHidden;
        double SigmaXiWi =0;
        double SigmaHiWi =0;
                   
        for(k=0;k<Yhidden;k++)
        {
            SigmaXiWi = 0;
            for(l=0;l<Xinput;l++)
            {
                    SigmaXiWi = SigmaXiWi + (InputLayer[iInput][l] * BobotWxi[l][k]);   
                 
            }
            SigmaXiWi = SigmaXiWi + BobotBiasV[k];
            HiddenLayer[k] = sigmoid(SigmaXiWi);
           // System.out.println("HiddenLayer" + i + ": " +HiddenLayer[k]);
        }
     
        for(i=0;i<Zoutput;i++)
        {
            SigmaHiWi = 0;
            for(j=0;j<Yhidden;j++)
            {
                    SigmaHiWi = SigmaHiWi + (HiddenLayer[j] * BobotWyi[j][i]);            
            }
               SigmaHiWi = SigmaHiWi + BobotBiasW[i];
               OutputLayer[i] = sigmoid(SigmaHiWi);            
               
//               System.out.println("OutputLayer:" + i + " : " +OutputLayer[i] + " Vs " + t[currentpattern][i]);
        }
        // System.out.println("-----------------------------------");    
      }
      
      public String compete(){
          String biner="";
         
        for(int i=0;i<OutputLayer.length;i++){
            if (OutputLayer[i]>0.8){
                OutputLayer[i]=1; 
            }else if (OutputLayer[i] < 0.3) {
                OutputLayer[i] = 0;
            }
            biner += String.valueOf((int)OutputLayer[i]);
        }
        return biner;
        
      }
   
      public void countError()
      {
        int i,j,k,l,u;       
        double FEHidden;      
        
       //error pada output layer
       for(j=0;j<Zoutput;j++)
          {  
            ErrorOutputLayer[j] = (t[currentpattern][j] - OutputLayer[j])*OutputLayer[j]*(1-OutputLayer[j]);
           //System.out.println("Target Layer" + i + " : " + t[pattern][j]);
          }
        
       
        //error faktor pada hidden layer; =============================
       for(i=0;i<Yhidden;i++)
       {
            FEHidden = 0;
            for(j=0;j<Zoutput;j++)
            {                
                FEHidden = FEHidden + (ErrorOutputLayer[j]*BobotWyi[i][j]);              
            }
            ErrorFactor[i] = FEHidden;
        }
        
        
        for(i=0;i<Yhidden;i++)
        {
            ErrorHiddenLayer[i] =ErrorFactor[i] * HiddenLayer[i]*(1-HiddenLayer[i]);                   //(ErrorFactor[i] - HiddenLayer[i])* HiddenLayer[i]*(1-HiddenLayer[i]);
        }
        //=========================================================================
        //error Global
        double temp = 0;       
        for(u=0;u<Zoutput;u++)
        {
            temp = temp + Math.pow((t[currentpattern][u] - OutputLayer[u]),2);       
           
        }
        double error = temp/Zoutput; 
        errormean = error;
        
          
       
      }
      
  public void backPropagate()
    {
        int i,j;
      
         //suku bobot Wyi
        for(i=0;i<Yhidden;i++)
        {
            for(j=0;j<Zoutput;j++)
            {
                if (i==0)
                {
                    DeltaBiasW[j]= lambda *ErrorOutputLayer[j];   
                   
                }
                
                DeltaWyi[i][j] = lambda * ErrorOutputLayer[j] * HiddenLayer[i];
              // System.out.println(DeltaWyi[i][j]);
            }    
        }
        
        
        //suku bobot Wxi
        for(i=0;i<Xinput;i++)
        {
            for(j=0;j<Yhidden;j++)
            {
                if (i==0){
                    DeltaBiasV[j] = lambda * ErrorHiddenLayer[j];
                }
                DeltaWxi[i][j] = lambda * ErrorHiddenLayer[j]* InputLayer[currentpattern][i];
            }
        }
        
        //perubahan bobot bias Wxi
        for(i=0;i<Xinput;i++)
        {
            for(j=0;j<Yhidden;j++)
            {
                if (i==0){
                    BobotBiasV[j] = BobotBiasV[j] + DeltaBiasV[j];
                }
                BobotWxi[i][j] = BobotWxi[i][j] + DeltaWxi[i][j];
             //   System.out.println("BobotWXi " + i + " , " + j + " : " +BobotWxi[i][j]);
            }
        }
        
        //perubahan bobot bias Wyi
        for(i=0;i<Yhidden;i++)
        {
            for(j=0;j<Zoutput;j++)
            {
                  if (i==0){
                    BobotBiasW[j] = BobotBiasW[j] + DeltaBiasW[j];
                }
                BobotWyi[i][j] = BobotWyi[i][j] + DeltaWyi[i][j];
               // System.out.println("BobotWyi " + i + " , " + j + " : " +BobotWyi[i][j]);
            }
        }
        
         System.out.println("Error Lama :" + String.valueOf(t[currentpattern][0] - OutputLayer[0]));
     
    } 
  
  public double[] getBobotBiasV()
  {
      return BobotBiasV;
  }
  
   public double[] getBobotBiasW()
  {
      return BobotBiasW;
  }
  
   private double sigmoid(double x)
    {
        double hasil;
        hasil = (1/(1+Math.exp(-2* x)));
        return hasil;
    }
     
    
    
}
