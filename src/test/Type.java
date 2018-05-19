package test;


public enum Type
{
    //KeyWord

	KW,
        KW_PROG,
        KW_IF,
        KW_ELSE,
        KW_WHILE,
        KW_WRITE,
        KW_READ,
        KW_NUM,
        KW_CHAR,
        KW_NOT,
        KW_OR,
        KW_AND,

	//Idenficador

	ID,

	//Literal

	LIT,

	//Constantes

	CON_NUM,
	CON_CHAR,

	//Operadores

	OP_MUL, // *
	OP_DIV, // /
	OP_ADD,	// +
	OP_MIN,	// -
	OP_EQ,  // ==
	OP_GT,  // >
	OP_GE,  // >= 
	OP_LT,  // <
	OP_LE,  // <=
	OP_NE,  // !=
	OP_ASS, // =

	//Símbolos

	SMB_OBC, // { 
	SMB_CBC, // } 
	SMB_COM, // ,
	SMB_SEM, // ;
	SMB_OPA, // (
	SMB_CPA, // )


        // Não tem retorno, apenas para conferência.
        
	SMB_OPC, // /*
 	SMB_CSC, // */
 	SMB_LIC, // //
        SMB_COME, // Exibe o comentario de varias linhas
        
        EOF

}
