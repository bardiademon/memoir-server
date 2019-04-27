package Model.IS;

import Interface.ResultModel;
import Model.CheckId;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.Useful.UsefulLog;
import bardiademon.Interface.IsModel;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.SQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@bardiademon
@IsModel
public class IsActiveAccount implements Model
{
    private int id;

    private Connection connection;
    private Statement statement;
    private ResultSet resultCheck;

    private boolean isThereAResult;
    private Object result;

    @bardiademon
    public IsActiveAccount (int id)
    {
        if ((this.id = id) > 0) RunClass ();
        else SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        try
        {
            if (checkId () && GetConnection ())
            {
                CommunicationWithTheDatabase ();
                CloseConnectionDb ();
            }
            else SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLException (Thread.currentThread ().getStackTrace () , e);
            SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
        }
    }

    @bardiademon
    private boolean checkId ()
    {
        return (new CheckId (InfoDatabase.TAccount.NT , InfoDatabase.TAccount.ID , id).isFound ());
    }

    @bardiademon
    @Override
    public boolean GetConnection () throws SQLException
    {
        connection = HandlerDb.GetConnection ();
        return HandlerDb.CheckConnection (connection);
    }

    @bardiademon
    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        statement = connection.createStatement ();
        resultCheck = statement.executeQuery (MakeQuery ());
        SetResult (((HandlerDb.GetCountSelectedRow (resultCheck) > 0) ? ResultModel.PublicResult.IS_OK : ResultModel.PublicResult.NOT_OK));
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultCheck);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (InfoDatabase.TAccount.STATUS);
        select.setNameTable (InfoDatabase.TAccount.NT);
        select.putWhere (InfoDatabase.TAccount.ID , id , "and");
        select.putWhere (InfoDatabase.TAccount.STATUS , InfoDatabase.TAccount.DV.STATUS_ACTIVE , null);

        return select.apply ();
    }

    @bardiademon
    @Override
    public void SetResult (Object Result)
    {
        isThereAResult = true;
        this.result = Result;
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

    @bardiademon
    public boolean isActive ()
    {
        return (isThereAResult && (Result ()).equals (ResultModel.PublicResult.IS_OK));
    }
}
