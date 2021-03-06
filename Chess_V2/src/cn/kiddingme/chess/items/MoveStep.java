package cn.kiddingme.chess.items;

public class MoveStep {
	private int type=0;//1 move step,2 some chessman was killed
	private int from_X=0;
	private int from_Y=0;
	private ChessMan role=null;
	private int to_X=0;
	private int to_Y=0;
	private int weight=0;
	public MoveStep(int type,int from_X,int from_Y,ChessMan role){
		this.type=type;
		this.from_X=from_X;
		this.from_Y=from_Y;
		this.role=role;
	}
	public MoveStep(int Type,int from_X,int from_Y,ChessMan role,int to_X,int to_Y,int weight)
	{
		this.type=type;
		this.from_X=from_X;
		this.from_Y=from_Y;
		this.role=role;
		this.to_X=to_X;
		this.to_Y=to_Y;
		this.weight=weight;
	}
	public MoveStep(int Weight)
	{
		this.weight=Weight;
	}
	public void setToXY(int to_X,int to_Y)
	{
		this.to_X=to_X;
		this.to_Y=to_Y;
	}
	public void setWeight(int weight)
	{
		this.weight=weight;
	}
	public int getFromX()
	{
		return this.from_X;
	}
	public int getFromY()
	{
		return this.from_Y;
	}
	public int getToX()
	{
		return this.to_X;
	}
	public int getToY()
	{
		return this.to_Y;
	}
	public int getType()
	{
		return this.type;
	}
	public ChessMan getChessMan()
	{
		return this.role;
	}
	public int getWeight(){
		return this.weight;
	}
	public String toString(){
		return "fromX:"+this.from_X+" fromY:"+this.from_Y+" toX:"+this.to_X+" toY:"+this.to_Y+" chessman:"+this.role;
	}
}
