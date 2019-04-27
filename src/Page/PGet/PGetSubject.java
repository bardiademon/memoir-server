package Page.PGet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CGet.CGetSubject;
import Controller.Request;
import Interface.Page;
import Controller.CPage;

@WebServlet (urlPatterns = "/GetSubject", name = "get_subject")
public class PGetSubject extends HttpServlet implements Page
{

    private static final int PAGE_CODE = 6;

    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @Override
    public void SendToController (boolean IsOkRequest)
    {
        if (IsOkRequest) new CGetSubject (Request.RequestUser.GetJsonRequest ());
    }
}
