package Model.Get;

import Controller.CController;
import Controller.Request;
import Interface.ResultModel;
import Interface.ResultModel.KJR.KJRGetMemoirUser;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase.TMemoirList;
import Other.MakeJson;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsModel;
import bardiademon.Other.Log;
import bardiademon.Other.SQL;
import bardiademon.Other.Str;

import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@bardiademon
@IsModel
public class MGetMemoirUser implements Model
{

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private int idMemoir;

    private boolean isThereAResult;
    private Object result;

    @bardiademon
    public MGetMemoirUser (int IdMemoir)
    {
        if (CController.ForModel.IsLogin ())
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
        return (HandlerDb.CheckConnection (connection = HandlerDb.GetConnection ()));
    }

    @bardiademon
    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        String query;
        if (idMemoir == Request.RequestUser.KJR.KJR__GetMemoirUser.DV.DV_ID__GET_ALL)
            query = MakeQuery ();
        else
        {
            query = null;
            // Code For Get With Id
        }

        statement = connection.createStatement ();
        resultSet = statement.executeQuery (query);
        if (HandlerDb.GetCountSelectedRow (resultSet) > 0)
        {
            JSONArray jsonArray = new JSONArray ();
            MakeJson makeJson;
            GetSubject getSubject;
            while (resultSet.next ())
            {
                makeJson = new MakeJson ();

                int idSubject = resultSet.getInt (TMemoirList.SUBJECT);

                getSubject = new GetSubject (idSubject);
                if (getSubject.isFound ())
                {
                    makeJson.put (KJRGetMemoirUser.ID , resultSet.getInt (TMemoirList.ID));
                    makeJson.put (KJRGetMemoirUser.NAME , resultSet.getString (TMemoirList.NAME));
                    makeJson.put (KJRGetMemoirUser.SUBJECT , Str.EnCoder (getSubject.getName ()));
                    makeJson.put (KJRGetMemoirUser.DATE , resultSet.getString (TMemoirList.DATE));
                    makeJson.put (KJRGetMemoirUser.CONFIRMATION , (resultSet.getInt (TMemoirList.CONFIRMATION) == TMemoirList.DV.IS_ACCEPT));
                    makeJson.put (KJRGetMemoirUser.LINK , resultSet.getString (TMemoirList.LINK));
                    makeJson.put (KJRGetMemoirUser.OPEN , resultSet.getBoolean (TMemoirList.OPEN));
                }
                else continue;
                try
                {
                    jsonArray.put (makeJson.getJsonString ());
                }
                catch (JSONException e)
                {
                    Log.NL (HttpServletResponse.SC_INTERNAL_SERVER_ERROR , ResultModel.GetMemoirUser.SC200.PUBLIC_ERROR , Thread.currentThread ().getStackTrace () , e.getMessage ());
                    SetResult (ResultModel.GetMemoirUser.SC200.NOT_FOUND);
                    return;
                }
            }
            SetResult (jsonArray.toString ());
        }
        else
        {
            Log.NL (HttpServletResponse.SC_OK , ResultModel.GetMemoirUser.SC200.NOT_FOUND , Thread.currentThread ().getStackTrace () , "");
            SetResult (ResultModel.GetMemoirUser.SC200.NOT_FOUND);
        }
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();

        select.setNameTable (TMemoirList.NT);
        select.putSelect (TMemoirList.ID , TMemoirList.NAME , TMemoirList.SUBJECT , TMemoirList.DATE , TMemoirList.CONFIRMATION , TMemoirList.LINK , TMemoirList.OPEN);
        select.putWhere (TMemoirList.ID_USER , Request.RequestUser.GetId () , null);

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
}
