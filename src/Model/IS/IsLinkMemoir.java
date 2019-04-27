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
import bardiademon.Interface.bardiademon;
import bardiademon.Other.SQL;

@bardiademon
public class IsLinkMemoir implements Model
{

    private String link;

    private int idMemoir;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private Object result;
    private boolean isThereAResult;

    @bardiademon
    public IsLinkMemoir (String Link)
    {
        if (Request.RequestUser.IsLogin ())
        {
            if (Link != null && !Link.isEmpty ())
            {
                this.link = Link;
                RunClass ();
            }
            else SetResult (ResultModel.PublicResult.NOT_FOUND);
        }
    }

    @bardiademon
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
        resultSet = statement.executeQuery (MakeQuery ());
        if (HandlerDb.GetCountSelectedRow (resultSet) > 0 && resultSet.first ())
        {
            idMemoir = resultSet.getInt (InfoDatabase.TMemoirList.ID);
            SetResult (ResultModel.PublicResult.FOUND);
        }
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultSet);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (InfoDatabase.TMemoirList.ID);
        select.setNameTable (InfoDatabase.TMemoirList.NT);
        select.putWhere (InfoDatabase.TMemoirList.LINK , link , null);
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
    public boolean isFound ()
    {
        return (IsThereAResult () && Result ().toString ().equals (String.valueOf (ResultModel.PublicResult.FOUND)));
    }

    @bardiademon
    public int getIdMemoir ()
    {
        return idMemoir;
    }
}
