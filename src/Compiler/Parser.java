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

        if (eat(Type.KW_PROG)) {

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

        if (token.getClassType() != Type.SMB_OBC) {

            Decllist();

        } else if (eat(Type.SMB_OBC)) {

            if (token.getClassType() != Type.SMB_CBC) {

                Stmtlist();

            }

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
    public void Decllist() throws IOException {

        Decl();

        if (eat(Type.SMB_SEM)) {

            if (token.getClassType() != Type.SMB_OBC) {

                Decllist();

            }
        } else {
            erroSintatico("Esperado \";\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // decl → type id-list 
    public void Decl() throws IOException {

        if (token.getClassType() == Type.KW_NUM || token.getClassType() == Type.KW_CHAR) {

            Type();

        } else {
            erroSintatico("Esperado \"num, char\", encontrado " + token.getLexeme());
            System.exit(1);
        }

        if (token.getClassType() == Type.ID) {

            Idlist();

        } else {
            erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // type → “num” | “char” 
    public void Type() throws IOException {

        if (!eat(Type.KW_NUM) && !eat(Type.KW_CHAR)) {
            erroSintatico("Esperado \"num, char\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // id-list → “id” id-list' 
    public void Idlist() throws IOException {

        if (eat(Type.ID)) {

            if (token.getClassType() != Type.SMB_SEM) {

                Idlistlinha();
            }

        } else {
            erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // id-list' → “,” id-list | ε 
    public void Idlistlinha() throws IOException {

        if (eat(Type.SMB_COM)) {

            Idlist();

        } else {
            erroSintatico("Esperado \",\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // stmt-list → stmt “;” stmt-list | ε 
    public void Stmtlist() throws IOException {

        if (token.getClassType() == Type.ID || token.getClassType() == Type.KW_IF || token.getClassType() == Type.KW_WHILE || token.getClassType() == Type.KW_READ || token.getClassType() == Type.KW_WRITE) {

            Stmt();

            if (!eat(Type.SMB_SEM)) {
                erroSintatico("Esperado \";\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            if (token.getClassType() != Type.SMB_CBC) {

                Stmtlist();

            }

        } else {
            erroSintatico("Esperado \"id, if, while, read, write\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // stmt → assign-stmt | if-stmt | while-stmt | read-stmt | write-stmt 
    public void Stmt() throws IOException {

        if (token.getClassType() == Type.ID) {

            Assignstmt();

        } else if (token.getClassType() == Type.KW_IF) {

            Ifstmt();

        } else if (token.getClassType() == Type.KW_WHILE) {

            Whilestmt();

        } else if (token.getClassType() == Type.KW_READ) {

            Readstmt();

        } else if (token.getClassType() == Type.KW_WRITE) {

            Writestmt();

        } else {
            erroSintatico("Esperado \"id, if, while, read, write\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // assign-stmt → “id” “=” simple_expr 
    public void Assignstmt() throws IOException {

        if (eat(Type.ID)) {

            if (!eat(Type.OP_ASS)) {
                erroSintatico("Esperado \"=\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            Simpleexpr();

        } else {
            erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // if-stmt → “if” “(“ condition “)” “{“ stmt-list “}” if-stmt'   
    public void Ifstmt() throws IOException {

        if (eat(Type.KW_IF)) {

            if (!eat(Type.SMB_OPA)) {
                erroSintatico("Esperado \"(\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            Condition();

            if (!eat(Type.SMB_CPA)) {
                erroSintatico("Esperado \")\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            if (!eat(Type.SMB_OBC)) {
                erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            if (token.getClassType() != Type.SMB_CBC) {

                Stmtlist();

            }

            if (!eat(Type.SMB_CBC)) {
                erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            if (token.getClassType() != Type.SMB_SEM) {

                Ifstmtlinha();

            }
        } else {
            erroSintatico("Esperado \"if\", encontrado " + token.getLexeme());
            System.exit(1);
        }
    }

    // if-stmt' → “else” “{“ stmt-list “}” | ε 
    public void Ifstmtlinha() throws IOException {

        if (eat(Type.KW_ELSE)) {

            if (!eat(Type.SMB_OBC)) {
                erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            if (token.getClassType() != Type.SMB_CBC) {

                Stmtlist();

            }

            if (!eat(Type.SMB_CBC)) {
                erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());
                System.exit(1);
            }

        } else {
            erroSintatico("Esperado \"else\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // condition → expression 
    public void Condition() {

        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Expression();

        } else {
            erroSintatico("Esperado \"id, (, not, num_const, char_const\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // while-stmt → stmt-prefix “{“ stmt-list “}” 
    public void Whilestmt() throws IOException {

        if (token.getClassType() != Type.KW_WHILE) {

            Stmtprefix();

            if (!eat(Type.SMB_OBC)) {
                erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            if (token.getClassType() != Type.SMB_CBC) {

                Stmtlist();

            }

            if (!eat(Type.SMB_CBC)) {
                erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());
                System.exit(1);
            }

        } else {
            erroSintatico("Esperado \"while\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // stmt-prefix → “while” “(“ condition “)” 
    public void Stmtprefix() throws IOException {

        if (eat(Type.KW_WHILE)) {

            if (!eat(Type.SMB_OPA)) {
                erroSintatico("Esperado \"(\", encontrado " + token.getLexeme());
                System.exit(1);
            }

            Condition();

            if (!eat(Type.SMB_CPA)) {
                erroSintatico("Esperado \")\", encontrado " + token.getLexeme());
                System.exit(1);
            }

        } else {
            erroSintatico("Esperado \"while\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // read-stmt → “read” “id” 
    public void Readstmt() throws IOException {

        if (eat(Type.KW_READ)) {

            if (!eat(Type.ID)) {
                erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());
                System.exit(1);
            }

        } else {
            erroSintatico("Esperado \"read\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // write-stmt → “write” writable 
    public void Writestmt() throws IOException {

        if (eat(Type.KW_WRITE)) {

            Writable();

        } else {
            erroSintatico("Esperado \"write\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // writable → simple-expr | “literal” 
    public void Writable() throws IOException {

        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Simpleexpr();

        } else if (eat(Type.LIT)) {

        } else {
            erroSintatico("Esperado \"literal\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // expression → simple-expr  expression' 
    public void Expression() {

        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Simpleexpr();

            if (token.getClassType() != Type.SMB_CPA) {

                Expressionlinha();

            }

        } else {
            erroSintatico("Esperado \"id, (, not, num_const, char_const\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // expression' → relop simple-expr | ε 
    public void Expressionlinha() {

        if (token.getClassType() == Type.OP_ASS || token.getClassType() == Type.OP_GT || token.getClassType() == Type.OP_GE || token.getClassType() == Type.OP_LT || token.getClassType() == Type.OP_LE || token.getClassType() == Type.OP_NE) {

            Relop();

            Simpleexpr();

        } else {
            erroSintatico("Esperado \"=, >, >=, <, <=, !=\", encontrado " + token.getLexeme());
            System.exit(1);
        }

    }

    // simple-expr →  term simple-expr' 
    public void Simpleexpr() {
        
        

    }

    // simple-expr' → addop term simple-expr' | ε 
    public void Simpleexprlinha() {

    }

    // term → factor-a term'  
    public void Term() {

    }

    // term' → mulop factor-a term' | ε 
    public void Termlinha() {

    }

    // factor-a → factor | “not” factor 
    public void Factora() {

    }

    // factor → “id” | constant | “(“ expression “)” 
    public void Factor() {

    }

    // relop → “==” | “>” | “>=” | “<” | “<=” | “!=” 
    public void Relop() {

    }

    // addop → “+” | “-” | “or” 
    public void Addop() {

    }

    // mulop → “*” | “/” | “and” 
    public void Mulop() {

    }

    // constant → “num_const” | “char_const” 
    public void Constant() {

    }

}
