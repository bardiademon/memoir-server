package Model;

import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.Useful.UsefulLog;
import bardiademon.Interface.IsModel;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@bardiademon
@IsModel
public class CheckRequest implements Model
{
    private String nameRequest;
    private Connection connection;
    private boolean isThereAResult = false;
    private Object result;
    private int pageCode;

    @bardiademon
    public CheckRequest (String nameRequest , int pageCode)
    {
        this.nameRequest = nameRequest;
        this.pageCode = pageCode;
        RunClass ();
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        try
        {
            if (GetConnection ())
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
    @Override
    public boolean GetConnection () throws SQLException
    {
        connection = HandlerDb.GetConnection ();
        return (connection != null && !connection.isClosed ());
    }

    @bardiademon
    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        Statement statement = connection.createStatement ();
        ResultSet foundRequest = statement.executeQuery (MakeQuery ());
        SetResult ((HandlerDb.GetCountSelectedRow (foundRequest) > 0) ? ResultModel.ResultCheckRequest.FOUND : ResultModel.ResultCheckRequest.NOT_FOUND);
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        return String.format ("SELECT `%s` FROM `%s` WHERE `%s`='%s' AND `%s`=%d"
                , InfoDatabase.TRequest.NR_ID , InfoDatabase.TRequest.NT , InfoDatabase.TRequest.NR_REQUEST , nameRequest , InfoDatabase.TRequest.NR_PAGE_CODE , pageCode);
    }

    @bardiademon
    @Override
    public void SetResult (Object Result)
    {
        isThereAResult = true;
        result = Result;
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
