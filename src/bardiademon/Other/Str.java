package bardiademon.Other;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class Str
{

    public static String EnCoder (String Value)
    {
        return (URLEncoder.encode (Value , StandardCharsets.UTF_8));
    }

    public static String DeCoder (String Value)
    {
        return (URLDecoder.decode (Value , StandardCharsets.UTF_8));
    }
}
