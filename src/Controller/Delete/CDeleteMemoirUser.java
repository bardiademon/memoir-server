package Controller.Delete;

import Interface.Controller;
import Controller.CController;
import Interface.ResultModel;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Model.Delete.MDeleteMemoirUser;
import Page.Response;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.IsController;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

@bardiademon
@IsController
public class CDeleteMemoirUser implements Controller
{

    private int idMemoir;

    private MDeleteMemoirUser mDeleteMemoirUser;

    @bardiademon
    public CDeleteMemoirUser (String JsonRequest)
    {
        if (CController.User.Ready.IsOkJsonJustId.IsOk (JsonRequest))
        {
            idMemoir = CController.User.Ready.IsOkJsonJustId.GetId ();
            RunClass ();
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
        }
        else
        {
            Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.ForLog.ID_NOT_FOUND , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Response.SC_BAD_REQUEST);
        }
    }

    private boolean checkId ()
    {
        return (new CheckId (InfoDatabase.TMemoirList.NT , CheckId.D_ROW_NAME , idMemoir).isFound ());
    }

    @bardiademon
    @Override
    public CJSON.Value GetCJsonValue ()
    {
        return null;
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        mDeleteMemoirUser = new MDeleteMemoirUser (idMemoir);
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (mDeleteMemoirUser.IsThereAResult ())
        {
            Log.NL (Response.SC_OK , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT , Thread.currentThread ().getStackTrace () , mDeleteMemoirUser.Result ().toString ());
            Controller.SetResult (mDeleteMemoirUser.Result () , Response.SC_OK);
        }
    }
}
