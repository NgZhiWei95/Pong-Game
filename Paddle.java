package pong; //All .java files in pong package
import java.awt.Color; //Import awt librarys
import java.awt.Graphics;

public class Paddle //Paddle class to configure paddle
{
	//Declare some variables
	public int x, y, width = 50, height = 250, paddleNum, score;
	//Constructor
	public Paddle(Pong pong, int paddleNum) {
		this.paddleNum = paddleNum;
		if (paddleNum == 1) {
			this.x = 0;
		}
		//Paddle width and height
		if (paddleNum == 2) {
			this.x = pong.width - width;
		}
		this.y = pong.height / 2 - this.height / 2;
	}
	
	//Color of paddle
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
	}

	//Paddle move speed
	public void move(boolean up) {
		int speed = 15;
		if (up) {
			if (y - speed > 0) {
				y -= speed;
			}
			else {
				y = 0;
			}
		}
		else {
			if (y + height + speed < Pong.pong.height) {
				y += speed;
			}
			else {
				y = Pong.pong.height - height;
			}
		}
	}
}