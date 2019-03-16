package ssii.rna;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Some utility functions for the handwriting recognition problem
 * 
 *  Distributed under GPLv3 license
 * 
 * @author Jorge J. Gómez Sanz
 *
 */

public class Utils {

	/**
	 * It converts an array into a 2 dimensional char representation
	 * For pretty printing the array
	 * 
	 * @param array the array to pretty print
	 * @return the string representing the array
	 */
	public  static String getStringImageFromArray(double array[], int height) {
		String s="";
		int counter=0;
		for (double d: array) {
			s=s+(int)d+"";
			counter++;
			if (counter%height==0)
				s=s+"\n";
		}
		return s;				
	}

	/**
	 * This method is used to print the output of the ANN, the last layer 
	 * 
	 * @param array representing the output of a ANN
	 * @return the string representing this output
	 */
	public  static String getStringFromArray(double array[]) {
		String s="";
		for (double d: array) {
			s=s+d+",";
		}
		return s;				
	}

	/**
	 *  this reads the image from a path and converts it to a sequence of 0s and 1s
	 * @param path the file image
	 * @return the array representing the same image
	 */
	public static double[] getImage(String path) {
		double[] b = null;
		SimpleClassifier.imheight=0;
		try {
			BufferedImage bi = ImageIO.read(new File(path));
			int height = bi.getHeight();
			SimpleClassifier.imheight=height;
			int width = bi.getWidth();
			b = new double[height*width];
			for (int i = 0; i < height; i++)
				for (int j = 0; j < width; j++)
					if (bi.getRGB(j, i) != -1)
						b[i*height+j]=1.0;
					else
						b[i*height+j]=0.0;
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

}
