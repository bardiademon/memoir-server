package Model.MRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

@bardiademon
public class MRecordNewVisitUser implements Model
{
    private int idMemoir, idUser;

    private boolean isThereAResult;
    private Object result;

    private Connection connection;
    private Statement statement;

    @bardiademon
    public MRecordNewVisitUser (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idUser = Request.RequestUser.GetId ();
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
            else SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLException (Thread.currentThread ().getStackTrace () , e);
            SetResult (ResultModel.CheckInfoLogin.PUBLIC_ERROR);
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
        SetResult (ResultModel.PublicResult.IS_OK);
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
        return String.format ("INSERT INTO `%s` VALUES (null,%d,%d)" , InfoDatabase.TVisitUser.NT , idUser , idMemoir);
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
