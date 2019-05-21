package Controller;


import Controller.Request.RequestUser.KJR;
import Interface.Controller;
import Interface.ResultModel;
import Model.CheckId;
import Model.Database.InfoDatabase;
import Model.Get.GetSubject;
import Model.MRecordChangeMemoir;
import Other.Str;
import bardiademon.Controller.CJSON;
import bardiademon.Controller.CheckString;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsController;
import bardiademon.Other.Log;
import bardiademon.Other.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


@bardiademon
@IsController
public final class CRecordChangeMemoir implements Controller
{
    private String name, subject, link, date, text;
    private boolean open;
    private int id;

    private GetSubject getSubject;

    private boolean change;

    private final String[] KEY =
        Str.ToStrArray (
            KJR.KJR_RecordChangeNewMemoir.NAME ,
            KJR.KJR_RecordChangeNewMemoir.KJR_SUBJECT ,
            KJR.KJR_RecordChangeNewMemoir.KJR_LINK ,
            KJR.KJR_RecordChangeNewMemoir.KJR_DATE ,
            KJR.KJR_RecordChangeNewMemoir.KJR_TEXT ,
            KJR.KJR_RecordChangeNewMemoir.KJR_OPEN
        );
    private final String[] TYPE =
        Str.ToStrArray (
            CJSON.VD.IS_STRING ,
            CJSON.VD.IS_STRING ,
            CJSON.VD.IS_STRING ,
            CJSON.VD.IS_STRING ,
            CJSON.VD.IS_STRING ,
            CJSON.VD.IS_BOOLEAN
        );

    private MRecordChangeMemoir mRecordChangeMemoir;

    @bardiademon
    public CRecordChangeMemoir (String JsonRequest)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ()))
        {
            getValue ();
            RunClass ();
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
        final CJSON.Value value = new CJSON.Value ();
        final List <String> KEY_C = new ArrayList <> ();
        final List <String> TYPE_C = new ArrayList <> ();
        if (KEY_C.addAll (Arrays.asList (KEY)) && TYPE_C.addAll (Arrays.asList (TYPE)))
        {
            KEY_C.add (KJR.KJR_RecordChangeNewMemoir.KJR_C_CHANGE);
            TYPE_C.add (CJSON.VD.IS_BOOLEAN);

            KEY_C.add (KJR.KJR_RecordChangeNewMemoir.ID);
            TYPE_C.add (CJSON.VD.IS_INT);

            value.putLen (KJR.KJR_RecordChangeNewMemoir.LEN , KJR.KJR_RecordChangeNewMemoir.LEN_C);

            final String[] KEY_CC = KEY_C.toArray (new String[]{});
            final String[] TYPE_CC = TYPE_C.toArray (new String[]{});
            value.putKeyValue (Str.ToArray (KEY , TYPE) , Str.ToArray (KEY_CC , TYPE_CC));
            value.putDefaultValue (null , Str.ToList (null , null , null , null , null , null , true , null));
            return value;
        }
        return null;
    }

    @bardiademon
    private void getValue ()
    {
        CJSON cjson = CController.User.GetCJson ();
        name = cjson.getVString (KJR.KJR_RecordChangeNewMemoir.NAME);
        subject = cjson.getVString (KJR.KJR_RecordChangeNewMemoir.KJR_SUBJECT);
        link = cjson.getVString (KJR.KJR_RecordChangeNewMemoir.KJR_LINK);
        date = cjson.getVString (KJR.KJR_RecordChangeNewMemoir.KJR_DATE);
        text = cjson.getVString (KJR.KJR_RecordChangeNewMemoir.KJR_TEXT);
        open = cjson.getVBoolean (KJR.KJR_RecordChangeNewMemoir.KJR_OPEN);
        Boolean valueChange;
        change = ((valueChange = cjson.getVBoolean (KJR.KJR_RecordChangeNewMemoir.KJR_C_CHANGE)) != null && valueChange.equals (true));
        if (change) id = cjson.getVInteger (KJR.KJR_RecordChangeNewMemoir.ID);
    }

    @bardiademon
    private int checking2 ()
    {
        int result = ResultModel.PublicResult.NULL, resultCheckLink;
        if ((resultCheckLink = checkLink ()) == ResultModel.PublicResult.NULL)
        {
            if (!checkDate ()) result = ResultModel.RecordChangeMemoir.SC200.ERROR_DATE;
        }
        else result = resultCheckLink;
        return result;
    }

    @bardiademon
    private int checkLink ()
    {
        if (!(CheckString.checkUsername (link)))
            return ResultModel.RecordChangeMemoir.SC200.ERROR_LINK;

        SQL.Ready.CheckValueInSQL checkValueInSQL = new SQL.Ready.CheckValueInSQL ();
        checkValueInSQL.setGetId ();
        if (checkValueInSQL.is (InfoDatabase.TMemoirList.NT , InfoDatabase.TMemoirList.LINK , link))
        {
            if (!change || checkValueInSQL.getId () != id)
                return ResultModel.RecordChangeMemoir.SC400.DUPLICATE_LINK;
        }
        return ResultModel.RecordChangeMemoir.NULL;
    }

    @bardiademon
    private boolean checkDate ()
    {
        return (CheckString.CheckJustNumber (date));
    }

    @bardiademon
    @Override
    public final void SendToModel ()
    {
        if (checkSubId ())
        {
            mRecordChangeMemoir = new MRecordChangeMemoir (name , getSubject.getId () , link , date , text , open , change , id);
            GetResultFromModel ();
        }
        else
        {
            Log.NL (HttpServletResponse.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SUBJECT_ERROR);
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR_REQUEST , HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean checkSubId ()
    {
        getSubject = new GetSubject (subject);
        CheckId checkId = new CheckId (InfoDatabase.TMemoirList.NT , InfoDatabase.TMemoirList.ID , id);
        boolean result;
        result = getSubject.isFound ();
        if (change) result = checkId.isFound ();
        return result;
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        if (mRecordChangeMemoir.IsThereAResult ())
        {
            Log.NL (HttpServletResponse.SC_OK , mRecordChangeMemoir.Result () , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT);
            Controller.SetResult (mRecordChangeMemoir.Result () , HttpServletResponse.SC_OK);
        }
    }

}
