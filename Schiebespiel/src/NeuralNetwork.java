import java.util.concurrent.ThreadLocalRandom;


public class NeuralNetwork {
	
	private Layer[] hiddenLayers;
	private Layer inputLayer;
	private Layer outputLayer;
	public static double biasNeuron = 1;
	
	private double[][][] weightMatrix;    // [#Layers][#NeuronsInLayer][#NeuronsInNextLayer]
	private double[][] neuronMatrix;
	private double[][] smallDelta;
	private double[][][][] weightDelta; //average of all deltas in 1 training set
	private int setSize;
	
	public NeuralNetwork(int[] neuronCountPerLayer, int setSize)
	{
//		this.inputLayer = new Layer(neuronCountPerLayer[0],null);
//		this.hiddenLayers = new Layer[neuronCountPerLayer.length - 2];
//		hiddenLayers[0] = new Layer(neuronCountPerLayer[1],inputLayer);
//		setupLayers(neuronCountPerLayer);
//		this.outputLayer = new Layer(neuronCountPerLayer[neuronCountPerLayer.length-1],hiddenLayers[hiddenLayers.length-1]);
//		
//		for (Layer l : hiddenLayers) {
//			l.inputMatrix = setupWeights(l.getInputMatrix());
//		}
//		outputLayer.inputMatrix = setupWeights(outputLayer.getInputMatrix());
		
		this.weightMatrix = setupWeightMatrix(neuronCountPerLayer);
		this.weightMatrix = setupRandomWeights(this.weightMatrix);
		this.neuronMatrix = setupNeuronMatrix(neuronCountPerLayer);
		this.smallDelta = setupSmallDeltaMatrix(neuronCountPerLayer);
		this.setSize = setSize;
		this.weightDelta = setupWeightDelta(weightMatrix);
		
	}
	
	public NeuralNetwork(double[][][] weightMatrix)
	{
		this.weightMatrix = weightMatrix;
		this.neuronMatrix = setupNeuronMatrix(weightMatrix);
//		this.smallDelta = setupSmallDeltaMatrix(weightMatrix); TODO
	}
	
	private double[][][] setupWeightMatrix(int[] neuronCountPerLayer)
	{
		double[][][] weightMatrix = new double[neuronCountPerLayer.length][][];  // [layer][inputneuron][outputneuron]
		for (int i = 0; i < weightMatrix.length - 1; i++)  // -1 weil kein output weight auf outputneuron
		{
			weightMatrix[i] = new double[neuronCountPerLayer[i]+1][neuronCountPerLayer[i+1]]; // [][+1][] für den BiasNeuron
		}
		weightMatrix[weightMatrix.length-1] = new double[neuronCountPerLayer[neuronCountPerLayer.length-1]][1]; // outputlayer
		return weightMatrix;
	}
	
	private double[][] setupNeuronMatrix(int[] neuronCountPerLayer)
	{
		double[][] temp = new double[neuronCountPerLayer.length][];  // [layers][neurons]
		for (int i = 0; i < temp.length-1; i++)
			temp[i] = new double[neuronCountPerLayer[i]+1]; // +1 für Biasneuron
		temp[temp.length-1] = new double[neuronCountPerLayer[neuronCountPerLayer.length-1]];  // Output-layer hat keine Bias
		for (int j = 0; j < temp.length; j++)
			temp[j][temp[j].length-1] = 1;   // Setze BiasNeuron-wert auf 1
		
		return temp;
	}
	
	private double[][] setupSmallDeltaMatrix(int[] neuronCountPerLayer)
	{
		double[][] temp = new double[neuronCountPerLayer.length][];  // [layers][neurons]
		for (int i = 0; i < temp.length; i++)
			temp[i] = new double[neuronCountPerLayer[i]]; // keine Biasneurons
		
		return temp;
	}
	
	private double[][][][] setupWeightDelta(double[][][] weightMatrix)
	{
		double[][][][] weightDelta = new double[weightMatrix.length][][][];
		for (int i = 0; i < weightMatrix.length; i++)
		{
			for (int j = 0; j < weightMatrix[i].length; j++)
			{
				weightDelta[i] = new double [weightMatrix[i].length][weightMatrix[i][j].length][setSize];
			}
		}
		return weightDelta;
	}
	
	private double[][] setupNeuronMatrix(double[][][] weightMatrix) // TODO BiasNeuron implementieren!!
	{
		int[] neuronCountPerLayer = new int[weightMatrix.length];
		for (int i = 0; i < weightMatrix.length; i++)
		{
			neuronCountPerLayer[i] = weightMatrix[i].length;
		}
		return setupNeuronMatrix(neuronCountPerLayer);
	}

	
	private double[][][] setupRandomWeights(double[][][] weightMatrix)
	{
		double [][][] temp = weightMatrix;
		for (int i = 0; i < weightMatrix.length; i++)
		{
			for (int j = 0; j < weightMatrix[i].length; j++)
			{
				for (int k = 0; k < weightMatrix[i][j].length; k++)
				{
					temp[i][j][k] = (ThreadLocalRandom.current().nextDouble(-100, 100) / 100);
				}
			}
		}
		return temp;
	}
	
