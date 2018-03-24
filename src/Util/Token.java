package Util;


public class Token
{
	private String lexeme;
	private Type classType;
	private int row, column;

	public Token(Type classType, String lexeme, int row, int column)
	{
		this.classType = classType;
		this.lexeme = lexeme;
		this.row = row;
		this.column = column;
	}

		public String getLexeme()
		{
			return lexeme;
		}

		public void setLexeme (String lexeme)
		{
			this.lexeme = lexeme;
		}

		public Type getClassType()
		{
			return classType;
		}

		public void setClassType (Type classType)
		{
			this.classType = classType;
		}

		public int getRow()
		{
			return row;
		}

		public void setRow (int row)
		{
			this.row = row;
		}

		public int getColumn()
		{
			return column;
		}

		public void setColumn (int column)
		{
			this.column = column;
		}

}