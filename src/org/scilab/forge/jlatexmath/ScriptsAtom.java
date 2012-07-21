/* ScriptsAtom.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 * 
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 * 
 */

/* Modified by Calixte Denizet */

package org.scilab.forge.jlatexmath;

import java.util.ArrayList;

/**
 * An atom representing scripts to be attached to another atom.
 */
public class ScriptsAtom extends Atom {
    
	private Atom treeParent = null;
	ArrayList<Atom> children = new ArrayList<Atom>();
	
	private Atom parent = null;
	private Atom nextSibling = null;
	private Atom prevSibling = null;
	private Atom subExpr = null;
	
    // TeX constant: what's the use???
    private final static SpaceAtom SCRIPT_SPACE = new SpaceAtom(TeXConstants.UNIT_POINT, 0.5f, 0, 0);
    
    // base atom
    private Atom base;
    
    // subscript and superscript to be attached to the base (if not null)
    private final Atom subscript;
    private Atom superscript;
    private int align = TeXConstants.ALIGN_LEFT;
    
    public ScriptsAtom(Atom base, Atom sub, Atom sup) {
	this.base = base;
	subscript = sub;
	superscript = sup;
    }
    
    public ScriptsAtom(Atom base, Atom sub, Atom sup, boolean left) {
	this(base, sub, sup);
	if (!left) 
	    align = TeXConstants.ALIGN_RIGHT;
    }
    
