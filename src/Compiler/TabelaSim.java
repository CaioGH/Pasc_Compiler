
package Compiler;

import java.util.HashMap;

public class TabelaSim {

    private HashMap<Token, Identifier> symbolTable; 

    public TabelaSim() {
        symbolTable = new HashMap();

        Token word;
        word = new Token(Type.KW_PROG, "program", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_IF, "if", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_ELSE, "else", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_WHILE, "while", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_WRITE, "write", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_READ, "read", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_NUM, "num", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_CHAR, "char", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_NOT, "not", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_OR, "or", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW_AND, "and", 0, 0);
        this.symbolTable.put(word, new Identifier());
    }

    public void put(Token w, Identifier i) {
        symbolTable.put(w, i);
    }

    public Identifier getIdentifier(Token w) {
        Identifier infoIdentificador = (Identifier) symbolTable.get(w);
        return infoIdentificador;
    }

    public Token returnToken(String lexeme) {
        for (Token token : symbolTable.keySet()) {
            if (token.getLexeme().equals(lexeme)) {
                return token;
            }
        }
        return null;
    }

    public void show() {
        int i = 1;
        
        for (Token token : symbolTable.keySet())
        {
            System.out.println("posicao " + i + ": \t" + token.toString());
            i++;
        }
    }
}
