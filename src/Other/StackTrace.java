package Other;

public abstract class StackTrace
{
    public static int LineNumber (StackTraceElement[] StackTrace)
    {
        return StackTrace[1].getLineNumber ();
    }

    public static String NameClass (StackTraceElement[] StackTrace)
    {
        return StackTrace[1].getClassName ();
    }

    public static String FileName (StackTraceElement[] StackTrace)
    {
        return StackTrace[1].getFileName ();
    }

    public static String ToString (StackTraceElement[] StackTrace)
    {
        return StackTrace[1].toString ();
    }

}
