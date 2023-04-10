package pong; //All .java files in pong package
import java.awt.Color; //Import awt librarys
import java.awt.Graphics;
import java.util.Random;

public class Ball { //Ball class to configure ball
	//Declare some variables
	public int x, y, width = 25, height = 25, moveX, moveY, numOfHits;
	public Random random;
	private Pong pong;
	//Constructor
	public Ball(Pong pong) {
		this.pong = pong;
		this.random = new Random(); //Ball randomly go to a side
		spawn(); //Spawn and respawn ball 
	}

	//Ball size
	//Change ball speed
	public void update(Paddle paddle1, Paddle paddle2) {
		int speed = 5;
		this.x += moveX * speed;
		this.y += moveY * speed;
		if (this.y + height - moveY > pong.height || this.y + moveY < 0) {
			if (this.moveY < 0) {
				this.y = 0;
				this.moveY = random.nextInt(4);
				if (moveY == 0) {
					moveY = 1;
				}
			}
			else {
				this.moveY = -random.nextInt(4);
				this.y = pong.height - height;
				if (moveY == 0) {
					moveY = -1;
				}
			}
		}
		//If paddle hit ball
		if (checkCollision(paddle1) == 1) {
			this.moveX = 1 + (numOfHits / 5);
			this.moveY = -2 + random.nextInt(4);
			if (moveY == 0) {
				moveY = 1;
			}
			numOfHits++;
		}
		//If paddle hit ball
		else if (checkCollision(paddle2) == 1) {
			this.moveX = -1 - (numOfHits / 5);
			this.moveY = -2 + random.nextInt(4);
			if (moveY == 0) {
				moveY = 1;
			}
			numOfHits++;
		}
		//If paddle do not hit ball
		if (checkCollision(paddle1) == 2) {
			paddle2.score++;
			spawn();
		}
		//If paddle do not hit ball
		else if (checkCollision(paddle2) == 2) {
			paddle1.score++;
			spawn();
		}
	}

	//Spawn ball
	public void spawn() {
		this.numOfHits = 0;
		this.x = pong.width / 2 - this.width / 2;
		this.y = pong.height / 2 - this.height / 2;
		this.moveY = -2 + random.nextInt(4);
		if (moveY == 0) {
			moveY = 1;
		}
		//Ball move either left or right by random
		if (random.nextBoolean()) {
			moveX = 1;
		}
		else {
			moveX = -1;
		}
	}

	public int checkCollision(Paddle paddle) {
		if (this.x < paddle.x + paddle.width && this.x + width > paddle.x && this.y < paddle.y + paddle.height && this.y + height > paddle.y) {
			return 1; //Ball bounce
		}
		else if ((paddle.x > x && paddle.paddleNum == 1) || (paddle.x < x - width && paddle.paddleNum == 2)) {
			return 2; //Ball score
		}
		return 0;
	}

	//Ball color
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, width, height);
	}
}