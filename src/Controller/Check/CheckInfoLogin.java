package Controller.Check;

import Interface.Controller;
import Interface.KJRequest;
import Interface.ResultModel;
import Other.Str;
import Controller.CController;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.IsController;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

@bardiademon
@IsController
public class CheckInfoLogin implements Controller
{

    private JSONObject jsonRequest;
    private Model.Check.CheckInfoLogin checkInfoLogin;

    @bardiademon
    public CheckInfoLogin (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue () , false))
        {
            this.jsonRequest = CController.User.GetJsonRequest ();
            RunClass ();
        }
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        SendToModel ();
        GetResultFromModel ();
    }

    @bardiademon
    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (3);
        value.putKeyValue (Str.ToArray (Str.ToStrArray ("username" , "password" , "serial") , Str.ToStrArray (CJSON.VD.IS_STRING , CJSON.VD.IS_STRING , CJSON.VD.IS_STRING)));
        return value;
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        try
        {
            String username = jsonRequest.getString (KJRequest.CheckInfoLogin.KJR__USERNAME);
            String password = jsonRequest.getString (KJRequest.CheckInfoLogin.KJR__PASSWORD);
            String serial = jsonRequest.getString (KJRequest.CheckInfoLogin.KJR__SERIAL);
            checkInfoLogin = new Model.Check.CheckInfoLogin (username , password , serial);
        }
        catch (JSONException e)
        {
            Log.NL (HttpServletResponse.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , e.getMessage ());
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (checkInfoLogin.IsThereAResult ())
        {
            Log.NL (HttpServletResponse.SC_OK , checkInfoLogin.Result () , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (checkInfoLogin.Result () , HttpServletResponse.SC_OK);
        }
    }
}
