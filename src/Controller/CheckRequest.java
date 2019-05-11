package Controller;

import Interface.ResultModel;
import bardiademon.Interface.Controller;
import bardiademon.Interface.IsController;
import bardiademon.Interface.bardiademon;

@bardiademon
@IsController
public class CheckRequest implements Controller
{
    private String nameRequest;
    private Model.CheckRequest checkRequest;
    private static final int MIN_LEN_REQUEST = 2;
    private int pageCode;

    @bardiademon
    CheckRequest (String nameRequest , int pageCode)
    {
        this.nameRequest = nameRequest;
        this.pageCode = pageCode;
        RunClass ();
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        if (Checking ())
        {
            SendToModel ();
            GetResultFromModel ();
        }
    }

    @bardiademon
    @Override
    public boolean Checking ()
    {
        return (nameRequest != null && !nameRequest.equals ("null") && nameRequest.length () >= MIN_LEN_REQUEST);
    }

    @Override
    public boolean IsHas ()
    {
        return false;
    }

    @Override
    public boolean CheckingValue ()
    {
        return false;
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        checkRequest = new Model.CheckRequest (nameRequest , pageCode);
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
    }

    @Override
    public void SetResult (Object Result , int Status)
    {
    }

    @Override
    public void SendToView (Object Result)
    {
    }

    @bardiademon
    boolean isFound ()
    {
        return (checkRequest != null && checkRequest.IsThereAResult () && checkRequest.Result ().equals (ResultModel.ResultCheckRequest.FOUND));
    }

}
