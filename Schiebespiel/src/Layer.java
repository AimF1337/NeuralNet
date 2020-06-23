import java.util.Arrays;

public class Layer {

	public int NeuronCount;
	public float[][] inputMatrix;
	
	public Layer(int Neurons, Layer lastLayer)
	{
		this.NeuronCount = Neurons;
		if (lastLayer == null)
			setupInputWeightMatrix(1); // nur der InputLayer hat keine Inputs, deswegen 0.
		else if (lastLayer != null)
			setupInputWeightMatrix(lastLayer.NeuronCount);
	}

	private void setupInputWeightMatrix(int inputNeurons)
	{
		this.inputMatrix = new float[inputNeurons][NeuronCount]; // +1 für BiasNeuron
		for (float[] f : inputMatrix)
			Arrays.fill(f, 1);
	}
	
	public float[][] getInputMatrix()
	{
		return inputMatrix;
	}
	
	public float[] calculate(float[] input)
	{
		float[] temp = new float[NeuronCount];
		for (int i=0;i<inputMatrix.length;i++)
		{
			for (int j=0;j<inputMatrix[0].length;j++)
			{
				temp[j] += input[i] * inputMatrix[i][j];
			}
		}
		
		for (int f=0;f<temp.length;f++)
			temp[f] = activation(temp[f]);
			
		return temp;
	}
	
	// leaky ReLu
	public float activation(float x)
	{
		if (x >= 0)
			return x;
		else
			return (0.01f * x);
	}
}
