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
    public static int charAux = 0;
    private boolean erroas = false;

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
        System.out.println(TS.toString());

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

    public void nextChar(char c) throws IOException {
        lastChar = file_reference.read();
        if (lastChar != EOF) {
            c = (char) lastChar;
            column++;

            if (c == '\n') {
                column = 0;
                line++;
            } else if (c == '\t') {
                column += 3;
            }
        }
    }

    private static boolean IsASCII(Character c) {
        return c.toString().matches("^[\\x20-\\xFF]*$");
    }

    public Token proxToken() throws IOException {

        StringBuilder lexeme = new StringBuilder();
        int estado = 1;
        char c;

        while (true) {
            c = '\u0000';

            try {
                lastChar = file_reference.read();
                if (lastChar != EOF) {
                    c = (char) lastChar;
                    column++;

                    if (c == '\n') {
                        column = 0;
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
                    if (lastChar == EOF) {
                        return new Token(Type.EOF, "EOF", line, column);
                    } else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
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
                        return new Token(Type.OP_MUL, "*", line, column);
                    } else if (c == '+') {
                        estado = 18;
                        return new Token(Type.OP_ADD, "+", line, column);
                    } else if (c == '-') {
                        estado = 19;
                        return new Token(Type.OP_MIN, "-", line, column);
                    } else if (c == ',') {
                        estado = 31;
                        return new Token(Type.SMB_COM, ",", line, column);
                    } else if (c == ';') {
                        estado = 30;
                        return new Token(Type.SMB_SEM, ";", line, column);
                    } else if (c == ')') {
                        estado = 29;
                        return new Token(Type.SMB_CPA, ")", line, column);
                    } else if (c == '(') {
                        estado = 22;
                        return new Token(Type.SMB_OPA, "(", line, column);
                    } else if (c == '{') {
                        estado = 20;
                        return new Token(Type.SMB_OBC, "{", line, column);
                    } else if (c == '}') {
                        estado = 21;
                        return new Token(Type.SMB_CBC, "}", line, column);
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
                        return new Token(Type.OP_EQ, "==", line, column);
                    } else {
                        returnCharPosition();
                        return new Token(Type.OP_ASS, "=", line, column);
                    }

                case 5:
                    if (c == '=') {
                        estado = 6;
                        return new Token(Type.OP_NE, "!=", line, column);
                    } else {
                        EM.lexerError("Token incompleto: Esperando != na linha:" + line + " e coluna " + column);
                        estado = 1;
                    }

                case 7:
                    if (c == '=') {
                        estado = 8;
                        return new Token(Type.OP_LE, "<=", line, column);
                    } else {
                        estado = 9;
                        returnCharPosition();
                        return new Token(Type.OP_LT, "<", line, column);
                    }

                case 10:
                    if (c == '=') {
                        estado = 11;
                        return new Token(Type.OP_GE, ">=", line, column);
                    } else {
                        estado = 12;
                        returnCharPosition();
                        return new Token(Type.OP_GT, ">", line, column);
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
                        return new Token(Type.CON_NUM, lexeme.toString(), line, column);
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
                        return new Token(Type.CON_NUM, lexeme.toString(), line, column);
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

                            return new Token(Type.ID, lexeme.toString(), line, column);

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
                        return new Token(Type.OP_DIV, "/", line, column);
                    }
                    break;

                case 25:
                    if (c == '*') {
                        estado = 27;
                        lexeme.append(c);
                    } else if (Character.isLetterOrDigit(c) || c == '@' || c == '#' || c == '!' || c == '$' || c == '%' || c == '^' || c == '*' || c == '(' || c == ')' || c == ',' || c == '.' || c == '<' || c == '>' || c == '~' || c == '`' || c == '[' || c == ']' || c == '{' || c == '}' || c == '/' || c == '+' || c == '=' || c == '-') {
                        estado = 25;
                        lexeme.append(c);
                    }

                    break;
                case 27:
                    if (c == '/') {
                        estado = 1;
                        lexeme.append(c);
                        //   return new Token(Type.SMB_COME, lexeme.toString(), line, column);
                    } else if (c == '*') {
                        estado = 27;
                        lexeme.append(c);
                    } else if (Character.isLetterOrDigit(c) || c == '@' || c == '#' || c == '!' || c == '$' || c == '%' || c == '^' || c == '*' || c == '(' || c == ')' || c == ',' || c == '.' || c == '<' || c == '>' || c == '~' || c == '`' || c == '[' || c == ']' || c == '{' || c == '}' || c == '/' || c == '+' || c == '=' || c == '-') {
                        estado = 25;
                        lexeme.append(c);
                    }
                    break;
                case 33:
                    if (Character.isDigit(c)) {
                        lexeme.append(c);
                    } else {
                        returnCharPosition();
                        return new Token(Type.CON_NUM, lexeme.toString(), line, column);
                    }
                    break;
                case 34:
                    if (c == '\"') {
                        lexeme.append(c);
                        estado = 1;
                        return new Token(Type.LIT, lexeme.toString(), line, column);
                    } else if (c == '\n' && !erroas) {
                        estado = 34;
                        erroas = true;
                        EM.lexerError("Erro lexico, esperando fecha aspas na linha " + line + " e coluna " + column);
                    } else if (Character.isLetterOrDigit(c) || c == '@' || c == '#' || c == '!' || c == '$' || c == '%' || c == '^' || c == '*' || c == '(' || c == ')' || c == ',' || c == '.' || c == '<' || c == '>' || c == '~' || c == '`' || c == '[' || c == ']' || c == '{' || c == '}' || c == '/' || c == '+' || c == '=' || c == '-') {
                        if (!erroas) {
                            estado = 34;
                            lexeme.append(c);
                        } else {
                            EM.lexerError("Caractere inválido " + c + " na linha: " + line + " coluna: " + column);
                        }
                    }
                    break;

                case 37:

                    if (Character.isLetterOrDigit(c) || c == '@' || c == '#' || c == '!' || c == '$' || c == '%' || c == '^' || c == '*' || c == '(' || c == ')' || c == ',' || c == '.' || c == '<' || c == '>' || c == '~' || c == '`' || c == '[' || c == ']' || c == '{' || c == '}' || c == '/' || c == '+' || c == '=' || c == '-') {
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
                        return new Token(Type.CON_CHAR, lexeme.toString(), line, column);
                    } else {
                        EM.lexerError("Caractere inválido " + c + " na linha: " + line + " coluna: " + column);
                    }
                    break;

                case 40:
                    if (Character.isLetterOrDigit(c) || c == '@' || c == '#' || c == '!' || c == '$' || c == '%' || c == '^' || c == '*' || c == '(' || c == ')' || c == ',' || c == '.' || c == '<' || c == '>' || c == '~' || c == '`' || c == '[' || c == ']' || c == '{' || c == '}' || c == '/' || c == '+' || c == '=' || c == '-') {
                        lexeme.append(c);
                        estado = 40;
                    } else {
                        estado = 1;
                        //return new Token(Type.SMB_LIC, lexeme.toString(), line, column);
                    }
                    break;
            }

        }

    }
}
