package Model.Get;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Other.SQL;

public class MGetTextMemoir implements Model
{

    private int idMemoir;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private Object result;
    private boolean isThereAResult;


    public MGetTextMemoir (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idMemoir = IdMemoir;
            RunClass ();
        }
    }

    @Override
    public void RunClass ()
    {
        try
        {
            if (GetConnection ()) CommunicationWithTheDatabase ();
            else
            {
                UsefulLog.CannotGetConnection (Thread.currentThread ().getStackTrace ());
                SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
            }
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLException (Thread.currentThread ().getStackTrace () , e);
            SetResult (ResultModel.PublicResult.NOT_FOUND);
        }
        finally
        {
            CloseConnectionDb ();
        }
    }

    @Override
    public boolean GetConnection () throws SQLException
    {
        return (HandlerDb.CheckConnection ((connection = HandlerDb.GetConnection ())));
    }

    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        statement = connection.createStatement ();
        resultSet = statement.executeQuery (MakeQuery ());
        if (HandlerDb.GetCountSelectedRow (resultSet) > 0 && resultSet.first ())
            SetResult (resultSet.getString (InfoDatabase.TMemoirList.TEXT));
    }

    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultSet);
    }

    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (InfoDatabase.TMemoirList.TEXT);
        select.setNameTable (InfoDatabase.TMemoirList.NT);
        select.putWhere (InfoDatabase.TMemoirList.ID , idMemoir , null);
        return select.apply ();
    }

    @Override
    public void SetResult (Object Result)
    {
        this.result = Result;
        isThereAResult = true;
    }

    @Override
    public boolean IsThereAResult ()
    {
        return isThereAResult;
    }

    @Override
    public Object Result ()
    {
        return result;
    }
}
