package cn.kiddingme.chess.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cn.kiddingme.chess.items.ChessBoard;
import cn.kiddingme.chess.items.ChessMan;
import cn.kiddingme.chess.items.MoveStep;
import cn.kiddingme.chess.items.Node;

public class MainFrame extends JFrame{
	private int width=436;//Frame width and height
	private int height=512;
	
	private int correct_X=58;//for correcting the length
	private int correct_Y=82;
	
	private int startPoint_X=50;//something about the paint of chessboard,start_point is the (1,1) of chessboard 
	private int startPoint_Y=50;
	private int block_Width=50;
	
	private int MOD=0;//MOD==0 initial state ==1 a man has been selected
	private int selected_X=0;
	private int selected_Y=0;
	
	private int nextMoves[][]=new int[10][11];
	private ChessBoard chessBoard=new ChessBoard();
	private MainPanel mp=null;
	private Vector<MoveStep> steps=new Vector<MoveStep>();
	
	private AI ai=new AI();
	
	public static void main(String []args)
	{
		new MainFrame();
	}
	public MainFrame(){
		this.setLayout(null);
		
		mp=new MainPanel();
		mp.setBounds(0, 0, width+60, height+60);
		this.add(mp);
		
		ChessBoardMouseListener mouselistener=new ChessBoardMouseListener();
		this.addMouseListener(mouselistener);
		this.setSize(width+100,height+100);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public void markNextStepChoice(ChessMan cm)//give all move choices of a chessman
	{
		for(int i=0;i<10;i++)
			Arrays.fill(nextMoves[i], 0);
		int cm_X=cm.getX();
		int cm_Y=cm.getY();
		int cm_country=cm.getCountry();
		int cm_kind=cm.getKind();
		if(cm_kind==1)
		{
			if(cm_X+1<=6)
			{
				Node temp=chessBoard.getNode(cm_X+1,cm_Y);
				if(temp==null)
					nextMoves[cm_X+1][cm_Y]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
					nextMoves[cm_X+1][cm_Y]=1;
			}
			if(cm_X-1>=4)
			{
				Node temp=chessBoard.getNode(cm_X-1,cm_Y);
				if(temp==null)
					nextMoves[cm_X-1][cm_Y]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
					nextMoves[cm_X-1][cm_Y]=1;
			}
			if(cm_country==0)
			{
				if(cm_Y+1<=3)
				{
					Node temp=chessBoard.getNode(cm_X,cm_Y+1);
					if(temp==null)
						nextMoves[cm_X][cm_Y+1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][cm_Y+1]=1;
				}
				if(cm_Y-1>=1)
				{
					Node temp=chessBoard.getNode(cm_X,cm_Y-1);
					if(temp==null)
						nextMoves[cm_X][cm_Y-1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][cm_Y-1]=1;
				}
				Node temp=null;
				for(int y=cm_Y+1;temp==null&&y<=10;y++)//commander vs commander
				{
					temp=chessBoard.getNode(cm_X, y);
					if(temp!=null)
					{
						if(temp.getChessMan().getKind()==1)
						{
							nextMoves[cm_X][y]=1;
							break;
						}
					}
				}
			}
			else if(cm_country==1)
			{
				
				if(cm_Y+1<=10)
				{
					Node temp=chessBoard.getNode(cm_X,cm_Y+1);
					if(temp==null)
						nextMoves[cm_X][cm_Y+1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][cm_Y+1]=1;
				}
				if(cm_Y-1>=8)
				{
					Node temp=chessBoard.getNode(cm_X,cm_Y-1);
					if(temp==null)
						nextMoves[cm_X][cm_Y-1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][cm_Y-1]=1;
				}
				Node temp=null;
				for(int y=cm_Y-1;temp==null&&y<=10;y--)//commander vs commander
				{
					temp=chessBoard.getNode(cm_X, y);
					if(temp!=null)
					{
						if(temp.getChessMan().getKind()==1)
						{
							nextMoves[cm_X][y]=1;
							break;
						}
					}
				}
			}
		}
		
		else if(cm_kind==2)
		{
			int y_MAX=(cm_country==0)?3:10;
			int y_MIN=(cm_country==0)?1:8;
			if(cm_X+1<=6&&cm_Y+1<=y_MAX)
			{
				Node temp=chessBoard.getNode(cm_X+1,cm_Y+1);
				if(temp==null)
					nextMoves[cm_X+1][cm_Y+1]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
					nextMoves[cm_X+1][cm_Y+1]=1;
			}
			if(cm_X-1>=4&&cm_Y+1<=y_MAX)
			{
				Node temp=chessBoard.getNode(cm_X-1,cm_Y+1);
				if(temp==null)
					nextMoves[cm_X-1][cm_Y+1]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
					nextMoves[cm_X-1][cm_Y+1]=1;
			}
			if(cm_X+1<=6&&cm_Y-1>=y_MIN)
			{
				Node temp=chessBoard.getNode(cm_X+1,cm_Y-1);
				if(temp==null)
					nextMoves[cm_X+1][cm_Y-1]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
					nextMoves[cm_X+1][cm_Y-1]=1;
			}
			if(cm_X-1>=4&&cm_Y-1>=y_MIN)
			{
				Node temp=chessBoard.getNode(cm_X-1,cm_Y-1);
				if(temp==null)
					nextMoves[cm_X-1][cm_Y-1]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
					nextMoves[cm_X-1][cm_Y-1]=1;
			}
		}
		
		else if(cm_kind==3)//������
		{
			int y_MAX=(cm_country==0)?5:10;
			int y_MIN=(cm_country==0)?1:6;
			if(cm_X+2<=9&&cm_Y+2<=y_MAX)
			{
				
				Node crackExame=chessBoard.getNode(cm_X+1, cm_Y+1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X+2,cm_Y+2);
					if(temp==null)
						nextMoves[cm_X+2][cm_Y+2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X+2][cm_Y+2]=1;
				}
			}
			if(cm_X-2>=1&&cm_Y+2<=y_MAX)
			{
				
				Node crackExame=chessBoard.getNode(cm_X-1, cm_Y+1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X-2,cm_Y+2);
					if(temp==null)
						nextMoves[cm_X-2][cm_Y+2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X-2][cm_Y+2]=1;
				}
			}
			if(cm_X+2<=9&&cm_Y-2>=y_MIN)
			{
				
				Node crackExame=chessBoard.getNode(cm_X+1, cm_Y-1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X+2,cm_Y-2);
					if(temp==null)
						nextMoves[cm_X+2][cm_Y-2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X+2][cm_Y-2]=1;
				}
			}
			if(cm_X-2>=1&&cm_Y-2>=y_MIN)
			{
				Node crackExame=chessBoard.getNode(cm_X-1, cm_Y-1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X-2,cm_Y-2);
					if(temp==null)
						nextMoves[cm_X-2][cm_Y-2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X-2][cm_Y-2]=1;
				}
			}
		}
		
		else if(cm_kind==4)//������
		{
			if(cm_X+2<=9&&cm_Y+1<=10)
			{
				Node crackExame=chessBoard.getNode(cm_X+1, cm_Y);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X+2,cm_Y+1);
					if(temp==null)
						nextMoves[cm_X+2][cm_Y+1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X+2][cm_Y+1]=1;
				}
			}
			if(cm_X+2<=9&&cm_Y-1>=1)
			{
				Node crackExame=chessBoard.getNode(cm_X+1, cm_Y);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X+2,cm_Y-1);
					if(temp==null)
						nextMoves[cm_X+2][cm_Y-1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X+2][cm_Y-1]=1;
				}
			}
			if(cm_X-2>=1&&cm_Y+1<=10)
			{
				Node crackExame=chessBoard.getNode(cm_X-1, cm_Y);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X-2,cm_Y+1);
					if(temp==null)
						nextMoves[cm_X-2][cm_Y+1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X-2][cm_Y+1]=1;
				}
			}
			if(cm_X-2>=1&&cm_Y-1>=1)
			{
				Node crackExame=chessBoard.getNode(cm_X-1, cm_Y);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X-2,cm_Y-1);
					if(temp==null)
						nextMoves[cm_X-2][cm_Y-1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X-2][cm_Y-1]=1;
				}
			}
			if(cm_X+1<=9&&cm_Y+2<=10)
			{
				Node crackExame=chessBoard.getNode(cm_X, cm_Y+1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X+1,cm_Y+2);
					if(temp==null)
						nextMoves[cm_X+1][cm_Y+2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X+1][cm_Y+2]=1;
				}
			}
			if(cm_X+1<=9&&cm_Y-2>=1)
			{
				Node crackExame=chessBoard.getNode(cm_X, cm_Y-1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X+1,cm_Y-2);
					if(temp==null)
						nextMoves[cm_X+1][cm_Y-2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X+1][cm_Y-2]=1;
				}
			}
			if(cm_X-1>=1&&cm_Y+2<=10)
			{
				Node crackExame=chessBoard.getNode(cm_X, cm_Y+1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X-1,cm_Y+2);
					if(temp==null)
						nextMoves[cm_X-1][cm_Y+2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X-1][cm_Y+2]=1;
				}
			}
			if(cm_X-1>=1&&cm_Y-2>=1)
			{
				Node crackExame=chessBoard.getNode(cm_X, cm_Y-1);
				if(crackExame==null)
				{
					Node temp=chessBoard.getNode(cm_X-1,cm_Y-2);
					if(temp==null)
						nextMoves[cm_X-1][cm_Y-2]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X-1][cm_Y-2]=1;
				}
			}
		}
		
		else if(cm_kind==5)//����ɱ�񣬷�ɱ��
		{
			for(int x_temp=cm_X+1;x_temp<=9;x_temp++)
			{
				Node temp=chessBoard.getNode(x_temp,cm_Y);
				if(temp==null)
					nextMoves[x_temp][cm_Y]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
				{
					nextMoves[x_temp][cm_Y]=1;
					break;
				}
				else
					break;
			}
			
			for(int x_temp=cm_X-1;x_temp>=1;x_temp--)
			{
				Node temp=chessBoard.getNode(x_temp,cm_Y);
				if(temp==null)
					nextMoves[x_temp][cm_Y]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
				{
					nextMoves[x_temp][cm_Y]=1;
					break;
				}
				else
					break;
			}
			
			for(int y_temp=cm_Y+1;y_temp<=10;y_temp++)
			{
				Node temp=chessBoard.getNode(cm_X,y_temp);
				if(temp==null)
					nextMoves[cm_X][y_temp]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
				{
					nextMoves[cm_X][y_temp]=1;
					break;
				}
				else
					break;
			}
			
			for(int y_temp=cm_Y-1;y_temp>=1;y_temp--)
			{
				Node temp=chessBoard.getNode(cm_X,y_temp);
				if(temp==null)
					nextMoves[cm_X][y_temp]=1;
				else if(temp.getChessMan().getCountry()!=cm_country)
				{
					nextMoves[cm_X][y_temp]=1;
					break;
				}
				else
					break;
			}
		}
		
		else if(cm_kind==6)//�� ��ɽ��ţ
		{
			boolean flag=false;
			for(int x_temp=cm_X+1;x_temp<=9;x_temp++)
			{
				Node temp=chessBoard.getNode(x_temp,cm_Y);
				if(temp==null&&flag==false)
					nextMoves[x_temp][cm_Y]=1;
				else if(temp!=null&&flag==false)
					flag=true;
				else if(temp==null&&flag==true)
					continue;
				else if(temp!=null&&flag==true)
				{
					if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[x_temp][cm_Y]=1;
					break;
				}
			}
			flag=false;
			for(int x_temp=cm_X-1;x_temp>=1;x_temp--)
			{
				Node temp=chessBoard.getNode(x_temp,cm_Y);
				if(temp==null&&flag==false)
					nextMoves[x_temp][cm_Y]=1;
				else if(temp!=null&&flag==false)
					flag=true;
				else if(temp==null&&flag==true)
					continue;
				else if(temp!=null&&flag==true)
				{
					if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[x_temp][cm_Y]=1;
					break;
				}
			}
			flag=false;
			for(int y_temp=cm_Y+1;y_temp<=10;y_temp++)
			{
				Node temp=chessBoard.getNode(cm_X,y_temp);
				if(temp==null&&flag==false)
					nextMoves[cm_X][y_temp]=1;
				else if(temp!=null&&flag==false)
					flag=true;
				else if(temp==null&&flag==true)
					continue;
				else if(temp!=null&&flag==true)
				{
					if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][y_temp]=1;
					break;
				}
			}
			flag=false;
			for(int y_temp=cm_Y-1;y_temp>=1;y_temp--)
			{
				Node temp=chessBoard.getNode(cm_X,y_temp);
				if(temp==null&&flag==false)
					nextMoves[cm_X][y_temp]=1;
				else if(temp!=null&&flag==false)
					flag=true;
				else if(temp==null&&flag==true)
					continue;
				else if(temp!=null&&flag==true)
				{
					if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][y_temp]=1;
					break;
				}
			}
		}
		
		else if(cm_kind==7)
		{
			if(cm_country==0)
			{
				if(cm_Y<=5)
				{
					Node temp=chessBoard.getNode(cm_X,cm_Y+1);
					if(temp==null)
						nextMoves[cm_X][cm_Y+1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][cm_Y+1]=1;
				}
				else
				{
					if(cm_Y+1<=10)
					{
						Node temp=chessBoard.getNode(cm_X,cm_Y+1);
						if(temp==null)
							nextMoves[cm_X][cm_Y+1]=1;
						else if(temp.getChessMan().getCountry()!=cm_country)
							nextMoves[cm_X][cm_Y+1]=1;
					}
					if(cm_X-1>=1)
					{
						Node temp=chessBoard.getNode(cm_X-1,cm_Y);
						if(temp==null)
							nextMoves[cm_X-1][cm_Y]=1;
						else if(temp.getChessMan().getCountry()!=cm_country)
							nextMoves[cm_X-1][cm_Y]=1;
					}
					if(cm_X+1<=9)
					{
						Node temp=chessBoard.getNode(cm_X+1,cm_Y);
						if(temp==null)
							nextMoves[cm_X+1][cm_Y]=1;
						else if(temp.getChessMan().getCountry()!=cm_country)
							nextMoves[cm_X+1][cm_Y]=1;
					}
				}
			}
			else
			{
				if(cm_Y>=6)
				{
					Node temp=chessBoard.getNode(cm_X,cm_Y-1);
					if(temp==null)
						nextMoves[cm_X][cm_Y-1]=1;
					else if(temp.getChessMan().getCountry()!=cm_country)
						nextMoves[cm_X][cm_Y-1]=1;
				}
				else
				{
					if(cm_Y-1>=1)
					{
						Node temp=chessBoard.getNode(cm_X,cm_Y-1);
						if(temp==null)
							nextMoves[cm_X][cm_Y-1]=1;
						else if(temp.getChessMan().getCountry()!=cm_country)
							nextMoves[cm_X][cm_Y-1]=1;
					}
					if(cm_X-1>=1)
					{
						Node temp=chessBoard.getNode(cm_X-1,cm_Y);
						if(temp==null)
							nextMoves[cm_X-1][cm_Y]=1;
						else if(temp.getChessMan().getCountry()!=cm_country)
							nextMoves[cm_X-1][cm_Y]=1;
					}
					if(cm_X+1<=9)
					{
						Node temp=chessBoard.getNode(cm_X+1,cm_Y);
						if(temp==null)
							nextMoves[cm_X+1][cm_Y]=1;
						else if(temp.getChessMan().getCountry()!=cm_country)
							nextMoves[cm_X+1][cm_Y]=1;
					}
				}
			}
		}
	}
	class ChessBoardMouseListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int x_c=e.getX(),y_c=e.getY();
		//	System.out.println("x: "+x_c+" y: "+y_c);
			
			int man_X=(x_c+block_Width/2-correct_X)/block_Width+1;
			if(y_c>=330)
				y_c-=25;
			int man_Y=(y_c+block_Width/2-correct_Y)/block_Width+1;
		//	System.out.println(i+" "+j);
			if(man_X>9||man_X<1||man_Y>10||man_Y<1)
				return;
			Node temp=chessBoard.getNode(man_X, man_Y);
			if(MOD==0&&temp.getChessMan().getCountry()==1)
			{
				if(temp!=null)
				{
					selected_X=man_X;
					selected_Y=man_Y;
					markNextStepChoice(temp.getChessMan());
					MOD=1;
					mp.repaint();
				}
			}
			else if(MOD==1)
			{
				
				if(nextMoves[man_X][man_Y]==1)
				{
					Node selected=chessBoard.getNode(selected_X, selected_Y);
					chessBoard.setNode(man_X, man_Y, selected);
					chessBoard.setNode(selected_X, selected_Y, null);
					selected.getChessMan().setXY(man_X, man_Y);
					selected_X=man_X;
					selected_Y=man_Y;
					MOD=2;
					mp.repaint();
				}
				else if(temp!=null&&temp.getChessMan().getCountry()==1)
				{
					selected_X=man_X;
					selected_Y=man_Y;
					markNextStepChoice(temp.getChessMan());
					mp.repaint();
				}
			}
			else if(MOD==2)
			{
				
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	class MainPanel extends JPanel{
		private String chessBoardPath="images/chess.jpg";
		public MainPanel()
		{
			this.setLayout(null);
		}
		public void paint(Graphics g)
		{
			super.paint(g);
			BufferedImage image;
			try {
				image = ImageIO.read(new FileInputStream(chessBoardPath));
				g.drawImage(image, 30, 30, image.getWidth()/2, image.getHeight()/2, null);
				
				if(MOD==1||MOD==0)
				{
					BufferedImage selected_back_Ground=ImageIO.read(new FileInputStream("images/OOS.GIF"));
					g.drawImage(selected_back_Ground, (selected_X-1)*block_Width+startPoint_X-block_Width/2, (selected_Y-1)*block_Width+startPoint_Y-block_Width/2+((selected_Y>=6)?25:0), block_Width, block_Width, null);
				} 
				
				for(int i=1;i<=9;i++)//������9��
				{
					for(int j=1;j<=10;j++)
					{
						Node temp=chessBoard.getNode(i, j);
						
						
						if(temp!=null)
						{
							//System.out.println("X: "+((i-1)*block_Width+startPoint_X-block_Width/2)+" Y: "+((j-1)*block_Width+startPoint_Y-block_Width/2));
							BufferedImage chessman_image= ImageIO.read(new FileInputStream(temp.getChessMan().getSourceImage()));//
							g.drawImage(chessman_image, (i-1)*block_Width+startPoint_X-block_Width/2, (j-1)*block_Width+startPoint_Y-block_Width/2+((j>=6)?25:0), block_Width, block_Width, null);
						}
						if(MOD==1&&nextMoves[i][j]==1)
						{
							BufferedImage squreNotice= ImageIO.read(new FileInputStream("images/bullet.png"));
							g.drawImage(squreNotice, (i-1)*block_Width+startPoint_X-block_Width/2, (j-1)*block_Width+startPoint_Y-block_Width/2+((j>=6)?25:0), block_Width, block_Width, null);
						}
						
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

	class AI{
		private int commander_weight[][]=new int[10][11];
		private int bodyguard_weight[][]=new int[10][11];
		private int minister_weight[][]=new int[10][11];
		private int cavalry_weight[][]=new int[10][11];
		private int tank_weight[][]=new int[10][11];
		private int cannon_weight[][]=new int[10][11];
		private int soilder_weight[][]=new int[10][11];
		
		public AI()
		{
			init();
		}
		public void init(){
			BufferedInputStream bi=null;
			try {
				bi=new BufferedInputStream(new FileInputStream(new File("conf/weight.txt")));
				Scanner bis=new Scanner(bi);
				int temp[][]=null;
				int flag=0;
				for(int k=1;k<=7;k++)
				{
					flag=bis.nextInt();
					switch (flag){
						case 1: temp=commander_weight;
								break;
						case 2: temp=bodyguard_weight;
						  		break;
						case 3: temp=minister_weight;
						        break;
						case 4: temp=cavalry_weight;
								break;
						case 5: temp=tank_weight;
				  				break;
						case 6: temp=cannon_weight;
				        		break;
						case 7: temp=soilder_weight;
					}
					for(int j=1;j<=10;j++)
					{
						for(int i=1;i<=9;i++)
						{
							temp[i][j]=bis.nextInt();
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					bi.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		public MoveStep giveNextMove(ChessBoard cb)//������һ������
//		{
//			
//		}
	}
}