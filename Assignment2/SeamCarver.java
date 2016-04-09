import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

/**
 * Created by qq456cvb on 4/9/16.
 */
public class SeamCarver {

    private Color[][] colors;
    private double[][] energy;
    private boolean transposed;

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }
        colors = new Color[picture.height()][picture.width()];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                colors[j][i] = picture.get(i, j);
            }
        }
        transposed = false;
    }

    private void checkTranspose()
    {
        if (transposed) {
            energy = transposeMatrix(energies());
            colors = transposeMatrix(colors);
            transposed = false;
        }
    }

    private double[][] energies()
    {
        if (energy == null) {
            energy = new double[height()][width()];
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    energy[j][i] = energy(i, j);
                }
            }
        }
        return energy;
    }

    private double getDx2(int x, int y)
    {
        Color left = colors[y][x-1], right = colors[y][x+1];
        return Math.pow(right.getRed()-left.getRed(), 2)
                + Math.pow(right.getGreen()-left.getGreen(), 2)
                + Math.pow(right.getBlue()-left.getBlue(), 2);
    }

    private double getDy2(int x, int y)
    {
        Color up = colors[y-1][x], down = colors[y+1][x];
        return Math.pow(up.getRed()-down.getRed(), 2)
                + Math.pow(up.getGreen()-down.getGreen(), 2)
                + Math.pow(up.getBlue()-down.getBlue(), 2);
    }

    private double[][] transposeMatrix(double [][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    private Color[][] transposeMatrix(Color [][] m) {
        Color[][] temp = new Color[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    private void removeVerticalSeamImpl(int[] seam)
    {
        energies();
        int rowSize = colors[0].length-1;
        if (seam == null) {
            throw new java.lang.NullPointerException();
        }
        if (seam.length != colors.length) {
            throw new java.lang.IllegalArgumentException();
        }
        int lastIndex = -1;
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= rowSize+1) {
                throw new java.lang.IllegalArgumentException();
            }
            if (i > 0) {
                if (Math.abs(seam[i]-lastIndex) > 1) {
                    throw new java.lang.IllegalArgumentException();
                }
                lastIndex = seam[i];
            } else {
                lastIndex = seam[i];
            }
        }
        if (colors[0].length <= 1) {
            throw new java.lang.IllegalArgumentException();
        }

        for (int i = 0; i < seam.length; i++) {
            Color[] colorRow = new Color[rowSize];
            System.arraycopy(colors[i], 0, colorRow, 0, seam[i]);
            System.arraycopy(colors[i], seam[i]+1, colorRow, seam[i], colorRow.length-seam[i]);
            colors[i] = colorRow;
        }

        for (int i = 0; i < seam.length; i++) {
            double[] energyRow = new double[rowSize];
            System.arraycopy(energies()[i], 0, energyRow, 0, seam[i]);
            System.arraycopy(energies()[i], seam[i]+1, energyRow, seam[i], energyRow.length-seam[i]);
            energies()[i] = energyRow;
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] > 0)
                energies()[i][seam[i]-1] = energyImpl(seam[i]-1, i);
            if (seam[i] < colors[0].length-1)
                energies()[i][seam[i]] = energyImpl(seam[i], i);
        }
    }

    private int widthImpl()
    {
        return colors[0].length;
    }

    private int heightImpl()
    {
        return colors.length;
    }

    private double energyImpl(int x, int y)
    {
        if (x < 0 || x > widthImpl()-1 || y < 0 || y > heightImpl()-1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (x == 0 || x == widthImpl()-1 || y == 0 || y == heightImpl()-1)
        {
            return 1000;
        }
        double dx2 = getDx2(x, y), dy2 = getDy2(x, y);
        return Math.sqrt(dx2+dy2);
    }

    private int[] findVerticalSeamImpl()
    {
        double min = 1 << 31 - 1;
        double[][] score = new double[heightImpl()][widthImpl()];
        int[][] lastTo = new int[heightImpl()][widthImpl()];
        for (int i = 0; i < score.length; i++) {
            Arrays.fill(score[i], min);
        }
        Arrays.fill(score[0], 1000);
        for (int i = 0; i < heightImpl()-1; i++) {
            for (int j = 0; j < widthImpl(); j++) {
                for (int n = -1; n <= 1; n++) {
                    if (j == 0 && n == -1)
                        continue;
                    if (j == widthImpl()-1 && n == 1)
                        continue;
                    if (score[i][j] + energies()[i+1][j+n] < score[i+1][j+n]) {
                        score[i+1][j+n] = score[i][j] + energies()[i+1][j+n];
                        lastTo[i+1][j+n] = j;
                    }
                }
            }
        }
        int minPos = 0;
        for (int i = 0; i < widthImpl(); i++) {
            if (score[heightImpl()-1][i] < min) {
                min = score[heightImpl()-1][i];
                minPos = i;
            }
        }
        int[] minSeam = new int[heightImpl()];
        minSeam[minSeam.length-1] = minPos;
        for (int i = heightImpl()-2; i >= 0; i--) {
            minSeam[i] = lastTo[i+1][minPos];
            minPos = minSeam[i];
        }

        return minSeam;
    }

    public Picture picture()                          // current picture
    {
        checkTranspose();
        Picture picture = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                picture.set(i, j, colors[j][i]);
            }
        }
        return picture;
    }

    public int width()                            // width of current picture
    {
        checkTranspose();
        return widthImpl();
    }

    public int height()                           // height of current picture
    {
        checkTranspose();
        return heightImpl();
    }

    public  double energy(int x, int y)               // energy of pixel at column x and row y
    {
        checkTranspose();
        return energyImpl(x, y);
    }

    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        if (!transposed) {
            energy = transposeMatrix(energies());
            colors = transposeMatrix(colors);
        }
        int[] minSeam = findVerticalSeamImpl();
        transposed = true;
        return minSeam;
    }

    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        if (transposed) {
            energy = transposeMatrix(energies());
            colors = transposeMatrix(colors);
        }
        int[] minSeam = findVerticalSeamImpl();
        transposed = false;
        return minSeam;
    }

    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {

        if (!transposed) {
            energy = transposeMatrix(energies());
            colors = transposeMatrix(colors);
        }
        removeVerticalSeamImpl(seam);
        transposed = true;
    }

    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if (transposed) {
            energy = transposeMatrix(energies());
            colors = transposeMatrix(colors);
        }
        removeVerticalSeamImpl(seam);
        transposed = false;
    }
}
