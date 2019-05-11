package Model.Delete;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Model.MSet.MSetINeedItCmnt;
import Model.Update.UpdateVVuLC;
import Other.Useful.UsefulLog;
import Model.CheckId;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

@bardiademon
public class MDeleteComment implements Model
{
    private int idMemoir, idComment;

    private Connection connection;
    private Statement statement;

    private boolean isThereAResult;
    private Object result;

    @bardiademon
    public MDeleteComment (int IdMemoir , int IdComment)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idMemoir = IdMemoir;
            this.idComment = IdComment;
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
            SetResult (ResultModel.DelMemoirMemoir.SC200.NOT_DELETED);
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
        return (HandlerDb.CheckConnection ((connection = HandlerDb.GetConnection ())));
    }

    @bardiademon
    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        statement = connection.createStatement ();
        statement.execute (MakeQuery ());
        CloseConnectionDb ();
        CheckId checkId = new CheckId (InfoDatabase.TComment.NT , InfoDatabase.TComment.ID , idComment);
        if (checkId.isFound ()) SetResult (ResultModel.PublicResult.ForLog.ERROR);
        else
        {
            UpdateVVuLC updateVVuLC = new UpdateVVuLC (idMemoir);
            updateVVuLC.setUpdateComment (false);
            updateVVuLC.apply ();
            new MSetINeedItCmnt (Request.RequestUser.GetId () , idComment);
            SetResult (ResultModel.PublicResult.IS_OK);
            return;
        }
        SetResult (ResultModel.PublicResult.NOT_OK);
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        return String.format ("DELETE FROM `%s` WHERE `%s`=%d AND `%s`=%d AND `%s`=%d" ,
                InfoDatabase.TComment.NT ,
                InfoDatabase.TComment.ID , idComment ,
                InfoDatabase.TComment.ID_MEMOIR , idMemoir ,
                InfoDatabase.TComment.ID_USER , Request.RequestUser.GetId ()
        );
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
