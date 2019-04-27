package Other.Useful;

import org.json.JSONException;

import Interface.ResultModel;
import Page.Response;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

import java.sql.SQLException;

@bardiademon
public abstract class UsefulLog
{
    @bardiademon
    public static void ForSQLException (StackTraceElement[] StackTrace , SQLException e)
    {
        Log.NL (Response.SC_INTERNAL_SERVER_ERROR , ResultModel.PublicResult.PUBLIC_ERROR , StackTrace , e.getMessage ());
    }

    @bardiademon
    public static void CannotGetConnection (StackTraceElement[] StackTrace)
    {
        Log.NL (Response.SC_INTERNAL_SERVER_ERROR , ResultModel.PublicResult.PUBLIC_ERROR , StackTrace , "Cannot get connection");
    }

    @bardiademon
    public static void ForSQLExceptionServer (StackTraceElement[] StackTrace , SQLException e)
    {
        Log.NL (Response.SC_INTERNAL_SERVER_ERROR , ResultModel.PublicResult.SERVER , StackTrace , e.getMessage ());
    }

    @bardiademon
    public static void ForJSONException (StackTraceElement[] StackTrace , JSONException e)
    {
        Log.NL (Response.SC_INTERNAL_SERVER_ERROR , ResultModel.PublicResult.SERVER , StackTrace , e.getMessage ());
    }

}
