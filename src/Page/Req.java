package Page;

import bardiademon.Interface.bardiademon;

import javax.servlet.http.HttpServletRequest;

@bardiademon
public abstract class Req
{
    private static HttpServletRequest Request;

    @bardiademon
    public static void SetRequest (HttpServletRequest Request)
    {
        Req.Request = Request;
    }

    @bardiademon
    public static HttpServletRequest GetRequest ()
    {
        return Request;
    }
}
