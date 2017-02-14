import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Yard extends Frame { //open a window
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


	PaintThread paintThread = new PaintThread();
	
	public static final int ROWS = 30;
	public static final int COLS = 30;
	public static final int BLOCK_SIZE = 15;
	
	private int score = 0;
	
	Snake s = new Snake(this);
	Egg e = new Egg();
	
	Image offScreenImage = null;			//双缓冲声明

	private boolean gameOver = false;
	
	public void launch(){					//main运行方法
		this.setLocation(200, 200);			
		this.setSize(COLS *BLOCK_SIZE, ROWS * BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter(){

			
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor());
		new Thread(paintThread).start();
	}
	
	public static void main(String[] args) {
		new Yard().launch();
		
	}
	
	public void stop(){
		gameOver  = true;
	}

	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, COLS *BLOCK_SIZE, ROWS * BLOCK_SIZE);
		g.setColor(Color.DARK_GRAY);
		//write the row
		for(int i = 0; i < ROWS; i++){
			g.drawLine(0, BLOCK_SIZE * i, COLS * BLOCK_SIZE, BLOCK_SIZE *i);
		}
		//write the col
		for(int i = 0; i < COLS; i++){
			g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, ROWS * BLOCK_SIZE);
		}
		
		g.setColor(Color.YELLOW);
		g.drawString("score: " + score, 10, 60);
		if(gameOver){
			g.setFont(new Font("verdana", Font.BOLD, 50));
			g.drawString("game over", 120, 80);
			paintThread.pause();
		}
		g.setColor(c);
		s.eat(e);
		e.draw(g);
		s.draw(g);
		
		
	}
	
	public void update(Graphics g) {
		if(offScreenImage == null){
			offScreenImage = this.createImage(COLS *BLOCK_SIZE, ROWS * BLOCK_SIZE);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}	
	private class PaintThread implements Runnable{
		private boolean running = true;
		private boolean pause = false;
		public void run() {
			while(running){
				if(pause) continue;
				else repaint();
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		
		public void pause() {
			this.pause = true;
		}
		
		public void reStart(){
			this.pause = false;
			s = new Snake(Yard.this);
			gameOver = false;
		}
		
	}
	
	
	private class KeyMonitor extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_F2){
				paintThread.reStart();
			}
			s.keyPressed(e) ;
		}
		
	}
}
