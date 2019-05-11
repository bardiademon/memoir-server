package Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CPage;
import Controller.Request;
import Controller.Update.CUpdateVLC;
import Interface.Page;
import bardiademon.Interface.bardiademon;

@bardiademon
@WebServlet (urlPatterns = "/LikeUser", name = "update_like")
public class PLikeUser extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 7;

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
        if (IsOkRequest) new CUpdateVLC (Request.RequestUser.GetJsonRequest () , CUpdateVLC.UL);
    }
}
