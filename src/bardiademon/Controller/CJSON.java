package bardiademon.Controller;

import bardiademon.Interface.JustController;
import bardiademon.Interface.bardiademon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedHashMap;


@bardiademon
public class CJSON implements JustController
{

    private JSONObject json;
    private boolean ok;
    private String[] key, value;
    private int indexRequestInfoJson;
    private Object dValue;
    private List <Object> defaultValue;
    private Value values;
    private boolean getValueJson;

    public CJSON (JSONObject Json , Value _Value)
    {
        this (Json , _Value , false);
    }

    public CJSON (JSONObject Json , Value _Value , boolean GetValueJson)
    {
        this.json = Json;
        this.values = _Value;
        this.getValueJson = GetValueJson;
        RunClass ();
    }


    @Override
    public void RunClass ()
    {
        ok = Checking ();
        if (getValueJson && (json != null && json.length () > 0)) ok = (ok && getValue ());
    }

    @Override
    public boolean Checking ()
    {
        Integer lenJson = json.length ();
        boolean found = false;
        for (int i = 0, len = values.len.size (); i < len; i++)
        {
            if (lenJson.equals (values.len.get (i)))
            {
                found = true;
                indexRequestInfoJson = i;
                break;
            }
        }
        if (found)
        {
            boolean lenZero = values.len.get (indexRequestInfoJson).equals (0);
            if (lenZero) return (lenJson.equals (0));

            if (values.defaultValue != null && values.defaultValue.size () > 0)
                defaultValue = values.defaultValue.get (indexRequestInfoJson);

            try
            {
                Map.Entry <String[], String[]> next = (values.keyValue.get (indexRequestInfoJson)).entrySet ().iterator ().next ();
                key = next.getKey ();
                value = next.getValue ();
                return (IsHas () && CheckingValue ());
            }
            catch (NullPointerException ignored)
            {
            }
        }
        return false;
    }

    @Override
    public boolean IsHas ()
    {
        for (String k : key) if (!json.has (k)) return false;
        return true;
    }

    @Override
    public boolean CheckingValue ()
    {
        try
        {
            for (int i = 0; i < value.length; i++)
            {
                String val = value[i];
                switch (val)
                {
                    case VD.IS_INT:
                    {
                        if (getJson (i) instanceof Integer)
                        {
                            if (checkValueDefaultValue (i) && dValue instanceof Integer)
                                if (!getJson (i).equals (dValue)) return false;
                        }
                        else return false;
                        break;
                    }
                    case VD.IS_STRING__EMPTY:
                    case VD.IS_STRING:
                    {
                        if (getJson (i) instanceof String)
                        {
                            if (checkValueDefaultValue (i) && dValue instanceof String)
                                if (!getJson (i).equals (dValue)) return false;
                        }
                        else return false;
                        if (val.equals (VD.IS_STRING) && getJson (i).equals ("")) return false;
                        break;
                    }
                    case VD.IS_BOOLEAN:
                    {
                        if (getJson (i) instanceof Boolean)
                        {
                            if (checkValueDefaultValue (i) && dValue instanceof Boolean)
                                if (!getJson (i).equals (dValue)) return false;
                        }
                        else return false;
                        break;
                    }
                    case VD.IS_DOUBLE:
                    {
                        if (getJson (i) instanceof Double)
                        {
                            if (checkValueDefaultValue (i) && dValue instanceof Double)
                                if (!getJson (i).equals (dValue)) return false;
                        }
                        else return false;
                        break;
                    }
                    case VD.IS_FLOAT:
                    {
                        if (getJson (i) instanceof Float)
                        {
                            if (checkValueDefaultValue (i) && dValue instanceof Float)
                                if (!getJson (i).equals (dValue)) return false;
                        }
                        else return false;
                        break;
                    }
                    case VD.IS_LONG:
                    {
                        if (getJson (i) instanceof Long)
                        {
                            if (checkValueDefaultValue (i) && dValue instanceof Long)
                                if (!getJson (i).equals (dValue)) return false;
                        }
                        else return false;
                        break;
                    }
                    case VD.IS_SHORT:
                    {
                        if (getJson (i) instanceof Short)
                        {
                            if (checkValueDefaultValue (i) && dValue instanceof Short)
                                if (!getJson (i).equals (dValue)) return false;
                        }
                        else return false;
                        break;
                    }
                    case VD.IS_OBJECT:
                    {
                        if (getJson (i) != null)
                        {
                            if (checkValueDefaultValue (i))
                                if (!getJson (i).equals (defaultValue)) return false;
                        }
                        else return false;
                        break;
                    }
                }
            }
        }
        catch (JSONException ignored)
        {
        }
        return true;
    }

