package Model.Delete;

import Controller.CController;
import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@bardiademon
public class MDeleteMemoirUser implements Model
{

    private Connection connection;
    private Statement statement;

    private int idMemoir;

    private boolean isThereAResult;
    private Object result;

    @bardiademon
    public MDeleteMemoirUser (int IdMemoir)
    {
        if (CController.ForModel.IsLogin ())
        {
            this.idMemoir = IdMemoir;
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
        SetResult (ResultModel.DelMemoirMemoir.SC200.DELETED);
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
        return String.format ("DELETE FROM `%s` WHERE `%s`=%d" , InfoDatabase.TMemoirList.NT , InfoDatabase.TMemoirList.ID , idMemoir);
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
