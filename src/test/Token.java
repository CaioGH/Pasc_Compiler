package test;


public class Token {

    private String lexeme;
    private Type classType;
    private int line, column;

    public Token(Type classType, String lexeme, int line, int column) {
        this.classType = classType;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    Token(){}

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Type getClassType() {
        return classType;
    }

    public void setClassType(Type classType) {
        this.classType = classType;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    
    @Override
    public String toString() {
        return "<" + classType + ", \"" + lexeme + "\">";
    }
    
 
         public int resetColumn(int column)
            {
                return column = 1;
            }     
        
        public int columnAdd(int column)
            {
                return column++;
            }
        
        public int columnAddTabMode(int column)
            {
                return column += 3;
            }
        
        public int columnReturn(int column)
            {
                return column--;
            }
       
        public int lineAdd(int line)
            {
                return line++;
            }

}
