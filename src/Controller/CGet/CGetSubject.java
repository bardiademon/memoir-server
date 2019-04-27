package Controller.CGet;

import Interface.Controller;
import Interface.ResultModel;
import Model.Get.MGetSubject;
import Page.Response;
import bardiademon.Controller.CJSON;
import Controller.CController;
import bardiademon.Other.Log;

public class CGetSubject implements Controller
{

    private MGetSubject mGetSubject;

    public CGetSubject (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ())) RunClass ();
    }

    @Override
    public void RunClass ()
    {
        SendToModel ();
        GetResultFromModel ();
    }

    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (0);
        return value;
    }

    @Override
    public void SendToModel ()
    {
        mGetSubject = new MGetSubject ();
    }

    @Override
    public void GetResultFromModel ()
    {
        if (mGetSubject.IsThereAResult ())
        {
            Log.NL (Response.SC_OK , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT , Thread.currentThread ().getStackTrace () , mGetSubject.Result ().toString ());
            Controller.SetResult (mGetSubject.Result () , Response.SC_OK);
        }
    }
}
