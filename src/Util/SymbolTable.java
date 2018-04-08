package Util;

import java.util.HashMap;

public class SymbolTable
 {
    
    private HashMap<Token, Identifier> symbolTable;

	public SymbolTable()
	{
		
		Token word;
		word = new Token (Type.KW, "while",0 ,0);
		this.symbolTable.put(word, new Identifier());

		word = new Token (Type.KW, "read",0 ,0);
		this.symbolTable.put(word, new Identifier());
		
		word = new Token (Type.KW, "write",0 ,0);
		this.symbolTable.put(word, new Identifier());
		
		word = new Token (Type.KW, "if",0 ,0);
		this.symbolTable.put(word, new Identifier());

		word = new Token (Type.KW, "else",0 ,0);
		this.symbolTable.put(word, new Identifier());
		
		word = new Token (Type.KW, "or",0 ,0);
		this.symbolTable.put(word, new Identifier());
		
		word = new Token (Type.KW, "and",0 ,0);
		this.symbolTable.put(word, new Identifier());

		word = new Token (Type.KW, "program",0 ,0);
		this.symbolTable.put(word, new Identifier());

		word = new Token (Type.KW, "not",0 ,0);
		this.symbolTable.put(word, new Identifier());

		word = new Token (Type.KW, "char",0 ,0);
		this.symbolTable.put(word, new Identifier());

		word = new Token (Type.KW, "num",0 ,0);
		this.symbolTable.put(word, new Identifier());
	}

}