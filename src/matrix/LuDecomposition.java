/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;


public class LuDecomposition 
{
        private double[][] LU;
        private int pivotSign;
        private int[] pivotVector;
        
         private static void printError(String message)
        {
            System.out.println("Error:" + message);
        }
        
        public LuDecomposition(double[][] value)
        {
            if (value == null)
            {
                printError("value");
            }

            this.LU = (double[][])value.clone();
            double[][] lu = LU;
            int rows = value.length;
            int columns = value[0].length;
            pivotVector = new int[rows];
            for (int i = 0; i < rows; i++)
            {
                pivotVector[i] = i;
            }

            pivotSign = 1;
            double[] LUrowi;
            double[] LUcolj = new double[rows];

            
            for (int j = 0; j < columns; j++)
            {
                
                for (int i = 0; i < rows; i++)
                {
                    LUcolj[i] = lu[i][j];
                }

                
                for (int i = 0; i < rows; i++)
                {
                    //LUrowi = lu[i];

                    
                    int kmax = Math.min(i, j);
                    double s = 0.0;
                    for (int k = 0; k < kmax; k++)
                    {
                        s += lu[i][k] * LUcolj[k];
                    }
                    lu[i][j] = LUcolj[i] -= s;
                }

              
                int p = j;
                for (int i = j + 1; i < rows; i++)
                {
                    if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p]))
                    {
                        p = i;
                    }
                }

                if (p != j)
                {
                    for (int k = 0; k < columns; k++)
                    {
                        double t = lu[p][k];
                        lu[p][k] = lu[j][k];
                        lu[j][k] = t;
                    }

                    int v = pivotVector[p];
                    pivotVector[p] = pivotVector[j];
                    pivotVector[j] = v;

                    pivotSign = -pivotSign;
                }

             

                if (j < rows & lu[j][j] != 0.0)
                {
                    for (int i = j + 1; i < rows; i++)
                    {
                        lu[i][j] /= lu[j][j];
                    }
                }
            }
        }

        
        public boolean isNonSingular()
        {           
                for (int j = 0; j < LU[0].length; j++)
                    if (LU[j][j] == 0)
                        return false;
                return true;             
        }

        
        public double Determinant()
        {             
                if (LU.length != LU[0].length)
                      printError("Matrix must be square.");
                double determinant = (double)pivotSign;
                for (int j = 0; j < LU[0].length; j++)
                    determinant *= LU[j][j];
                return determinant;            
        }

       
        public double[][] LowerTriangularFactor()
        {
             
                int rows = LU.length;
                int columns = LU[0].length;
                double[][] X = new double[rows][columns];
                for (int i = 0; i < rows; i++)
                    for (int j = 0; j < columns; j++)
                        if (i > j)
                            X[i][j] = LU[i][j];
                        else if (i == j)
                            X[i][j] = 1.0;
                        else
                            X[i][j] = 0.0;
                return X;
            
        }

        
        public double[][] UpperTriangularFactor()
        {            
                int rows = LU.length;
                int columns = LU[0].length;
                double[][] X = new double[rows][columns];
                for (int i = 0; i < rows; i++)
                    for (int j = 0; j < columns; j++)
                        if (i <= j)
                            X[i][j] = LU[i][j];
                        else
                            X[i][j] = 0.0;
                return X;
             
        }

      
        public double[] PivotPermutationVector()
        { 
                int rows = LU.length;

                double[] p = new double[rows];
                for (int i = 0; i < rows; i++)
                {
                    p[i] = (double)this.pivotVector[i];
                }

                return p;
             
        }

        
        public double[][] Inverse()
        {
            if (!this.isNonSingular())
            {                 
                printError("Matrix is singular");
            }

           
            int rows = LU[0].length;
            int columns = LU[0].length;
            int count = rows;
            double[][] lu = LU;

            double[][] X = new double[rows][ columns];
            for (int i = 0; i < rows; i++)
            {
                int k = pivotVector[i];
                X[i][k] = 1.0; 
            }

            // Solve L*Y = B(piv,:)
            for (int k = 0; k < columns; k++)
            {
                for (int i = k + 1; i < columns; i++)
                {
                    for (int j = 0; j < count; j++)
                    {
                        X[i][j] -= X[k][j] * lu[i][k];
                    }
                }
            }

            // Solve U*X = Y;
            for (int k = columns - 1; k >= 0; k--)
            {
                for (int j = 0; j < count; j++)
                {
                    X[k][j] /= lu[k][k];
                }

                for (int i = 0; i < k; i++)
                {
                    for (int j = 0; j < count; j++)
                    {
                        X[i][j] -= X[k][j] * lu[i][k];
                    }
                }
            }

            return X;
        }

        
        public double[][] Solve(double[][] value)
        {
            if (value == null)
            {
                printError("value");
            }

            if (value.length != this.LU.length)
            {
                 printError("Invalid matrix dimensions. value");
            }

            if (!this.isNonSingular())
            {
                printError("Matrix is singular");
            }

            
            int count = value[0].length;
           
            double[][] X = Matrix.Submatrix(value,pivotVector, 0, count - 1);

            int rows = LU[0].length;
            int columns = LU[0].length;
            double[][] lu = LU;

            // Solve L*Y = B(piv,:)
            for (int k = 0; k < columns; k++)
            {
                for (int i = k + 1; i < columns; i++)
                {
                    for (int j = 0; j < count; j++)
                    {
                        X[i][j] -= X[k][j] * lu[i][k];
                    }
                }
            }

            // Solve U*X = Y;
            for (int k = columns - 1; k >= 0; k--)
            {
                for (int j = 0; j < count; j++)
                {
                    X[k][j] /= lu[k][k];
                }

                for (int i = 0; i < k; i++)
                {
                    for (int j = 0; j < count; j++)
                    {
                        X[i][j] -= X[k][j] * lu[i][k];
                    }
                }
            }

            return X;
        }



    
        public double[] Solve(double[] value)
        {
            if (value == null)
            {
                printError("value");
            }

            if (value.length != this.LU.length)
            {
                printError("Invalid matrix dimensions. value");
            }

            if (!this.isNonSingular())
            {
                printError("Matrix is singular");
            }

            
            int count = value.length;
            double[] b = new double[count];
            for (int i = 0; i < b.length; i++)
            {
                b[i] = value[pivotVector[i]];
            }

            int rows = LU[0].length;
            int columns = LU[0].length;
            double[][] lu = LU;

            

            // Solve L*Y = B
            double[] X = new double[count];
            for (int i = 0; i < rows; i++)
            {
                X[i] = b[i];
                for (int j = 0; j < i; j++)
                {
                    X[i] -= lu[i][j]*X[j];
                }
            }

            // Solve U*X = Y;
            for (int i = rows - 1; i >= 0; i--)
            {
                //double sum = 0.0;
                for (int j = columns - 1; j > i; j--)
                {
                    X[i] -= lu[i][j] * X[j];
                }
                X[i] /= lu[i][ i];
            }
            return X;
        }
}
