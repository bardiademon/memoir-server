package Page.PGet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CGet.CGetMemoir;
import Controller.CPage;
import Controller.Request;
import Interface.Page;
import bardiademon.Interface.bardiademon;

@bardiademon
@WebServlet (urlPatterns = "/GetMemoir", name = "get_memoir")
public class PGetMemoir extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 5;

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
        if (IsOkRequest) new CGetMemoir (Request.RequestUser.GetJsonRequest ());
    }
}
