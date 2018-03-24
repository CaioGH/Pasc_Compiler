
package states;

class state6
    {
        public void takeAChar (char c)
        {
            if (c == '/')
            {
               // return new Token(Type.SMB_LIC, "//", row, column); 
            }
            else if (c == '*')
            {
               // return new Token(Type.SMB_OPC, "/*", row, column);  
               // chamar outro state
            }
            else
            {
                // return new Token(Type.OP_DIV, "/", row, column); 
            }


        }
    }
