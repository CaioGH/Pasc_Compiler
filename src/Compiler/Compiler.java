package Compiler;

import java.io.IOException;
import static Compiler.Lexer.TS;
import static Compiler.Lexer.column;
import static Compiler.Lexer.line;
 
public class Compiler {
public static void main(String[] args) throws IOException
        {
      
        Lexer lexer = new Lexer ("Teste.jvn");
       
        Token token;
        TS = new TabelaSim ();
        
        Parser parser = new Parser(lexer);
        parser.Prog();
     
        TS.show();
}

}
