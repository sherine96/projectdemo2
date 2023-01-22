package exceptions;

public class ReplacementException extends Exception{
    public ReplacementException(){
        super("Student courses after removing will be 0");
    }
}
