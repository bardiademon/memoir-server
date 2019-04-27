package bardiademon.Other;

public abstract class JsonConvert
{
    public static String Convert (String json)
    {
        char[] chars = json.toCharArray();

        StringBuilder jsonResult;
        jsonResult = new StringBuilder();
        for (int i = 0, len = chars.length; i < len; i++)
        {
            if (chars[i] == '\\') continue;
            jsonResult.append(chars[i]);
        }
        chars = jsonResult.toString().toCharArray();
        jsonResult = new StringBuilder();
        for (int i = 0, len = chars.length; i < len; i++)
        {
            if (chars[i] == '\"')
            {
                if (chars[i + 1] == '{') continue;
                else if (chars[i - 1] == '}') continue;
            }
            jsonResult.append(chars[i]);
        }
        return jsonResult.toString();
    }
}
