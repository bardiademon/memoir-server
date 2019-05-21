package Model;

import Interface.ResultModel;
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
public class CheckId implements Model
{
    // D=>Default
    public static final String D_ROW_NAME = InfoDatabase.PublicRow.ID;

    private String tableName, rowName;
    private Object valueCheck;
    private boolean isThereAResult;
    private Object result;
    private Connection connection;

    @bardiademon
    public CheckId (String TableName , String RowName , Object ValueCheck)
    {
        this.tableName = TableName;
        this.rowName = RowName;
        this.valueCheck = ValueCheck;
        RunClass ();
    }

    @bardiademon
    public CheckId (String TableName , Object ValueCheck)
    {
        this.tableName = TableName;
        this.rowName = InfoDatabase.PublicRow.ID;
        this.valueCheck = ValueCheck;
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
            else SetResult (ResultModel.PublicResult.NOT_FOUND);
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
        ResultSet resultSet = statement.executeQuery (MakeQuery ());
        if (HandlerDb.GetCountSelectedRow (resultSet) > 0)
            SetResult (ResultModel.PublicResult.FOUND);
        else
            SetResult (ResultModel.PublicResult.NOT_FOUND);
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
        SQL.Query.Select select = new SQL.Query.Select ();

        select.putSelect (rowName);
        select.setNameTable (tableName);
        select.putWhere (rowName , valueCheck , null);

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
        return (Result ().equals (ResultModel.PublicResult.FOUND));
    }
}
