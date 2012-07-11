package org.scilab.forge.jlatexmath.editor;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.scilab.forge.jlatexmath.Atom;
import org.scilab.forge.jlatexmath.TeXFormula;

public class myModel extends java.util.Observable
{
	TeXFormula formula = null;
	String latex = null;
	ArrayList<Atom> rootSel = null; 
	
	public myModel()
	{
		latex = "x";
		formula = new TeXFormula(latex);
		rootSel = new ArrayList<Atom>();
	}
	
	public void keyTyped(char c)
	{
		formula.formulaEditedKeyTyped(c);
		Atom root = formula.getRoot();
		Atom selAtom = formula.treEd.getSelAtm();
		if(rootSel != null)
			rootSel.clear();
		rootSel.add(root);
		rootSel.add(selAtom);
		this.setChanged();
		this.notifyObservers(rootSel);
	}
	
	public void keyPressed(KeyEvent e)
	{
		formula.formulaEditedKeyPressed(e.getKeyCode(), e);
		Atom root = formula.getRoot();
		Atom selAtom = formula.treEd.getSelAtm();
		if(rootSel != null)
			rootSel.clear();
		rootSel.add(root);
		rootSel.add(selAtom);
		this.setChanged();
		this.notifyObservers(rootSel);
	}
	
	public void init()
	{
		Atom root = formula.getRoot();
		Atom selAtom = formula.getRoot();
		if(rootSel != null)
			rootSel.clear();
		rootSel.add(root);
		rootSel.add(selAtom);
		this.setChanged();
		this.notifyObservers(rootSel);
	}
}
