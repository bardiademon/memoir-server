package Controller.PSet;

import javax.servlet.http.HttpServletResponse;

import Controller.CController;
import Interface.Controller;
import Interface.ResultModel;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Model.MSet.MSetINeedItCmnt;
import Page.Response;
import Controller.Request;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

@bardiademon
public class CSetINeedItCmnt implements Controller
{
    private int idComment;

    private MSetINeedItCmnt mSetINeedItCmnt;

    @bardiademon
    public CSetINeedItCmnt (String JsonRequest)
    {
        if (CController.User.Ready.IsOkJsonJustId.IsOk (JsonRequest))
        {
            idComment = CController.User.Ready.IsOkJsonJustId.GetId ();
            RunClass ();
        }
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        CheckId checkId = new CheckId (InfoDatabase.TComment.NT , InfoDatabase.TComment.ID , idComment);
        if (checkId.isFound ())
        {
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
    public CJSON.Value GetCJsonValue ()
    {
        return null;
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        mSetINeedItCmnt = new MSetINeedItCmnt (Request.RequestUser.GetId () , idComment);
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (mSetINeedItCmnt.IsThereAResult ())
        {
            Log.NL (HttpServletResponse.SC_OK , mSetINeedItCmnt.Result () , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (mSetINeedItCmnt.Result () , HttpServletResponse.SC_OK);
        }
    }
}
