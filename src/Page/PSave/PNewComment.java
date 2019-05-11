package Page.PSave;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.CPage;
import Controller.CSave.CNewComment;
import Controller.Request;
import Interface.Page;

// NC , nc => New Comment
@WebServlet (urlPatterns = "/NC", name = "nc")
public class PNewComment extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 13;

    @Override
    protected void service (HttpServletRequest Request , HttpServletResponse Response)
    {
        SendToController (CPage.User.IsOkRequest (Request , Response , PAGE_CODE));
    }

    @Override
    public void SendToController (boolean IsOkRequest)
    {
        System.out.println (IsOkRequest);
        if (IsOkRequest) new CNewComment (Request.RequestUser.GetJsonRequest ());
    }
}
