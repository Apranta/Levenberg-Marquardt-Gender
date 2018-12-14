/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

 
public class QrDecomposition 
{
        private double[][] qr;
	private double[] Rdiag;

	
        private static void printError(String message)
        {
            System.out.println("Error:" + message);
        }
         
         private   double Hypotenuse(double a, double b)
        {
            double r = 0.0;

            if (Math.abs(a) > Math.abs(b))
            {
                r = b / a;
                r =  Math.abs(a) *  Math.sqrt(1 + r * r);
            }
            else if (b != 0)
            {
                r = a / b;
                r =  Math.abs(b) *  Math.sqrt(1 + r * r);
            }

            return r;
        }
         
        public QrDecomposition(double[][] value)
		{
			if (value == null)
			{
			      printError("value");	
			}

                  this.qr = (double[][])value.clone();
			double[][] qr = this.qr;
			int m = value.length;
			int n = value[0].length;
			this.Rdiag = new double[n];
	
			for (int k = 0; k < n; k++) 
			{
				
				double nrm = 0;
				for (int i = k; i < m; i++)
				{
					nrm =  Hypotenuse(nrm,qr[i][k]);
				}
				 
				if (nrm != 0.0) 
				{
					 
					if (qr[k][k] < 0)
					{
						nrm = -nrm;
					}
					
					for (int i = k; i < m; i++)
					{
						qr[i][k] /= nrm;
					}

					qr[k][k] += 1.0;
	
					
					for (int j = k+1; j < n; j++) 
					{
						double s = 0.0;

						for (int i = k; i < m; i++)
						{
							s += qr[i][k]*qr[i][j];
						}

						s = -s/qr[k][k];

						for (int i = k; i < m; i++)
						{
							qr[i][j] += s*qr[i][k];
						}
					}
				}

				this.Rdiag[k] = -nrm;
			}
		}

		 
               public double[][] Solve(double[][] value)
		{
			if (value == null)
			{
				printError("value");	
			}

			if (value.length != qr.length)
			{
				printError("Matrix row dimensions must agree.");
			}
			
			if (!this.FullRank()) 
			{
				printError("Matrix is rank deficient.");
			}
				
			// Copy right hand side
			int count = value[0].length;
                        double[][] X = (double[][])value.clone();
			int m = qr.length;
			int n = qr[0].length;
			
			// Compute Y = transpose(Q)*B
			for (int k = 0; k < n; k++) 
			{
				for (int j = 0; j < count; j++) 
				{
					double s = 0.0; 
					
					for (int i = k; i < m; i++)
					{
						s += qr[i][k] * X[i][j];
					}

					s = -s / qr[k][k];
					
					for (int i = k; i < m; i++)
					{
						X[i][j] += s * qr[i][k];
					}
				}
			}
				
			// Solve R*X = Y;
			for (int k = n-1; k >= 0; k--) 
			{
				for (int j = 0; j < count; j++) 
				{
					X[k][j] /= Rdiag[k];
				}
	
				for (int i = 0; i < k; i++) 
				{
					for (int j = 0; j < count; j++) 
					{
						X[i][j] -= X[k][j] * qr[i][k];
					}
				}
			}

                    double[][] r = new double[n][ count];
                    for (int i = 0; i < r.length; i++)
                        for (int j = 0; j < r[0].length; j++)
                            r[i][j] = X[i][j];
            
                 return r;
		}

		 
		public boolean FullRank()
		{
			 
				int columns = qr[0].length;

				for (int i = 0; i < columns; i++)
				{
					if (this.Rdiag[i] == 0)
					{
						return false;
					}
				}

				return true;
			 			
		}
	
		 
                 public double[][] UpperTriangularFactor()
		{
			 
				int n = this.qr[0].length;
				double[][] x = new double[n][n];

				for (int i = 0; i < n; i++) 
				{
					for (int j = 0; j < n; j++) 
					{
						if (i < j)
						{
							x[i][j] = qr[i][j];
						}
						else if (i == j) 
						{
							x[i][j] = Rdiag[i];
						}
						else
						{
							x[i][j] = 0.0;
						}
					}
				}
	
				return x;
			 
		}

		 
                public double[][] OrthogonalFactor()
		{			 
                        int rows = qr.length;
                        int cols = qr[0].length;
                        
			double[][] x = new double[rows][cols];
                
				for (int k = qr[0].length - 1; k >= 0; k--) 
				{
					for (int i = 0; i < rows; i++)
					{
						x[i][k] = 0.0;
					}

					x[k][k] = 1.0;
					for (int j = k; j < cols; j++) 
					{
						if (qr[k][k] != 0) 
						{
							double s = 0.0;
				
							for (int i = k; i < rows; i++)
							{
								s += qr[i][k] * x[i][j];
							}

							s = -s / qr[k][k];
				
							for (int i = k; i < rows; i++)
							{
								x[i][j] += s * qr[i][k];
							}
						}
					}
				}

				return x;
			 
		}
}
