/* EmptyAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2010 DENIZET Calixte
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

package org.scilab.forge.jlatexmath;

import java.util.ArrayList;

/**
 * An empty atom.
 */
public class EmptyAtom extends Atom {
	
	float height;
	float width;
	float depth;
	float shift;
	
	private Atom treeParent = null;
	ArrayList<Atom> children = new ArrayList<Atom>();
	
	private Atom parent = null;
	private Atom nextSibling = null;
	private Atom prevSibling = null;
	private Atom subExpr = null;

    public EmptyAtom() { 
    	height = 0;
    	width = 0;
    	depth = 0;
    	shift = 0;
    }
    
    public EmptyAtom(float h, float w, float d, float s)
    {
    	height = h;
    	width = w;
    	depth = d;
    	shift = s;
    }
 
    public Box createBox(TeXEnvironment env) 
    {
    	TreeEditor.addAtoms(this);
    	usedBox = new StrutBox(height, width, depth, shift);
    	return usedBox;
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
	public void setChildren(Atom at) {
		// TODO Auto-generated method stub
		
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
