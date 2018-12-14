/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

import java.lang.Exception;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Matrix 
{    
        public static double[][] Multiply(double[][] a, double[][] b)
        {
            double[][] r = new double[a.length][ b[0].length];
            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < b[0].length; j++)
                    for (int k = 0; k < a[0].length; k++)
                        r[i][j] += a[i][k] * b[k][j];

            return r;
        }

        public static double[][] Multiply(double[][] a, double x)
        {
            double[][] r = new double[a.length][a[0].length];
            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < a[0].length; j++)
                        r[i][j] += a[i][j] * x;

            return r;
        }

        public static double[][] Sum(double[][] a, double x)
        {
            double[][] r = new double[a.length][a[0].length];
            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < a[0].length; j++)
                    r[i][j] += a[i][j] + x;

            return r;
        }

        public static double[][] Sum(double[][] a, double[][] b)
        {
            double[][] r = new double[a.length][a[0].length];

            for (int i = 0; i < a.length; i++)
                for (int j = 0; j < a[0].length; j++)
                    r[i][j] += a[i][j] + b[i][j];

            return r;
        }

        private static void printError(String message)
        {
            System.out.println("Error:" + message);
        }
        
        public static double[][] Submatrix( double[][] data, int startRow, int endRow, int startColumn, int endColumn) 
        {
            int rows = data.length;
            int cols = data[0].length;

            if ((startRow > endRow) || (startColumn > endColumn) || (startRow < 0) ||
                (startRow >= rows) || (endRow < 0) || (endRow >= rows) ||
                (startColumn < 0) || (startColumn >= cols) || (endColumn < 0) ||
                (endColumn >= cols)) 
            {
             
                   printError("Lebih dari Batas");
                
            }

            double[][] X = new double[endRow - startRow + 1][endColumn - startColumn + 1];
            for (int i = startRow; i <= endRow; i++)
            {
                for (int j = startColumn; j <= endColumn; j++)
                {
                    X[i - startRow][j - startColumn] = data[i][j];
                }
            }

            return X;
        }

      
        public static double[][] Submatrix(double[][] data, int[] rowIndexes, int[] columnIndexes)
        {
            double[][] X = new double[rowIndexes.length][columnIndexes.length];

            for (int i = 0; i < rowIndexes.length; i++)
            {
                for (int j = 0; j < columnIndexes.length; j++)
                {
                    if ((rowIndexes[i] < 0) || (rowIndexes[i] >= data.length) ||
                        (columnIndexes[j] < 0) || (columnIndexes[j] >= data[0].length))
                    {
                         
                          printError("Argument out of range.");
                    }

                    X[i][j] = data[rowIndexes[i]][columnIndexes[j]];
                }
            }

            return X;
        }

 
        public static double[][] Submatrix(double[][] data, int i0, int i1, int[] c)
        {
            if ((i0 > i1) || (i0 < 0) || (i0 >= data.length)
                || (i1 < 0) || (i1 >= data.length))
            {
                  printError("Argument out of range.");
            }

            double[][] X = new double[i1 - i0 + 1][c.length];

            for (int i = i0; i <= i1; i++)
            {
                for (int j = 0; j < c.length; j++)
                {
                    if ((c[j] < 0) || (c[j] >= data[0].length))
                    {
                         printError("Argument out of range.");
                    }

                    X[i - i0][j] = data[i][c[j]];
                }
            }

            return X;
        }

 
        public static double[][] Submatrix(double[][] data, int[] r, int j0, int j1)
        {
            if ((j0 > j1) || (j0 < 0) || (j0 >= data[0].length) || (j1 < 0)
                || (j1 >= data[0].length))
            {
                  printError("Argument out of range.");
            }

            double[][] X = new double[r.length][j1 - j0 + 1];

            for (int i = 0; i < r.length; i++)
            {
                for (int j = j0; j <= j1; j++)
                {
                    if ((r[i] < 0) || (r[i] >= data.length))
                    {
                          printError("Argument out of range.");
                    }

                    X[i][j - j0] = data[r[i]][j];
                }
            }

            return X;
        }

        public static boolean IsSquare(double[][] matrix)
        {
            return matrix.length == matrix[0].length;
        }

        public static boolean IsSymmetric(double[][] matrix)
        {
            if (IsSquare(matrix))
            {
                for (int i = 0; i < matrix.length; i++)
                {
                    for (int j = 0; j <= i; j++)
                    {
                        if (matrix[i][j] != matrix[j][i])
                        {
                            return false;
                        }
                    }
                }
                return true;
            }
            return false;
        }

       
        public static double[][] Inverse(double[][] m)
        {
            return Solve(m,Matrix.Diagonal(m.length, 1.0));
        }

      
        public static double[][] Solve(double[][] m, double[][] rightSide)
        {
            return (m.length == m[0].length) ?
                new LuDecomposition(m).Solve(rightSide) :
                new QrDecomposition(m).Solve(rightSide);
        }

        
        public static double[][] Diagonal(int size, double value)
        {
            double[][] m = new double[size][size];

            for (int i = 0; i < size; i++)
                m[i][ i] = value;

            return m;
        }

        public static double[] GetColumn(double[][] m, int index)
        {
            double[] column = new double[m.length];

            for (int i = 0; i < column.length; i++)
            {
                column[i] = m[i][index];
            }
            return column;
        }

        public static double[] GetRow(double[][] m, int index)
        {
            double[] row = new double[m[0].length];

            for (int i = 0; i < row.length; i++)
            {
                row[i] = m[index][i];
            }
            return row;
        }

        public static double[][] Transpose(double[][] m)
        {
            double[][] t = new double[m[0].length][m.length];
            for (int i = 0; i < m.length; i++)
            {
                for (int j = 0; j < m[0].length; j++)
                {
                    t[j][i] = m[i][j];
                }
            }
            return t;
        }

 
        public static double Trace(double[][] m)
        {
            double trace = 0.0;
            for (int i = 0; i < m.length; i++)
            {
                trace += m[i][i];
            }
            return trace;
        }
}
