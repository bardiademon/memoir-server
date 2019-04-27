package Controller.CGet;

import Interface.Controller;
import Interface.ResultModel;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Model.Get.MGetComment;
import Page.Response;
import bardiademon.Controller.CJSON;
import Controller.CController;
import Controller.Request;
import bardiademon.Other.Log;

public class CGetComment implements Controller
{
    private int idMemoir;

    private MGetComment mGetComment;

    public CGetComment (String JsonRequest)
    {
        if (CController.User.Ready.IsOkJsonJustId.IsOk (JsonRequest , Request.RequestUser.KJR.KJR_GetComment.ID_MEMOIR))
        {
            this.idMemoir = CController.User.Ready.IsOkJsonJustId.GetId ();
            RunClass ();
        }
    }

    @Override
    public void RunClass ()
    {
        CheckId checkId = new CheckId (InfoDatabase.TMemoirList.NT , InfoDatabase.TMemoirList.ID , idMemoir);
        if (checkId.isFound ())
        {
            SendToModel ();
            GetResultFromModel ();
        }
    }

    @Override
    public CJSON.Value GetCJsonValue ()
    {
        return null;
    }

    @Override
    public void SendToModel ()
    {
        mGetComment = new MGetComment (idMemoir);
    }

    @Override
    public void GetResultFromModel ()
    {
        if (mGetComment.IsThereAResult ())
        {
            Log.NL (Response.SC_OK , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT , Thread.currentThread ().getStackTrace () , mGetComment.Result ().toString ());
            Controller.SetResult (mGetComment.Result () , Response.SC_OK);
        }
    }
}
