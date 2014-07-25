
import java.awt.Color;

public class SeamCarver {
    private int width, height;
    private int[][] RGB;
    public SeamCarver(Picture picture) {
       width = picture.width();
       height = picture.height();
       RGB = new int[height][width];
       for (int i = 0; i < height; i++)
           for (int j = 0; j < width; j++){
               RGB[i][j] = picture.get(j, i).getRGB();
           }
    }
    private int getRed(int RGB) {
        return (RGB >> 16) & 255;
    }
    private int getGreen(int RGB) {
        return (RGB >> 8 ) & 255;
    }
    private int getBlue(int RGB) {
        return RGB & 255;
    }
    public Picture picture() {
        // current picture  
        Picture newPicture = new Picture(width, height);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) 
                newPicture.set(j, i, new Color(RGB[i][j]));
        return newPicture;
    }
    public int width() {
        // width  of current picture
        return width;
    }
    public int height() {
        // height of current picture
        return height;
    }
    public double energy(int x, int y) {
        // energy of pixel at column x and row y in current picture
        if (x < 0 || y < 0 || x >= width || y >= height)
            throw new IndexOutOfBoundsException();
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) 
            return 195075.0;
        else {
            int left = RGB[y][x - 1];
            int right= RGB[y][x + 1];
            int up = RGB[y - 1][x];
            int down= RGB[y + 1][x];
            return Math.pow(getRed(left) - getRed(right), 2.0) 
                    + Math.pow(getGreen(left) - getGreen(right), 2.0) 
                    + Math.pow(getBlue(left) - getBlue(right), 2.0) 
                    + Math.pow(getRed(up) - getRed(down), 2.0)
                    + Math.pow(getGreen(up) - getGreen(down), 2.0)
                    + Math.pow(getBlue(up) - getBlue(down), 2.0);
        }
    }
    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam in current picture
        double[][] energy = new double[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                energy[i][j] = energy(j , i);
        int[] H = new int[width];
        int[][] from = new int[height][width];
        for (int j = 1; j < width; j++) {
            for (int i = 0; i < height; i++) {
                if (i == 0) {
                    if (energy[i][j - 1] <= energy[i + 1][j - 1]) {
                        energy[i][j] = energy[i][j - 1] + energy[i][j];
                        from[i][j] = i;
                    }
                    else {
                        energy[i][j] = energy[i + 1][j - 1] + energy[i][j];
                        from[i][j] = i + 1;
                    }
                }
                else if (i == height - 1) {
                    if (energy[i - 1][j - 1] <= energy[i][j - 1]) {
                        energy[i][j] = energy[i - 1][j - 1] + energy[i][j];
                        from[i][j] = i - 1;
                    }
                    else {
                        energy[i][j] = energy[i][j - 1] + energy[i][j];
                        from[i][j] = i;
                    }
                }
                else {
                    if (energy[i - 1][j - 1] <= energy[i][j - 1]) {
                        if (energy[i - 1][j - 1] <= energy[i + 1][j - 1]) {
                            energy[i][j] = energy[i - 1][j - 1] + energy[i][j];
                            from[i][j] = i - 1;
                        }
                        else {
                            energy[i][j] = energy[i + 1][j - 1] + energy[i][j];
                            from[i][j] = i + 1;
                        }
                    }
                    else if (energy[i][j - 1] <= energy[i + 1][j - 1]) {
                        energy[i][j] = energy[i][j - 1] + energy[i][j];
                        from[i][j] = i;
                    }
                    else {
                        energy[i][j] = energy[i + 1][j - 1] + energy[i][j];
                        from[i][j] = i + 1;
                    }
                }
            }
        }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < height; i++) 
            if (energy[i][width - 1] < min) {
                H[width - 1] = i;
                min = energy[i][width - 1];
            }
        for (int j = width - 2; j >= 0; j--) {
            H[j] = from[H[j + 1]][j + 1];
        }
        return H;
    }
    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam in current picture
        double[][] energy = new double[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                energy[i][j] = energy(j , i);
        int[] V = new int[height];
        int[][] from = new int[height][width];
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j == 0) {
                    if (energy[i - 1][j] <= energy[i - 1][j + 1]) {
                        energy[i][j] = energy[i - 1][j] + energy[i][j];
                        from[i][j] = j;
                    }
                    else {
                        energy[i][j] = energy[i - 1][j + 1] + energy[i][j];
                        from[i][j] = j + 1;
                    }
                }
                else if (j == width - 1) {
                    if (energy[i - 1][j] <= energy[i - 1][j - 1]) {
                        energy[i][j] = energy[i - 1][j] + energy[i][j];
                        from[i][j] = j;
                    }
                    else {
                        energy[i][j] = energy[i - 1][j - 1] + energy[i][j];
                        from[i][j] = j - 1;
                    }
                }
                else {
                    if (energy[i - 1][j - 1] <= energy[i - 1][j]) {
                        if (energy[i - 1][j - 1] <= energy[i - 1][j + 1]) {
                            energy[i][j] = energy[i - 1][j - 1] + energy[i][j];
                            from[i][j] = j - 1;
                        }
                        else {
                            energy[i][j] = energy[i - 1][j +1] + energy[i][j];
                            from[i][j] = j + 1;
                        }
                    }
                    else if (energy[i - 1][j] <= energy[i - 1][j + 1]) {
                        energy[i][j] = energy[i - 1][j] + energy[i][j];
                        from[i][j] = j;
                    }
                    else {
                        energy[i][j] = energy[i - 1][j +1] + energy[i][j];
                        from[i][j] = j + 1;
                    }
                }
            }
        }
        double min = Double.MAX_VALUE;
        for (int j = 0; j < width; j++) 
            if (energy[height - 1][j] < min) {
                V[height - 1] = j;
                min = energy[height - 1][j];
            }
        for (int i = height - 2; i >= 0; i--) {
            V[i] = from[i + 1][V[i + 1]];
        }
        return V;
    }
    public void removeHorizontalSeam(int[] a) {
        // remove horizontal seam from current picture
        if (a.length != width || height <= 1) 
            throw new IllegalArgumentException();
        for (int j = 0; j < width - 1; j++)
            if (Math.abs(a[j] - a[j + 1]) > 1 || a[j] < 0 || a[j] >= height) 
                throw new IllegalArgumentException();
        if (a[width - 1] < 0 || a[width - 1] >= height)
            throw new IllegalArgumentException();
        for (int j = 0; j < width; j++) {
            for (int i = a[j]; i < height - 1; i++)
                RGB[i][j] = RGB[i + 1][j];
        }
        height--;
    }
    public void removeVerticalSeam(int[] a) {
        // remove vertical seam from current picture
        if (a.length != height || width <= 1) 
            throw new IllegalArgumentException();
        for (int i = 0; i < height - 1; i++)
            if (Math.abs(a[i] - a[i + 1]) > 1 || a[i] < 0 || a[i] >= width) 
                throw new IllegalArgumentException();
        if (a[height - 1] < 0 || a[height - 1] >= width)
            throw new IllegalArgumentException();
        for (int i = 0; i < height; i++) {
            for (int j = a[i]; j < width - 1; j++)
                RGB[i][j] = RGB[i][j + 1];
        }
        width--;
    }
}
