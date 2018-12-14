/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;
 
public class Identifikasisidikjarilevenbergbackpro
{
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args)
    {
        // TODO code application logic here
       double[][]  input = new double[][]{{0, 0},
				          {0, 1},
				          {1, 0},
			                  {1, 1}
                                          };
       
       double[][] output = new double[][]{
					    {0},
					    {1},
                                            {1},
					    {0}
				          };
       
       int epoch =1;
       double MSE=1;
       
       double err = 0.001;
       
       LevenbergMarquardt lm = new LevenbergMarquardt(input, output, 2, 1, 0.1);
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
          
            System.out.println("************************* EPOCH Ke-" + String.valueOf(epoch) + " **********************");   
            System.out.println("MSE = " + MSE);
         
           // frmTrain.setStatus(String.valueOf(MSE),String.valueOf(epoch));
            epoch++;
        }while (epoch<6);
        
       
        int maxepoch = 100;
        
        System.out.println("Masuk Levenber-Marquart ----------------------------");
        
        epoch = 0;
        
        while(MSE > err) 
        {			
	    if ( epoch >= maxepoch ) break;			
	
            MSE = lm.RunEpoch(input, output);            
            System.out.println("MSE EPOCH -" + String.valueOf(epoch+1)  + ":" + MSE);            
            epoch++;		 
			
	}
		
       System.out.println("Number of epochs: "+epoch);
       
       System.out.println("Sukses");
    }
    
}
