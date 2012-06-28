package org.scilab.forge.jlatexmath;

import java.awt.event.KeyEvent;

/**
 * Maintains a structure like MathML to convert JLaTeXMath into WYSIWYG
 * @author neel
 *
 */

public class TreeEditor 
{
	protected Atom selAtom = null;
	protected Atom root = null;
	protected Atom treeParent = null;
	protected TeXFormula formula = null;
	protected boolean isWritingCommand = false;
	protected StringBuffer command = new StringBuffer(); 
	
	public void keyTyped(TeXFormula formula, Atom r, char c)
	{
		this.formula = formula;
		if(selAtom == null)
			selAtom = r;
		treeParent = selAtom.getTreeParent();
		String com = null;
		switch(c)
		{
			case '^' :
				this.isWritingCommand = false;
				this.makeScripts();
				break;
			case '/' :
				this.isWritingCommand = false;
				this.makeFraction();
				break;
			case '|' :
				this.isWritingCommand = true;
				if(command.length() > 1)
					command.delete(0, command.length());
				com = command.toString();
				this.symbolChar('|');
				break;
			default :
				if(c == '')
					return;
				this.symbolChar(c);
				if(this.isWritingCommand)
				{
					command.append(c);
					com = command.toString();
					int value = Commands.getValue(com);
					if(value != 0)
						this.commandWritten(value);
				}
		}
	}
	
	public void keyPressed(TeXFormula formula, int n)
	{
		this.formula = formula;
		if(selAtom == null)
			selAtom = formula.getRoot();
		treeParent = selAtom.getTreeParent();
		switch(n)
		{
		case 37 :
			if(selAtom.getPrevSibling() != null)
				selAtom = selAtom.getPrevSibling();
			break;
		case 38 :
			 if(selAtom.getParent() != null)
				 selAtom = selAtom.getParent();
			 break;
		case 39 :
			if(selAtom.getNextSibling() != null)
				selAtom = selAtom.getNextSibling();
			break;
		case 40 :
			if(selAtom.getSubExpr() != null)
				selAtom = selAtom.getSubExpr();
			break;
		case 8 :
			if(selAtom != null)
				this.delete();
		default :
			return;
		}
	}
	
	public void commandWritten(int value)
	{
		
		switch(value)
		{
		case 1 :
			this.isWritingCommand = false;
			this.formula.createBox(this.formula.style);
			this.squareRoot();
			break;
		default :
			return;
		}	
	}
	
