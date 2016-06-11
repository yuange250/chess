package cn.kiddingme.chess.items;

public class Node {
	private ChessMan cm=null;
	public Node(ChessMan chessMan) {
		// TODO Auto-generated constructor stub
		setChessMan(chessMan);
	}
	public ChessMan getChessMan(){
		return cm;
	}
	public void setChessMan(ChessMan _cm){
		this.cm=_cm;
	}
}
