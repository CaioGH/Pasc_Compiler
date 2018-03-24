
package states;

public class state7 {
    
        static state8 s8 = new state8 ();
    
    public void takeAChar (char c)
        {
            if (c == '*')
            {
               s8.takeAChar (c);
            }
            else
            {
               // nada 
            }

        }
    
}
