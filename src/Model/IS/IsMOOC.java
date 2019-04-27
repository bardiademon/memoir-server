package Model.IS;


// IsMOOC => Is M => Memoir ; O => Open ; O => Or ; C => Confirmation ; IsMemoirOpenOrConfirmation


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

//IsMOOC ==> M => Memoir ;  O => Open ; O => Or ; C => Confirmation ; OpenOrConfirmation

@bardiademon
public class IsMOOC implements Model
{
    private int idMemoir;

    private boolean isThereAResult;
    private Object result;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private boolean open, confirmation;

    @bardiademon
    public IsMOOC (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idMemoir = IdMemoir;
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

        resultSet = statement.executeQuery (makeQuery2 (InfoDatabase.TMemoirList.OPEN));
        open = isOOC (InfoDatabase.TMemoirList.OPEN);

        resultSet = statement.executeQuery (makeQuery2 (InfoDatabase.TMemoirList.CONFIRMATION));
        confirmation = isOOC (InfoDatabase.TMemoirList.CONFIRMATION);
    }

    private boolean isOOC (String OCC) throws SQLException
    {
        return ((HandlerDb.GetCountSelectedRow (resultSet) > 0 && resultSet.first () && resultSet.getInt (OCC) == InfoDatabase.TMemoirList.DV.IS_OPEN_OR_CONFIRMATION));
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        return null;
    }

    //  O => Open ; O => Or ; C => Confirmation ; OpenOrConfirmation
    @bardiademon
    private String makeQuery2 (String OCC)
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (OCC);
        select.setNameTable (InfoDatabase.TMemoirList.NT);
        select.putWhere (InfoDatabase.TMemoirList.ID , idMemoir , null);
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

    public boolean isOpen ()
    {
        return open;
    }

    public boolean isConfirmation ()
    {
        return confirmation;
    }
}
