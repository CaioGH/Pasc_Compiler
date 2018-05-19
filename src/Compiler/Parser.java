package Compiler;

import java.io.IOException;

public class Parser {

    private final Lexer lexer;
    private Token token;

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        token = lexer.proxToken(); // Leitura inicial obrigatoria do primeiro simbolo
        System.out.println("[DEBUG]" + token.toString());
    }

    // Fecha os arquivos de entrada e de tokens
    public void fechaArquivos() {

        lexer.closeFile();
    }

    public void erroSintatico(String mensagem) {

        System.out.println("[Erro Sintatico] na linha " + token.getLine() + " e coluna " + token.getColumn());
        System.out.println(mensagem + "\n");
    }

    public void advance() throws IOException {
        token = lexer.proxToken();
        System.out.println("[DEBUG]" + token.toString());
    }

    // verifica token esperado t
    public boolean eat(Type t) throws IOException {
        if (token.getClassType() == t) {
            advance();
            return true;
        } else {
            return false;
        }
    }

    // prog → “program” “id” body 
    public void Prog() throws IOException {

        // Espera program
        if (token.getClassType() == Type.KW_PROG) {
            // Espera "id"
            if (!eat(Type.ID)) {
                erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            Body();
        } else {
            erroSintatico("Esperado \"program\", encontrado " + token.getLexeme());
            System.exit(1);
        }
    }

    // body → decl-list “{“ stmt-list “}” 
    public void Body() throws IOException {

        if (token.getClassType() == Type.KW_NUM || token.getClassType() == Type.KW_CHAR) {

            Decllist();

        } else if (token.getClassType() == Type.SMB_OBC) {

            Stmtlist();

            if (!eat(Type.SMB_CBC)) {
                erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());
                System.exit(1);
            }

        } else {
            erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());
            System.exit(1);
        }
    }

    // decl-list → decl “;” decl-list | ε 
    public void Decllist() {

    }
    
    // stmt-list → stmt “;” stmt-list | ε 
    public void Stmtlist(){
        
    }

}
