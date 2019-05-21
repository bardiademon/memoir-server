package Controller.CGet;

import javax.servlet.http.HttpServletResponse;

import Interface.Controller;
import Interface.ResultModel;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Model.Get.MGetTextMemoir;
import Page.Response;
import bardiademon.Controller.CJSON;
import Controller.CController;
import bardiademon.Other.Log;

public class CGetTextMemoir implements Controller
{
    private int idMemoir;

    private MGetTextMemoir mGetTextMemoir;

    public CGetTextMemoir (String JsonRequest)
    {
        if (CController.User.Ready.IsOkJsonJustId.IsOk (JsonRequest))
        {
            idMemoir = CController.User.Ready.IsOkJsonJustId.GetId ();
            RunClass ();
        }
    }

    @Override
    public void RunClass ()
    {
        CheckId checkId = new CheckId (InfoDatabase.TMemoirList.NT , idMemoir);
        if (checkId.isFound ())
        {
            SendToModel ();
            GetResultFromModel ();
        }
        else
        {
            Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , HttpServletResponse.SC_BAD_REQUEST);
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
        mGetTextMemoir = new MGetTextMemoir (idMemoir);
    }

    @Override
    public void GetResultFromModel ()
    {
        if (mGetTextMemoir.IsThereAResult ())
        {
            Log.NL (Response.SC_OK , mGetTextMemoir.Result () , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT);
            Controller.SetResult (mGetTextMemoir.Result () , HttpServletResponse.SC_OK);
        }
        else
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Response.SC_INTERNAL_SERVER_ERROR);
    }
}
