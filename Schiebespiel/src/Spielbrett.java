import java.util.concurrent.ThreadLocalRandom;


public class Spielbrett 
{
	
	private Block[][] Brett; 
	public int length;
	private Vector2 zeroPos;

	public Spielbrett(int length)
	{
		this.Brett = new Block[length][length];
		this.length = length;
	}
	
	public void initialiseBrett()
	{
		for(int i = 0;i < Brett.length; i++)
		{
			for(int j = 0; j < Brett.length; j++)
			{
				Brett[j][i] = new Block(j + (4*i)+1,j,i);
			}
		}
		zeroPos = new Vector2(length-1,length-1);
		Brett[zeroPos.x][zeroPos.y].setValue(0);
	}

	public Block[][] getBrett() {
		return Brett;
	}
	
	// possible moves include: up,down,left,right
	public String move(String dir)
	{
		if (dir.equals("up") || dir.equals("u"))
		{
			if (zeroPos.y == 0)
				return "impossible command";
			Brett[zeroPos.x][zeroPos.y].setValue(Brett[zeroPos.x][zeroPos.y-1].getValue()); 
			zeroPos.y -= 1;
		}else if (dir.equals("down") || dir.equals("d"))
		{
			if (zeroPos.y == length-1)
				return "impossible command";
			Brett[zeroPos.x][zeroPos.y].setValue(Brett[zeroPos.x][zeroPos.y+1].getValue()); 
			zeroPos.y += 1;
		}else if (dir.equals("left") || dir.equals("l"))
		{
			if (zeroPos.x == 0)
				return "impossible command";
			Brett[zeroPos.x][zeroPos.y].setValue(Brett[zeroPos.x-1][zeroPos.y].getValue()); 
			zeroPos.x -= 1;
		}else if (dir.equals("right") || dir.equals("r"))
		{
			if (zeroPos.x == length-1)
				return "impossible command";
			Brett[zeroPos.x][zeroPos.y].setValue(Brett[zeroPos.x+1][zeroPos.y].getValue()); 
			zeroPos.x += 1;
		}else
			return "unknown command";
		
		Brett[zeroPos.x][zeroPos.y].setValue(0);
		return dir;
	}
	
	public void shuffle() {
		for (int i = 0; i<1000; i++)
		{
			int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
			switch (randomNum) {
			case 0:
				move("up");
				break;
			case 1:
				move("down");
				break;
			case 2:
				move("left");
				break;
			case 3:
				move("right");
				break;
			default:
				break;
			}
		}
	}
	
	public Vector2 getZeroPos()
	{
		return zeroPos;
	}
	
}
