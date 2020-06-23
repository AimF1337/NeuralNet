import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

	static Spielbrett game;
	
	public static void main(String[] args) 
	{	
//		Scanner scnr = new Scanner(System.in);
//		game = new Spielbrett(4);
//		game.initialiseBrett();
//		game.shuffle();
//		while (!checkWin(game))
//		{
//		System.out.print(drawBrett(game));
//		System.out.println(game.move(scnr.nextLine()));
//		}
//		System.out.println("ez win");
		
		int[] neuronMatrix = {16,2,3,4};
		float[] input = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		NeuralNetwork nn = new NeuralNetwork(neuronMatrix,1);
//		int temp = nn.compute(input);
//		System.out.println(temp);
	}
	
	public static String drawBrett(Spielbrett spielbrett)
	{
		String temp = "";
		Block[][] brett = spielbrett.getBrett();
		for (int i = 0; i < brett.length; i++)
		{
			for (int j = 0; j < brett[0].length; j++)
			{
				temp += brett[j][i].getValue();
				if (brett[j][i].getValue() < 10)
					temp += "    ";
				else 
					temp += "   ";
			}
			temp += "\n";
		}
		return temp;
	}
	
	public static boolean checkWin(Spielbrett spielbrett)
	{
		Block[][] brett = spielbrett.getBrett();
		for (int i = 0; i < brett.length; i++)
		{
			for (int j = 0; j < brett[0].length; j++)
			{
				Vector2 Pos = brett[j][i].getPos();
				if (brett[j][i].getValue() != Pos.x + (4*Pos.y)+1){
					if (i==brett.length-1 && j==brett.length-1)
						return true;
					else	
						return false;
				}
			}
		}
		return false;
	}

}
