package Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bardiademon.Interface.bardiademon;

import Interface.ResultModel;
import Page.Req;
import Page.Response;
import bardiademon.Other.Log;

@bardiademon
public abstract class CPage
{
    @bardiademon
    public static abstract class User
    {
        private static HttpServletRequest request;
        private static HttpServletResponse response;
        private static int pageCode;

        @bardiademon
        public static boolean IsOkRequest (HttpServletRequest Request, HttpServletResponse Response, int PageCode)
        {
            User.request = Request;
            User.response = Response;
            User.pageCode = PageCode;
            SetReqAndRes ();
            if (CheckRequest ()) return true;
            else
            {
                Error ();
                return false;
            }
        }

        @bardiademon
        private static boolean CheckRequest ()
        {
            return (Request.RequestUser.Checking (request, pageCode));
        }

        @bardiademon
        private static void SetReqAndRes ()
        {
            Log.SetPageCode (pageCode);
            Response.SetResponse (response);
            Response.SetContentType (request.getContentType ());
            Req.SetRequest (request);
        }

        @bardiademon
        private static void Error ()
        {
            Log.NL (Response.SC_BAD_REQUEST, ResultModel.PublicResult.PUBLIC_ERROR_REQUEST, Thread.currentThread ().getStackTrace (), "");
            Response.GetResponse ().setStatus (Response.SC_BAD_REQUEST);
        }

    }
}
