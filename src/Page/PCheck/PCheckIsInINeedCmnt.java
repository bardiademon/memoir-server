package Page.PCheck;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CPage;
import Controller.Check.CCheckIsInINeedCmnt;
import Controller.Request;
import Interface.Page;

// ciiinc => CheckIsInINeedCmnt
@WebServlet (urlPatterns = "/IsInINeedCmnt", name = "ciiinc")
public class PCheckIsInINeedCmnt extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 10;

    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @Override
    public void SendToController (boolean IsOkRequest)
    {
        if (IsOkRequest) new CCheckIsInINeedCmnt (Request.RequestUser.GetJsonRequest ());
    }
}
