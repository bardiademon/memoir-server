package Controller;

import Interface.ResultModel;
import Other.Str;
import Page.Response;
import View.ViewResult;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;


@bardiademon
public abstract class CController
{

    @bardiademon
    public static abstract class User
    {
        private static JSONObject JsonRequest;

        public static boolean IsOkJson (String JsonRequest , CJSON.Value Value)
        {
            return (IsOkJson (JsonRequest , Value , true));
        }

        public static boolean IsOkJson (String JsonRequest , CJSON.Value Value , boolean CheckLogin)
        {
            if (!CheckLogin || Request.RequestUser.IsLogin ())
            {
                try
                {
                    User.JsonRequest = new JSONObject (JsonRequest);
                }
                catch (JSONException | NullPointerException e)
                {
                    Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , e.getMessage ());
                    SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST);
                    return false;
                }
            }
            else
            {
                Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.NOT_LOGGED_IN , Thread.currentThread ().getStackTrace () , "");
                SetResult (ResultModel.PublicResult.NOT_LOGGED_IN);
                return false;
            }
            return (CheckRequest (Value));
        }

        private static CJSON cjson;

        private static boolean CheckRequest (CJSON.Value Value)
        {
            if ((cjson = new CJSON (JsonRequest , Value , true)).isOk ()) return true;
            else
            {
                Log.NL (HttpServletResponse.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , "");
                SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST);
                return false;
            }
        }

        public static CJSON GetCJson ()
        {
            return cjson;
        }

        public static JSONObject GetJsonRequest ()
        {
            System.gc ();
            return JsonRequest;
        }


        public static abstract class Ready
        {
            public static class IsOkJsonJustId
            {
                private static int Id;

                public static boolean IsOk (String JsonRequest)
                {
                    return IsOk (JsonRequest , Request.RequestUser.KJR.PublicKJR.ID);
                }

                public static boolean IsOk (String JsonRequest , String KeyId)
                {
                    CJSON.Value value = new CJSON.Value ();
                    value.putLen (1);
                    value.putKeyValue (Str.ToArray (Str.ToStrArray (KeyId) , Str.ToStrArray (CJSON.VD.IS_INT)));
                    if (User.IsOkJson (JsonRequest , value))
                    {
                        try
                        {
                            Id = User.JsonRequest.getInt (KeyId);
                            return true;
                        }
                        catch (JSONException e)
                        {
                            Log.NL (HttpServletResponse.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , e.getMessage ());
                            return false;
                        }
                    }
                    else return false;
                }

                public static int GetId ()
                {
                    System.gc ();
                    return Id;
                }
            }
        }
    }


    @bardiademon
    private static void SetResult (Object Result)
    {
        Response.SetStatus (HttpServletResponse.SC_BAD_REQUEST);
        SendToView (Result);
    }

    @bardiademon
    private static void SendToView (Object Result)
    {
        new ViewResult (Result);
    }

    @bardiademon
    public static abstract class ForModel
    {

        @bardiademon
        public static boolean IsLogin ()
        {
            if (Request.RequestUser.IsLogin ()) return true;
            else
            {
                Log.NL (Response.SC_INTERNAL_SERVER_ERROR , ResultModel.PublicResult.NOT_LOGGED_IN , Thread.currentThread ().getStackTrace () , "");
                return false;
            }
        }
    }
}
