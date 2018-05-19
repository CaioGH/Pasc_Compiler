package Compiler;

import java.io.IOException;
import static Compiler.Lexer.TS;
import static Compiler.Lexer.column;
import static Compiler.Lexer.line;
 
public class Compiler {
public static void main(String[] args) throws IOException
        {
      
        Lexer lexer = new Lexer ("teste1_correto.jvn");

        Token token;
        TS = new TabelaSim ();


        do
          {
            token = lexer.proxToken ();

 
            if (token != null)
              {

                System.out.println ("Token: " + token.toString () + "\t Linha: " + line + "\t Coluna: " + column);
                TS.put(token, new Identifier());

              }

          }
        
        while (token != null && token.getClassType () != Type.EOF);
        lexer.closeFile ();
}

}
