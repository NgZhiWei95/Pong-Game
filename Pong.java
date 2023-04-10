package pong; //All .java files in pong package
import java.awt.BasicStroke; //Import awt librarys
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener { //Pong class, main driver program

	//Declare some variables
	public static Pong pong;
	public int width = 700, height = 700, gameStatus = 0, scoreLimit = 1, playerWon, botDifficulty, botMoves, botCooldown = 0;
	public Design renderer;
	public Paddle p1, p2;
	public Ball ball;
	public boolean bot = false, chooseDifficulty, wKey, sKey, upKey, downKey;
	public Random random;
	public JFrame jframe;

	//Constructor, make the frame
	public Pong() {
		Timer timer = new Timer(20, this);
		random = new Random();
		jframe = new JFrame("AI & 2P Soccer Pong");
		renderer = new Design();
		jframe.setSize(width + 15, height + 35);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(renderer);
		jframe.addKeyListener(this);
		timer.start();
	}

	//Start game
	public void start() {
		gameStatus = 2;
		p1 = new Paddle(this, 1);
		p2 = new Paddle(this, 2);
		ball = new Ball(this);
	}

	//Update as playing
	public void update() {
		if (p1.score >= scoreLimit) { //Update score
			playerWon = 1;
			gameStatus = 3;
		}
		if (p2.score >= scoreLimit) { //Update score
			gameStatus = 3;
			playerWon = 2;
		}
		if (wKey) { //Player 1 move
			p1.move(true);
		}
		if (sKey) {
			p1.move(false);
		}
		if (!bot) { //Player 2 move
			if (upKey) {
				p2.move(true);
			}
			if (downKey) {
				p2.move(false);
			}
		}
		else { //AI move
			if (botCooldown > 0) {
				botCooldown--;
				if (botCooldown == 0) {
					botMoves = 0;
				}
			}
			if (botMoves < 10) {
				if (p2.y + p2.height / 2 < ball.y) {
					p2.move(false);
					botMoves++;
				}
				if (p2.y + p2.height / 2 > ball.y) {
					p2.move(true);
					botMoves++;
				}
				if (botDifficulty == 0) {
					botCooldown = 20;
				}
				if (botDifficulty == 1) {
					botCooldown = 15;
				}
				if (botDifficulty == 2) {
					botCooldown = 10;
				}
			}
		}
		ball.update(p1, p2); //Update ball status 
	}

	//Display
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (gameStatus == 0) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PONG", width / 2 - 75, 50);
			if (!chooseDifficulty) { //Main menu
				g.setFont(new Font("Arial", 1, 30));
				g.drawString("Press Space to Play", width / 2 - 150, height / 2 - 25);
				g.drawString("Press Shift to Play with Bot", width / 2 - 200, height / 2 + 25);
				g.drawString("<< Score Limit: " + scoreLimit + " >>", width / 2 - 150, height / 2 + 75);
			}
		}
		if (chooseDifficulty) { //Play with AI
			String string = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : "Hard");
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("<< Bot Difficulty: " + string + " >>", width / 2 - 180, height / 2 - 25);
			g.drawString("Press Space to Play", width / 2 - 150, height / 2 + 25);
		}
		if (gameStatus == 1) { //Pause game
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PAUSED", width / 2 - 103, height / 2 - 25);
		}
		if (gameStatus == 1 || gameStatus == 2) { //Score during game
			g.setColor(Color.BLUE);
			g.setStroke(new BasicStroke(5f));
			g.drawLine(width / 2, 0, width / 2, height);
			g.setStroke(new BasicStroke(2f));
			g.drawOval(width / 2 - 150, height / 2 - 150, 300, 300);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(String.valueOf(p1.score), width / 2 - 90, 50);
			g.drawString(String.valueOf(p2.score), width / 2 + 65, 50);
			p1.render(g);
			p2.render(g);
			ball.render(g);
		}
		if (gameStatus == 3) { //Declare winner and play again or quit
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PONG", width / 2 - 75, 50);
			if (bot && playerWon == 2) {
				g.drawString("The Bot Wins!", width / 2 - 170, 200);
			}
			else {
				g.drawString("Player " + playerWon + " Wins!", width / 2 - 165, 200);
			}
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
			g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { //Override method for update game
		if (gameStatus == 2) {
			update();
		}
		renderer.repaint();
	}

	public static void main(String[] args) { //Driver program
		pong = new Pong();
	}

	@Override
	public void keyPressed(KeyEvent e) { //Override method for options movement
		int id = e.getKeyCode();
		if (id == KeyEvent.VK_W) {
			wKey = true;
		}
		else if (id == KeyEvent.VK_S) {
			sKey = true;
		}
		else if (id == KeyEvent.VK_UP) {
			upKey = true;
		}
		else if (id == KeyEvent.VK_DOWN) {
			downKey = true;
		}
		else if (id == KeyEvent.VK_RIGHT) {
			//Choose difficulty against AI
			if (chooseDifficulty) {
				if (botDifficulty < 2) {
					botDifficulty++;
				}
				else {
					botDifficulty = 0;
				}
			}
			else if (gameStatus == 0) {
				scoreLimit++;
			}
		}
		else if (id == KeyEvent.VK_LEFT) {
			if (chooseDifficulty) {
				if (botDifficulty > 0) {
					botDifficulty--;
				}
				else {
					botDifficulty = 2;
				}
			}
			else if (gameStatus == 0 && scoreLimit > 1) {
				scoreLimit--;
			}
		}
		//Game status
		else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3)) {
			gameStatus = 0;
		}
		else if (id == KeyEvent.VK_SHIFT && gameStatus == 0) {
			bot = true;
			chooseDifficulty = true;
		}
		else if (id == KeyEvent.VK_SPACE) {
			if (gameStatus == 0 || gameStatus == 3) {
				if (!chooseDifficulty) {
					bot = false;
				}
				else {
					chooseDifficulty = false;
				}
				start();
			}
			else if (gameStatus == 1) {
				gameStatus = 2;
			}
			else if (gameStatus == 2) {
				gameStatus = 1;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { //Method for key pressing
		int id = e.getKeyCode();
		if (id == KeyEvent.VK_W) {
			wKey = false;
		}
		else if (id == KeyEvent.VK_S) {
			sKey = false;
		}
		else if (id == KeyEvent.VK_UP) {
			upKey = false;
		}
		else if (id == KeyEvent.VK_DOWN) {
			downKey = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {} //Not used
}