package Page.PGet;

import Controller.CGet.CGetMemoirUser;
import Controller.CPage;
import Controller.Request;
import Interface.Page;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsPage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@IsPage
@bardiademon
@WebServlet (urlPatterns = "/GetMemoirUser", name = "get_memoir_user")
public class PGetMemoirUser extends HttpServlet implements Page
{

    private static final int PAGE_CODE = 3;

    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @Override
    public void SendToController (boolean IsOkRequest)
    {
        if (IsOkRequest) new CGetMemoirUser (Request.RequestUser.GetJsonRequest ());
    }
}
