package org.scilab.forge.jlatexmath.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class myController implements KeyListener, MouseListener
{
	myModel model = null;
	myView view = null;
	
	public myController()
	{
		model = new myModel();
		view = new myView();
		model.addObserver(view);
		model.init();
		view.addListener(this);
		view.frame.show();
	}

	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		model.keyTyped(c);
	}

	public void keyPressed(KeyEvent e) 
	{
		model.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) 
	{
		
	}

	public void mouseClicked(MouseEvent e) 
	{
		view.text.requestFocus();
		model.formulaClicked(e.getX() - 8, e.getY() - 30, view.te);
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
