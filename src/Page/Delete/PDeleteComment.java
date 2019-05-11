package Page.Delete;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CPage;
import Controller.Delete.CDeleteComment;
import Controller.Request;
import Interface.Page;
import bardiademon.Interface.bardiademon;


@bardiademon
@WebServlet (urlPatterns = "/DeleteComment", name = "delcmnt") // delcmnt => Delete Comment
public class PDeleteComment extends HttpServlet implements Page
{

    private final static int PAGE_CODE = 12;

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
        if (IsOkRequest) new CDeleteComment (Request.RequestUser.GetJsonRequest ());
    }
}
