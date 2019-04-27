package Page.PCheck;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CPage;
import Controller.Check.CheckInfoLogin;
import Controller.Request;
import Interface.Page;
import bardiademon.Interface.IsPage;
import bardiademon.Interface.bardiademon;

@bardiademon
@IsPage
@WebServlet (urlPatterns = "/CheckInfoLogin")
public class PCheckInfoLogin extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 1;

    @bardiademon
    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @bardiademon
    @Override
    public void SendToController (boolean IsOkRequest)
    {
        if (IsOkRequest) new CheckInfoLogin (Request.RequestUser.GetJsonRequest ());
    }
}
