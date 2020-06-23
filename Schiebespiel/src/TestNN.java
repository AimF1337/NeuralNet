//Aus welchem Grund auch immer muss man beim Outputlayer ein neuron mehr hinzufügen als nötig 
//ich weiss nicht warum, aber es funktioniert
//
//
import java.util.concurrent.ThreadLocalRandom;


public class TestNN {

	public static void main(String[] args) 
	{
//		double[][][] weightMatrix = {	{	{3,1},			// looks like:	In		N		N		Out
//											{1,-3}	},		//				In		N		N		Out
//										{	{2,-2,-4},		//								N
//											{1,-3,1}	},		//
//										{	{-2,2},	
//											{1,2},
//											{1,-1}	},
//										{	{1},
//											{1}	}
//														};
		
		int[] neuronCount = {2,2,4,2};
		
		double[][] input = {{0,0},{0,1},{1,0},{1,1}};
		double[][] soll = {{0},{1},{1},{1}};
		
		NeuralNetwork NN = new NeuralNetwork(neuronCount,1);
		drawNeuralNetwork(NN);
//		drawWeightMatrix(NN);
//		drawNeuronMatrix(NN);
		double[] result;
		result = NN.compute(input[0]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
		result = NN.compute(input[1]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
		result = NN.compute(input[2]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
		result = NN.compute(input[3]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
//			result = NN.compute(input);		
//			System.out.println(result[0] + " " + result[1]);
//			NN.deltaLearning(soll, 0.01);
			
//		for (int i = 0; i<10000;i++)
//		{
//			for (int k = 0; k < input.length;k++)
//			{
//				result = NN.compute(input[k]);
//				System.out.println(result[0] + " " + i);
//				NN.deltaLearning(soll[k], 0.01);
//			}
//		}
		
		for (int i = 0; i<10000;i++)
		{
			NN.learnDataset(soll, input, 0.1);
//			drawWeightMatrix(NN);

		}
		result = NN.compute(input[0]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
		result = NN.compute(input[1]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
		result = NN.compute(input[2]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
		result = NN.compute(input[3]);			
		System.out.println(result[0]);
		NN.clearNeuronMatrix();
		
		
//		drawWeightMatrix(NN);
//		drawNeuronMatrix(NN);

	}
	
	public static void drawNeuronMatrix(NeuralNetwork NN)
	{
		String temp = "";
		double[][] neuronMatrix = NN.getNeuronMatrix();
		for (int i = 0; i < neuronMatrix.length;i++) // ohne outputlayer
		{
			for (int j = 0; j < neuronMatrix[i].length-1;j++)
			{
				temp += neuronMatrix[i][j] + "    ";
			}
			temp += "\n";
		}
		System.out.print(temp);
	}
	
	public static void drawWeightMatrix(NeuralNetwork NN)
	{
		String temp = "";
		double[][][] weightMatrix = NN.getWeightMatrix();
		for (int i = 0; i < weightMatrix.length-1;i++) // ohne outputlayer
		{
			for (int j = 0; j < weightMatrix[i].length;j++)
			{
				for (int k = 0; k < weightMatrix[i][j].length;k++)
				{
					temp += weightMatrix[i][j][k] + "    ";
				}
			}
			temp += "\n";
		}
		System.out.print(temp);
	}
	
	public static void drawNeuralNetwork(NeuralNetwork NN)
	{
		String temp = "";
		double[][] neuronMatrix = NN.getNeuronMatrix();
		for (int i = 0; i < neuronMatrix.length; i++)
		{
			if (i == 0) {
				for (double f : neuronMatrix[i])
					temp += "I    ";
			}
			else if(i > 0 && i < neuronMatrix.length-1) {
				for (double f : neuronMatrix[i])
					temp += "N    ";
			}
			else {
				for (double f : neuronMatrix[i])
					temp += "O    "; 
			}
			temp += "\n";
		}
		System.out.print(temp);
	}
	
	

}
