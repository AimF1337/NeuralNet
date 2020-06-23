
public class Block {

	private int value;
	private Vector2 Pos;
	
	public Block(int value, int xPos, int yPos)
	{
		this.setValue(value);
		Pos = new Vector2 (xPos,yPos);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Vector2 getPos() {
		return Pos;
	}

	public void setPos(Vector2 pos) {
		Pos = pos;
	}
	
}