    public Box createBox(TeXEnvironment env) {
    	TreeEditor.addAtoms(this);
    	this.setTreeRelations();
    	this.setArrowRelations();
	Box b = (base == null ? new StrutBox(0, 0, 0, 0) : base.createBox(env));
        Box deltaSymbol = new StrutBox(0, 0, 0, 0);
	if (subscript == null && superscript == null)
	{
		usedBox = b;
	    return b;
	}
	else {
	    TeXFont tf = env.getTeXFont();
	    int style = env.getStyle();
	    
	    if (base.type_limits == TeXConstants.SCRIPT_LIMITS || (base.type_limits == TeXConstants.SCRIPT_NORMAL && style == TeXConstants.STYLE_DISPLAY))
	    {
	    	usedBox = new UnderOverAtom(new UnderOverAtom(base, subscript, TeXConstants.UNIT_POINT, 0.3f, true, false),
					 superscript, TeXConstants.UNIT_POINT, 3.0f, true, true).createBox(env);
	    	return usedBox; 
		}
	    
	    HorizontalBox hor = new HorizontalBox(b);
	    
	    int lastFontId = b.getLastFontId();
	    // if no last font found (whitespace box), use default "mu font"
	    if (lastFontId == TeXFont.NO_FONT)
		lastFontId = tf.getMuFontId();
	    
	    TeXEnvironment subStyle = env.subStyle(), supStyle = env.supStyle();
	    
	    // set delta and preliminary shift-up and shift-down values
	    float delta = 0, shiftUp, shiftDown;
	    
	    // TODO: use polymorphism?
	    if (base instanceof AccentedAtom) { // special case :
		// accent. This positions superscripts better next to the accent!
		Box box = ((AccentedAtom) base).base.createBox(env.crampStyle());
		shiftUp = box.getHeight() - tf.getSupDrop(supStyle.getStyle());
		shiftDown = box.getDepth() + tf.getSubDrop(subStyle.getStyle());
	    } else if (base instanceof SymbolAtom
		       && base.type == TeXConstants.TYPE_BIG_OPERATOR) { // single big operator symbol
		Char c = tf.getChar(((SymbolAtom) base).getName(), style);
		if (style < TeXConstants.STYLE_TEXT && tf.hasNextLarger(c)) // display
		    // style
		    c = tf.getNextLarger(c, style);
		Box x = new CharBox(c);
		
		x.setShift(-(x.getHeight() + x.getDepth()) / 2
			   - env.getTeXFont().getAxisHeight(env.getStyle()));
		hor = new HorizontalBox(x);
		
		// include delta in width or not?
		delta = c.getItalic();
                deltaSymbol = new SpaceAtom(TeXConstants.MEDMUSKIP).createBox(env);
		if (delta > TeXFormula.PREC && subscript == null)
		    hor.add(new StrutBox(delta, 0, 0, 0));
		
		shiftUp = hor.getHeight() - tf.getSupDrop(supStyle.getStyle());
		shiftDown = hor.getDepth() + tf.getSubDrop(subStyle.getStyle());
	    } else if (base instanceof CharSymbol) {
		shiftUp = shiftDown = 0;
		CharFont cf = ((CharSymbol) base).getCharFont(tf);
		if (!((CharSymbol) base).isMarkedAsTextSymbol() || !tf.hasSpace(cf.fontId)) {
		    delta = tf.getChar(cf, style).getItalic();
		}
		if (delta > TeXFormula.PREC && subscript == null) {
		    hor.add(new StrutBox(delta, 0, 0, 0));
		    delta = 0;
		}
	    } else {
		shiftUp = b.getHeight() - tf.getSupDrop(supStyle.getStyle());
		shiftDown = b.getDepth() + tf.getSubDrop(subStyle.getStyle());
	    }
	    
	    if (superscript == null) { // only subscript
		Box x = subscript.createBox(subStyle);
		// calculate and set shift amount
		x.setShift(Math.max(Math.max(shiftDown, tf.getSub1(style)), x.getHeight() - 4 * Math.abs(tf.getXHeight(style, lastFontId)) / 5));
		hor.add(x);
		hor.add(deltaSymbol);
                
		usedBox = hor;
		return hor;
	    } else {
		Box x = superscript.createBox(supStyle);
		float msiz = x.getWidth();
		if (subscript != null && align == TeXConstants.ALIGN_RIGHT) {
		    msiz = Math.max(msiz, subscript.createBox(subStyle).getWidth());
		}
		
		HorizontalBox sup = new HorizontalBox(x, msiz, align);
		// add scriptspace (constant value!)
		sup.add(SCRIPT_SPACE.createBox(env));
		// adjust shift-up
		float p;
		if (style == TeXConstants.STYLE_DISPLAY)
		    p = tf.getSup1(style);
		else if (env.crampStyle().getStyle() == style)
		    p = tf.getSup3(style);
		else
		    p = tf.getSup2(style);
		shiftUp = Math.max(Math.max(shiftUp, p), x.getDepth()
				   + Math.abs(tf.getXHeight(style, lastFontId)) / 4);
		
		if (subscript == null) { // only superscript
		    sup.setShift(-shiftUp);
		    hor.add(sup);
		} else { // both superscript and subscript
		    Box y = subscript.createBox(subStyle);
		    HorizontalBox sub = new HorizontalBox(y, msiz, align);
		    // add scriptspace (constant value!)               
		    sub.add(SCRIPT_SPACE.createBox(env));
		    // adjust shift-down
		    shiftDown = Math.max(shiftDown, tf.getSub2(style));
		    // position both sub- and superscript
		    float drt = tf.getDefaultRuleThickness(style);
		    float interSpace = shiftUp - x.getDepth() + shiftDown
			- y.getHeight(); // space between sub- en
		    // superscript
		    if (interSpace < 4 * drt) { // too small
			shiftUp += 4 * drt - interSpace;
			// set bottom superscript at least 4/5 of X-height
			// above
			// baseline
			float psi = 4 * Math.abs(tf.getXHeight(style, lastFontId))
			    / 5 - (shiftUp - x.getDepth());
			
			if (psi > 0) {
			    shiftUp += psi;
			    shiftDown -= psi;
			}
		    }
		    // create total box
		    
		    VerticalBox vBox = new VerticalBox();
		    sup.setShift(delta);
		    vBox.add(sup);
		    // recalculate interspace
		    interSpace = shiftUp - x.getDepth() + shiftDown - y.getHeight();
		    vBox.add(new StrutBox(0, interSpace, 0, 0));
		    vBox.add(sub);
		    vBox.setHeight(shiftUp + x.getHeight());
		    vBox.setDepth(shiftDown + y.getDepth());
		    hor.add(vBox);
		}
                hor.add(deltaSymbol);

        usedBox = hor;
		return hor;
	    }
	}
    }
    
    public int getLeftType() {
	return base.getLeftType();
    }
    
    public int getRightType() {
	return base.getRightType();
    }
    
    public void setTreeRelations()
    {
    	if(children != null)
    		children.clear();
    	children.add(base);
    	children.add(superscript);
    	base.setTreeParent(this);
    	if(superscript != null)		
    		superscript.setTreeParent(this);
    }

    public void setArrowRelations()
    {
    	this.setSubExpr(superscript);
    	base.setParent(superscript);
    	base.setNextSibling(superscript);
    	base.setPrevSibling(this);
    	if(superscript != null)		
    	{
    		superscript.setParent(this);
    		superscript.setNextSibling(this);
    		superscript.setPrevSibling(base);
    		if(superscript instanceof CharAtom || superscript instanceof SymbolAtom)
    			superscript.setSubExpr(base);
    	}
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
	public Atom getParent() {
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
	
	public Atom getBase()
	{
		return this.base;
	}
	
	public Atom getSuperScript()
	{
		return this.superscript;
	}
	
	public void setBase(Atom at)
	{
		this.base = at;
	}
	
	public void setSuperScript(Atom at)
	{
		this.superscript = at;
	}
}
