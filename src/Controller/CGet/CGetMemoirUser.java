package Controller.CGet;

import Controller.Request.RequestUser.KJR;
import Interface.ResultModel;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Model.Get.MGetMemoirUser;
import Page.Response;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsController;
import Interface.Controller;
import bardiademon.Other.Log;
import Controller.CController;

import javax.servlet.http.HttpServletResponse;

@bardiademon
@IsController
public class CGetMemoirUser implements Controller
{

    private MGetMemoirUser mGetMemoirUser;

    private int idMemoir;

    @bardiademon
    public CGetMemoirUser (String JsonRequest)
    {
        if (CController.User.Ready.IsOkJsonJustId.IsOk (JsonRequest))
        {
            idMemoir = CController.User.Ready.IsOkJsonJustId.GetId ();
            RunClass ();
            System.gc ();
        }
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        if (checkId ())
        {
            SendToModel ();
            GetResultFromModel ();
        } else
        {
            Log.NL (Response.SC_BAD_REQUEST, ResultModel.PublicResult.PUBLIC_ERROR_REQUEST, Thread.currentThread ().getStackTrace (), "");
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @bardiademon
    @Override
    public CJSON.Value GetCJsonValue ()
    {
        return null;
    }

    @bardiademon
    private boolean checkId ()
    {
        boolean result;
        if (idMemoir == KJR.KJR__GetMemoirUser.DV.DV_ID__GET_ALL) result = true;
        else
            result = new CheckId (InfoDatabase.TMemoirList.NT, InfoDatabase.TMemoirList.ID, idMemoir).isFound ();
        return result;
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        mGetMemoirUser = new MGetMemoirUser (idMemoir);
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (mGetMemoirUser.IsThereAResult ())
        {
            Log.NL (Response.SC_OK, ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT, Thread.currentThread ().getStackTrace (), mGetMemoirUser.Result ().toString ());
            Controller.SetResult (mGetMemoirUser.Result (), Response.SC_OK);
        }
    }
}
