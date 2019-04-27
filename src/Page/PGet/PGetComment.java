package Page.PGet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CGet.CGetComment;
import Controller.CPage;
import Controller.Request;
import Interface.Page;

public class PGetComment extends HttpServlet implements Page
{

    private static final int PAGE_CODE = 8;

    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @Override
    public void SendToController (boolean IsOkRequest)
    {
        if (IsOkRequest) new CGetComment (Request.RequestUser.GetJsonRequest ());
    }
}
