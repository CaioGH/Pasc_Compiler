package states;

import Util.Token;

public class state1 
{
    static state2 s2 = new state2();
    static state3 s3 = new state3();
    static state4 s4 = new state4();
    static state5 s5 = new state5();
    static state6 s6 = new state6();


    public void takeAChar (char c)
   {
       Token Token;


        switch(c) 
        {
            case '$':
                    {
                        //end of file
                    }
            break;

                    case ' ':
                    {
                        // cont de coluna
            }
                    break;

                    case '\t':
                    {
                        // 3 cont coluna
                    }
                    break;

                    case '\n':
                    {
                        // cont linha
                    }
                    break;

                    case '\r':
                    {
                        // sei la
                    }
                    break;

            case '=': 
            {
                     s2.takeAChar (c);
            }
                    break;

            case '!':
            {
                        s3.takeAChar (c);
            }
                    break;

            case '<': 
            {
                        s4.takeAChar (c);

            }
                    break;

            case '>': 
            {
                        s5.takeAChar (c);

            }
                    break;

            case '*': 
            {

                      //  return new Token(Type.OP_MULT, "*", row, column);
            }
                    break;

            case '+': 
            {
                     //   return new Token(Type.OP_AD, "+", row, column);
            }
                    break;

            case '-': 
                    {
                      //  return new Token(Type.OP_MIN, "-", row, column);
            }
                    break;

            case '/': 
            {
             s6.takeAChar (c);
            }
            break;

                    case ',': 
            {
                       // return new Token(Type.SMB_COM, ",", row, column);
            }
            break;

                    case ';':
            {
                      //  return new Token(Type.SMB_SEM, ";", row, column);
            }
            break;

                    case '(': 
            {
                      //  return new Token(Type.SMB_OPA, "(", row, column);
            }
            break;

                    case ')':
            {
                      //  return new Token(Type.SMB_CPA, ")", row, column);
            }
            break;

                    case '{':
            {
                      //  return new Token(Type.SMB_OBC, "{", row, column);
            }
            break;

                    case '}':
            {
                     //   return new Token(Type.SMB_CBC, "}", row, column);
            }
            break;
        }
    }
       
   
}