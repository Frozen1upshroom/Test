package pack;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2019049049560988091L;
	Thread loop;
	boolean run;
	
	Main(){
		new Window(500, 500, "", this);
		
	}
	
	public static void main(String[] args) {
		new Main();
	}

	public synchronized void start() {
		run = true;
		loop = new Thread(this);
		loop.start();
	}
	
	public synchronized void stop() {
		try {
			loop.join();
			run = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
	//	double ns= 1000000000*2000000 / (amountOfTicks/20);
		double ns= 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis(); //1s = 1,000ms
		int frames = 0; 
		while(run){
			long now = System.nanoTime(); //1s = 1,000,000,000ns
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1 ) {
				delta--;
			}
			if(run)
				render();
			
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				System.out.print("");
				timer += 1000;
	/////			System.out.println("FPS: " + frames);// + handler.object.size()+ " "+ frames);			
				frames = 0;	
			}	
		}
		stop();
	}

	void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
	//	Graphics2D g2d = (Graphics2D) g;
		
		g.setColor(new Color(34,255,100));

		g.fillRect(0, 0, 100, 100);	
		
		g.dispose();
		bs.show();
	}
}

class Window extends Canvas{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1141100816475274856L;

	JFrame frame;

	public Window(int width, int height, String title, Main test) {
		JFrame frame = new JFrame(title);
		this.frame = frame;
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(test);
		frame.setVisible(true);
		test.start();
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
