package ssii.rna;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/*
 * Encog(tm) Java Examples v3.2
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2013 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 *
 * Codigo adaptado por Jorge J. Gomez Sanz
 * Reusando código de Manuel Pascual López 
 *
 */
public class SimpleClassifier {

	/**
	 * The neural network will be tested on these patterns, to see which of the last
	 * set they are the closest to.
	 * 
	 */
	public static final double[][] INPUT = { 
			Utils.getImage("images/arriba.png"), 
			Utils.getImage("images/abajo.png"),
			Utils.getImage("images/izquierda.png"), 
			Utils.getImage("images/derecha.png"), 
			Utils.getImage("images/arriba1.png"), 
			Utils.getImage("images/abajo1.png"),
			Utils.getImage("images/izquierda1.png"), 
			Utils.getImage("images/izquierdaa.png"), 
			Utils.getImage("images/derecha1.png"),
			Utils.getImage("images/izquierda2.png"),
			Utils.getImage("images/izquierda3.png"),  
			};


	
	/**
	 * The neural network will learn these patterns.
	 * We have four categories and each 1 in a row corresponds to the classification of 
	 * each pattern of the same row in INPUT 
	 */
	public static final double[][] RESULT = {
			// arriba, abajo, izquierda, derecha
			{ 1.0, 0.0, 0.0, 0.0 },
			{ 0.0, 1.0, 0.0, 0.0 },
			{ 0.0, 0.0, 1.0, 0.0 },
			{ 0.0, 0.0, 0.0, 1.0 },
			{ 1.0, 0.0, 0.0, 0.0 },
			{ 0.0, 1.0, 0.0, 0.0 },
			{ 0.0, 0.0, 1.0, 0.0 },
			{ 0.0, 0.0, 1.0, 0.0 },
			{ 0.0, 0.0, 0.0, 1.0 },
			{ 0.0, 0.0, 1.0, 0.0 },
			{ 0.0, 0.0, 1.0, 0.0 }};
	

	static int imheight;

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
	private static MLData classifyOneImage(BasicNetwork network, String imagePath) {
		MLData image=new org.encog.neural.data.basic.BasicNeuralData(Utils.getImage(imagePath));
		MLData output = network.compute(image);		
		System.out.println(Utils.getStringImageFromArray(image.getData(),imheight) + 
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
	private static NeuralDataSet trainANN(BasicNetwork network) {
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
		} while (train.getError() > 0.0001);
		return trainingSet;
	}

	/**
	 * It creates a multi-layer ANN with 4 output neurons and as many inputs as cells
	 * in the input array that represents the image 
	 * 
	 * @return
	 */
	private static BasicNetwork createANN() {
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, INPUT[0].length));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 4));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 4));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, RESULT[0].length));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}
	
	public static void main(String[] args) {
		BasicNetwork network = createANN();
		NeuralDataSet trainingSet = trainANN(network);
		// it tests the network and shows you how well it performs
		testNetworkwithTrainingExamples(network, trainingSet);
		// It takes a random image (tested or not) to see how it works
		classifyOneImage(network,"images/izquierdaa.png");		
	}
}
