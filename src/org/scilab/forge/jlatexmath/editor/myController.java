package org.scilab.forge.jlatexmath.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class myController implements KeyListener
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
}
