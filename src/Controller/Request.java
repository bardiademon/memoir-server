package Controller;

import Model.Check.CheckInfoLogin;

import Page.Req;
import bardiademon.Controller.CJSON;
import bardiademon.Interface.IsController;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

@bardiademon
@IsController
public abstract class Request
{

    @bardiademon
    public static class RequestUser
    {
        private static boolean Login;
        private static int Id = 0;
        private static String JsonRequest;

        private static final String DEFAULT_METHOD = "POST";
        private static final String KEY_HEADER__NAME_REQUEST = "request";
        private static final String KEY_HEADER__INFO_LOGIN = "info_login";
        private static final String NameRequest = "_REQUEST_";

        @bardiademon
        static boolean Checking ()
        {
            return Checking (true);
        }

        @bardiademon
        public static boolean Checking (boolean CheckInfoLogin)
        {
            if (!Req.GetRequest ().getMethod ().equals (DEFAULT_METHOD)) return false;
            String nameRequest = Req.GetRequest ().getHeader (KEY_HEADER__NAME_REQUEST);
            if (nameRequest == null) return false;
            CheckRequest checkRequest = new CheckRequest (nameRequest , Log.GetPageCode ());

            boolean checkInfoLogin = true;
            if (CheckInfoLogin)
                checkInfoLogin = RequestUser.CheckingInfoLogin (Req.GetRequest ().getHeader (KEY_HEADER__INFO_LOGIN));

            if (checkRequest.isFound () && Req.GetRequest ().getQueryString () == null && checkInfoLogin)
            {
                JsonRequest = Req.GetRequest ().getParameter (NameRequest);
                return true;
            }
            else return false;
        }

        @bardiademon
        private static boolean CheckingInfoLogin (String InfoLogin)
        {
            if (InfoLogin != null)
            {
                try
                {
                    JSONObject jsonInfoLogin = new JSONObject (InfoLogin);

                    final int LEN_JSON_INFO_LOGIN = 3;
                    if (jsonInfoLogin.length () == LEN_JSON_INFO_LOGIN)
                    {
                        final int IK_USERNAME = 0, IK_PASSWORD = 1, IK_SERIAL = 2;
                        final String[] KEY_JSON = {"username" , "password" , "serial"};
                        for (String key : KEY_JSON) if (!jsonInfoLogin.has (key)) return false;
                        CheckInfoLogin checkInfLogin = new CheckInfoLogin (jsonInfoLogin.getString (KEY_JSON[IK_USERNAME]) , jsonInfoLogin.getString (KEY_JSON[IK_PASSWORD]) , jsonInfoLogin.getString (KEY_JSON[IK_SERIAL]));
                        if (checkInfLogin.isValid ())
                        {
                            Id = checkInfLogin.getId ();
                            Login = true;
                            return true;
                        }
                    }
                    return false;
                }
                catch (JSONException | NullPointerException e)
                {
                    return false;
                }
            }
            else
            {
                Login = false;
                return true;
            }
        }

        public static boolean IsLogin ()
        {
            return Login;
        }

        public static int GetId ()
        {
            return Id;
        }

        public static String GetJsonRequest ()
        {
            return JsonRequest;
        }

        @bardiademon
        // KJR => Key Json Request
        public interface KJR
        {
            @bardiademon
            abstract class PublicKJR
            {
                public static final String NAME = "name", ID = "id";
                public static final String KJR_TEXT = "text";
                public static final String ID_COMMENT = "idc", LINK_MEMOIR = "lnkm";
            }

            @bardiademon
            abstract class KJR_RecordNewMemoir extends PublicKJR
            {
                public static final int LEN = 6;
                public static final String KJR_SUBJECT = "subject";
                public static final String KJR_LINK = "link";
                public static final String KJR_DATE = "date";
                public static final String KJR_OPEN = "open";
            }

            @bardiademon
            abstract class KJR_GetMemoir extends PublicKJR
            {
                public static final int LEN = 1;
                public static final String KJR_LINK = "link";

                public static class Type
                {
                    public static final String T_LINK = CJSON.VD.IS_STRING;
                }
            }

            @bardiademon
            abstract class KJR_LikeUser extends PublicKJR
            {
                public static final int LEN = 1;
                public static final String LINK_MEMOIR = "lnk_memoir";

            }

            @bardiademon
            abstract class KJR__GetMemoirUser extends PublicKJR
            {
                // VT => Value Type
                //    public static final int LEN = 1;

                // DV => Default Value
                public static class DV
                {
                    public static final int DV_ID__GET_ALL = 0;
                }
            }

            @bardiademon
            abstract class KJR_GetComment extends PublicKJR
            {
                public static final int LEN = 1;
                public static final String LINK_MEMOIR = "link";
            }

            @bardiademon
            abstract class KJR_DeleteComment extends PublicKJR
            {
                public static final int LEN = 2;
            }

            @bardiademon
            abstract class KJR_NewComment extends PublicKJR
            {
                public static final int LEN = 2;
            }
        }

    }

}