    private boolean checkValueDefaultValue (int index)
    {
        return (defaultValue != null && (dValue = defaultValue.get (index)) != null);
    }

    private Object getJson (int index) throws JSONException
    {
        return json.get (key[index]);
    }

    public boolean isOk ()
    {
        return ok;
    }

    public static class Value
    {
        private static final int INDEX_KEY = 0, INDEX_VALUE = 1;

        private List <Integer> len;
        private Map <Integer, List <Object>> defaultValue;
        private Map <Integer, Map <String[], String[]>> keyValue;

        public void putLen (Integer... len)
        {
            this.len = new ArrayList <> ();
            Collections.addAll (this.len , len);
        }

        public void putKeyValue (Object[]... keyValue)
        {
            Map <String[], String[]> tempKeyValue;
            this.keyValue = new LinkedHashMap <> ();
            int counter = 0;
            for (Object[] KV : keyValue)
            {
                if (KV != null && KV[INDEX_KEY] instanceof String[] && KV[INDEX_VALUE] instanceof String[])
                {
                    tempKeyValue = new LinkedHashMap <> ();
                    tempKeyValue.put ((String[]) KV[INDEX_KEY] , (String[]) KV[INDEX_VALUE]);
                    this.keyValue.put (counter++ , tempKeyValue);
                }
            }
        }

        @SafeVarargs
        public final void putDefaultValue (List <Object>... DefaultValue)
        {
            this.defaultValue = new LinkedHashMap <> ();
            List <Object> tempDefaultValue = new ArrayList <> ();
            int counter = 0;
            for (List arrayList : DefaultValue)
            {
                if (arrayList == null) arrayList = new ArrayList ();
                tempDefaultValue.addAll (arrayList);
                this.defaultValue.put (counter++ , tempDefaultValue);
            }
        }
    }

    private Map <String, Object> valueJson;

    private boolean getValue ()
    {
        try
        {
            valueJson = new LinkedHashMap <> ();
            for (String key : this.key) valueJson.put (key , json.get (key));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Map <String, Object> getValueJson ()
    {
        return valueJson;
    }

    public Object getVObject (String key)
    {
        try
        {
            return valueJson.get (key);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // V => Value
    public String getVString (String key)
    {
        try
        {
            return (String) valueJson.get (key);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // V => Value
    public Integer getVInteger (String key)
    {
        try
        {
            return (Integer) valueJson.get (key);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // V => Value
    public Boolean getVBoolean (String key)
    {
        try
        {
            return (Boolean) valueJson.get (key);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // V => Value
    public Long getVLong (String key)
    {
        try
        {
            return (long) valueJson.get (key);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // V => Value
    public Short getVShort (String key)
    {
        try
        {
            return (short) valueJson.get (key);
        }
        catch (Exception e)
        {
            return null;
        }
    }


    // VD => Value Default
    public interface VD
    {
        String
            IS_OBJECT = "obj",
            IS_INT = "int",
            IS_STRING = "str",
            IS_BOOLEAN = "bool",
            IS_DOUBLE = "db",
            IS_FLOAT = "flt",
            IS_SHORT = "shrt",
            IS_LONG = "lng",
            IS_STRING__EMPTY = "strempt";
    }
}
