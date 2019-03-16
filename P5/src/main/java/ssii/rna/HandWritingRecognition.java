package ssii.rna;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 * 
 * Based on the ENCOG framework. Distributed under GPLv3
 * 
 * This class performs basic pattern recognition to identify handwritten
 * numbers. It launches a GUI so that users can write down numbers. The recognition
 * button triggers the ANN recognition of the written number
 * 
 * @author Jorge J. Gómez Sanz
 *
 */
public class HandWritingRecognition {
	/** 
	 * Sample images
	 */
	public static final double[][] INPUT = { 
			Utils.getImage("images/uno.png"), 
			Utils.getImage("images/uno1.png"),
			Utils.getImage("images/uno2.png"), 
			Utils.getImage("images/uno3.png"),
			Utils.getImage("images/uno4.png"),
			Utils.getImage("images/uno5.png"),
			Utils.getImage("images/dos.png"), 
			Utils.getImage("images/dos1.png"),
			Utils.getImage("images/dos2.png"), 
			Utils.getImage("images/dos3.png"), 
			Utils.getImage("images/dos4.png"),
			Utils.getImage("images/dos5.png"),
			};
	/**
	 * The neural network will learn these patterns.
	 * We have four categories and each 1 in a row corresponds to the classification of 
	 * each pattern of the same row in INPUT 
	 */
	public static final double[][] RESULT = {
			// number 1, number 2
			{ 1.0, 0.0},
			{ 1.0, 0.0},
			{ 1.0, 0.0},
			{ 1.0, 0.0},
			{ 1.0, 0.0},
			{ 1.0, 0.0},
			{ 0.0, 1.0},
			{ 0.0, 1.0},
			{ 0.0, 1.0},
			{ 0.0, 1.0},
			{ 0.0, 1.0},
			{ 0.0, 1.0}};	
	
	private static int imheight;
	
	
	/**
	 * Evaluates the network against a training set
	 * 
	 * @param network
	 * @param trainingSet
	 */
	private static void testNetworkwithTrainingExamples(BasicNetwork network, NeuralDataSet trainingSet) {
		System.out.println("Neural Network Results:");
		for (MLDataPair pair : trainingSet) {
			final MLData output = network.compute(pair.getInput());
			System.out.println(Utils.getStringImageFromArray(pair.getInput().getData(),imheight) + 
					", computed="
					+ Utils.getStringFromArray(output.getData())
					+"\n" + ",ideal=" +
					Utils.getStringFromArray(pair.getIdeal().getData()));

		}
					
	}
	
	/** 
	 * Given a trained network, it applies this network to recognise an impage
	 * It returns the activation of the network
	 * 
	 * @param network a trained ANN
	 * @param imagePath the file to recognise
	 * @return the activation of the network
	 */
	private static MLData classifyOneImage(BasicNetwork network, double[] inputImage, int imageheight) {
		MLData image=new org.encog.neural.data.basic.BasicNeuralData(inputImage);
		MLData output = network.compute(image);		
		System.out.println(Utils.getStringImageFromArray(image.getData(),imageheight) + 
				", computed="
				+ Utils.getStringFromArray(output.getData()));
		return output;
	}
	

	/**
	 * It trains the network with the examples defined in INPUT and RESULT attributes
	 * 
	 * @param network
	 * @return
	 */
	private static NeuralDataSet trainANN(BasicNetwork network, double error) {
		int lastlength=-1;
		int counter=0;
		for (double [] image:INPUT) {
			
			if (lastlength==-1)
					lastlength=image.length;
			if (lastlength!=image.length)
				throw new RuntimeException("The image "+counter+ " (being 0 the first) has a size different from the rest");
				counter++;
		}
		NeuralDataSet trainingSet = new BasicNeuralDataSet(INPUT, RESULT);
		final Train train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;
		do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while (train.getError() > error);
		return trainingSet;
	}
	

	/**
	 * It creates a multi-layer ANN with 3 output neurons and as many inputs as cells
	 * in the input array that represents the image 
	 * 
	 * @return
	 */
	private static BasicNetwork createANN() {
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, INPUT[0].length));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 50));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 50));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 50));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, RESULT[0].length));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	/**
	 * It trains an ANN to recognise numbers. Then it launches the GUI so that
	 * an operator can draw the numbers and make it recognise them.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		final BasicNetwork network = createANN();
		final NeuralDataSet trainingSet = trainANN(network,0.000001d);
		final FreeHandCanvas fhc=new FreeHandCanvas();
		fhc.getButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				MLData result = classifyOneImage(network,fhc.getImage(),150);
				int maxindex=0;
				double maxval=Double.MIN_VALUE;
				for (int k=0;k<result.getData().length;k++)
					if (result.getData()[k]>maxval) {
						maxindex=k;
						maxval=result.getData()[k];
					}
				
				if (maxval>0.5) {
					switch (maxindex) {
					case 0:fhc.setResult("One");break;
					case 1:fhc.setResult("Two");break;				
					}
				}else 					
					fhc.setResult("Unknown");
				

				
			}
			
		});
		fhc.show();

				
	}
	
}
