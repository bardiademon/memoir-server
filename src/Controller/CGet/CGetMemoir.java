package Controller.CGet;

import org.json.JSONException;
import org.json.JSONObject;

import Interface.Controller;
import Interface.ResultModel;
import Model.Get.MGetMemoir;
import Model.IS.IsLinkMemoir;
import Model.IS.IsMOOC;
import Other.Str;
import Controller.Request;
import Controller.CController;
import Page.Response;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

@bardiademon
public class CGetMemoir implements Controller
{

    private MGetMemoir mGetMemoir;

    private JSONObject jsonRequest;

    @bardiademon
    public CGetMemoir (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ()))
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
    }

    @bardiademon
    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (Request.RequestUser.KJR.KJR_GetMemoir.LEN);
        value.putKeyValue (Str.ToArray (Str.ToStrArray (Request.RequestUser.KJR.KJR_GetMemoir.KJR_LINK) , Str.ToStrArray (Request.RequestUser.KJR.KJR_GetMemoir.Type.T_LINK)));
        return value;
    }

    @bardiademon
    private String getLink ()
    {
        try
        {
            return jsonRequest.getString (Request.RequestUser.KJR.KJR_GetMemoir.KJR_LINK);
        }
        catch (JSONException e)
        {
            Log.NL (Response.SC_INTERNAL_SERVER_ERROR , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , e.getMessage ());
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Response.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        String link = getLink ();
        IsLinkMemoir isLinkMemoir = new IsLinkMemoir (link);
        if (isLinkMemoir.isFound ())
        {
            IsMOOC isMOOC = new IsMOOC (isLinkMemoir.getIdMemoir ());
            if (!isMOOC.isConfirmation ())
            {
                Controller.SetResult (ResultModel.GetMemoir.SC200.NOT_CONFIRMATION , Response.SC_OK);
                Log.NL (Response.SC_OK , ResultModel.GetMemoir.SC200.NOT_CONFIRMATION , Thread.currentThread ().getStackTrace () , "");
            }
            else if (!isMOOC.isOpen ())
            {
                Controller.SetResult (ResultModel.GetMemoir.SC200.NOT_OPEN , Response.SC_OK);
                Log.NL (Response.SC_OK , ResultModel.GetMemoir.SC200.NOT_OPEN , Thread.currentThread ().getStackTrace () , "");
            }
            else
            {
                mGetMemoir = new MGetMemoir (isLinkMemoir.getIdMemoir ());
                GetResultFromModel ();
            }
        }
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (mGetMemoir.IsThereAResult ())
        {
            Log.NL (Response.SC_OK , mGetMemoir.Result () , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (mGetMemoir.Result () , Response.SC_OK);
        }
    }
}
