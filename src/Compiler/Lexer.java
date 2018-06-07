package Compiler;

import java.io.*;

public class Lexer {

    private static final int EOF = -1;
    private static int lastChar = 0;
    public static int line = 1;
    public static int column = 1;
    public static TabelaSim TS;
    RandomAccessFile file_reference;
    ErrorMessages EM = new ErrorMessages();
    Token TK = new Token();
    private boolean erroas = false;
    int estado;
    StringBuilder lexeme;

    static char ESPACE = ' ';
    static char BREAK_LINE = '\n';

    public Lexer(String file) {

        try {
            file_reference = new RandomAccessFile(file, "r");
        } catch (IOException e) {
            EM.openFileError();
        } catch (Exception e) {
            EM.programError();
        }
    }

    public void closeFile() {

        try {
            file_reference.close();
        } catch (IOException e) {
            EM.closeFileError();
        }

        System.out.println("Tabela de simbolos:");
        TS.show();
    }

    public void returnCharPosition() {

        try {

            if (lastChar != EOF) {
                file_reference.seek(file_reference.getFilePointer() - 1);
                column--;

            }
        } catch (IOException e) {
            EM.readFile();
        }
    }

    private static boolean IsASCII(Character c) {
        return c.toString().matches("^[\\x20-\\xFF]*$");
    }

    public Token proxToken() throws IOException {
        lexeme = new StringBuilder();
        estado = 1;
        char c;

        while (true) {
            c = '\u0000';

            try {
                lastChar = file_reference.read();
                if (lastChar != EOF) {
                    c = (char) lastChar;
                    column++;

                    if (c == '\n') {
                        column = 1;
                        line++;
                    } else if (c == '\t') {
                        column += 3;
                    }
                }
            } catch (IOException e) {
                EM.readFile();
            }

            switch (estado) {
                case 1:
                    if (lastChar == EOF)
                    {
                        return AddToken(Type.EOF);
                    }
                    else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                        estado = 1;
                    } else if (c == '=') {
                        estado = 2;
                    } else if (c == '!') {
                        estado = 5;
                    } else if (c == '<') {
                        estado = 7;
                    } else if (c == '>') {
                        estado = 10;
                    } else if (Character.isDigit(c)) {
                        lexeme.append(c);
                        estado = 13;
                    } else if (Character.isLetter(c)) {
                        lexeme.append(c);
                        estado = 15;
                    } else if (c == '*') {
                        estado = 17;
                        return AddToken(Type.OP_MUL);
                    } else if (c == '+') {
                        estado = 18;
                        return AddToken(Type.OP_ADD);
                    } else if (c == '-') {
                        estado = 19;
                        return AddToken(Type.OP_MIN);
                    } else if (c == ',') {
                        estado = 31;
                        return AddToken(Type.SMB_COM, ",");
                    } else if (c == ';') {
                        estado = 30;
                        return AddToken(Type.SMB_SEM, ";");
                    } else if (c == ')') {
                        estado = 29;
                        return AddToken(Type.SMB_CPA, ")");
                    } else if (c == '(') {
                        estado = 22;
                        return AddToken(Type.SMB_OPA, "(");
                    } else if (c == '{') {
                        estado = 20;
                        return AddToken(Type.SMB_OBC, "{");
                    } else if (c == '}') {
                        estado = 21;
                        return AddToken(Type.SMB_CBC, "}");
                    } else if (c == '/') {
                        estado = 23;
                        lexeme.append(c);
                    } else if (c == '\"') {
                        estado = 38;

                    } else if (c == '\'') {
                        estado = 37;
                        lexeme.append(c);
                    } else {
                        EM.lexerError("Simbolo inesperado: " + c + " Linha: " + line + " e coluna " + column);
                        estado = 1;
                    }

                    break;

                case 2:
                    if (c == '=') {
                        estado = 3;
                        return AddToken(Type.OP_EQ, "==");
                    } else {
                        returnCharPosition();
                        return AddToken(Type.OP_ASS, "=");
                    }

                case 5:
                    if (c == '=') {
                        estado = 6;
                        return AddToken(Type.OP_NE, "!=");
                    } else {
                        EM.lexerError("Token incompleto: Esperando != na linha:" + line + " e coluna " + column);
                        estado = 1;
                    }

                case 7:
                    if (c == '=') {
                        estado = 8;
                        return AddToken(Type.OP_LE, "<=");
                    } else {
                        estado = 9;
                        returnCharPosition();
                        return AddToken(Type.OP_LT, "<");
                    }

                case 10:
                    if (c == '=') {
                        estado = 11;
                        return AddToken(Type.OP_GE, ">=");
                    } else {
                        estado = 12;
                        returnCharPosition();
                        return AddToken(Type.OP_GT, ">");
                    }

                case 13:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                        estado = 13;
                    } else if (c == '.') {
                        lexeme.append(c);
                        estado = 32;
                    } else {

                        returnCharPosition();
                        return AddToken(Type.CON_NUM);
                    }
                    break;

                case 32:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                        estado = 14;

                    } else {
                        EM.lexerError("Caractere inválido " + c + " na linha: " + line + " coluna: " + column);

                    }