	public double[] compute(double[] input)
	{	
//		if (input.length != neuronMatrix[0].length) {
//			System.out.println("Input.length does not match Number of InputNeurons! Errorcode: 5");
//			return null;
//		}
		
		double[] result = new double[weightMatrix[weightMatrix.length-1].length-1];  // Neurons im letzten Layer //-1:siehe TestNN comment
		
		for (int c = 0; c < neuronMatrix[0].length-1; c++) // feed input ; -1 wegen BiasNeuron
		{
			neuronMatrix[0][c] = input[c];
		}
		for (int i = 0; i < weightMatrix.length-1; i++)  // i = Layer
		{
			for (int j = 0; j < weightMatrix[i].length; j++)  // j = InputNeuron
			{
				for (int k = 0; k < weightMatrix[i][j].length; k++)  // k = OutputNeuron
				{
					neuronMatrix[i+1][k] += weightMatrix[i][j][k] * neuronMatrix[i][j];
				}
			}
			for (int l = 0; l < neuronMatrix[i+1].length; l++)
			{
				neuronMatrix[i+1][l] = Neuron.activationFunction(neuronMatrix[i+1][l]);
				neuronMatrix[i+1][l] = round(neuronMatrix[i+1][l], 6);
			}
		}
		
		result = neuronMatrix[neuronMatrix.length-1];
		
		return result;
	}
	
	
	public void deltaLearning(double[] soll, double epsilon, int iter) // int iter = iterator um average zu bestimmen
	{
		smallDelta = calcSmallDelta(soll);
		for (int i = 0; i < weightMatrix.length-1;i++) // anfangen bei vorletztem layer
		{
			for (int j = 0; j < weightMatrix[i].length;j++)
			{
				for (int k = 0; k < weightMatrix[i][j].length;k++)
				{
					double deltaW = epsilon * smallDelta[i+1][k] * neuronMatrix[i][j];
					weightDelta[i][j][k][iter] = deltaW;
//					weightMatrix[i][j][k] += deltaW;
//					weightMatrix[i][j][k] = round(weightMatrix[i][j][k],6);
				}
			}
		}
		clearNeuronMatrix();
	}
	
	public void learnDataset(double[][] soll, double[][] dataset,double epsilon)
	{
		double[] result;
		for (int i = 0; i < dataset.length; i+=setSize)
		{
			for (int k = 0; k < setSize; k++)
			{
				result = compute(dataset[i+k]);
				deltaLearning(soll[i+k],epsilon,k);
			}
			changeWeights();
		}
	}
	
	private void changeWeights()
	{
		for (int i = 0; i < weightMatrix.length;i++)
		{
			for (int j = 0; j < weightMatrix[i].length;j++)
			{
				for (int k = 0; k < weightMatrix[i][j].length;k++)
				{
					weightMatrix[i][j][k] += average(i,j,k);
//					weightMatrix[i][j][k] = round(weightMatrix[i][j][k],6);
				}
			}
		}
		for (int i = 0;i < weightDelta.length;i++)  // setze weightDelta auf 0 zur�ck
		{
			for (int j = 0;j < weightDelta[i].length;j++)
			{
				for (int k = 0; k < weightDelta[i][j].length; k++)
				{
					for (int l = 0; l < weightDelta[i][j][k].length; l++)
					{
						weightDelta[i][j][k][l] = 0;
					}
				}
			}
		}
	}
	
	private double average(int layer, int inNeuron, int outNeuron)
	{
		double temp = 0;
		for (int i = 0; i < weightDelta[layer][inNeuron][outNeuron].length; i++)
		{
			temp += weightDelta[layer][inNeuron][outNeuron][i]; 
		}
		temp = temp/weightDelta[layer][inNeuron][outNeuron].length;
		return temp;
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	private double[][] calcSmallDelta(double[] soll)
	{
		double [][] temp = smallDelta;
		for (int i = neuronMatrix.length-1; i > 0; i--) // anfangen mit letztem Layer
		{
			for (int j = 0; j < neuronMatrix[i].length-1; j++) // j = neuron ; -1 wegen Biasneuron
			{
				 if (isOutputNeuron(i)) // d = f'(netzinput) * (soll - ist)
					 temp[i][j] = Neuron.getAbltVonNetInput(neuronMatrix[i][j]) * (soll[j] - neuronMatrix[i][j]);
				 else // d = f'(netzinput) * (Summe von deltas * weights)
					 temp[i][j] = Neuron.getAbltVonNetInput(neuronMatrix[i][j]) * calcSumOfLayer(i,j,temp[i+1]);
			}
		}
		return temp;
	}
	
	private double calcSumOfLayer(int layer, int neuron, double[] delta) // Summe von deltas * weights
	{
		double temp = 0;
		for (int i = 0; i < delta.length; i++)
		{
			temp += weightMatrix[layer][neuron][i] * delta[i];
		}
		return temp;
	}
	
	public void clearNeuronMatrix()
	{
		for (int i = 0;i < neuronMatrix.length;i++)  // setze NeuronMatrix wieder zurück
		{
			for (int j = 0;j < neuronMatrix[i].length-1;j++)
			{
				neuronMatrix[i][j] = 0;
			}
			neuronMatrix[i][neuronMatrix[i].length-1] = 1; // Setze Bias Neuron auf 1
		}
	}
	
	
	private int chooseBiggest(double[] result)
	{
		double temp = result[0];
		int tempPos = 0;
		for (int i = 1; i < result.length; i++)
		{
			if ( result[i] > temp)
			{
				temp = result[i];	
				tempPos = i;
			}
		}
		return tempPos;
	}
	
	public double[][] getNeuronMatrix()
	{
		return this.neuronMatrix;
	}
	public double[][][] getWeightMatrix()
	{
		return this.weightMatrix;
	}
	
	private boolean isOutputNeuron(int layer)
	{
		if (layer == neuronMatrix.length-1)
			return true;
		return false;
	}
	
}
