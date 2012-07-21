/* SymbolAtom.java
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
import java.util.BitSet;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A box representing a symbol (a non-alphanumeric character).
 */
public class SymbolAtom extends CharSymbol {
	
	private Atom treeParent = null;
	ArrayList<Atom> children = new ArrayList<Atom>();
	
	private Atom parent = null;
	private Atom nextSibling = null;
	private Atom prevSibling = null;
	private Atom subExpr = null;
    
    // whether it's is a delimiter symbol
    private final boolean delimiter;
    
    // symbol name
    private final String name;
    
    // contains all defined symbols
    public static Map<String, SymbolAtom> symbols;
    
    // contains all the possible valid symbol types
    private static BitSet validSymbolTypes;

    private char unicode;
    
    static {
        symbols = new TeXSymbolParser().readSymbols();
        
        // set valid symbol types
        validSymbolTypes =  new BitSet(16);
        validSymbolTypes.set(TeXConstants.TYPE_ORDINARY);
        validSymbolTypes.set(TeXConstants.TYPE_BIG_OPERATOR);
        validSymbolTypes.set(TeXConstants.TYPE_BINARY_OPERATOR);
        validSymbolTypes.set(TeXConstants.TYPE_RELATION);
        validSymbolTypes.set(TeXConstants.TYPE_OPENING);
        validSymbolTypes.set(TeXConstants.TYPE_CLOSING);
        validSymbolTypes.set(TeXConstants.TYPE_PUNCTUATION);
        validSymbolTypes.set(TeXConstants.TYPE_ACCENT);
    }
    
    public SymbolAtom(SymbolAtom s, int type) throws InvalidSymbolTypeException {
        if (!validSymbolTypes.get(type))
            throw new InvalidSymbolTypeException(
                    "The symbol type was not valid! "
                    + "Use one of the symbol type constants from the class 'TeXConstants'.");
        name = s.name;
        this.type = type;
	if (type == TeXConstants.TYPE_BIG_OPERATOR)
	    this.type_limits = TeXConstants.SCRIPT_NORMAL;

        delimiter = s.delimiter;
    }
    
    /**
     * Constructs a new symbol. This used by "TeXSymbolParser" and the symbol
     * types are guaranteed to be valid.
     *
     * @param name symbol name
     * @param type symbol type constant
     * @param del whether the symbol is a delimiter
     */
    public SymbolAtom(String name, int type, boolean del) {
        this.name = name;
        this.type = type;
	if (type == TeXConstants.TYPE_BIG_OPERATOR)
	    this.type_limits = TeXConstants.SCRIPT_NORMAL;

        delimiter = del;
    }

    public SymbolAtom setUnicode(char c) {
	this.unicode = c;
	return this;
    }

    public char getUnicode() {
	return unicode;
    }
    
    public static void addSymbolAtom(String file) {
	FileInputStream in;
	try {
	    in = new FileInputStream(file);
	} catch (FileNotFoundException e) {
	    throw new ResourceParseException(file, e);
	}
	addSymbolAtom(in, file);
    }

    public static void addSymbolAtom(InputStream in, String name) {
	TeXSymbolParser tsp = new TeXSymbolParser(in, name);
	symbols.putAll(tsp.readSymbols());
    }

    public static void addSymbolAtom(SymbolAtom sym) {
	symbols.put(sym.name, sym);
    }
    
    /**
     * Looks up the name in the table and returns the corresponding SymbolAtom representing
     * the symbol (if it's found).
     *
     * @param name the name of the symbol
     * @return a SymbolAtom representing the found symbol
     * @throws SymbolNotFoundException if no symbol with the given name was found
     */
    public static SymbolAtom get(String name) throws SymbolNotFoundException {
        Object obj = symbols.get(name);
        if (obj == null) // not found
            throw new SymbolNotFoundException(name);
        else
        {
        	SymbolAtom ob = (SymbolAtom) obj;
        	SymbolAtom s = new SymbolAtom(ob, ob.type);
            return s;
        }
    }
    
    /**
     *
     * @return true if this symbol can act as a delimiter to embrace formulas
     */
    public boolean isDelimiter() {
        return delimiter;
    }
    
    public String getName() {
        return name;
    }
    
    public Box createBox(TeXEnvironment env) {
    	TreeEditor.addAtoms(this);
        TeXFont tf = env.getTeXFont();
        int style = env.getStyle();
	Char c = tf.getChar(name, style);
	Box cb = new CharBox(c);
	if (env.getSmallCap() && unicode != 0 && Character.isLowerCase(unicode)) {
	    try {
		cb = new ScaleBox(new CharBox(tf.getChar(TeXFormula.symbolTextMappings[Character.toUpperCase(unicode)], style)), 0.8, 0.8);
	    } catch (SymbolMappingNotFoundException e) { }
	}

	if (type == TeXConstants.TYPE_BIG_OPERATOR) {
	    if (style < TeXConstants.STYLE_TEXT && tf.hasNextLarger(c))
		c = tf.getNextLarger(c, style);
	    cb = new CharBox(c);
	    cb.setShift(-(cb.getHeight() + cb.getDepth()) / 2 - env.getTeXFont().getAxisHeight(env.getStyle()));
	    float delta = c.getItalic();
	    HorizontalBox hb = new HorizontalBox(cb);
	    if (delta > TeXFormula.PREC)
		hb.add(new StrutBox(delta, 0, 0, 0));
	    
	    usedBox = hb;
	    return hb;
	}
	
	usedBox = cb;
	return cb;
    }
    
    public CharFont getCharFont(TeXFont tf) {
        // style doesn't matter here
        return tf.getChar(name, TeXConstants.STYLE_DISPLAY).getCharFont();
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
