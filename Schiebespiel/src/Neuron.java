
public abstract class Neuron {

	public static Neuron bias;
	
	public Neuron() {

	}
	
	// leaky ReLu
	public static double activationFunction(double x)
	{
		if (x >= 0)
			return x;
		return (0.05f * x);
	}
	
	public static double getAbltVonNetInput(double x)
	{
		return abltActivationFunction(arcActivationFunction(x));
	}
	
	private static double abltActivationFunction(double x)  // Ableitung
	{
		if (x>=0)
			return x;
		return 0.05f;
	}
	
	private static double arcActivationFunction(double x)  // Gegenteil, um netzinput zu bestimmen
	{
		if (x<0)
			return x*20;
		return x;
	}
	
	public static double calculate(double[] inputs)
	{
		double temp = 0;
		for (double f : inputs)
			temp += f;
		temp = activationFunction(temp);
		return temp;
	}
	
}
