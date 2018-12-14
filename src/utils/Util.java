/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;
import identifikasisidikjarilevenbergbackpro.NetworkSetting;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JWindow;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.*;
import java.text.*;
import javax.swing.UIManager.LookAndFeelInfo;




public class Util {
  public static void TengahWindow(Window f){

          // Get the size of the screen
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    // Determine the new location of the window
    int w = f.getSize().width;
    int h = f.getSize().height;
    int x = (dim.width-w)/2;
    int y = (dim.height-h)/2;

    // Move the window
    f.setLocation(x, y);
    }

  

  public static void LookAndFeel(Frame f)
  {
      try
      {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      SwingUtilities.updateComponentTreeUI(f);
      }catch (ClassNotFoundException ex){
              JOptionPane.showMessageDialog(f,"Kelas tak ditemukan.. ulangi installasi");
      }catch (InstantiationException ex){
      }catch (IllegalAccessException ex){
      }catch (UnsupportedLookAndFeelException ex){
          JOptionPane.showMessageDialog(f,"Dak Support");
      }

  }
  
   public final static boolean checkFile(String dir,String file,String format)
    {
        return new File(dir+ "/" + file+ "." + format).exists();
    }
  
  public final static NetworkSetting readNetworkSetting(String dir,String fileName) throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(new File(dir+ "/" + fileName+".txt"))); 
        NetworkSetting ns = new NetworkSetting();     
        ns.setNNeuronHidden(Integer.parseInt(reader.readLine()));
        ns.setNNeuronOutput(Integer.parseInt(reader.readLine()));    
        ns.setLearningRate(Double.parseDouble(reader.readLine()));
        ns.setErrTolerance(Double.parseDouble(reader.readLine()));
        ns.setMaxEpoch(Integer.parseInt(reader.readLine()));        
        return ns;
    }
  
  
  public static void initNimbusTheme()
  {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
           
        }


  }
          


}


