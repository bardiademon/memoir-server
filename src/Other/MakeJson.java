package Other;


import bardiademon.Interface.bardiademon;
import bardiademon.Other.Str;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@bardiademon
public class MakeJson
{
    private boolean defaultEncode = false;

    private static final int INDEX_KEY = 0, INDEX_VALUE = 1, INDEX_IS_ENCODE = 2;

    private List <Object[]> info;

    @bardiademon
    public void put (String key , Object value)
    {
        put (key , value , defaultEncode);
    }

    @bardiademon
    public void putUtf8 (String key , Object value)
    {
        put (key , value , true);
    }

    @bardiademon
    public void put (String key , Object value , boolean utf8)
    {
        put (toInfo (key , value , utf8));
    }

    @bardiademon
    public void put (Object[]... info)
    {
        if (this.info == null) this.info = new ArrayList <> ();
        this.info.addAll (Arrays.asList (info));
    }

    @bardiademon
    public Object[] toInfo (String key , Object value)
    {
        return toInfo (key , value , defaultEncode);
    }

    @bardiademon
    public Object[] toInfo (String key , Object value , boolean utf8)
    {
        return new Object[]{key , value , utf8};
    }

    @bardiademon
    public void setDefaultEncode (boolean defaultEncode)
    {
        this.defaultEncode = defaultEncode;
    }

    @bardiademon
    public boolean isDefaultEncode ()
    {
        return defaultEncode;
    }

    @bardiademon
    public JSONObject apply () throws JSONException
    {
        if (info == null) return null;
        String key;
        Object value;
        boolean isEncode;

        JSONObject resultJson = new JSONObject ();
        for (Object[] inf : info)
        {
            key = (String) inf[INDEX_KEY];
            value = inf[INDEX_VALUE];
            isEncode = (Boolean) inf[INDEX_IS_ENCODE];
            if (value instanceof Integer) resultJson.put (key , value);
            else if (value instanceof String)
                resultJson.put (key , ((isEncode) ? Str.EnCoder ((String) value) : value));
            else if (value instanceof Boolean) resultJson.put (key , value);
            else if (value instanceof Double) resultJson.put (key , value);
            else if (value instanceof Float) resultJson.put (key , value);
            else if (value instanceof Long) resultJson.put (key , value);
            else if (value instanceof Short) resultJson.put (key , value);
            else resultJson.put (key , value);
        }
        return resultJson;
    }

    @bardiademon
    public String getJsonString () throws JSONException
    {
        if (info == null) return null;
        else return apply ().toString ();
    }
}
