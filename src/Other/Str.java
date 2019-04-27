package Other;

import bardiademon.Interface.bardiademon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@bardiademon
public abstract class Str
{
    @bardiademon
    public static Object[] ToArray (Object... obj)
    {
        return obj;
    }

    @bardiademon
    public static String[] ToStrArray (String... str)
    {
        return str;
    }

    @bardiademon
    public static List<Object> ToList (Object... obj)
    {
        List<Object> list = new ArrayList<>();
        Collections.addAll(list , obj);
        return list;
    }

    @bardiademon
    public static List<String> ToStrList (String... obj)
    {
        List<String> list = new ArrayList<>();
        Collections.addAll(list , obj);
        return list;
    }

    public static String CheckNull (String Value)
    {
        return CheckNull(Value , null);
    }

    public static String CheckNull (String Value , Object SetIfNull)
    {
        if (Value == null)
        {
            if (SetIfNull == null) return "null";
            else return String.valueOf(SetIfNull);
        }
        else return Value;
    }
}
