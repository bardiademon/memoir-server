package bardiademon.Other;

import Controller.Request;
import Other.InfoProject.Address;
import Other.MakeJson;
import Page.Req;
import bardiademon.Interface.bardiademon;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@bardiademon
public class Log
{

    private static int PageCode;

    private HttpServletRequest request;
    private int pageCode;
    private String where, message;

    private int statusCode;
    private Object resultModel;

    private static final String NAME_FILE_LOG = "Log.lg";

    private static final String
            KJ_HEADER = "header",
            KJ_PAGE_CODE = "page_code",
            KJ_WHERE = "where",
            KJ_MESSAGE = "message",
            KJ_IP = "ip_client",
            KJ_ID_USER = "id_user",
            KJR_METHOD_REQ = "method_request",
            KJR_QUERY_STRING = "query_string",
            KJR_SERVLET_PATH = "servlet_path",
            KJR_PARAMETER_POST = "parameter_post",
            KJ_INFO = "info",
            KJ_TIME = "time";

    private static final String KJ_STATUS_CODE = "status_code", KJ_RESULT_MODEL = "result_model", KJ_JSON_INFO_RES = "info_res";

    public static void SetPageCode (int pageCode)
    {
        PageCode = pageCode;
    }

    // NL => New Log
    public static void NL (int StatusCode , Object ResultModel , StackTraceElement[] StackTrace , String Message)
    {
        new Thread (() -> new Log (Req.GetRequest () , PageCode , StatusCode , ResultModel , Other.StackTrace.ToString (StackTrace) , Message).set ()).start ();
    }

    private Log (HttpServletRequest Request , int PageCode , int StatusCode , Object ResultModel , String Where , String Message)
    {
        this.request = Request;
        this.pageCode = PageCode;
        this.where = Where;
        this.message = Message;
        this.statusCode = StatusCode;
        this.resultModel = ResultModel;
    }

    private void set ()
    {
        String jsonInfo = makeJson ();

        File file;
        if (jsonInfo != null && (file = new File (Address.GetPathLog ())).exists ())
        {
            file = new File (file.getPath () + File.separator + NAME_FILE_LOG);
            try
            {
                if (file.exists () || file.createNewFile ())
                    Files.write (Paths.get (file.toURI ()) , (jsonInfo + "\n").getBytes () , StandardOpenOption.APPEND);

            }
            catch (IOException ignored)
            {
            }
        }
    }


    private String makeJson ()
    {
        try
        {
            JSONObject jsonHeader = new JSONObject ();
            Enumeration<String> headerNames = request.getHeaderNames ();
            while (headerNames.hasMoreElements ())
            {
                String headerName = headerNames.nextElement ();
                jsonHeader.put (headerName , request.getHeader (headerName));
            }

            String method = request.getMethod ();
            String queryString = request.getQueryString ();

            JSONObject parameter = new JSONObject ();
            for (Map.Entry<String, String[]> entry : request.getParameterMap ().entrySet ())
                parameter.put (entry.getKey () , Arrays.toString (entry.getValue ()));

            JSONObject parameterPost = new JSONObject ();
            Enumeration<String> paramPost = request.getParameterNames ();
            while (paramPost.hasMoreElements ())
            {
                String paramName = paramPost.nextElement ();
                parameterPost.put (paramName , request.getParameter (paramName));
            }
            String servletPath = request.getServletPath ();
            String ipClient = request.getRemoteAddr ();

            MakeJson jsonInfo = new MakeJson ();
            jsonInfo.put (KJ_IP , ipClient);
            jsonInfo.put (KJ_ID_USER , Request.RequestUser.GetId ());
            jsonInfo.put (KJ_HEADER , jsonHeader.toString ());
            jsonInfo.put (KJR_METHOD_REQ , method);
            jsonInfo.put (KJR_QUERY_STRING , queryString);
            jsonInfo.put (KJR_SERVLET_PATH , servletPath);
            jsonInfo.put (KJ_PAGE_CODE , pageCode);
            jsonInfo.put (KJR_PARAMETER_POST , parameterPost);

            MakeJson jsonRes = new MakeJson ();
            jsonRes.put (KJ_STATUS_CODE , statusCode);
            if (resultModel != null) jsonRes.put (KJ_RESULT_MODEL , resultModel.toString ().replace ("\"" , "\\\""));
            MakeJson jsonAllInfo = new MakeJson ();
            jsonAllInfo.put (KJ_WHERE , where);
            jsonAllInfo.put (KJ_MESSAGE , message);
            jsonAllInfo.put (KJ_INFO , jsonInfo.getJsonString ());
            jsonAllInfo.put (KJ_TIME , GetTimeStamp.get ());
            jsonAllInfo.put (KJ_JSON_INFO_RES , jsonRes.getJsonString ());

            return JsonConvert.Convert (jsonAllInfo.getJsonString ());
        }
        catch (JSONException | NullPointerException ignored)
        {
        }
        return null;
    }
}
