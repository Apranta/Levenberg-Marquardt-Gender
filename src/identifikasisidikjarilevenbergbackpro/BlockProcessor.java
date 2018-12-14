/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;

 

/**
 *
 * @author TOSHIBA
 */
public class BlockProcessor {

    FingerPrintImage fpiblock[];
    int colblockmax;
    int rowblockmax;
    
    public BlockProcessor() 
    {
      colblockmax=0;
      rowblockmax=0;
    }
    
    public  int  GetColBlockMax()
    {
       return colblockmax;
    }
    
    public int  GetRowBlockMax()
    {
       return rowblockmax;
    }
   
    public void CreateImageBlock(FingerPrintImage fpi,int w)
    {
      int k = fpi.getHeight()/w;
      int l = fpi.getWidth()/w;
      fpiblock = new FingerPrintImage[k*l];
   rowblockmax=k;
   colblockmax=l;
       //Image Block w x w
      int blockheight=w;
      int blockwidth=w;

      int startHeight=0;
      int endHeight=0;

      int startWidth=0;
      int endWidth=0;

      int jblok=0;

      for(int i=0; i<k; i++)
       {
            startHeight=blockheight*i;
            endHeight=blockheight*(i+1);
            for(int j=0; j<l; j++)
           {
              startWidth=blockwidth*j;
              endWidth=blockwidth*(j+1);
              jblok++;
             
              FingerPrintImage fpib = new FingerPrintImage(blockwidth,blockheight);
              for(int row=startHeight;row<endHeight;row++)
              {
                for(int col=startWidth;col<endWidth;col++)
                {
                   int pixel = fpi.getPixelOutput(row,col);
                   fpib.setPixelOutput(row-startHeight,col-startWidth,pixel);
                }
              }
              
              fpib.setX(startWidth);
              fpib.setY(startHeight);
              fpiblock[jblok-1]=fpib;
           }
       }
}
    
   
     
 
public FingerPrintImage[] GetImageBlock()
{
     return fpiblock;

}
      
 }


 

 




//---------------------------------------------------------------------------

 
 