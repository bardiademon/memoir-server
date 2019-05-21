package Page;

import Controller.CPage;
import Controller.CRecordChangeMemoir;
import Controller.Request;
import Interface.Page;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsPage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@bardiademon
@IsPage
@WebServlet (urlPatterns = "/RNMemoir", name = "rc_memoir")
public class PRecordChangeMemoir extends HttpServlet implements Page
{
    private static final int PAGE_CODE = 2;

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
        if (IsOkRequest) new CRecordChangeMemoir (Request.RequestUser.GetJsonRequest ());
    }
}
