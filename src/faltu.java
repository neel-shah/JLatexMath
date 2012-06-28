import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;


public class faltu 
{
	JFrame frame = null;
	JTextField nameTextField = null;
	TeXFormula formula = null;
	TeXIcon icon = null;
	BufferedImage image = null;
	Graphics2D g2 = null;
	JLabel jl = null;
	File file = null;
	
	public faltu()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		this.formula = Example.formula;
		this.icon = Example.icon;
		this.image = Example.image;
		this.g2 = Example.g2;
		this.jl = Example.jl;
		this.file = Example.file;
		
		KeyListener keyListener = new KeyListener()
		{

			public void keyTyped(KeyEvent e) 
			{
				char c = e.getKeyChar();
				formula.formulaEditedKeyTyped(c);
				icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 50);
				icon.setInsets(new Insets(5, 5, 5, 5));

				image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				g2 = image.createGraphics();
				g2.setColor(Color.white);
				g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
				jl = new JLabel();
				jl.setForeground(new Color(0, 0, 0));
				icon.paintIcon(jl, g2, 0, 0);
				file = new File("Example2.png");
				try {
				    ImageIO.write(image, "png", file.getAbsoluteFile());
				} catch (IOException ex) {}
			}

			public void keyPressed(KeyEvent e) 
			{
				formula.formulaEditedKeyPressed(e.getKeyCode(), e);
				if(e.getKeyCode() == 8)
				{
					icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 50);
					icon.setInsets(new Insets(5, 5, 5, 5));

					image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					g2 = image.createGraphics();
					g2.setColor(Color.white);
					g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
					jl = new JLabel();
					jl.setForeground(new Color(0, 0, 0));
					icon.paintIcon(jl, g2, 0, 0);
					file = new File("Example2.png");
					try {
					    ImageIO.write(image, "png", file.getAbsoluteFile());
					} catch (IOException ex) {}
				}
			}

			public void keyReleased(KeyEvent e) 
			{
				
			}
			
		};
		
		nameTextField = new JTextField();
	    frame.add(nameTextField, BorderLayout.NORTH);
	    nameTextField.addKeyListener(keyListener);
	    
	    frame.setSize(250, 100);
	    frame.setVisible(true);
	}
}
