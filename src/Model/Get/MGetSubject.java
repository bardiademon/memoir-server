package Model.Get;

import org.json.JSONException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.MakeJson;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;

public class MGetSubject implements Model
{

    private boolean isThereAResult;
    private Object result;


    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;

    {
        RunClass ();
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
        {
            MakeJson makeJson = new MakeJson ();
            int counter = 0;
            do
                makeJson.putUtf8 (String.valueOf (counter++) , resultSet.getString (InfoDatabase.TSubject.NAME));
            while (resultSet.next ());
            MakeJson makeJsonFinal = new MakeJson ();
            try
            {
                makeJsonFinal.put (ResultModel.KJR.KJRSubject.NAME , makeJson.getJsonString ());
                SetResult (makeJsonFinal.getJsonString ());
            }
            catch (JSONException e)
            {
                SetResult (ResultModel.PublicResult.NOT_FOUND);
            }
        }
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultSet);
    }

    @Override
    public String MakeQuery ()
    {
        return String.format ("SELECT `%s` FROM `%s`" , InfoDatabase.TSubject.NAME , InfoDatabase.TSubject.NT);
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
