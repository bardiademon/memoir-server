package Controller.CRecord;

import Controller.Request.RequestUser.KJR;
import Interface.Controller;
import Interface.ResultModel;
import Model.Database.InfoDatabase;
import Model.Get.GetSubject;
import Model.MRecord.MRecordNewMemoir;
import Other.Str;
import Controller.CController;
import bardiademon.Controller.CJSON;
import bardiademon.Controller.CheckString;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsController;
import bardiademon.Other.Log;
import bardiademon.Other.SQL;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;


@bardiademon
@IsController
public class CRecordNewMemoir implements Controller
{
    private String name, subject, link, date, text;
    private boolean open;

    private MRecordNewMemoir mRecordNewMemoir;

    @bardiademon
    public CRecordNewMemoir (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ()) && getValue (CController.User.GetJsonRequest ()))
        {
            RunClass ();
            System.gc ();
        }
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        int resultCheckValue;
        if ((resultCheckValue = checking2 ()) != ResultModel.PublicResult.NULL)
        {
            Log.NL (HttpServletResponse.SC_BAD_REQUEST , resultCheckValue , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (resultCheckValue , HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        SendToModel ();
    }

    @bardiademon
    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (KJR.KJR_RecordNewMemoir.LEN);
        value.putKeyValue (Str.ToArray
                (
                        Str.ToStrArray (KJR.KJR_RecordNewMemoir.NAME , KJR.KJR_RecordNewMemoir.KJR_SUBJECT , KJR.KJR_RecordNewMemoir.KJR_LINK , KJR.KJR_RecordNewMemoir.KJR_DATE , KJR.KJR_RecordNewMemoir.KJR_TEXT , KJR.KJR_RecordNewMemoir.KJR_OPEN) ,
                        Str.ToStrArray (CJSON.VD.IS_STRING , CJSON.VD.IS_STRING , CJSON.VD.IS_STRING , CJSON.VD.IS_STRING , CJSON.VD.IS_STRING , CJSON.VD.IS_BOOLEAN)
                ));
        return value;
    }

    @bardiademon
    private boolean getValue (JSONObject jsonRequest)
    {
        try
        {
            name = jsonRequest.getString (KJR.KJR_RecordNewMemoir.NAME);
            subject = jsonRequest.getString (KJR.KJR_RecordNewMemoir.KJR_SUBJECT);
            link = jsonRequest.getString (KJR.KJR_RecordNewMemoir.KJR_LINK);
            date = jsonRequest.getString (KJR.KJR_RecordNewMemoir.KJR_DATE);
            text = jsonRequest.getString (KJR.KJR_RecordNewMemoir.KJR_TEXT);
            open = jsonRequest.getBoolean (KJR.KJR_RecordNewMemoir.KJR_OPEN);
            return true;
        }
        catch (JSONException e)
        {
            Log.NL (HttpServletResponse.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , e.getMessage ());
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
    }

    @bardiademon
    private int checking2 ()
    {
        int result = ResultModel.PublicResult.NULL, resultCheckLink;
        if ((resultCheckLink = checkLink ()) == ResultModel.PublicResult.NULL)
        {
            if (!checkDate ()) result = ResultModel.RecordNewMemoir.SC200.ERROR_DATE;
        }
        else result = resultCheckLink;
        return result;
    }

    @bardiademon
    private int checkLink ()
    {
        if (!(CheckString.checkUsername (link)))
            return ResultModel.RecordNewMemoir.SC200.ERROR_LINK;
        if (new SQL.Ready.CheckValueInSQL ().is (InfoDatabase.TMemoirList.NT , InfoDatabase.TMemoirList.LINK , link))
            return ResultModel.RecordNewMemoir.SC400.DUPLICATE_LINK;
        return ResultModel.RecordNewMemoir.NULL;
    }

    @bardiademon
    private boolean checkDate ()
    {
        return (CheckString.CheckJustNumber (date));
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        GetSubject getSubject = new GetSubject (URLDecoder.decode (subject , StandardCharsets.UTF_8));
        if (getSubject.isFound ())
        {
            mRecordNewMemoir = new MRecordNewMemoir (name , getSubject.getId () , link , date , text , open);
            GetResultFromModel ();
        }
        else
        {
            Log.NL (HttpServletResponse.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SUBJECT_ERROR);
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (mRecordNewMemoir.IsThereAResult ())
        {
            Log.NL (HttpServletResponse.SC_OK , mRecordNewMemoir.Result () , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT);
            Controller.SetResult (mRecordNewMemoir.Result () , HttpServletResponse.SC_OK);
        }
    }

}
