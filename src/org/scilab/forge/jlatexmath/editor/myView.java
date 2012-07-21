package org.scilab.forge.jlatexmath.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.scilab.forge.jlatexmath.Atom;
import org.scilab.forge.jlatexmath.Box;
import org.scilab.forge.jlatexmath.DefaultTeXFont;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXEnvironment;

public class myView extends JPanel implements java.util.Observer
{
	JPanel panel = null;
	private Graphics saveg;
	Atom root = null;
	Atom selAtom  = null;
	int size = 18;
	JTextField text = null;
	private Box box = null;
	TeXEnvironment te = new TeXEnvironment(TeXConstants.STYLE_DISPLAY, new DefaultTeXFont(size));
	private Rectangle currentBox = new Rectangle();
	private Rectangle rect = null;
	JFrame frame = null;
	Container contentPane = null;
	
	public myView()
	{
		panel = new JPanel();
		panel.requestFocus();
		text = new JTextField(20);
		panel.add(text);
		
		frame = new JFrame();
		frame.setTitle("");
	    frame.setSize(400, 400);
	    frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    contentPane = frame.getContentPane();
	    contentPane.add(this);
	}
	
	public void addListener(KeyListener control)
	{
		frame.addMouseListener((MouseListener) control);
		text.addKeyListener(control);
	}

	public void update(Observable o, Object arg) 
	{
		ArrayList<Atom> a = (ArrayList) arg;
		root = a.get(0);
		selAtom = a.get(1);
		box = root.createBox(te);
		box.updateRectangle(size, (3/size), (3/size));
		currentBox = selAtom.getBox().getRectangle();
		frame.getContentPane().repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	    rect = new Rectangle(currentBox.x + 178, currentBox.y + 178, currentBox.width + 2, currentBox.height + 2);
	    Graphics2D g2 = (Graphics2D) g;
	    if(g != saveg)
	    {
	    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	 	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	 	    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	 	    saveg = g;
	    }
	    g2.setColor(Color.red);
	    AffineTransform oldAt = g2.getTransform();
	    g2.draw(rect);
	    g2.setColor(Color.black);
		g2.scale(size, size); // the point size
		box.draw(g2, 10, 10);
		g2.setTransform(oldAt);
	}
}
