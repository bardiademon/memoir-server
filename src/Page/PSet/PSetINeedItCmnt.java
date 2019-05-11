package Page.PSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CPage;
import Controller.PSet.CSetINeedItCmnt;
import Controller.Request;
import Interface.Page;

@WebServlet (urlPatterns = "/SetINeedCmnt", name = "sinic")
public class PSetINeedItCmnt extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 11;

    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @Override
    public void SendToController (boolean IsOkRequest)
    {
        if (IsOkRequest) new CSetINeedItCmnt (Request.RequestUser.GetJsonRequest ());
    }
}
