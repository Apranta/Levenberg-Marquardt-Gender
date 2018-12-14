/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;


public class NetworkWriter
{
     

 
    public static void writeBobotHidden(String dir, String fileName, double BobotWxi [][] ) throws IOException
    {
        createDir(dir);
        File newFile = new File(dir+"/" + fileName+".txt");
        FileWriter fw = new FileWriter(newFile);
         for(int i=0;i<BobotWxi.length;i++)
            {
                for(int j=0;j<BobotWxi[i].length;j++)
                {
                    String s =  String.valueOf(BobotWxi[i][j]) +"\r\n";                    
                    fw.write(s);
                }
            }
         fw.flush();
         fw.close();
    }
    
    public static void writeSetting(String dir, String fileName,NetworkSetting ns ) throws IOException{
        createDir(dir);
        File newFile = new File(dir+"/" + fileName+".txt");
        FileWriter fw = new FileWriter(newFile);
        StringBuilder content= new StringBuilder();
        content.append( String.valueOf(ns.getNNeuronHidden()) + "\n");
        content.append(ns.getNNeuronOutput() + "\n");     
        content.append(ns.getLearningRate() + "\n");
        content.append(ns.getErrTolerance() + "\n");
        content.append(ns.getMaxEpoch() + "\n");
        fw.write(content.toString());
        fw.flush();
        fw.close();
    }
    
    public static void writeBobotBias(String dir, String fileName, double BobotBias[] ) throws IOException
    {
        createDir(dir);
        File newFile = new File(dir+"/" + fileName+".txt");
        FileWriter fw = new FileWriter(newFile);
         
                for(int j=0;j<BobotBias.length;j++)
                {
                    String s =  String.valueOf(BobotBias[j]) +"\r\n";                    
                    fw.write(s);
                }
            
         fw.flush();
         fw.close();
    }
    
    public static void writeBobotOutput(String dir, String fileName, double BobotWyi [][]) throws IOException
    {        
        createDir(dir);
        File newFile = new File(dir+"/" + fileName+".txt");
        FileWriter fw = new FileWriter(newFile);
        for(int i=0;i<BobotWyi.length;i++)
          {
            for(int j=0;j<BobotWyi[i].length;j++)
             {
                 String s =  String.valueOf(BobotWyi[i][j]) +"\r\n";                    
                 fw.write(s);
              }
          }
        fw.flush();
        fw.close();
    }
   
    
    private static void createDir(String dir){
        File filePath = new File(dir);
        filePath.mkdirs();
    }
    
    private static void deleteDir(String dir){
        File filePath = new File(dir);
        System.out.println(filePath.delete());
    }
}
