package Model.IS;

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

public class IsIdMemoirForIdUser implements Model
{

    private int idMemoir;
    private int idUser;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private boolean isThereAResult;
    private Object result;
    private boolean is;

    public IsIdMemoirForIdUser (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idMemoir = IdMemoir;
            this.idUser = Request.RequestUser.GetId ();
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
                SetResult (ResultModel.PublicResult.NOT_FOUND);
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
        is = (HandlerDb.GetCountSelectedRow (resultSet) > 0 && resultSet.first () && (resultSet.getInt (InfoDatabase.TMemoirList.ID_USER)) == idUser);
        if (is) SetResult (ResultModel.PublicResult.FOUND);
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (InfoDatabase.TMemoirList.ID_USER);
        select.setNameTable (InfoDatabase.TMemoirList.NT);
        select.putWhere (InfoDatabase.TMemoirList.ID , idMemoir , "AND");
        select.putWhere (InfoDatabase.TMemoirList.ID_USER , idUser , null);
        return select.apply ();
    }

    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultSet);
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

    public boolean is ()
    {
        return is;
    }
}
