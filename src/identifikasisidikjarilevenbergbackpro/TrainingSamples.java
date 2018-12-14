/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

 
import java.util.ArrayList;
import java.util.List;


public class TrainingSamples 
{
    List<FingerPrintImage> lstfp;
    List<String> listnamafiles;
    List<String> lsttarget;

    public TrainingSamples() 
    {
        lstfp = new ArrayList<FingerPrintImage>();  
        lsttarget = new ArrayList<String>();
        listnamafiles = new ArrayList<String>();
    }
    
    public void addFPImage(FingerPrintImage hw,String nmfile, String target)
    {
        lstfp.add(hw);
        hw.SetName(target);
        lsttarget.add(target);
        listnamafiles.add(nmfile);
    }
    
    public FingerPrintImage getFingerPrintImage(int idx)
    {
        return lstfp.get(idx);
    }
    
    
    public void clear()
    {
        lstfp.clear();
        lsttarget.clear();
        listnamafiles.clear();
    }
    
    public int getTarget(int idx)
    {
        return  Integer.parseInt(lsttarget.get(idx));
    }
    
    public String getFileNames(int idx) {
         return listnamafiles.get(idx);
    }
    
    public int getTotalFingerPrint()
    {
        return lstfp.size();
    }
}
