package test;

public class ErrorMessages
    {

    public void openFileError()
        {
        System.out.println ("Erro de abertura do arquivo." + "\n");
        System.exit (1);
        }

    public void programError()
        {
        System.out.println ("Erro do programa ou falha da tabela de simbolos." + "\n");
        System.exit (2);
        }

    public void closeFileError()
        {
            System.out.println("Erro do ao fechar programa." + "\n");
            System.exit(3);
        }
    
    public void readFile()
        {
            System.out.println("Erro na leitura do arquivo");
            System.exit(4);
        }
    
    public void lexerError(String message)
        {    
            System.out.println("[Erro Léxico]: " + message + "\n" );
        }
    
    }
