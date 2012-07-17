package org.scilab.forge.jlatexmath;

import java.util.ArrayList;

public class TrigoAtom extends Atom
{
	private Atom treeParent = null;
	ArrayList<Atom> children = new ArrayList<Atom>();
	
	private Atom parent = null;
	private Atom nextSibling = null;
	private Atom prevSibling = null;
	private Atom subExpr = null;
	
	protected TypedAtom typed= null;
	protected FencedAtom fenced = null;
	
	public TrigoAtom(TypedAtom typed, FencedAtom fenced)
	{
		this.typed = typed;
		this.fenced = fenced;
	}

	@Override
	public Box createBox(TeXEnvironment env)
	{
		this.setTreeRelation();
		this.setArrowRelation();
		Box a = typed.createBox(env);
		Box b = fenced.createBox(env);
		HorizontalBox hBox = new HorizontalBox(env.getColor(), env.getBackground());
		hBox.add(a);
		hBox.add(b);
		
		usedBox = hBox;
		return hBox;
	}

	public void setTreeRelation()
	{
		typed.setTreeParent(this);
		fenced.setTreeParent(this);
		if(children != null)
    		children.clear();
		children.add(typed);
		children.add(fenced);
	}
	
	public void setArrowRelation()
	{
		this.setSubExpr(fenced);
		fenced.setNextSibling(this);
		fenced.setPrevSibling(this);
		fenced.setParent(this);
	}
	
	@Override
	public void setTreeParent(Atom at)
	{
		this.treeParent = at;
	}

	@Override
	public Atom getTreeParent()
	{
		return this.treeParent;
	}

	@Override
	public void setChildren(Atom at) 
	{
		
	}

	@Override
	public void setParent(Atom at) 
	{
		this.parent = at;
	}

	@Override
	public Atom getParent()
	{
		return this.parent;
	}

	@Override
	public void setNextSibling(Atom at) 
	{
		this.nextSibling = at;
	}

	@Override
	public Atom getNextSibling()
	{
		return this.nextSibling;
	}

	@Override
	public void setPrevSibling(Atom at) 
	{
		this.prevSibling = at;
	}

	@Override
	public Atom getPrevSibling()
	{
		return this.prevSibling;
	}

	@Override
	public void setSubExpr(Atom at)
	{
		this.subExpr = at;
	}

	@Override
	public Atom getSubExpr() 
	{
		return this.subExpr;
	}

}
