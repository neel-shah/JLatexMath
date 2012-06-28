/* LaTeXAtom.java
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
 * An atom representing whitespace. The dimension values can be set using different
 * unit types.
 */
public class LaTeXAtom extends Atom {

    public LaTeXAtom() {
    }
    
    public Box createBox(TeXEnvironment env) {
	env = env.copy(env.getTeXFont().copy());
	env.getTeXFont().setRoman(true);
	float sc = env.getTeXFont().getScaleFactor();

        TeXFormula.FontInfos fontInfos = TeXFormula.externalFontMap.get(Character.UnicodeBlock.BASIC_LATIN);
        if (fontInfos != null) {
            TeXFormula.externalFontMap.put(Character.UnicodeBlock.BASIC_LATIN, null);
        }
	RowAtom rat = (RowAtom)((RomanAtom)new TeXFormula("\\mathrm{XETL}").root).base;
        if (fontInfos != null) {
            TeXFormula.externalFontMap.put(Character.UnicodeBlock.BASIC_LATIN, fontInfos);
        }

	HorizontalBox hb = new HorizontalBox(rat.getLastAtom().createBox(env));
	hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.7f * sc, 0, 0).createBox(env));
	float f = new SpaceAtom(TeXConstants.UNIT_EX, 0.45f * sc, 0, 0).createBox(env).getWidth();
	float f1 = new SpaceAtom(TeXConstants.UNIT_EX, 0.45f * sc, 0, 0).createBox(env).getWidth();
	CharBox A = new CharBox(env.getTeXFont().getChar('A', "mathnormal", env.supStyle().getStyle()));
	A.setShift(-f);
	hb.add(A);
	hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.2f * sc, 0, 0).createBox(env));
	hb.add(rat.getLastAtom().createBox(env));
	hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.5f * sc, 0, 0).createBox(env));
	Box E = rat.getLastAtom().createBox(env);
	E.setShift(f1);
	hb.add(E);
	hb.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.25f * sc, 0, 0).createBox(env));
	hb.add(rat.getLastAtom().createBox(env));
	return hb;
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
