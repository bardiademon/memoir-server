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
import bardiademon.Interface.bardiademon;
import bardiademon.Other.SQL;
import bardiademon.Other.Str;

@bardiademon
public class GetSubject implements Model
{

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private String name = null;

    private Object result;
    private boolean isThereAResult;
    private int id = 0;

    @bardiademon
    public GetSubject (String Name)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.name = Name;
            RunClass ();
        }
    }

    @bardiademon
    public GetSubject (int Id)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.id = Id;
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
        String query;
        if (isGetName ()) query = MakeQuery ();
        else query = makeQueryId ();
        System.out.println (query);
        resultSet = statement.executeQuery (query);
        if (HandlerDb.GetCountSelectedRow (resultSet) > 0 && resultSet.first ())
        {
            if (isGetName ())
            {
                id = resultSet.getInt (InfoDatabase.TSubject.ID);
                SetResult (id);
            }
            else
            {
                name = Str.DeCoder (resultSet.getString (InfoDatabase.TSubject.NAME));
                SetResult (name);
            }
        }
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    private boolean isGetName ()
    {
        return (id != 0 && name != null);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (InfoDatabase.TSubject.ID);
        select.setNameTable (InfoDatabase.TSubject.NT);
        select.putWhere (InfoDatabase.TSubject.NAME , name , null);
        return select.apply ();
    }

    @bardiademon
    private String makeQueryId ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (InfoDatabase.TSubject.NAME);
        select.setNameTable (InfoDatabase.TSubject.NT);
        select.putWhere (InfoDatabase.TSubject.ID , id , null);
        return select.apply ();
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultSet);
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

    public int getId ()
    {
        return id;
    }

    public String getName ()
    {
        return name;
    }

    public boolean isFound ()
    {
        return !result.equals (ResultModel.PublicResult.NOT_FOUND);
    }
}
