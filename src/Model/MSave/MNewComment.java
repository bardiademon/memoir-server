package Model.MSave;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Model.Get.MGetComment;
import Model.Update.UpdateVVuLC;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.GetTimeStamp;
import bardiademon.Other.Str;

public class MNewComment implements Model
{
    private int idUserSendComment, idMemoir;
    private String textComment;

    private Connection connection;
    private Statement statement;

    private Object result;
    private boolean isThereAResult;

    public MNewComment (int IdUserSendComment , int IdMemoir , String TextComment)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idUserSendComment = IdUserSendComment;
            this.idMemoir = IdMemoir;
            this.textComment = TextComment;
            RunClass ();
        }
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        try
        {
            if (GetConnection ()) CommunicationWithTheDatabase ();
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLException (Thread.currentThread ().getStackTrace () , e);
            SetResult (ResultModel.RecordNewMemoir.SC200.NOT_RECORDED);
        }
        finally
        {
            CloseConnectionDb ();
        }
    }

    @bardiademon
    @Override
    public boolean GetConnection () throws SQLException
    {
        return HandlerDb.CheckConnection ((connection = HandlerDb.GetConnection ()));
    }

    @bardiademon
    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        statement = connection.createStatement ();
        MGetComment mGetComment = new MGetComment (idMemoir);
        int lenBeforeAddComment = 0;
        if (mGetComment.isFound ()) lenBeforeAddComment = mGetComment.getLenGet ();
        statement.execute (MakeQuery ());
        CloseConnectionDb ();
        mGetComment.RunClass ();
        if ((lenBeforeAddComment == 0 && mGetComment.isFound ()) || (lenBeforeAddComment < mGetComment.getLenGet ()))
        {
            UpdateVVuLC updateVVuLC = new UpdateVVuLC (idMemoir);
            updateVVuLC.setUpdateComment (true);
            updateVVuLC.apply ();
            SetResult (ResultModel.PublicResult.IS_OK);
        }
        else SetResult (ResultModel.PublicResult.NOT_OK);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        return String.format ("INSERT INTO `%s` VALUES (null,%d,%d,'%s','%s')" , InfoDatabase.TComment.NT , idUserSendComment , idMemoir , Str.EnCoder (textComment) , GetTimeStamp.get ());
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement);
    }

    @bardiademon
    @Override
    public void SetResult (Object Result)
    {
        this.result = Result;
        isThereAResult = true;
    }

    @bardiademon
    @Override
    public boolean IsThereAResult ()
    {
        return isThereAResult;
    }

    @bardiademon
    @Override
    public Object Result ()
    {
        return result;
    }
}
