package Compiler;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    private final Lexer lexer;
    private Token token;
    private int erros;
    private ArrayList<Type> tokensSincroniantes;

    public Parser(Lexer lexer) throws IOException {

        this.lexer = lexer;
        token = lexer.proxToken(); // Leitura inicial obrigatoria do primeiro simbolo
        System.out.println("[Token]" + token.toString());
        tokensSincroniantes = new ArrayList<Type>();

    }

    // Fecha os arquivos de entrada e de tokens
    public void fechaArquivos() {

        lexer.closeFile();

    }

    public void erroSintatico(String mensagem) {

        erros++;
        System.out.println("[Erro Sintatico]" + mensagem + " na linha " + token.getLine() + " e coluna " + token.getColumn());

        if (erros == 5) {
            System.out.println("5 Erros Sintáticos encontrados, finalizando sistema.");
            System.exit(0);
        }
    }

    public void advance() throws IOException {

        token = lexer.proxToken();

        System.out.println("[Token]" + token.toString());

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

    public void Skip(String mensagem) throws IOException {
        erroSintatico(mensagem);
        advance();
    }

    public void sincronizaToken(String mensagem) throws IOException {
        boolean casaToken = false;

        while (!casaToken && token.getClassType() != Type.EOF) {
            if (tokensSincroniantes.contains(token.getClassType())) {
                casaToken = true;
            } else {
                Skip(mensagem);
            }
        }
        tokensSincroniantes.clear(); // limpa a lista para a proxima sincronizacao
    }

    // prog → “program” “id” body 
    public void Prog() throws IOException {
        //System.out.println("Debug:Prog");
        if (eat(Type.KW_PROG)) {

            if (!eat(Type.ID)) {
                erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());

            }

            Body();
        } else {
            // token sincronizante: FOLLOW(Prog)
            tokensSincroniantes.add(Type.EOF);
            sincronizaToken("[Modo Panico] Esperado \"EOF\", encontrado " + token.getLexeme());
            erroSintatico("Esperado \"program\", encontrado " + token.getLexeme());

        }
    }

    // body → decl-list “{“ stmt-list “}” 
    public void Body() throws IOException {
        //System.out.println("Debug:Body");
        Decllist();

        if (eat(Type.SMB_OBC)) {

                Stmtlist();

            if (!eat(Type.SMB_CBC)) {
                erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());

            }

        } else {
            erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());

        }
    }

    // decl-list → decl “;” decl-list | ε 
    public void Decllist() throws IOException {
        //System.out.println("Debug:Declist");
        if (token.getClassType() == Type.SMB_OBC) {

            return;

        } else {

            Decl();

            if (eat(Type.SMB_SEM)) {

                Decllist();

            } else {
                erroSintatico("Esperado \";\", encontrado " + token.getLexeme());

            }

        }

    }

    // decl → type id-list 
    public void Decl() throws IOException {
        //System.out.println("Debug:Decl");
        if (token.getClassType() == Type.KW_NUM || token.getClassType() == Type.KW_CHAR) {

            Type();

        } else {
            erroSintatico("Esperado \"num, char\", encontrado " + token.getLexeme());

        }

        if (token.getClassType() == Type.ID) {

            Idlist();

        } else {
            erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());

        }

    }

    // type → “num” | “char” 
    public void Type() throws IOException {
        //System.out.println("Debug:Type");
        if (!eat(Type.KW_NUM) && !eat(Type.KW_CHAR)) {
            erroSintatico("Esperado \"num, char\", encontrado " + token.getLexeme());

        }

    }

    // id-list → “id” id-list' 
    public void Idlist() throws IOException {
    //System.out.println("Debug:Idlist");
        if (eat(Type.ID)) {

            Idlistlinha();

        } else {
            erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());

        }

    }

    // id-list' → “,” id-list | ε 
    public void Idlistlinha() throws IOException {
        //System.out.println("Debug:Idlistlinha");
        if (token.getClassType() == Type.SMB_SEM) {

        } else {

            if (eat(Type.SMB_COM)) {

                Idlist();

            } else {
                erroSintatico("Esperado \",\", encontrado " + token.getLexeme());
            }

        }
    }

    // stmt-list → stmt “;” stmt-list | ε 
    public void Stmtlist() throws IOException {
        //System.out.println("Debug:Stmtlist");
        if (token.getClassType() == Type.SMB_CBC) {

            return;

        } else {

            if (token.getClassType() == Type.ID || token.getClassType() == Type.KW_IF || token.getClassType() == Type.KW_WHILE
                    || token.getClassType() == Type.KW_READ || token.getClassType() == Type.KW_WRITE) {

                Stmt();

                if (!eat(Type.SMB_SEM)) {
                    erroSintatico("Esperado \";\", encontrado " + token.getLexeme());

                }

                Stmtlist();

            } else {
                erroSintatico("Esperado \"id, if, while, read, write\", encontrado " + token.getLexeme());

            }
        }

    }

    // stmt → assign-stmt | if-stmt | while-stmt | read-stmt | write-stmt 
    public void Stmt() throws IOException {
        //System.out.println("Debug:Stmt");
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

        }

    }

    // assign-stmt → “id” “=” simple_expr 
    public void Assignstmt() throws IOException {
        //System.out.println("Debug:Assignstmt");
        if (eat(Type.ID)) {

            if (!eat(Type.OP_ASS)) {
                erroSintatico("Esperado \"=\", encontrado " + token.getLexeme());

            }

            Simpleexpr();

        } else {
            erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());

        }

    }

    // if-stmt → “if” “(“ condition “)” “{“ stmt-list “}” if-stmt'   
    public void Ifstmt() throws IOException {
        //System.out.println("Ifstmt");
        if (eat(Type.KW_IF)) {

            if (!eat(Type.SMB_OPA)) {
                erroSintatico("Esperado \"(\", encontrado " + token.getLexeme());

            }

            Condition();

            if (!eat(Type.SMB_CPA)) {
                erroSintatico("Esperado \")\", encontrado " + token.getLexeme());

            }

            if (!eat(Type.SMB_OBC)) {
                erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());

            }

            if (token.getClassType() != Type.SMB_CBC) {

                Stmtlist();

            }

            if (!eat(Type.SMB_CBC)) {
                erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());

            }

            Ifstmtlinha();

        } else {
            erroSintatico("Esperado \"if\", encontrado " + token.getLexeme());

        }
    }

    // if-stmt' → “else” “{“ stmt-list “}” | ε 
    public void Ifstmtlinha() throws IOException {
        //System.out.println("Debug:Ifstmtlinha");
        if (token.getClassType() == Type.SMB_SEM) {

            return;

        } else {

            if (eat(Type.KW_ELSE)) {

                if (!eat(Type.SMB_OBC)) {
                    erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());

                }

                if (token.getClassType() != Type.SMB_CBC) {

                    Stmtlist();

                }

                if (!eat(Type.SMB_CBC)) {
                    erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());

                }

            } else {
                erroSintatico("Esperado \"else\", encontrado " + token.getLexeme());

            }
        }

    }

    // condition → expression 
    public void Condition() throws IOException {
        //System.out.println("Debug:Condition");
        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT
                || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Expression();

        } else {
            erroSintatico("Esperado \"id, (, not, num_const, char_const\", encontrado " + token.getLexeme());

        }

    }

    // while-stmt → stmt-prefix “{“ stmt-list “}” 
    public void Whilestmt() throws IOException {
        //System.out.println("Debug:Whilestmt");
        if (token.getClassType() != Type.KW_WHILE) {

            Stmtprefix();

            if (!eat(Type.SMB_OBC)) {
                erroSintatico("Esperado \"{\", encontrado " + token.getLexeme());

            }

            if (token.getClassType() != Type.SMB_CBC) {

                Stmtlist();

            }

            if (!eat(Type.SMB_CBC)) {
                erroSintatico("Esperado \"}\", encontrado " + token.getLexeme());

            }

        } else {
            erroSintatico("Esperado \"while\", encontrado " + token.getLexeme());

        }

    }

    // stmt-prefix → “while” “(“ condition “)” 
    public void Stmtprefix() throws IOException {
        //System.out.println("Debug:Stmtprefix");
        if (eat(Type.KW_WHILE)) {

            if (!eat(Type.SMB_OPA)) {
                erroSintatico("Esperado \"(\", encontrado " + token.getLexeme());

            }

            Condition();

            if (!eat(Type.SMB_CPA)) {
                erroSintatico("Esperado \")\", encontrado " + token.getLexeme());

            }

        } else {
            erroSintatico("Esperado \"while\", encontrado " + token.getLexeme());

        }

    }

    // read-stmt → “read” “id” 
    public void Readstmt() throws IOException {
        //System.out.println("Debug:Readstmt");
        if (eat(Type.KW_READ)) {

            if (!eat(Type.ID)) {
                erroSintatico("Esperado \"id\", encontrado " + token.getLexeme());

            }

        } else {
            erroSintatico("Esperado \"read\", encontrado " + token.getLexeme());

        }

    }

    // write-stmt → “write” writable 
    public void Writestmt() throws IOException {
        //System.out.println("Debug:Weitestmt");
        if (eat(Type.KW_WRITE)) {

            Writable();

        } else {
            erroSintatico("Esperado \"write\", encontrado " + token.getLexeme());

        }

    }

    // writable → simple-expr | “literal” 
    public void Writable() throws IOException {
        //System.out.println("Debug:Writable");
        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT
                || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Simpleexpr();

        } else if (eat(Type.LIT)) {

        } else {
            erroSintatico("Esperado \"literal\", encontrado " + token.getLexeme());

        }

    }

    // expression → simple-expr  expression' 
    public void Expression() throws IOException {
        //System.out.println("Debug:Expression");
        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT
                || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Simpleexpr();

            Expressionlinha();

        } else {
            erroSintatico("Esperado \"id, (, not, num_const, char_const\", encontrado " + token.getLexeme());

        }

    }

    // expression' → relop simple-expr | ε 
    public void Expressionlinha() throws IOException {
        //System.out.println("Debug:Expressionlinha");
        if (token.getClassType() == Type.SMB_CPA) {

            return;

        } else {

            if (token.getClassType() == Type.OP_ASS || token.getClassType() == Type.OP_GT || token.getClassType() == Type.OP_GE
                    || token.getClassType() == Type.OP_LT || token.getClassType() == Type.OP_LE || token.getClassType() == Type.OP_NE) {

                Relop();

                Simpleexpr();

            } else {
                erroSintatico("Esperado \"=, >, >=, <, <=, !=\", encontrado " + token.getLexeme());

            }
        }

    }

    // simple-expr →  term simple-expr' 
    public void Simpleexpr() throws IOException {
        //System.out.println("Debug:Simpleexpr");
        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT
                || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Term();

            Simpleexprlinha();

        } else {
            erroSintatico("Esperado \"id, (, not, num_const, char_const\", encontrado " + token.getLexeme());

        }

    }

    // simple-expr' → addop term simple-expr' | ε 
    public void Simpleexprlinha() throws IOException {
        //System.out.println("Debug:Simpleexprlinha");
        if (token.getClassType() == Type.SMB_SEM || token.getClassType() == Type.SMB_CPA || token.getClassType() == Type.OP_ASS
                || token.getClassType() == Type.OP_GT || token.getClassType() == Type.OP_GE || token.getClassType() == Type.OP_LT
                || token.getClassType() == Type.OP_LE || token.getClassType() == Type.OP_NE) {

            return;

        } else {

            if (token.getClassType() == Type.OP_ADD || token.getClassType() == Type.OP_MIN || token.getClassType() == Type.KW_OR) {

                Addop();

                Term();

                Simpleexprlinha();

            } else {
                erroSintatico("Esperado \"+, -, or\", encontrado " + token.getLexeme());

            }
        }
    }

    // term → factor-a term'  
    public void Term() throws IOException {
        //System.out.println("Debug:Term");
        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.KW_NOT
                || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Factora();

            Termlinha();

        } else {
            erroSintatico("Esperado \"id, (, not, num_const, char_const\", encontrado " + token.getLexeme());

        }

    }

    // term' → mulop factor-a term' | ε 
    public void Termlinha() throws IOException {
        //System.out.println("Debug:Termlinha");
        if (token.getClassType() == Type.SMB_SEM || token.getClassType() == Type.SMB_CPA || token.getClassType() == Type.OP_ASS
                || token.getClassType() == Type.OP_GT || token.getClassType() == Type.OP_GE || token.getClassType() == Type.OP_LT
                || token.getClassType() == Type.OP_LE || token.getClassType() == Type.OP_NE || token.getClassType() == Type.KW_OR
                || token.getClassType() == Type.OP_MIN || token.getClassType() == Type.OP_ADD) {

            return;

        } else {

            if (token.getClassType() == Type.KW_AND || token.getClassType() == Type.OP_MUL || token.getClassType() == Type.OP_DIV) {

                Mulop();

                Factora();

                Termlinha();

            } else {
                erroSintatico("Esperado \"and, *, /\", encontrado " + token.getLexeme());

            }
        }
    }

    // factor-a → factor | “not” factor 
    public void Factora() throws IOException {
        //System.out.println("Debug:Factora");
        if (token.getClassType() == Type.ID || token.getClassType() == Type.SMB_OPA || token.getClassType() == Type.CON_NUM || token.getClassType() == Type.CON_CHAR) {

            Factor();

        } else if (eat(Type.KW_NOT)) {

            Factor();

        } else {
            erroSintatico("Esperado \"id, (, num_const, char_const, not\", encontrado " + token.getLexeme());

        }

    }

    // factor → “id” | constant | “(“ expression “)” 
    public void Factor() throws IOException {
        //System.out.println("Debug:Factor");
        if (token.getClassType() == Type.ID) {

            eat(Type.ID);

        } else if (token.getClassType() == Type.CON_CHAR || token.getClassType() == Type.CON_NUM) {

            Constant();

        } else if (eat(Type.SMB_OPA)) {

            Expression();

            if (!eat(Type.SMB_CPA)) {

                erroSintatico("Esperado \")\", encontrado " + token.getLexeme());

            }

        } else {
            erroSintatico("Esperado \"id, (, num_const, char_const\", encontrado " + token.getLexeme());

        }
    }

    // relop → “==” | “>” | “>=” | “<” | “<=” | “!=” 
    public void Relop() throws IOException {
        //System.out.println("Debug:Relop");
        if (!eat(Type.OP_EQ) && !eat(Type.OP_GT) && !eat(Type.OP_GE) && !eat(Type.OP_LT) && !eat(Type.OP_LE) && !eat(Type.OP_NE)) {
            erroSintatico("Esperado \"==, >, >=, <, <=, !=\", encontrado " + token.getLexeme());

        }

    }

    // addop → “+” | “-” | “or” 
    public void Addop() throws IOException {
        //System.out.println("Debug:Addop");
        if (!eat(Type.OP_ADD) && !eat(Type.OP_MIN) && !eat(Type.KW_OR)) {
            erroSintatico("Esperado \"+, -, or\", encontrado " + token.getLexeme());

        }

    }

    // mulop → “*” | “/” | “and” 
    public void Mulop() throws IOException {
        //System.out.println("Debug:Mulop");
        if (!eat(Type.OP_MUL) && !eat(Type.OP_DIV) && !eat(Type.KW_AND)) {
            erroSintatico("Esperado \"*, /, and\", encontrado " + token.getLexeme());

        }

    }

    // constant → “num_const” | “char_const” 
    public void Constant() throws IOException {
        //System.out.println("Debug:Constant");
        if (!eat(Type.CON_CHAR) && !eat(Type.CON_NUM)) {
            erroSintatico("Esperado \"num_const, char_const\", encontrado " + token.getLexeme());

        }

    }

}
