/* TextStyleAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
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

package org.scilab.forge.jlatexmath;

/**
 * An atom representing a modification of style in a formula (e.g. textstyle or displaystyle).
 */
public class TextStyleAtom extends Atom {
    
    private String style;
    private Atom at;
    
    public TextStyleAtom(Atom at, String style) {
	this.style = style;
	this.at = at;
    }

    public Box createBox(TeXEnvironment env) {
	String prevStyle = env.getTextStyle();
	env.setTextStyle(style);
	Box box = at.createBox(env);
	env.setTextStyle(prevStyle);
	return box;
    }

	@Override
	public void setTreeParent(Atom at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Atom getTreeParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChildren(Atom at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParent(Atom at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Atom getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNextSibling(Atom at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Atom getNextSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrevSibling(Atom at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Atom getPrevSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSubExpr(Atom at) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Atom getSubExpr() {
		// TODO Auto-generated method stub
		return null;
	} 
}