                    break;
                case 14:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                        estado = 14;
                    } else {
                        returnCharPosition();
                        return AddToken(Type.CON_NUM);
                    }

                    break;
                case 15:
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        lexeme.append(c);
                    } else {
                        estado = 16;
                        returnCharPosition();
                        Token token = TS.returnToken(lexeme.toString());

                        if (token == null) {

                            return AddToken(Type.ID);

                        }

                        return token;
                    }
                    break;

                case 23:
                    if (c == '*') {
                        estado = 25;
                        lexeme.append(c);
                    } else if (c == '/') {
                        estado = 40;
                        lexeme.append(c);
                    } else {
                        returnCharPosition();
                        return AddToken(Type.OP_DIV, "/");
                    }
                    break;

                case 25:
                    if (lastChar == EOF) {
                        EM.lexerError("Comentário multilinha não fechado na linha: " + line);
                        closeFile();
                        System.exit(0);
                    }
                    else if (c == '*') {
                        estado = 27;
                        lexeme.append(c);
                    } else if (IsASCII(c)) {
                        estado = 25;
                        lexeme.append(c);
                    }

                    break;
                case 27:
                    if (lastChar == EOF) {
                        EM.lexerError("Comentário multilinha não fechado na linha: " + line);
                        closeFile();
                        System.exit(0);
                    }
                    else if (c == '/') {
                        estado = 1;
                        lexeme.append(c);
                        return AddToken(Type.SMB_COME);
                    } else if (c == '*') {
                        estado = 27;
                        lexeme.append(c);
                    } else if (IsASCII(c)) {
                        estado = 25;
                        lexeme.append(c);
                    }
                    break;
                case 33:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                    } else {
                        returnCharPosition();
                        return AddToken(Type.CON_NUM);
                    }
                    break;
                case 34:
                    if (c == '\"') {
                        lexeme.append(c);
                        estado = 1;
                        return AddToken(Type.LIT);
                    } else if (c == '\n' && !erroas) {
                        estado = 34;
                        erroas = true;
                        EM.lexerError("Erro lexico, esperando fecha aspas na linha " + line + " e coluna " + column);
                    } else if (IsASCII(c)) {
                        if (!erroas) {
                            estado = 34;
                            lexeme.append(c);
                        } else {
                            EM.lexerError("Caractere inválido " + c + " na linha: " + line + " coluna: " + column);
                        }
                    }
                    break;

                case 37:

                    if (IsASCII(c)) {
                        estado = 39;
                        lexeme.append(c);
                    } else {
                        EM.lexerError("Caractere inválido " + c + " na linha: " + line + " coluna: " + column);
                    }

                    break;

                case 38:

                    if (IsASCII(c)) {
                        lexeme.append(c);
                        estado = 34;
                    }
                    
                    else {
                        EM.lexerError("Falta de caractere  na linha " + line + " e coluna " + column);
                        estado = 1;
                    }

                    break;

                case 39:

                    if (c == '\'') {
                        estado = 1;
                        lexeme.append(c);
                        return AddToken(Type.CON_CHAR);
                    } else {
                        EM.lexerError("Caractere inválido " + c + " na linha: " + line + " coluna: " + column);
                        estado = 1;
                    }
                    break;

                case 40:
                    if (IsASCII(c)) {
                        lexeme.append(c);
                        estado = 40;
                    } else {
                        estado = 1;
                        return AddToken(Type.SMB_LIC);
                    }
                    break;
            }

        }

    }
    
    private Token AddToken(Type type)
    {
        Token currentToken = new Token(type, lexeme.toString(), line, column);
        
        if (type == Type.EOF)
        {
            TS.put(new Token(type, "EOF", line, column), new Identifier());
        }
        else
        {
            TS.put(currentToken, new Identifier());
        }
        
        return currentToken;
    }

    private Token AddToken(Type type, String sequence)
    {
        Token currentToken = new Token(type, sequence, line, column);
        
        if (type == Type.EOF)
        {
            TS.put(new Token(type, "EOF", line, column), new Identifier());
        }
        else
        {
            TS.put(currentToken, new Identifier());
        }
        
        return currentToken;
    }
}
