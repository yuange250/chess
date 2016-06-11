package cn.kiddingme.chess.items;

import cn.kiddingme.chess.exceptions.ChessmanInilizeException;

public class ChessMan {
	protected int Y;
	protected int X;
	protected int kind=0;//1 for commander 2 for bodyguard 3 for minister£®œ‡£© 4 for cavalry 
	//5 for tank(xixi) 6 for cannon 7 for soilder
	protected int country=0;//0 for red 1 for black
	protected String sourceImage=null;
	public ChessMan(int X,int Y,int kind,int country,String sourceImage) throws ChessmanInilizeException{
		if(X<0||X>9||Y<0||Y>10||kind>7||kind<0||country>1||country<0)
		{
			throw new ChessmanInilizeException();
		}
		this.X=X;
		this.Y=Math.abs(11*country-Y);
		this.kind=kind;
		this.country=country;
		this.sourceImage=sourceImage;
	}
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	public int getKind()
	{
		return kind;
	}
	public int getCountry(){
		return country;
	}
	public void move(int X,int Y)
	{
		this.X=X;
		this.Y=Y;
	}
	public String getSourceImage(){
		return this.sourceImage;
	}
	public void setXY(int x,int y)
	{
		this.X=x;
		this.Y=y;
	}
	public ChessMan deepCopy(){
		ChessMan cm=null;
		try {
			 cm=new ChessMan(this.X,this.Y,this.kind,this.country,this.sourceImage);
		} catch (ChessmanInilizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cm;
	}
}
