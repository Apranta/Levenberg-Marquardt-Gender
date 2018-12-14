/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identifikasisidikjarilevenbergbackpro;





public class GradientFinder 
{
    double theta[][];
    private double SobelXOperator(int i, int j,FingerPrintImage img)
    {
        int Sx[][] =new int[][] {{-1,0,1},{-2,0,2},{-1,0,1}};
	int n,m;
        float jumlah = 0;
        for(int x=-1;x<=1;x++)
        {
             for(int y=-1;y<=1;y++)
             {
                 n = x+1;
                 m = y+1;
		jumlah += (img.getPixelOutput(y+j,x+i)*Sx[m][n]);              
            }
    }
    return (jumlah);
    }
    
    private float SobelYOperator(int i, int j, FingerPrintImage img)
    {
	int Sy[][] = {{1,2,1},{0,0,0},{-1,-2,-1}};
	int m,n;    
        float jumlah= 0;
        for(int x=-1; x<=1; x++)
        {
           for(int y=-1; y<=1; y++)
           {
              m = x+1;
              n = y+1;
	      jumlah +=img.getPixelOutput(y+j,x+i)*Sy[n][m];
          }
        }
         return (jumlah);
     }
    
    public double[][] getTheta()
    {
        return theta;
    }
    
 public void FindingGradient(FingerPrintImage img)
{  
    int height = img.getHeight();
    int width = img.getWidth(); 
    int max=0;
    int[][] pixeltemp =new int[height][width];
    theta = new double[height][width];
    for(int i=1;i<height-1;i++)
    {
        for (int j=1;j<width-1;j++)
        {
		double sobx =SobelXOperator(j,i,img); 
		double soby =SobelYOperator(j,i,img); 
		int g=  (int)  Math.sqrt(Math.pow(soby,2) + Math.pow(sobx,2));
               
		pixeltemp[i][j] =g;
            
		if(max<g)max=g;

			  
		}
         
	 }

 
  double rasio =(double) max/255; 
 

  for(int i=1;i<height-1;i++)
  {
		for (int j=1;j<width-1;j++)
		{	  
                       double nbaru = pixeltemp[i][j]/rasio;	 
                       
                       if (nbaru>88) 
                       {
                           nbaru = 255;
                       }else {
                           nbaru = 0;
                       }
                       
		       img.setPixelOutput(i, j,(int) nbaru);
			  
		}
         
	 } 
}
    
    
    
}
