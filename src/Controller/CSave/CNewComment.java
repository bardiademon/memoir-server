package Controller.CSave;


import javax.servlet.http.HttpServletResponse;

import Controller.Request.RequestUser.KJR.KJR_NewComment;
import Interface.Controller;
import Controller.CController;
import Interface.ResultModel;
import Model.IS.IsLinkMemoir;
import Model.MSave.MNewComment;
import Other.Str;
import Controller.Request;
import Page.Response;
import bardiademon.Controller.CJSON;
import bardiademon.Other.Log;

public class CNewComment implements Controller
{

    private String linkMemoir, txtComment;
    private int idUserSendComment, idMemoir;

    private MNewComment mNewComment;

    public CNewComment (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ()))
        {
            CJSON cjson = CController.User.GetCJson ();
            linkMemoir = cjson.getVString (KJR_NewComment.LINK_MEMOIR);
            txtComment = cjson.getVString (KJR_NewComment.KJR_TEXT);
            idUserSendComment = Request.RequestUser.GetId ();
            System.gc ();
            RunClass ();
        }
    }

    @Override
    public void RunClass ()
    {
        IsLinkMemoir isLinkMemoir = new IsLinkMemoir (linkMemoir);
        if (isLinkMemoir.isFound ())
        {
            idMemoir = isLinkMemoir.getIdMemoir ();
            SendToModel ();
            GetResultFromModel ();
        }
        else
        {
            Response.SetStatus (Response.SC_BAD_REQUEST);
            Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.NULL , Thread.currentThread ().getStackTrace () , "link memoir not found " + "=>" + linkMemoir);
        }
    }

    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (KJR_NewComment.LEN);
        value.putKeyValue (Str.ToArray (
                Str.ToStrArray (KJR_NewComment.LINK_MEMOIR , KJR_NewComment.KJR_TEXT) ,
                Str.ToStrArray (CJSON.VD.IS_STRING , CJSON.VD.IS_STRING))
        );
        return value;
    }

    @Override
    public void SendToModel ()
    {
        mNewComment = new MNewComment (idUserSendComment , idMemoir , txtComment);
    }

    @Override
    public void GetResultFromModel ()
    {
        if (mNewComment.IsThereAResult ())
        {
            Log.NL (HttpServletResponse.SC_OK , mNewComment.Result () , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT);
            Controller.SetResult (mNewComment.Result () , HttpServletResponse.SC_OK);
        }
    }
}
