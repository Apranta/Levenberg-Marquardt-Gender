/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

import java.util.ArrayList;
import matrix.LuDecomposition;
import matrix.Matrix;
 

 
public class LevenbergMarquardt extends BackPropagation
{
    private static double lambdaMax = 1e25;
    
    private double sigmoidAlphaValue = 2;
    
    private double alpha = 0.0;
    private double beta = 1.0;
    private double gamma = 0.0;
    
   
    private double damping = 0.1;
   
    
    private double[][] jacobian;
    private double[][] hessian;
        
    private double[] diagonal;
    private double[] gradient;
    private double[] weigths;
    private double[] deltas;
    private double[] errors;
    
    private double v = 10.0;
    
    private int numberOfParameters;
    
    private double[][][] weightDerivatives = null;
    private double[][] thresholdsDerivatives = null;
    
    //lambda = learningrate

    public LevenbergMarquardt(double[][] InputLayer, double[][] t, int nHidden, int nOutput, double learningrate)
    {
        super(InputLayer, t, nHidden, nOutput, learningrate);
        
        numberOfParameters = ((Xinput*nHidden) + (nHidden*nOutput)) + (nHidden + nOutput);
        
        this.weigths = new double[numberOfParameters];
        this.hessian = new double[numberOfParameters][numberOfParameters];
        this.diagonal = new double[numberOfParameters];
        this.gradient = new double[numberOfParameters];
        
        this.weightDerivatives = new double[2][][];
        this.thresholdsDerivatives = new double[2][];

                // initialize arrays
        for (int i = 0; i < 2; i++)
        { 
            if (i==0) 
            {
               this.weightDerivatives[i] = new double[nHidden][Xinput];
               this.thresholdsDerivatives[i] = new double[nHidden];
            }else if (i==1) 
            {
               this.weightDerivatives[i] = new double[nOutput][nHidden];
               this.thresholdsDerivatives[i] = new double[nOutput];
            } 

               
         }
         System.out.println("Inisial");
        
    }
    
    public double RunEpoch(double[][] input, double[][] output)
        {          
            int N = input.length;
            this.errors = new double[N];
            this.jacobian = new double[N][numberOfParameters];
            LuDecomposition decomposition = null;
            double sumOfSquaredErrors = 0.0;
            double sumOfSquaredWeights = 0.0;
            double trace = 0.0;

            sumOfSquaredErrors = JacobianByChainRule(input, output);
           // System.out.println(">>>> " + sumOfSquaredErrors);
         
            sumOfSquaredWeights = CreateWeights();
           // System.out.println("Sebelum " + sumOfSquaredWeights);
          
            for (int i = 0; i < numberOfParameters; i++)
            {
                
                double s = 0.0;
                for (int j = 0; j < N; j++)
                {
                    s += jacobian[j][i] * errors[j];
                }
                gradient[i] = s;

                
                for (int j = 0; j < numberOfParameters; j++)
                {
                    double c = 0.0;
                    for (int k = 0; k < N; k++)
                    {
                        c += jacobian[k][i] * jacobian[k][j];
                    }
                    hessian[i][j] = beta * c;
                }
            }

             
            for (int i = 0; i < numberOfParameters; i++)
                diagonal[i] = hessian[i][i];


            
            double objective = beta * sumOfSquaredErrors + alpha * sumOfSquaredWeights;
            double current = objective + 1.0;



            
            lambda /= v;

           
            while (current >= objective && lambda < lambdaMax)
            {
              //  System.out.println("Yihaa");
                lambda *= v;

                
                for (int i = 0; i < numberOfParameters; i++)
                {
                    hessian[i][i] = diagonal[i] + (lambda + alpha);
                  //  System.out.println( hessian[i][i] );
                }

               
                decomposition = new LuDecomposition(hessian);
                
                if (!decomposition.isNonSingular()) continue;

              
                deltas = decomposition.Solve(gradient);

               
                sumOfSquaredWeights = UpdateWeights();
            //    System.out.println("Sesudah " + sumOfSquaredWeights);
               
                sumOfSquaredErrors = 0.0;
                for (int i = 0; i < N; i++)
                {
                    feedforwardBy(i);
                    sumOfSquaredErrors += CalculateError(output[i]);
                }                
                current = beta * sumOfSquaredErrors + alpha * sumOfSquaredWeights;                
            }

            
            lambda /= v; 
               
          
            return sumOfSquaredErrors;
        }

    
   
    private double JacobianByChainRule(double[][] input, double[][] output)
        {
            double sse = 0.0, e;
            int N = input.length;

          //  System.out.println("PJ Error " + errors.length);
            for (int i = 0; i < N; i++)
            {
                 
                feedforwardBy(i);

                int ji = i;
                int jj = 0;

               
                e = CalculateDerivatives(input[i], output[i], jj);
              //  System.out.println("ERRR " + e);
                errors[ji] = e;
                sse += e * e;
 
                for (int layer = 0; layer < 2; layer++)
                {                
                    int n,w;
                    n = 0;w=0;
                    if (layer==0)
                    {
                        n =Yhidden ;
                        w = Xinput;
                    }else if (layer==1)
                    {
                        n = Zoutput;
                        w = Yhidden;
                    } 
                    
                    for (int neuron = 0; neuron < n; neuron++)
                    {                       
                        for (int weight = 0; weight < w; weight++)
                        {                            
                            jacobian[ji][jj] = weightDerivatives[layer][neuron][weight];
                          //  System.out.println(jacobian[ji][jj]);
                            jj++;
                        }
                  
                        jacobian[ji][jj] = thresholdsDerivatives[layer][neuron];
                        jj++;
                    }
                }

            }
            return sse / 2.0;
        }
       
