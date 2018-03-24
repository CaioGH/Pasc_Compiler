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

		case '=': // state 2
		{
                    System.out.println("meee");
		}
                break;

		case '!': // state 5
		{
                    
		}
                break;

		case '<': // state 7
		{
                 //   return new Token(Type.OP_LE, "<", row, column);
		}
                break;
            
		case '>': // state 10
		{
                  //  return new Token(Type.OP_GT, ">", row, column);
		}
                break;

		case '*': // state 17
		{
                  //  return new Token(Type.OP_MULT, "*", row, column);
		}
                break;

		case '+': // state 18
		{
                 //   return new Token(Type.OP_AD, "+", row, column);
		}
                break;

		case '-': // state 19
                {
                  //  return new Token(Type.OP_MIN, "-", row, column);
		}
                break;

		case '/': // state 23
		{
		
		}
		break;
            
                case ',': // state 31
		{
                   // return new Token(Type.SMB_COM, ",", row, column);
		}
		break;
            
                case ';': // state 30
		{
                  //  return new Token(Type.SMB_SEM, ";", row, column);
		}
		break;
            
                case '(': // state 22
		{
                  //  return new Token(Type.SMB_OPA, "(", row, column);
		}
		break;
            
                case ')': // state 29
		{
                  //  return new Token(Type.SMB_CPA, ")", row, column);
		}
		break;
            
                case '{': // state 20
		{
                  //  return new Token(Type.SMB_OBC, "{", row, column);
		}
		break;
            
                case '}': // state 21
		{
                 //   return new Token(Type.SMB_CBC, "}", row, column);
		}
		break;
	}
    }
       
   
}