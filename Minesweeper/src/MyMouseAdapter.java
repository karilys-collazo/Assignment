import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
	private Random generator = new Random();	
	public int topLimit=0;
	public int leftLimit=0;	
	public static int bitMap [][] = new int [MyPanel.TOTAL_COLUMNS][MyPanel.TOTAL_ROWS]; //Used to identify all of the tiles in the game.
	public static int max=4;//Defines maximum amount of bombs
	public int pos = 0;//Keeps track of how many bombs have been placed in case one is repeated.
	public static int count=0;	//Counts how many tiles have been uncovered.
	public static int countRequired=((MyPanel.TOTAL_COLUMNS-1)*(MyPanel.TOTAL_ROWS-2))-max;//Defines how many tiles has to be uncovered to win the game.
	public static boolean bombs[][] = new boolean[MyPanel.TOTAL_COLUMNS][MyPanel.TOTAL_ROWS-1];//Determines if the tile has a bomb.
	public int X[] = new int[max];//Used to locate bomb in the X coordinate.
	public int Y[] = new int[max];//Used to locate bomb in the Y coordinate.
	public static boolean Painted[][] = new boolean[MyPanel.TOTAL_COLUMNS][MyPanel.TOTAL_ROWS-1];//Determines if the tile has been painted.
	private boolean nearBomb[][] = new boolean[MyPanel.TOTAL_COLUMNS][MyPanel.TOTAL_ROWS-1];//Used to avoid painting tiles after a bomb is near.
	public boolean tileCounted[][] = new boolean[MyPanel.TOTAL_COLUMNS][MyPanel.TOTAL_ROWS-1];//Avoids counting a tile more than one time.
	public static boolean gameLost=false;//The player lost the game.
	public static boolean BombCountReached=false;//Determines if the player won the game.
	public int flag[][]=new int [MyPanel.TOTAL_COLUMNS][MyPanel.TOTAL_ROWS-1];//Used for red flags.
	public static boolean flagsPressed=false;//Used for eliminating a flag if the uses chooses the same one twice.
	public static int flagUsed=0;//Used to display how many flags the user has left.
	Color prevColor=Color.WHITE;//Used when needed to replace a tile if it has been uncovered and then covered with a flag.
	
	
	
	{
		for(int n = 0; n < MyPanel.TOTAL_COLUMNS; n++){
			for(int m = 0; m < MyPanel.TOTAL_ROWS-1; m++){
				bitMap[n][m] = 0;
				bombs[n][m]=false;
				Painted[n][m]=false;
				nearBomb[n][m]=false;
				tileCounted[n][m]=false;
			}
		}		
			
		for(int b=0;b<max;b++){//Determines the locations of the mines.
			int u,v;
			u=generator.nextInt(MyPanel.TOTAL_COLUMNS-1)+1;
			v=generator.nextInt(MyPanel.TOTAL_ROWS-1-1)+1;
			System.out.println("Bomb"+b+"="+"	x:"+u+"	y:"+v);
			if(!bombs[u][v]){
				bombs[u][v]=true;
				bitMap[u][v] = 99;
				X[pos] = u;
				Y[pos] = v;
				pos++;
				
				//Counts the number of mines of the surrounding squares.
				if(!(u-1 < leftLimit || v - 1 < topLimit) && !(u-1 >= MyPanel.TOTAL_COLUMNS || v-1 >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u-1][v-1]++;
				}
				if(!(u < leftLimit || v - 1 < topLimit) && !(u >= MyPanel.TOTAL_COLUMNS || v-1 >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u][v-1]++;
				}
				if(!(u+1 < leftLimit || v - 1 < topLimit) && !(u+1 >= MyPanel.TOTAL_COLUMNS || v-1 >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u+1][v-1]++;				
				}
				if(!(u-1 < leftLimit || v < topLimit) && !(u-1 >= MyPanel.TOTAL_COLUMNS || v >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u-1][v]++;
				}
				if(!(u+1 < leftLimit || v < topLimit) && !(u+1 >= MyPanel.TOTAL_COLUMNS || v >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u+1][v]++;
				}
				if(!(u-1 < leftLimit || v + 1 < topLimit) && !(u-1 >= MyPanel.TOTAL_COLUMNS || v+1 >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u-1][v+1]++;
				}
				if(!(u < leftLimit || v + 1 < topLimit) && !(u >= MyPanel.TOTAL_COLUMNS || v+1 >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u][v+1]++;
				}
				if(!(u+1 < leftLimit || v + 1 < topLimit) && !(u+1 >= MyPanel.TOTAL_COLUMNS || v+1 >= MyPanel.TOTAL_ROWS-1)){
					bitMap[u+1][v+1]++;
				}							
								
			}
			else//If there is already a bomb in the random tile generated.		
				b--;						
		}
	}
	
	
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			Component k = e.getComponent();
			while (!(k instanceof JFrame)) {
				k = k.getParent();
				if (k == null) {
					return;
				}
			}
			JFrame myFrame2 = (JFrame) k;
			MyPanel myPanel2 = (MyPanel) myFrame2.getContentPane().getComponent(0);
			Insets myInsets2 = myFrame2.getInsets();
			int x2 = myInsets2.left;
			int y2 = myInsets2.top;
			e.translatePoint(-x2,-y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel2.x = x3;
			myPanel2.y = y3;
			myPanel2.mouseDownGridX = myPanel2.getGridX(x3, y3);
			myPanel2.mouseDownGridY = myPanel2.getGridY(x3, y3);
			myPanel2.repaint();
			
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing						
					} else {
						//Released the mouse button on the same cell where it was pressed
						if ((gridX == 0) || (gridY == 0)) {
							//On the left column and on the top row... do nothing
						} else {
							//On the grid other than on the left column and on the top row:
							Color newColor = null;
							
							if(bombs[gridX][gridY]){
								gameLost=true;
								
								for(int i = 0; i < max; i++){
									newColor = Color.BLACK;
									MyPanel.colorArray[X[i]][Y[i]] = newColor;
								}					
								myPanel.repaint();
							}
							
							if(bitMap[gridX][gridY]==0&&!gameLost){
								Painted[gridX][gridY]=true;
								
								for(int u=gridY;u<MyPanel.TOTAL_ROWS-2;u++){									
									for(int v=gridX;v<MyPanel.TOTAL_COLUMNS;v++){
										//Looks for the nearby tiles on the right-downward part of the grid selected and stops painting once it finds a nearby bomb.
										if(bitMap[v][u+1]>0&&!nearBomb[v][u]){
											Painted[v][u+1]=true;
											for(int bombAllY=u+1;bombAllY<MyPanel.TOTAL_ROWS-1;bombAllY++){
												for(int bombAllX=v;bombAllX<MyPanel.TOTAL_COLUMNS;bombAllX++){
													nearBomb[bombAllX][bombAllY]=true;
												}
											}											
										}
										if(bitMap[v][u]>0&&!nearBomb[v][u]){
											Painted[v][u]=true;
											for(int bombAllX=v;bombAllX<MyPanel.TOTAL_COLUMNS;bombAllX++){
												nearBomb[bombAllX][u]=true;												
											}
										}
									}
									
									for(int v=gridX;v>0;v--){
										//Looks for the nearby tiles on the left-downward part of the grid selected and stops painting once it finds a nearby bomb.
										if(bitMap[v][u+1]>0&&!nearBomb[v][u]){
											Painted[v][u+1]=true;											
											for(int bombAllY=u+1;bombAllY<MyPanel.TOTAL_ROWS-1;bombAllY++){
												for(int bombAllX=v;bombAllX>leftLimit;bombAllX--){
													nearBomb[bombAllX][bombAllY]=true;
												}
											}												
										}
										if(bitMap[v][u]>0&&!nearBomb[v][u]){
											Painted[v][u]=true;
											for(int bombAllX=v;bombAllX>leftLimit;bombAllX--){
												nearBomb[bombAllX][u]=true;												
											}
										}
									}									
								}
								
								
								for(int u=gridY;u>0;u--){									
									for(int v=gridX;v<MyPanel.TOTAL_COLUMNS;v++){
										//Looks for the nearby tiles on the upper-right part of the grid selected and stops painting once it finds a nearby bomb.
										if(bitMap[v][u-1]>0&&!nearBomb[v][u]){
											Painted[v][u-1]=true;
											for(int bombAllY=u-1;bombAllY>topLimit;bombAllY--){
												for(int bombAllX=v;bombAllX<MyPanel.TOTAL_COLUMNS;bombAllX++){
													nearBomb[bombAllX][bombAllY]=true;
												}
											}											
										}
										if(bitMap[v][u]>0&&!nearBomb[v][u]){
											Painted[v][u]=true;
											for(int bombAllX=v;bombAllX<MyPanel.TOTAL_COLUMNS;bombAllX++){
												nearBomb[bombAllX][u]=true;
											}
										}										
									}
									
									for(int v=gridX;v>0;v--){
										//Looks for the nearby tiles on the upper-left part of the grid selected and stops painting once it finds a nearby bomb.
										if(bitMap[v][u-1]>0&&!nearBomb[v][u]){
											Painted[v][u-1]=true;
											for(int bombAllY=u-1;bombAllY>topLimit;bombAllY--){
												for(int bombAllX=v;bombAllX>leftLimit;bombAllX--){
													nearBomb[bombAllX][bombAllY]=true;
												}
											}											
										}
										if(bitMap[v][u]>0&&!nearBomb[v][u]){
											Painted[v][u]=true;
											for(int bombAllX=v;bombAllX>leftLimit;bombAllX--){
												nearBomb[bombAllX][u]=true;												
											}											
										}
									}									
								}							
								//Paints all of the gray tiles selected on the previous array that does not contain a nearby bomb and resets the board for future choices.
								for(int py=0;py<MyPanel.TOTAL_ROWS-1;py++){ 
									for(int px=0;px<MyPanel.TOTAL_COLUMNS;px++){				
										if(!nearBomb[px][py])			
											Painted[px][py]=true;			
										
										if(nearBomb[px][py])
											nearBomb[px][py]=false;	
									}										
								}	
								
							}
							
							else{//The tile selected has a bomb near.						
								if(!gameLost)
								Painted[gridX][gridY]=true;
							}
							
							//Colors in the tiles according to it's value..
							for(int yDir=1;yDir<MyPanel.TOTAL_ROWS-1;yDir++){
								for(int xDir=1;xDir<MyPanel.TOTAL_COLUMNS;xDir++){
									if(Painted[xDir][yDir]&&!tileCounted[xDir][yDir]&&!gameLost){
										count++;
										tileCounted[xDir][yDir]=true;
										switch (bitMap[xDir][yDir]) {
										case 0:
											prevColor=Color.GRAY;
											MyPanel.colorArray[xDir][yDir] = Color.GRAY;													
											break;
										case 1:
											prevColor=Color.YELLOW;
											MyPanel.colorArray[xDir][yDir] = Color.YELLOW;	
											break;											
										case 2:
											prevColor=Color.MAGENTA;
											MyPanel.colorArray[xDir][yDir] = Color.MAGENTA;													
											break;
										case 3:
											prevColor=new Color(0x964B00);
											MyPanel.colorArray[xDir][yDir] = new Color(0x964B00);//Brown (from http://simple.wikipedia.org/wiki/List_of_colors)												
											break;
										case 4:
											prevColor=new Color(0xB57EDC);
											MyPanel.colorArray[xDir][yDir] = new Color(0xB57EDC); //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
											break;
										case 5:
											prevColor=Color.BLUE;
											MyPanel.colorArray[xDir][yDir] = Color.BLUE;											
											break;
										case 6:
											prevColor=Color.CYAN;
											MyPanel.colorArray[xDir][yDir] = Color.CYAN;													
											break;
										case 7:
											prevColor=Color.PINK;
											MyPanel.colorArray[xDir][yDir] = Color.PINK;												
											break;
										case 8:
											prevColor=Color.GREEN;
											MyPanel.colorArray[xDir][yDir] = Color.GREEN;												
											break;
										}
										
										if(count==countRequired){
											BombCountReached=true;
										}
									}
								}
							}
						}
					}
				}
			}
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			Component k = e.getComponent();
			while (!(k instanceof JFrame)) {
				k = k.getParent();
				if (k == null) {
					return;
				}
			}
			JFrame myFrame2 = (JFrame)k;
			MyPanel myPanel2 = (MyPanel) myFrame2.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets2 = myFrame2.getInsets();
			int x2 = myInsets2.left;
			int y2 = myInsets2.top;
			e.translatePoint(-x2, -y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel2.x = x3;
			myPanel2.y = y3;
			int gridX2 = myPanel2.getGridX(x3, y3);
			int gridY2 = myPanel2.getGridY(x3, y3);
			if ((myPanel2.mouseDownGridX == -1) || (myPanel2.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX2 == -1) || (gridY2 == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel2.mouseDownGridX != gridX2) || (myPanel2.mouseDownGridY != gridY2)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if ((gridX2 == 0) || (gridY2 == 0)) {
							//On the left column and on the top row... do nothing
						} 
						else {
							flagsPressed=true;
							if(flag[gridX2][gridY2]==0&&flagUsed<max){
								//Color newColor=Color.RED;
								myPanel2.colorArray[myPanel2.mouseDownGridX][myPanel2.mouseDownGridY] = Color.RED;
								myPanel2.repaint();
								flag[gridX2][gridY2]=1;
								flagUsed++;
							}
							else if(flag[gridX2][gridY2]==1){																
								myPanel2.colorArray[myPanel2.mouseDownGridX][myPanel2.mouseDownGridY] = prevColor;
								myPanel2.repaint();
								flag[gridX2][gridY2]=0;
								flagUsed--;
							}
						}
					}
				}
			}
			//Do nothing
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
}
	