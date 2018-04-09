/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.HashMap;

/**
 *
 * @author caios
 */
public class TabelaSim {

    private HashMap<Token, Identifier> symbolTable; // Tabela de símbolos do ambiente

    public TabelaSim() {
        symbolTable = new HashMap();

        // Inserindo as palavras reservadas
        Token word;
        word = new Token(Type.KW, "program", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "if", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "else", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "while", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "write", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "read", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "num", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "char", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "not", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "or", 0, 0);
        this.symbolTable.put(word, new Identifier());

        word = new Token(Type.KW, "and", 0, 0);
        this.symbolTable.put(word, new Identifier());
    }

    public void put(Token w, Identifier i) {
        symbolTable.put(w, i);
    }

    // Retorna um identificador de um determinado token
    public Identifier getIdentifier(Token w) {
        Identifier infoIdentificador = (Identifier) symbolTable.get(w);
        return infoIdentificador;
    }

    // Pesquisa na tabela de símbolos se há algum tokem com determinado lexema
    // vamos usar esse metodo somente para diferenciar ID e KW
    public Token returnToken(String lexeme) {
        for (Token token : symbolTable.keySet()) {
            if (token.getLexeme().equals(lexeme)) {
                return token;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String saida = "";
        int i = 1;
        for (Token token : symbolTable.keySet()) {
            saida += ("posicao " + i + ": \t" + token.toString()) + "\n";
            i++;
        }
        return saida;
    }
}