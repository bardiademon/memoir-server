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

// GetNumberOfVVuLC => Get V => Visit ; Vu => Visit user ; L => Like ; C => Comment

@bardiademon
public class GetNumberOfVVuLC implements Model
{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private int idMemoir;

    private int visit, visitUser, like, comment;
    private boolean isThereAResult;
    private Object result;

    public GetNumberOfVVuLC (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ())
        {
            if (IdMemoir > 0)
            {
                this.idMemoir = IdMemoir;
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
        resultSet = statement.executeQuery (MakeQuery ());
        if (HandlerDb.GetCountSelectedRow (resultSet) > 0 && resultSet.first ())
        {
            visit = resultSet.getInt (InfoDatabase.TMemoirList.VISIT);
            visitUser = resultSet.getInt (InfoDatabase.TMemoirList.VISIT_USER);
            like = resultSet.getInt (InfoDatabase.TMemoirList.NUMBER_OF_LIKE);
            comment = resultSet.getInt (InfoDatabase.TMemoirList.NUMBER_OF_COMMENT);
            SetResult (ResultModel.PublicResult.FOUND);
        }
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect (
                InfoDatabase.TMemoirList.VISIT ,
                InfoDatabase.TMemoirList.VISIT_USER ,
                InfoDatabase.TMemoirList.NUMBER_OF_LIKE ,
                InfoDatabase.TMemoirList.NUMBER_OF_COMMENT
        );
        select.setNameTable (InfoDatabase.TMemoirList.NT);
        select.putWhere (InfoDatabase.TMemoirList.ID , idMemoir , "AND");
        select.putWhere (InfoDatabase.TMemoirList.OPEN , 1 , "AND");
        select.putWhere (InfoDatabase.TMemoirList.CONFIRMATION , 1 , null);
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

    @bardiademon
    public boolean isFound ()
    {
        return (IsThereAResult () && Result ().equals (ResultModel.PublicResult.FOUND));
    }

    @bardiademon
    public int getVisit ()
    {
        return visit;
    }

    @bardiademon
    public int getVisitUser ()
    {
        return visitUser;
    }

    @bardiademon
    public int getLike ()
    {
        return like;
    }

    @bardiademon
    public int getComment ()
    {
        return comment;
    }
}
