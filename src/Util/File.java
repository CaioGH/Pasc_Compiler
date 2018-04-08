package Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import states.state1;

public class File
    {
    static  int rowCount = 0, columnCount = 0, i = 0;
    
    public static void read(String path) throws IOException
        {
        FileInputStream input = new FileInputStream(path);
        InputStreamReader inputFormat = new InputStreamReader(input);
        BufferedReader br = new BufferedReader (new FileReader (path));

   
        state1 s1 = new state1();
               
        int c = inputFormat.read(); 
        String line = br.readLine();
	   
        while(c!=-1)
                  
            {	    	   
                System.out.print ((char)c);
                s1.takeAChar ((char) c);
                c = inputFormat.read();
                columnCount++;
            }
        
            System.out.println ("\n colunas" + columnCount);         
               
                        
    
   }
}