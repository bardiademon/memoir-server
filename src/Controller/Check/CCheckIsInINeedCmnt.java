package Controller.Check;

import javax.servlet.http.HttpServletResponse;

import Interface.Controller;
import Interface.ResultModel;
import Model.Check.MCheckIsInINeedCmnt;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Page.Response;
import bardiademon.Controller.CJSON;
import Controller.CController;
import Controller.Request;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

@bardiademon
public class CCheckIsInINeedCmnt implements Controller
{
    private int idComment;

    private MCheckIsInINeedCmnt mCheckIsInINeedCmnt;

    @bardiademon
    public CCheckIsInINeedCmnt (String JsonRequest)
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
        mCheckIsInINeedCmnt = new MCheckIsInINeedCmnt (Request.RequestUser.GetId () , idComment);
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (mCheckIsInINeedCmnt.IsThereAResult ())
        {
            Log.NL (HttpServletResponse.SC_OK , mCheckIsInINeedCmnt.Result () , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (mCheckIsInINeedCmnt.Result () , HttpServletResponse.SC_OK);
        }
    }
}
