/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

/**
 *
 * @author TOSHIBA
 */
public class Input {
    double value[];
    int target;     

    public Input(int length)
    {
        value = new double[length];               
    }  
    
    
    public void setTarget(int target)
    {
        this.target = target;
    }
    
    public int GetTarget()
    {
        return target;
    }
    
    public void setValue(int idx,double val)
    {
        value[idx] = val;
    }
    
    public double[] getValues()
    {
        return value;
    }
    
    public double getValue(int idx)
    {
        return value[idx];
    }
    
    public int getInputLength(){
        return value.length;
    }
}