	private void delete() 
	{
		if(selAtom instanceof EmptyAtom)
		{
			treeParent = selAtom.getTreeParent();
			if(treeParent != null && treeParent instanceof ScriptsAtom)
			{
				ScriptsAtom s = (ScriptsAtom) treeParent;
				Atom grandPa = s.getTreeParent();
				if(grandPa != null && grandPa instanceof ScriptsAtom)
				{
					ScriptsAtom gs = (ScriptsAtom) grandPa;
					if(treeParent.equals(gs.getBase()))
					{
						if(selAtom.equals(s.getBase()))
							gs.setBase(s.getSuperScript());
						else
							gs.setBase(s.getBase());
						this.selAtom = gs.getBase();
					}
					else
					{
						if(selAtom.equals(s.getBase()))
							gs.setSuperScript(s.getSuperScript());
						else
							gs.setSuperScript(s.getBase());
						this.selAtom = gs.getSuperScript();
					}
				}
				if(grandPa != null && grandPa instanceof FractionAtom)
				{
					FractionAtom gf = (FractionAtom) grandPa;
					if(treeParent.equals(gf.getNum()))
					{
						if(selAtom.equals(s.getBase()))
							gf.setNum(s.getSuperScript());
						else
							gf.setNum(s.getBase());
						this.selAtom = gf.getNum();
					}
					else
					{
						if(selAtom.equals(s.getBase()))
							gf.setDenom(s.getSuperScript());
						else
							gf.setDenom(s.getBase());
						this.selAtom = gf.getDenom();
					}
				}
				if(grandPa != null && grandPa instanceof NthRoot)
				{
					NthRoot gn = (NthRoot) grandPa;
				}
				if(grandPa != null && grandPa instanceof RowAtom)
				{
					RowAtom gr = (RowAtom) grandPa;
					int j = gr.elements.size();
					int i = 0;
					while(i != j)
					{
						if(treeParent.equals(gr.elements.get(i)))
						{
							gr.elements.remove(i);
							break;
						}
						++i;
					}
					if(selAtom.equals(s.getBase()))
					{
						if(s.getSuperScript() instanceof RowAtom)
						{
							RowAtom temp = (RowAtom) s.getSuperScript();
							int k = temp.elements.size();
							int l = 0;
							while(l != k)
							{
								gr.elements.add(i, temp.elements.get(l));
								++i;
								++l;
							}
							this.selAtom = temp.elements.get(k-1);
						}
						else
						{
							gr.elements.add(i, s.getSuperScript());
							this.selAtom = s.getSuperScript();
						}
					}
					else
					{

						if(s.getBase() instanceof RowAtom)
						{
							RowAtom temp = (RowAtom) s.getBase();
							int k = temp.elements.size();
							int l = 0;
							while(l != k)
							{
								gr.elements.add(i, temp.elements.get(l));
								++i;
								++l;
							}
							this.selAtom = temp.elements.get(k-1);
						}
						else
						{
							gr.elements.add(i, s.getBase());
							this.selAtom = s.getBase();
						}
					}
				}
				if(grandPa == null)
				{
					if(selAtom.equals(s.getBase()))
						this.formula.setRoot(s.getSuperScript());
					else
						this.formula.setRoot(s.getBase());
					this.selAtom = this.formula.getRoot();
				}
			}
			if(treeParent != null && treeParent instanceof FractionAtom)
			{
				FractionAtom f = (FractionAtom) treeParent;
				Atom grandPa = f.getTreeParent();
				if(grandPa != null && grandPa instanceof ScriptsAtom)
				{
					ScriptsAtom gs = (ScriptsAtom) grandPa;
					if(treeParent.equals(gs.getBase()))
					{
						if(selAtom.equals(f.getNum()))
							gs.setBase(f.getDenom());
						else
							gs.setBase(f.getNum());
						this.selAtom = gs.getBase();
					}
					else
					{
						if(selAtom.equals(f.getNum()))
							gs.setSuperScript(f.getDenom());
						else
							gs.setSuperScript(f.getNum());
						this.selAtom = gs.getSuperScript();
					}
				}
				if(grandPa != null && grandPa instanceof FractionAtom)
				{
					FractionAtom gf = (FractionAtom) grandPa;
					if(treeParent.equals(gf.getNum()))
					{
						if(selAtom.equals(f.getNum()))
							gf.setNum(f.getDenom());
						else
							gf.setNum(f.getNum());
						this.selAtom = gf.getNum();
					}
					else
					{
						if(selAtom.equals(f.getNum()))
							gf.setDenom(f.getDenom());
						else
							gf.setDenom(f.getNum());
						this.selAtom = gf.getDenom();
					}
				}
				if(grandPa != null && grandPa instanceof NthRoot)
				{
					NthRoot gn = (NthRoot) grandPa;
				}
				if(grandPa != null && grandPa instanceof RowAtom)
				{
					RowAtom gr = (RowAtom) grandPa;
					int j = gr.elements.size();
					int i = 0;
					while(i != j)
					{
						if(treeParent.equals(gr.elements.get(i)))
						{
							gr.elements.remove(i);
							break;
						}
						++i;
					}
					if(selAtom.equals(f.getNum()))
					{
						if(f.getDenom() instanceof RowAtom)
						{
							RowAtom temp = (RowAtom) f.getDenom();
							int k = temp.elements.size();
							int l = 0;
							while(l != k)
							{
								gr.elements.add(i, temp.elements.get(l));
								++i;
								++l;
							}
							this.selAtom = temp.elements.get(k-1);
						}
						else
						{
							gr.elements.add(i, f.getDenom());
							this.selAtom = f.getDenom();
						}
					}
					else
					{
						if(f.getNum() instanceof RowAtom)
						{
							RowAtom temp = (RowAtom) f.getNum();
							int k = temp.elements.size();
							int l = 0;
							while(l != k)
							{
								gr.elements.add(i, temp.elements.get(l));
								++i;
								++l;
							}
							this.selAtom = temp.elements.get(k-1);
						}
						else
						{
							gr.elements.add(i, f.getNum());
							this.selAtom = f.getNum();
						}
					}
				}
				if(grandPa == null)
				{
					if(selAtom.equals(f.getNum()))
						this.formula.setRoot(f.getDenom());
					else
						this.formula.setRoot(f.getNum());
					this.selAtom = this.formula.getRoot();
				}
			}
			if(treeParent != null && treeParent instanceof RowAtom)
			{
				RowAtom r = (RowAtom) treeParent;
				int j = r.elements.size();
				int i = 0;
				while(i != j)
				{
					if(selAtom.equals(r.elements.get(i)))
					{
						r.elements.remove(i);
						break;
					}
					++i;
				}
				if(j > 2)
				{
					if(i == j-1)
						this.selAtom = r.elements.get(i-1);
					else
						this.selAtom = r.elements.get(i);
				}
				else
				{
					Atom grandPa = treeParent.getTreeParent();
					if(grandPa != null && grandPa instanceof ScriptsAtom)
					{
						ScriptsAtom gs = (ScriptsAtom) grandPa;
						if(treeParent.equals(gs.getBase()))
						{
							gs.setBase(r.elements.getFirst());
							this.selAtom = gs.getBase();
						}
						else
						{
							gs.setSuperScript(r.elements.getFirst());
							this.selAtom = gs.getSuperScript();
						}
					}
					if(grandPa != null && grandPa instanceof FractionAtom)
					{
						FractionAtom gf = (FractionAtom) grandPa;
						if(treeParent.equals(gf.getNum()))
						{
							gf.setNum(r.elements.getFirst());
							this.selAtom = gf.getNum();
						}
						else
						{
							gf.setDenom(r.elements.getFirst());
							this.selAtom = gf.getDenom();
						}
					}
					if(grandPa != null && grandPa instanceof NthRoot)
					{
						NthRoot gn = (NthRoot) grandPa;
						if(treeParent.equals(gn.getBase()))
							gn.setBase(r.elements.getFirst());
						else
							gn.setRoot(r.elements.getFirst());
						this.selAtom = r.elements.getFirst();
					}
					if(grandPa == null)
					{
						this.formula.setRoot(r.elements.getFirst());
						this.formula.createBox(this.formula.style);
						this.selAtom = this.formula.getRoot();
					}
				}
			}
		}
		else
		{
			treeParent = selAtom.getTreeParent();
			if(treeParent != null && treeParent instanceof ScriptsAtom)
			{
				ScriptsAtom s = (ScriptsAtom) treeParent;
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				if(selAtom.equals(s.getBase()))
					s.setBase(e);
				else
					s.setSuperScript(e);
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof FractionAtom)
			{
				FractionAtom f = (FractionAtom) treeParent;
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				if(selAtom.equals(f.getNum()))
					f.setNum(e);
				else
					f.setDenom(e);
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof NthRoot)
			{
				NthRoot n = (NthRoot) treeParent;
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof RowAtom)
			{
				RowAtom r = (RowAtom) treeParent;
				int j = r.elements.size();
				int i = 0;
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				while(i != j)
				{
					if(selAtom.equals(r.elements.get(i)))
					{
						r.elements.remove(i);
						break;	
					}
					++i;
				}
				r.elements.add(i, e);
				this.selAtom = e;
			}
			if(treeParent == null)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				this.formula.setRoot(e);
				this.formula.createBox(this.formula.style);
				this.selAtom = e;
			}
		}
	}
	
	public void makeScripts()
	{
		if(treeParent != null && treeParent instanceof ScriptsAtom)
		{
			ScriptsAtom temp = (ScriptsAtom) treeParent;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			ScriptsAtom s = new ScriptsAtom(selAtom, null, e);
			if(selAtom.equals(temp.getBase()))
				temp.setBase(s);
			else
				temp.setSuperScript(s);
			this.selAtom = e;
		}
		if(treeParent != null && treeParent instanceof FractionAtom)
		{
			FractionAtom temp = (FractionAtom) treeParent;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			ScriptsAtom s = new ScriptsAtom(selAtom, null, e);
			if(selAtom.equals(temp.getNum()))
				temp.setNum(s);
			else
				temp.setDenom(s);
			this.selAtom = e;
		}
		if(treeParent != null && treeParent instanceof NthRoot)
		{
			NthRoot temp = (NthRoot) treeParent;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			ScriptsAtom s = new ScriptsAtom(selAtom, null, e);
			this.selAtom = e;
		}
		if(treeParent != null && treeParent instanceof RowAtom)
		{
			RowAtom temp = (RowAtom) treeParent;
			int j = temp.elements.size();
			int i = 0;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			ScriptsAtom s = new ScriptsAtom(selAtom, null, e);
			while(i != j)
			{
				if(selAtom.equals(temp.elements.get(i)))
				{
					temp.elements.remove(i);
					break;
				}
				++i;
			}
			temp.elements.add(i, s);
			this.selAtom = e;
		}
		if(treeParent == null)
		{
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			ScriptsAtom s = new ScriptsAtom(selAtom, null, e);
			formula.setRoot(s);
			this.selAtom = e;
		}
	}
	
	public void makeFraction()
	{
		if(treeParent != null && treeParent instanceof ScriptsAtom)
		{
			ScriptsAtom temp = (ScriptsAtom) treeParent;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			FractionAtom f = new FractionAtom(selAtom, e);
			if(selAtom.equals(temp.getBase()))
				temp.setBase(f);
			else
				temp.setSuperScript(f);
			this.selAtom = e;
		}
		if(treeParent != null && treeParent instanceof FractionAtom)
		{
			FractionAtom temp = (FractionAtom) treeParent;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			FractionAtom f = new FractionAtom(selAtom, e);
			if(selAtom.equals(temp.getNum()))
				temp.setNum(f);
			else
				temp.setDenom(f);
			this.selAtom = e;
		}
		if(treeParent != null && treeParent instanceof NthRoot)
		{
			NthRoot temp = (NthRoot) treeParent;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			FractionAtom f = new FractionAtom(selAtom, e);
			this.selAtom = e;
		}
		if(treeParent != null && treeParent instanceof RowAtom)
		{
			RowAtom temp = (RowAtom) treeParent;
			int j = temp.elements.size();
			int i = 0;
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			FractionAtom f = new FractionAtom(selAtom, e);
			while(i != j)
			{
				if(selAtom.equals(temp.elements.get(i)))
				{
					temp.elements.remove(i);
					break;
				}
				++i;
			}
			temp.elements.add(i, f);
			this.selAtom = e;
		}
		if(treeParent == null)
		{
			EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
			FractionAtom f = new FractionAtom(selAtom, e);
			formula.setRoot(f);
			this.selAtom = e;
		}
	}
	
	public void symbolChar(char c)
	{
		if(selAtom instanceof EmptyAtom)
		{
			if(treeParent != null && treeParent instanceof ScriptsAtom)
			{
				ScriptsAtom temp = (ScriptsAtom) treeParent;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				if(selAtom.equals(temp.getBase()))
					temp.setBase(root);
				else
					temp.setSuperScript(root);
				this.selAtom = root;
			}
			if(treeParent != null && treeParent instanceof FractionAtom)
			{
				FractionAtom temp = (FractionAtom) treeParent;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				if(selAtom.equals(temp.getNum()))
					temp.setNum(root);
				else
					temp.setDenom(root);
				this.selAtom = root;
			}
			if(treeParent != null && treeParent instanceof NthRoot)
			{
				NthRoot temp = (NthRoot) treeParent;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				this.selAtom = root;
			}
			if(treeParent != null && treeParent instanceof RowAtom)
			{
				RowAtom temp = (RowAtom) treeParent;
				int j = temp.elements.size();
				int i = 0;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				while(i != j)
				{
					if(selAtom.equals(temp.elements.get(i)))
					{
						temp.elements.remove(i);
						break;
					}
					++i;
				}
				temp.elements.add(i, root);
				this.selAtom = root;
			}
			if(treeParent == null)
			{
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				this.formula.setRoot(root);
				this.selAtom = root;
			}
		}
		else
		{
			if(treeParent != null && treeParent instanceof ScriptsAtom)
			{
				ScriptsAtom s = (ScriptsAtom) treeParent;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				if(selAtom instanceof RowAtom)
				{
					RowAtom temp = (RowAtom) selAtom;
					temp.elements.addLast(root);
				}
				else
				{
					RowAtom temp = new RowAtom(selAtom);
					temp.add(root);
					if(selAtom.equals(s.getBase()))
						s.setBase(temp);
					else
						s.setSuperScript(temp);
				}
				this.selAtom = root;
			}
			if(treeParent != null && treeParent instanceof FractionAtom)
			{
				FractionAtom f = (FractionAtom) treeParent;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				if(selAtom instanceof RowAtom)
				{
					RowAtom temp = (RowAtom) selAtom;
					temp.elements.addLast(root);
				}
				else
				{
					RowAtom temp = new RowAtom(selAtom);
					temp.add(root);
					if(selAtom.equals(f.getNum()))
						f.setNum(temp);
					else
						f.setDenom(temp);
				}
				this.selAtom = root;
			}
			if(treeParent != null && treeParent instanceof NthRoot)
			{
				NthRoot n = (NthRoot) treeParent;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				this.selAtom = root;
			}
			if(treeParent != null && treeParent instanceof RowAtom)
			{
				RowAtom r = (RowAtom) treeParent;
				int j = r.elements.size();
				int i = 0;
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				while(i != j)
				{
					if(selAtom.equals(r.elements.get(i)))
						break;
					++i;
				}
				r.elements.add(i+1,root);
				this.selAtom = root;
			}
			if(treeParent == null)
			{
				String latex = c + "";
				TeXFormula formula = new TeXFormula(latex);
				Atom root = formula.getRoot();
				if(selAtom instanceof RowAtom)
				{
					RowAtom r = (RowAtom) selAtom;
					r.elements.addLast(root);
				}
				else
				{
					RowAtom r = new RowAtom(selAtom);
					r.add(root);
					this.formula.setRoot(r);
				}
				this.selAtom = root;
			}
		}
	}
	
	public void squareRoot()
	{
		treeParent = selAtom.getTreeParent();
		if(selAtom instanceof EmptyAtom)
		{
			if(treeParent != null && treeParent instanceof ScriptsAtom)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				ScriptsAtom temp = (ScriptsAtom) treeParent;
				if(selAtom.equals(temp.getBase()))
					temp.setBase(n);
				else
					temp.setSuperScript(n);
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof FractionAtom)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				FractionAtom temp = (FractionAtom) treeParent;
				if(selAtom.equals(temp.getNum()))
					temp.setNum(n);
				else
					temp.setDenom(n);
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof NthRoot)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				NthRoot temp = (NthRoot) treeParent;
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof RowAtom)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				RowAtom temp = (RowAtom) treeParent;
				int j = temp.elements.size();
				int i = 0;
				while(i != j)
				{
					if(selAtom.equals(temp.elements.get(i)))
					{
						temp.elements.remove(i);
						break;
					}
					++i;
				}
				temp.elements.add(i, n);
				this.selAtom = e;
			}
			if(treeParent == null)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				this.formula.setRoot(n);
				this.selAtom = e;
			}
		}
		else
		{
			if(treeParent != null && treeParent instanceof ScriptsAtom)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				ScriptsAtom s = (ScriptsAtom) treeParent;
				if(selAtom instanceof RowAtom)
				{
					RowAtom temp = (RowAtom) selAtom;
					temp.elements.addLast(n);
				}
				else
				{
					RowAtom temp = new RowAtom(selAtom);
					temp.add(n);
					if(selAtom.equals(s.getBase()))
						s.setBase(temp);
					else
						s.setSuperScript(temp);
				}
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof FractionAtom)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				FractionAtom f = (FractionAtom) treeParent;
				if(selAtom instanceof RowAtom)
				{
					RowAtom temp = (RowAtom) selAtom;
					temp.elements.addLast(n);
				}
				else
				{
					RowAtom temp = new RowAtom(selAtom);
					temp.add(n);
					if(selAtom.equals(f.getNum()))
						f.setNum(temp);
					else
						f.setDenom(temp);
				}
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof NthRoot)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				NthRoot tn = (NthRoot) treeParent;
				this.selAtom = e;
			}
			if(treeParent != null && treeParent instanceof RowAtom)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				RowAtom r = (RowAtom) treeParent;
				int j = r.elements.size();
				int i = 0;
				while(i != j)
				{
					if(selAtom.equals(r.elements.get(i)))
						break;
					++i;
				}
				r.elements.add(i+1,n);
				this.selAtom = e;
			}
			if(treeParent == null)
			{
				EmptyAtom e = new EmptyAtom(0.5f, 0.5f, 0, 0);
				NthRoot n = new NthRoot(e, null);
				if(selAtom instanceof RowAtom)
				{
					RowAtom r = (RowAtom) selAtom;
					r.elements.addLast(n);
				}
				else
				{
					RowAtom r = new RowAtom(selAtom);
					r.add(n);
					this.formula.setRoot(r);
				}
				this.selAtom = e;
			}
		}
	}
}
