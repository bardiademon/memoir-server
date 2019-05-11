package Controller.Delete;

import Interface.Controller;
import Interface.ResultModel;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Model.Delete.MDeleteComment;
import Model.IS.IsLinkMemoir;
import Other.Str;
import Page.Response;
import bardiademon.Controller.CJSON;
import Controller.CController;
import Controller.Request;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

@bardiademon
public class CDeleteComment implements Controller
{

    private String linkMemoir;
    private int idComment;
    private int idMemoir;

    private MDeleteComment mDeleteComment;

    @bardiademon
    public CDeleteComment (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ()))
        {
            linkMemoir = CController.User.GetCJson ().getVString (Request.RequestUser.KJR.KJR_DeleteComment.LINK_MEMOIR);
            idComment = CController.User.GetCJson ().getVInteger (Request.RequestUser.KJR.KJR_DeleteComment.ID_COMMENT);
            System.gc ();
            RunClass ();
        }
    }

    @bardiademon
    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (Request.RequestUser.KJR.KJR_DeleteComment.LEN);
        value.putKeyValue (Str.ToArray (
                Str.ToStrArray (Request.RequestUser.KJR.KJR_DeleteComment.ID_COMMENT , Request.RequestUser.KJR.KJR_DeleteComment.LINK_MEMOIR) ,
                Str.ToStrArray (CJSON.VD.IS_INT , CJSON.VD.IS_STRING)));
        return value;
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        IsLinkMemoir isLinkMemoir = new IsLinkMemoir (linkMemoir);
        CheckId checkId = new CheckId (InfoDatabase.TComment.NT , InfoDatabase.TComment.ID , idComment);
        if (isLinkMemoir.isFound () && checkId.isFound ())
        {
            idMemoir = isLinkMemoir.getIdMemoir ();
            SendToModel ();
            GetResultFromModel ();
        }
        else
        {
            Response.SetStatus (Response.SC_BAD_REQUEST);
            Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.NULL , Thread.currentThread ().getStackTrace () , "id comment not found => " + idComment);
        }
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        mDeleteComment = new MDeleteComment (idMemoir , idComment);
    }

    @Override
    public void GetResultFromModel ()
    {
        if (mDeleteComment.IsThereAResult ())
        {
            Log.NL (Response.SC_OK , mDeleteComment.Result () , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT);
            Controller.SetResult (mDeleteComment.Result () , Response.SC_OK);
        }
    }
}
