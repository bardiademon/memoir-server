package Page.Delete;

import Controller.CPage;
import Controller.Delete.CDeleteMemoirUser;
import Controller.Request;
import Interface.Page;
import bardiademon.Interface.IsPage;
import bardiademon.Interface.bardiademon;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@bardiademon
@IsPage
@WebServlet (urlPatterns = "/DelMemoirUser", name = "del_memoir_user")
public class PDeleteMemoirUser extends HttpServlet implements Page
{

    private static final int PAGE_CODE = 4;

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
        if (IsOkRequest) new CDeleteMemoirUser (Request.RequestUser.GetJsonRequest ());
    }
}
