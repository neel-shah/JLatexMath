package org.scilab.forge.jlatexmath.editor;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class testEditor {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame();
	    frame.setTitle("");
	    frame.setSize(500, 100);
	    frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    Container contentPane = frame.getContentPane();
	    myController control = new myController();
	    contentPane.add(control.view.panel);
	 
	    frame.show();
	}

}
