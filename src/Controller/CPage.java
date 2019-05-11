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

        @bardiademon
        public static boolean IsOkRequest (HttpServletRequest Request , HttpServletResponse Response , int PageCode)
        {
            SetReqAndRes (Request , Response , PageCode);
            Page.Response.GetWriter ().println (Req.GetRequest ().getParameter ("_REQUEST_"));
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
            return (Request.RequestUser.Checking ());
        }

        @bardiademon
        private static void SetReqAndRes (HttpServletRequest request , HttpServletResponse response , int pageCode)
        {
            Log.SetPageCode (pageCode);
            Response.SetResponse (response);
            Response.SetContentType (request.getContentType ());
            Req.SetRequest (request);
        }

        @bardiademon
        private static void Error ()
        {
            Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , "");
            Response.GetResponse ().setStatus (Response.SC_BAD_REQUEST);
        }

    }
}
