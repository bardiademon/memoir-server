package View;

import Other.MakeJson;
import Page.Response;
import bardiademon.Interface.bardiademon;
import org.json.JSONException;

import java.io.PrintWriter;

public class ViewResult
{
    private static final String KEY_JSON_RES_SHOW_RES_VIEW = "Result";

    @bardiademon
    public ViewResult (Object Result)
    {
        this (Result , true);
    }

    @bardiademon
    public ViewResult (Object Result , boolean UTF8)
    {
        if (UTF8) Response.SetCharacterEncodingUTF8 ();
        try (PrintWriter writer = Response.GetWriter ())
        {
            MakeJson makeJson = new MakeJson ();
            makeJson.put (KEY_JSON_RES_SHOW_RES_VIEW , Result);
            assert writer != null;
            writer.print (makeJson.getJsonString ());
            Response.SetContentType ("application/json");
        }
        catch (JSONException | NullPointerException ignored)
        {
        }
    }
}
