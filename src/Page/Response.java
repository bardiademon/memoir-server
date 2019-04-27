package Page;

import bardiademon.Interface.bardiademon;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@bardiademon
public abstract class Response implements HttpServletResponse
{
    private static HttpServletResponse Response;

    private static ServletContext servletContext;

    public static void SetStatus (int Status)
    {
        Response.setStatus (Status);
    }

    public static void SetResponse (HttpServletResponse Response)
    {
        Page.Response.Response = Response;
    }

    @bardiademon
    public static PrintWriter GetWriter ()
    {
        try
        {
            return Response.getWriter ();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    @bardiademon
    public static HttpServletResponse GetResponse ()
    {
        return Response;
    }

    @bardiademon
    public static void SetServletContext (ServletContext servletContext)
    {
        Page.Response.servletContext = servletContext;
    }

    @bardiademon
    public static ServletContext GetServletContext ()
    {
        return servletContext;
    }

    @bardiademon
    public static void SetContentType (String ContentType)
    {
        if (CheckResponse ()) Response.setContentType (ContentType);
    }

    @bardiademon
    private static boolean CheckResponse ()
    {
        return (Response != null);
    }

    @bardiademon
    public static void SetCharacterEncodingUTF8 ()
    {
        if (CheckResponse ()) Response.setCharacterEncoding ("UTF-8");
    }

}
