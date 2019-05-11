package Controller.CGet;

import Controller.Request.RequestUser.KJR.KJR_GetComment;
import Interface.Controller;
import Interface.ResultModel;
import Model.Get.MGetComment;
import Model.IS.IsLinkMemoir;
import Other.Str;
import Page.Response;
import bardiademon.Controller.CJSON;
import Controller.CController;
import bardiademon.Other.Log;

public class CGetComment implements Controller
{
    private String linkMemoir;

    private MGetComment mGetComment;
    private IsLinkMemoir isLinkMemoir;

    public CGetComment (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ()))
        {
            linkMemoir = CController.User.GetCJson ().getVString (KJR_GetComment.LINK_MEMOIR);
            RunClass ();
        }
    }

    @Override
    public void RunClass ()
    {
        isLinkMemoir = new IsLinkMemoir (linkMemoir);
        if (isLinkMemoir.isFound ())
        {
            SendToModel ();
            GetResultFromModel ();
        }
    }

    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (KJR_GetComment.LEN);
        value.putKeyValue (Str.ToArray (Str.ToStrArray (KJR_GetComment.LINK_MEMOIR) , Str.ToStrArray (CJSON.VD.IS_STRING)));
        return value;
    }

    @Override
    public void SendToModel ()
    {
        assert isLinkMemoir != null;
        mGetComment = new MGetComment (isLinkMemoir.getIdMemoir ());
    }

    @Override
    public void GetResultFromModel ()
    {
        if (mGetComment.IsThereAResult ())
        {
            Log.NL (Response.SC_OK , mGetComment.Result () , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT);
            Controller.SetResult (mGetComment.Result () , Response.SC_OK);
        }
    }
}
