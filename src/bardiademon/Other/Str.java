package bardiademon.Other;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class Str
{

    public static String EnCoder (String Value)
    {
        try
        {
            return (URLEncoder.encode (Value , "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            return Value;
        }
    }

    public static String DeCoder (String Value)
    {
        try
        {
            return (URLDecoder.decode (Value , "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            return Value;
        }
    }
}
