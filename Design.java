package pong; //All .java files in pong package
import java.awt.Graphics; //Import swing and graphics librarys
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Design extends JPanel { //Design class for interface
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) { //To make interface
		super.paintComponent(g);
		Pong.pong.render((Graphics2D) g);
	}
}