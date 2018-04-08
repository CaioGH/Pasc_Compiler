package test;

import java.io.*;

public class Lexer
    {

    private static final int EOF = -1; // contante para fim do arquivo
    private static int lastChar = 0; // armazena o último caractere lido do arquivo	
    public static int line = 1; // contador de linhas
    public static int column = 0; // contador de linhas
    public static TabelaSim TS; // tabela de simbolos
    RandomAccessFile file_reference; // referencia para o arquivo
    ErrorMessages EM = new ErrorMessages ();
    Token TK = new Token ();

    // Variáveis alto explicativas para facilitar o controle.
    static char ESPACE = ' ';
    static char BREAK_LINE = '\n';

    public Lexer(String file)
        {

        try
          {
            file_reference = new RandomAccessFile (file, "r");
          }
        catch (IOException e)
          {
            EM.openFileError ();
          }
        catch (Exception e)
          {
            EM.programError ();
          }
        }

    // Fecha instance_file de input_data
    public void closeFile()
        {

        try
          {
            file_reference.close ();
          }
        catch (IOException e)
          {
            EM.closeFileError ();
          }

        System.out.println ("Tabela de simbolos:");
        System.out.println (TS.toString ());

        }

    //Volta uma posição do buffer de leitura
    public void returnCharPosition()
        {

        try
          {
            // Não é necessário retornar o ponteiro em caso de Fim de Arquivo
            if (lastChar != EOF)
              {
                file_reference.seek (file_reference.getFilePointer () - 1);
                column--;
                
              }
          }
        catch (IOException e)
          {
            EM.readFile ();
          }
        }

    public void nextChar(char c) throws IOException
        {
        lastChar = file_reference.read ();
        if (lastChar != EOF)
                  {
                    c = (char) lastChar;
                    column ++;

                  if (c == '\n')
                      {
                        column = 0;
                        line ++;
                      }

                    else if (c == '\t')
                      {
                        column += 3;
                      }
                  }
        }

    public Token proxToken() throws IOException
        {

        StringBuilder lexeme = new StringBuilder ();
        int estado = 1;
        char c;

        while (true)
          {
            c = '\u0000';

            try
              {
                lastChar = file_reference.read ();
                if (lastChar != EOF)
                  {
                    c = (char) lastChar;
                    column ++;

                  if (c == '\n')
                      {
                        column = 0;
                        line ++;
                      }

                    else if (c == '\t')
                      {
                        column += 3;
                      }
                  }
              }
            catch (IOException e)
              {
                EM.readFile ();
              }

            // movimentacao do automato
            switch (estado)
              {
                case 1:
                    if (lastChar == EOF)
                      {
                        return new Token (Type.EOF, "EOF", line, column);
                      }
                    else if (c == ' ' || c == '\t' || c == '\n' || c == '\r')
                      {
                        estado = 1;
                      }

                    else if (c == '=')
                      {
                        estado = 2;
                      }

                    else if (c == '!')
                      {
                        estado = 5;
                      }
                    else if (c == '<')
                      {
                        estado = 7;
                      }
                    else if (c == '>')
                      {
                        estado = 10;
                      }
                    else if (Character.isDigit (c))
                      {
                        lexeme.append (c);
                        estado = 13;
                      }
                    else if (Character.isLetter (c))
                      {
                        lexeme.append (c);
                        estado = 15;
                      }

                    else if (c == '*')
                      {
                        estado = 17;
                        return new Token (Type.OP_MUL, "*", line, column);
                      }
                    else if (c == '+')
                      {
                        estado = 18;
                        return new Token (Type.OP_ADD, "+", line, column);
                      }
                    else if (c == '-')
                      {
                        estado = 19;
                        return new Token (Type.OP_MIN, "-", line, column);
                      }
                    else if (c == ',')
                      {
                        estado = 31;
                        return new Token (Type.SMB_COM, ",", line, column);
                      }
                    else if (c == ';')
                      {
                        estado = 30;
                        return new Token (Type.SMB_SEM, ";", line, column);
                      }
                    else if (c == ')')
                      {
                        estado = 29;
                        return new Token (Type.SMB_CPA, ")", line, column);
                      }
                    else if (c == '(')
                      {
                        estado = 22;
                        return new Token (Type.SMB_OPA, "(", line, column);
                      }
                    else if (c == '{')
                      {
                        estado = 20;
                        return new Token (Type.SMB_OBC, "{", line, column);
                      }
                    else if (c == '}')
                      {
                        estado = 21;
                        return new Token (Type.SMB_CBC, "}", line, column);
                      }
                    else if (c == '/')
                      {
                        estado = 23;
                      }
                        else if (c == '\"')
                      {
                      
                       returnCharPosition();
                       estado = 38;
                        
                        
                      }
                    else if (c == '\'')
                      {
                        lexeme.append (c);
                        estado = 37;
                      }
                    break;

                case 2:
                    if (c == '=')
                      {
                        estado = 3;
                        return new Token (Type.OP_EQ, "==", line, column);
                      }
                    else
                      {
                        returnCharPosition ();
                        return new Token (Type.OP_ASS, "=", line, column);
                      }

                case 5:
                    if (c == '=')
                      {
                        estado = 6;
                        return new Token (Type.OP_NE, "!=", line, column);
                      }
                    else
                      {
                        returnCharPosition ();
                        EM.lexerError ("Token incompleto: Esperando != na linha:" + line + " e coluna " + column);
                        return null;
                      }

                case 7:
                    if (c == '=')
                      {
                        estado = 8;
                        return new Token (Type.OP_LE, "<=", line, column);
                      }
                    else
                      {
                        estado = 9;
                        returnCharPosition ();
                        return new Token (Type.OP_LT, "<", line, column);
                      }

                case 10:
                    if (c == '=')
                      {
                        estado = 11;
                        return new Token (Type.OP_GE, ">=", line, column);
                      }
                    else
                      {
                        estado = 12;
                        returnCharPosition ();
                        return new Token (Type.OP_GT, ">", line, column);
                      }

                case 13:
                    if (Character.isDigit (c))
                      {
                        lexeme.append (c);

                      }

                    else if (Character.isLetter (c))
                      {

                        EM.lexerError ("Caractere inválido " + c + " na linha: " + line + " coluna: " + column);
                      }

                    else if (c == '.')
                      {
                        lexeme.append (c);
                        estado = 13;
                      }

                    else
                      {
                        estado = 13;
                        returnCharPosition ();
                        return new Token (Type.CON_NUM, lexeme.toString (), line, column);
                      }
                    break;

                case 15:
                    if (Character.isLetterOrDigit (c) || c == '_')
                      {
                        lexeme.append (c);
                      }
                    else
                      {
                        estado = 16;
                        returnCharPosition ();
                        Token token = TS.returnToken (lexeme.toString ());

                        if (token == null)
                          {
                            return new Token (Type.ID, lexeme.toString (), line, column);
                          }
                        return token;
                      }
                    break;

                case 32:
                    if (Character.isDigit (c))
                      {
                        lexeme.append (c);
                        estado = 33;
                      }
                    else
                      {
                        EM.lexerError ("Padrao para double invalido na linha " + line + " coluna " + column);

                      }
                    break;

                case 23:
                    if (c == '/')
                      {
                        estado = 24;
                        return new Token (Type.SMB_LIC, "//", line, column);
                      }
                    else if (c == '*')
                      {
                        estado = 25;
                        return new Token (Type.SMB_OPC, "/*", line, column);
                      }
                    else
                      {
                        return new Token (Type.OP_DIV, "/", line, column);
                      }

                case 25:
                    if (Character.isLetterOrDigit (c) || c == '_')
                      {
                        lexeme.append (c);
                        // Permanece no estado 25
                      }
                    else if (c == '*')
                      {
                        estado = 27;
                      }
                    break;
                case 27:
                    if (c == '/')
                      {
                        estado = 28;
                        return new Token (Type.SMB_CSC, "*/", line, column);
                      }
                    else
                      {
                        estado = 25;
                      }
                    break;
                case 33:
                    if (Character.isDigit (c))
                      {
                        lexeme.append (c);
                      }
                    else
                      {
                        returnCharPosition ();
                        return new Token (Type.CON_NUM, lexeme.toString (), line, column);
                      }
                    break;
                case 34:

                    nextChar (c);  
                 
                    if (Character.isLetterOrDigit (c))
                      {

                        returnCharPosition ();
                        lexeme.append (c);
                      
                        if (lastChar == ESPACE)
                       {
                          nextChar (c);
                          nextChar(c);
                          returnCharPosition();        
                       }
                       
                        else if(lastChar == BREAK_LINE)
                          {
                              System.out.println ("Erro");
                          }
                        
                      }                      
                      

                    else if (c == '\"')
                      {

                        lexeme.append (c);
                        returnCharPosition ();
                        return new Token (Type.LIT, lexeme.toString (), line, column);
                      }
                    else
                      {
                        EM.lexerError ("Caractere inesperado " + c + " na linha " + line + " e coluna " + column);
                      }
              
                    break;



                case 37:

                    nextChar (c);
                    lexeme.append (c);
                   

                    if (Character.isLetterOrDigit (c))
                      {
                      returnCharPosition ();
                      }

                    else if (c == '\'')
                      {
                       returnCharPosition ();
                        return new Token (Type.CON_CHAR, lexeme.toString (), line, column);
                        
                       
                      }
               
                    else
                      {
                     lexeme.append (c);
                        EM.lexerError ("Caractere inesperado " + c + " na linha " + line + " e coluna " + column);
                        return new Token (Type.CON_CHAR, lexeme.toString (), line, column);
                      }
                    
                    break;
                    
                case 38:
                    
                    if(c == '\"')
                      {
                        
                          lexeme.append (c);
                          estado = 34;
                      }
                     
                     
                    if(c != '\"')
                    {
                        lexeme.append (c);
                        estado = 34;
                    }
                    
              }

          }

        }
    }
