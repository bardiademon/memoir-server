package Interface;

import View.ViewResult;
import Page.Response;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.bardiademon;

@bardiademon
public interface Controller
{
    @bardiademon
    void RunClass ();

    @bardiademon
    CJSON.Value GetCJsonValue ();

    @bardiademon
    void SendToModel ();

    @bardiademon
    void GetResultFromModel ();

    @bardiademon
    static void SetResult (Object Result , int Status)
    {
        Response.SetStatus (Status);
        Controller.SendToView (Result);
    }

    @bardiademon
    static void SendToView (Object Result)
    {
        new ViewResult (Result);
    }

}
