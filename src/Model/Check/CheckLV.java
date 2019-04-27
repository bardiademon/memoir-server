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

// CheckLV => Check Like Visit
@bardiademon
public class CheckLV implements Model
{

    public static final String L = "L";// LIKE
    public static final String V = "V";// Visit

    private boolean isThereAResult;
    private Object result;

    private int idUser, idMemoir;

    private boolean liked;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private String LV;

    @bardiademon
    public CheckLV (int IdUser , int IdMemoir , String LV)
    {
        if (Request.RequestUser.IsLogin () && (LV.equals (L) || LV.equals (V)))
        {
            this.idUser = IdUser;
            this.idMemoir = IdMemoir;
            this.LV = LV;
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
            else SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLException (Thread.currentThread ().getStackTrace () , e);
            SetResult (ResultModel.CheckInfoLogin.PUBLIC_ERROR);
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
        liked = (HandlerDb.GetCountSelectedRow (resultSet) > 0);
        SetResult (ResultModel.PublicResult.IS_OK);
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
        select.putSelect (InfoDatabase.TLike.ID);

        if (LV.equals (L)) select.setNameTable (InfoDatabase.TLike.NT);
        else if (LV.equals (V)) select.setNameTable (InfoDatabase.TVisitUser.NT);

        select.putWhere (InfoDatabase.TLike.ID_MEMOIR , idMemoir , "AND");
        select.putWhere (InfoDatabase.TLike.ID_USER , idUser , null);
        return select.apply ();
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
    public boolean isLV ()
    {
        return liked;
    }
}
