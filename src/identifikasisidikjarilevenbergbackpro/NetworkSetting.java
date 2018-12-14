/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;
 
public class NetworkSetting 
{
    private int nneuron_input;
    private int hidden_layer;
    private int nneuron_hidden;
    private int nneuron_output;
    private double momentum_rate;
    private double learning_rate;
    private double err_tolerance;
    private int maxepoch;
    private int bias;
    
    public NetworkSetting() 
    {
        nneuron_input = 5;
        hidden_layer = 1;
        nneuron_hidden = 0;
        nneuron_output=0;
        momentum_rate=0.0;
        learning_rate=0.0;
        err_tolerance=0.0;
        maxepoch=0;
        bias = 0;
    }

    public void setMomentumRate(double momentum_rate) {
        this.momentum_rate = momentum_rate;
    }

    

    public void setNneuron_input(int nneuron_input) {
        this.nneuron_input = nneuron_input;
    }

    

    public void setHidden_layer(int hidden_layer) {
        this.hidden_layer = hidden_layer;
    }

   
    public void setBias(int bias) {
        this.bias = bias;
    }

    public void setErrTolerance(double err_tolerance) {
        this.err_tolerance = err_tolerance;
    }

    public void setLearningRate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    public void setNNeuronHidden(int nneuron_hidden) {
        this.nneuron_hidden = nneuron_hidden;
    }

    public void setNNeuronOutput(int nneuron_output) {
        this.nneuron_output = nneuron_output;
    }

    public void setMaxEpoch(int maxepoch) {
        this.maxepoch = maxepoch;
    }

    public double getErrTolerance() {
        return err_tolerance;
    }

    public double getLearningRate() {
        return learning_rate;
    }

    public int getMaxEpoch() {
        return maxepoch;
    }

    public double getMomentumRate() {
        return momentum_rate;
    }
    public int getNneuron_input() {
        return nneuron_input;
    }

    public int getHidden_layer() {
        return hidden_layer;
    }
    
    public int getNNeuronHidden() {
        return nneuron_hidden;
    }

    public int getNNeuronOutput() {
        return nneuron_output;
    }    
   
     public int getBias() {
        return bias;
    }

}
