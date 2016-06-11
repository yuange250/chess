package cn.kiddingme.chess.items;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.kiddingme.chess.exceptions.ChessmanInilizeException;

public class ChessBoard {
	private Node[][] nodes=new Node[10][11];
	public void init()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse("conf/chessMan_conf.xml");
			NodeList chessManList=doc.getElementsByTagName("chessMan");
			org.w3c.dom.Node chessMans = chessManList.item(0);
			org.w3c.dom.Node chessMan = chessMans.getFirstChild();
			chessMan = chessMan.getNextSibling();
			Node node_temp=null;
			
			for(int i=0;chessMan!=null;chessMan = chessMan.getNextSibling())
			{
				if(chessMan.getNodeType()==chessMan.ELEMENT_NODE)
				{
					org.w3c.dom.Node temp=chessMan.getFirstChild();
					temp=temp.getNextSibling();
					int x=Integer.valueOf(temp.getTextContent());
					temp=temp.getNextSibling();
					temp=temp.getNextSibling();
					int y=Integer.valueOf(temp.getTextContent());
					temp=temp.getNextSibling();
					temp=temp.getNextSibling();
					int kind=Integer.valueOf(temp.getTextContent());
					temp=temp.getNextSibling();
					temp=temp.getNextSibling();
					int country=Integer.valueOf(temp.getTextContent());
					temp=temp.getNextSibling();
					temp=temp.getNextSibling();
					String imagePath=temp.getTextContent().split("[\"]")[1];
					
					node_temp=new Node(new ChessMan(x,y,kind,country,imagePath));
					nodes[node_temp.getChessMan().getX()][node_temp.getChessMan().getY()]=node_temp;
					node_temp=new Node(new ChessMan(x,y,kind,Math.abs(1-country),imagePath.substring(0, 7)+"R"+imagePath.substring(8, 13)));
					nodes[node_temp.getChessMan().getX()][node_temp.getChessMan().getY()]=node_temp;
					if((10-x)!=x)
					{
						node_temp=new Node(new ChessMan(10-x,y,kind,country,imagePath));
						nodes[node_temp.getChessMan().getX()][node_temp.getChessMan().getY()]=node_temp;
						node_temp=new Node(new ChessMan(10-x,y,kind,Math.abs(1-country),imagePath.substring(0, 7)+"R"+imagePath.substring(8, 13)));
						nodes[node_temp.getChessMan().getX()][node_temp.getChessMan().getY()]=node_temp;
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException | ChessmanInilizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ChessBoard(){
		init();
	}
	public Node getNode(int x,int y)
	{
		return nodes[x][y];
	}
	public void setNode(int x,int y,Node n)
	{
		nodes[x][y]=n;
	}
	public ChessBoard deepCopy(){
		ChessBoard cb=new ChessBoard();
		Node temp=null;
		for(int i=1;i<10;i++)
		{
			for(int j=1;j<11;j++)
			{
				temp=new Node(nodes[i][j].getChessMan());
				cb.setNode(i, j, temp);
			}
		}
		return cb;
	}
}
