
package states;

class state6
    {
    
    static state7 s7 = new state7 ();
    
        public void takeAChar (char c)
        {
            if (c == '/')
            {
               // return new Token(Type.SMB_LIC, "//", row, column); 
            }
            else if (c == '*')
            {
               // return new Token(Type.SMB_OPC, "/*", row, column);  
              s7.takeAChar (c);
            }
            else
            {
                // return new Token(Type.OP_DIV, "/", row, column); 
            }


        }
    }
