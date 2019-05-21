package Page.PGet;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CGet.CGetTextMemoir;
import Controller.CPage;
import Controller.Request;
import Interface.Page;

// GTM , gtm => Get Text Memoir
@WebServlet (urlPatterns = "/GTM", name = "gtm")
public class PGetTextMemoir extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 14;

    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @Override
    public void SendToController (boolean IsOkRequest)
    {
        if (IsOkRequest) new CGetTextMemoir (Request.RequestUser.GetJsonRequest ());
    }
}
