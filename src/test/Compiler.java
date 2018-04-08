package test;

import java.io.IOException;
import static test.Lexer.TS;
import static test.Lexer.column;
import static test.Lexer.line;
 
public class Compiler {
public static void main(String[] args) throws IOException
        {
      
        Lexer lexer = new Lexer ("teste1_correto.jvn");
        // parametro do Lexer: Um programa de acordo com a gramatica


        Token token;
        TS = new TabelaSim ();

        // Enquanto não houver erros ou não for fim de arquivo:
        do
          {
            token = lexer.proxToken ();

            // Imprime token
            if (token != null)
              {

                System.out.println ("Token: " + token.toString () + "\t Linha: " + line + "\t Coluna: " + column);

                // Verificar se existe o lexema na tabela de símbolos
              }

          }
        while (token != null && token.getClassType () != Type.EOF);
        lexer.closeFile ();
}

}
