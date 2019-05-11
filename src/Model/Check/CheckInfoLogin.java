package Model.Check;

import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Model.IS.IsActiveAccount;
import Other.Str;
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
public class CheckInfoLogin implements Model
{
    private Connection connection;

    private String username, password, serial;

    private boolean isThereAResult;
    private Object result = ResultModel.CheckInfoLogin.INVALID;

    private int id;

    @bardiademon
    public CheckInfoLogin (String username , String password , String serial)
    {
        this.username = username;
        this.password = password;
        this.serial = serial;
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
            SetResult (ResultModel.CheckInfoLogin.PUBLIC_ERROR);
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
        CheckSerialNumber checkSerialNumber = new CheckSerialNumber (serial , true);
        if (checkSerialNumber.isFound ())
        {
            Statement statement = connection.createStatement ();
            ResultSet checkInfo = statement.executeQuery (MakeQuery ());
            if (checkInfo != null && HandlerDb.GetCountSelectedRow (checkInfo) > 0 && checkInfo.next ())
            {
                id = checkInfo.getInt (InfoDatabase.TAccount.ID);
                if (new IsActiveAccount (id).isActive ())
                    SetResult (ResultModel.CheckInfoLogin.VALID);
                else SetResult (ResultModel.CheckInfoLogin.DEACTIVE_ACCOUNT);
            }
            else SetResult (ResultModel.CheckInfoLogin.INVALID);
        }
        else SetResult (checkSerialNumber.Result ());
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

        select.setNameTable (InfoDatabase.TAccount.NT);
        select.putSelect (InfoDatabase.TAccount.ID);
        select.putWhere
                (
                        Str.ToArray (InfoDatabase.TAccount.USERNAME , username , "AND") ,
                        Str.ToArray (InfoDatabase.TAccount.PASSWORD , password , "AND") ,
                        Str.ToArray (InfoDatabase.TAccount.SERIAL , serial , null)
                );
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
    public boolean isValid ()
    {
        return (IsThereAResult () && Result ().equals (ResultModel.CheckInfoLogin.VALID));
    }

    @bardiademon
    public int getId ()
    {
        return id;
    }
}
