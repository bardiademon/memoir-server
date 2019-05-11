package Model.Check;

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
public class MCheckIsInINeedCmnt implements Model
{
    private int idUser, idComment;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private Object result;
    private boolean isThereAResult;

    @bardiademon
    public MCheckIsInINeedCmnt (int IdUser , int IdComment)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idUser = IdUser;
            this.idComment = IdComment;
            RunClass ();
        }
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
        if (HandlerDb.GetCountSelectedRow (resultSet) > 0)
            SetResult (ResultModel.PublicResult.FOUND);
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.setNameTable (InfoDatabase.TINeedItCmnt.NT);
        select.putSelect (InfoDatabase.TINeedItCmnt.ID);
        select.putWhere (InfoDatabase.TINeedItCmnt.ID_USER , idUser , "AND");
        select.putWhere (InfoDatabase.TINeedItCmnt.ID_COMMENT , idComment , null);
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
}