       private double CreateWeights()
        {
            int j = 0;
            double sumOfSquaredWeights = 0.0;
            double w;
           
            for (int layer = 0; layer < 2; layer++)
            {
                w=0.0;
                 int ni,wi;
                 ni = 0;wi=0;
                 
                    if (layer==0)
                    {
                        ni =Yhidden ;
                        wi = Xinput;
                    }else if (layer==1)
                    {
                        ni = Zoutput;
                        wi = Yhidden;
                    } 
                    
                for (int neuron = 0; neuron < ni; neuron++)
                {                    
                    for (int weight = 0; weight < wi; weight++)
                    {                     
                        if (layer==0)
                        {
                            w = BobotWxi[weight][neuron];
                        }else if (layer==1)
                        {
                            w = BobotWyi[weight][neuron];
                        }
                        
                        weigths[j] = w;
                        sumOfSquaredWeights += w * w;
                        j++;
                    }
                    // also for each threshold value (bias):
                    
                   // w = network[layer][neuron].Threshold;
                    if (layer==0)
                    {
                        w = BobotBiasV[neuron];
                    }else if (layer==1)
                    {
                        w = BobotBiasW[neuron];
                    }
                        
                    
                    weigths[j] = w;
                    sumOfSquaredWeights += w * w;
                    j++;
                }
            }
            return sumOfSquaredWeights / 2.0;
        }
       
       public double Function( double x )

        {

            return ( 1 / ( 1 + Math.exp( -sigmoidAlphaValue * x )));

        }
       
       public double Derivative( double x )

        {

            double y = Function( x );



            return ( sigmoidAlphaValue * y * ( 1 - y ) );

        }
       
        public double Derivative2( double y )

        {

            return ( sigmoidAlphaValue * y * ( 1 - y ) );

        }
       
        private double CalculateDerivatives(double[] input, double[] desiredOutput, int outputIndex)
        {          
            double e = 0.0;
            double sum = 0.0;
            
            int layer =1;

            double[] previousLayerOutput =  HiddenLayer;
 
            double output = OutputLayer[outputIndex]; 
            e = desiredOutput[outputIndex] - output;
            
           // System.out.println("Error : " + e);
            for (int i = 0; i < Yhidden; i++)
            {
                weightDerivatives[layer][outputIndex][i] =  Derivative2(output) * previousLayerOutput[i];
            }
            thresholdsDerivatives[layer][outputIndex] = Derivative2(output);

            layer = 0;
            previousLayerOutput = input;
               
                for (int neuron = 0; neuron < Yhidden; neuron++)
                {
                    output = HiddenLayer[neuron];                     
                    for (int i = 0; i < Xinput; i++)
                    {
                        sum = 0.0;
                     
                        for (int j = 0; j < Zoutput; j++)
                        {                           
                            for (int k = 0; k < Yhidden; k++)
                            {
                               // System.out.println("Bobot " + BobotWyi[k][j]);
                                sum += BobotWyi[k][j]  *  HiddenLayer[k];
                            }
                            sum += BobotBiasW[j];// network[layer + 1][j].Threshold;
                        }

                      
                        double w = BobotWyi[neuron][outputIndex]; 

                        weightDerivatives[layer][neuron][i] =  Derivative2(output) *
                             Derivative(sum) * w * previousLayerOutput[i];
                       // System.out.println("Turunan " + weightDerivatives[layer][neuron][i] );

                        thresholdsDerivatives[layer][neuron] = Derivative2(output) *
                            Derivative(sum) * w;
                    }
                }
             

          
            return e;
        }
        
        private double CalculateError(double[] desiredOutput)
        {
            double sumOfSquaredErrors = 0.0;
            double e = 0.0;

            for (int j = 0; j < desiredOutput.length; j++)
            {
                e = desiredOutput[j] - OutputLayer[j];
                sumOfSquaredErrors += e * e;
            }

            return sumOfSquaredErrors / 2.0;
        }
        
         private double UpdateWeights()
        {
            double sumOfSquaredWeights = 0.0;
            double w;
            int j = 0;
            
            for (int layer = 0; layer < 2; layer++)
            {   
                int ni,wi;
                ni = 0;wi=0;
                if (layer==0)
                {
                     ni =Yhidden ;
                     wi = Xinput;
                }else if (layer==1)
                {
                     ni = Zoutput;
                     wi = Yhidden;
                } 
                
                for (int neuron = 0; neuron < ni; neuron++)
                {                   
                    for (int weight = 0; weight < wi; weight++)
                    {
                         //System.out.println("Delta " + deltas[j] );
                        w = weigths[j] + deltas[j];
                        sumOfSquaredWeights += w * w;
                        
                        if (layer==0)
                        {
                             BobotWxi[weight][neuron] = w;
                        }else if (layer==1)
                        {
                             BobotWyi[weight][neuron] = w;
                        }
                        
                        j++;
                    }
                    // for each threshold value (bias):
                    w = weigths[j] + deltas[j];
                    if (layer==0)
                    {
                        BobotBiasV[neuron] = w;
                    }else if (layer==1)
                    {
                        BobotBiasW[neuron] = w;                         
                    }
                    
                    sumOfSquaredWeights += w * w;
                    j++;
                }
            }

            return sumOfSquaredWeights / 2.0;
        }
    
    
}
