package Controller.Update;

import Controller.Request;
import Interface.Controller;
import Interface.ResultModel;
import Model.Check.CheckLV;
import Model.IS.IsIdMemoirForIdUser;
import Model.IS.IsLinkMemoir;
import Model.MRecord.MRecordNewVisitUser;
import Model.Update.MLikeUser;
import Model.Update.UpdateVVuLC;
import Other.Str;
import Page.Response;
import bardiademon.Controller.CJSON;
import Controller.CController;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.Log;

// VLC => View Like Comment
@bardiademon
public class CUpdateVLC implements Controller
{

    public static final String UV = "uv";// UPDATE VISIT
    public static final String UL = "ul";// UPDATE LIKE
    public static final String UC = "uc";// UPDATE COMMENT

    private int idMemoir;

    private MLikeUser mLikeUser;
    private MRecordNewVisitUser mRecordNewVisitUser;

    private String updateLVC;

    @bardiademon
    public CUpdateVLC (String JsonRequest , String UpdateVLC)
    {
        if (CController.User.IsOkJson (JsonRequest , GetCJsonValue ()))
        {
            this.updateLVC = UpdateVLC;
            RunClass ();
        }
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        IsLinkMemoir isLinkMemoir = new IsLinkMemoir (CController.User.GetCJson ().getVString (Request.RequestUser.KJR.KJR_LikeUser.LINK_MEMOIR));
        if (isLinkMemoir.isFound ())
        {
            idMemoir = isLinkMemoir.getIdMemoir ();
            SendToModel ();
        }
        else
        {

            Object result;
            if (isLinkMemoir.IsThereAResult ()) result = isLinkMemoir.Result ();
            else result = ResultModel.PublicResult.PUBLIC_ERROR;
            Log.NL (Response.SC_BAD_REQUEST , result , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (result , Response.SC_BAD_REQUEST);
        }
    }

    @bardiademon
    @Override
    public CJSON.Value GetCJsonValue ()
    {
        CJSON.Value value = new CJSON.Value ();
        value.putLen (Request.RequestUser.KJR.KJR_LikeUser.LEN);
        value.putKeyValue (Str.ToArray (Str.ToStrArray (Request.RequestUser.KJR.KJR_LikeUser.LINK_MEMOIR) , Str.ToStrArray (CJSON.VD.IS_STRING)));
        return value;
    }

    @bardiademon
    @Override
    public void SendToModel ()
    {
        UpdateVVuLC updateVVuLC = new UpdateVVuLC (idMemoir);
        CheckLV checkLV = null;
        IsIdMemoirForIdUser isIdMemoirForIdUser = null;
        if (updateLVC.equals (UL))
        {
            checkLV = new CheckLV (Request.RequestUser.GetId () , idMemoir , CheckLV.L);
            updateVVuLC.setUpdateLike (!checkLV.isLV ());
        }
        else if (updateLVC.equals (UV))
        {
            isIdMemoirForIdUser = new IsIdMemoirForIdUser (idMemoir);
            if (isIdMemoirForIdUser.is ()) return;
            else
            {
                updateVVuLC.setUpdateVisit (true);
                checkLV = new CheckLV (Request.RequestUser.GetId () , idMemoir , CheckLV.V);
                updateVVuLC.setUpdateVisitUser ((!checkLV.isLV ()));
            }
        }
        updateVVuLC.apply ();
        if (updateVVuLC.Result ().equals (ResultModel.PublicResult.IS_OK))
        {
            switch (updateLVC)
            {
                case UL:
                    mLikeUser = new MLikeUser (idMemoir);
                    break;
                case UC:
                    break;
                case UV:
                {
                    assert checkLV != null && isIdMemoirForIdUser != null;
                    if (!isIdMemoirForIdUser.is () && !checkLV.isLV ())
                        mRecordNewVisitUser = new MRecordNewVisitUser (idMemoir);
                }
                break;
            }
            GetResultFromModel ();
        }
        else
        {
            Log.NL (Response.SC_BAD_REQUEST , ResultModel.PublicResult.PUBLIC_ERROR , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (ResultModel.PublicResult.PUBLIC_ERROR , Response.SC_BAD_REQUEST);
        }
    }

    @bardiademon
    @Override
    public void GetResultFromModel ()
    {
        boolean isThereAResult = false;
        Object result = null;
        switch (updateLVC)
        {
            case UL:
                if ((isThereAResult = mLikeUser.IsThereAResult ())) result = mLikeUser.Result ();
                break;
            case UC:
                break;
            case UV:
                if ((isThereAResult = mRecordNewVisitUser.IsThereAResult ()))
                    result = mRecordNewVisitUser.Result ();
                break;
        }
        if (isThereAResult)
        {
            Log.NL (Response.SC_OK , result , Thread.currentThread ().getStackTrace () , "");
            Controller.SetResult (result , Response.SC_OK);
        }
    }
}
