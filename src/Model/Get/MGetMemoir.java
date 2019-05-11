package Model.Get;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Interface.ResultModel.KJR.KJRGetMemoir;
import Model.Check.CheckLV;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase.TMemoirList;
import Model.IS.IsIdMemoirForIdUser;
import Other.MakeJson;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;
import bardiademon.Other.SQL;
import bardiademon.Other.Str;

@bardiademon
public class MGetMemoir implements Model
{

    private int idMemoir;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private boolean isThereAResult;
    private Object result;

    private GetNumberOfVVuLC getNumberOfVVuLC;

    @bardiademon
    public MGetMemoir (int IdMemoir)
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
        getNumberOfVVuLC = new GetNumberOfVVuLC (idMemoir);
        if (getNumberOfVVuLC.isFound ())
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
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
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
            MakeJson makeJson = new MakeJson ();
            try
            {
                String subject = new GetSubject (resultSet.getInt (TMemoirList.SUBJECT)).getName ();
                if (subject == null) throw new Exception ();
                makeJson.putUtf8 (KJRGetMemoir.NAME , Str.DeCoder (resultSet.getString (TMemoirList.NAME)));
                makeJson.putUtf8 (KJRGetMemoir.SUBJECT , subject);
                makeJson.putUtf8 (KJRGetMemoir.TEXT , Str.DeCoder (resultSet.getString (TMemoirList.TEXT)));
                makeJson.put (KJRGetMemoir.DATE , resultSet.getString (TMemoirList.DATE));
                makeJson.put (KJRGetMemoir.TIME_RECORD , resultSet.getString (TMemoirList.TIME_RECORD));
                makeJson.put (KJRGetMemoir.TIME_LAST_EDIT , resultSet.getString (TMemoirList.TIME_LAST_EDIT));
                makeJson.put (KJRGetMemoir.VISIT , getNumberOfVVuLC.getVisit ());
                makeJson.put (KJRGetMemoir.VISIT_USER , getNumberOfVVuLC.getVisitUser ());
                makeJson.put (KJRGetMemoir.LIKE , getNumberOfVVuLC.getLike ());
                makeJson.put (KJRGetMemoir.COMMENT , getNumberOfVVuLC.getComment ());
                makeJson.put (KJRGetMemoir.LIKED , new CheckLV (Request.RequestUser.GetId () , idMemoir , CheckLV.L).isLV ());
                IsIdMemoirForIdUser isIdMemoirForIdUser = new IsIdMemoirForIdUser (idMemoir);
                if (isIdMemoirForIdUser.is ()) makeJson.put (KJRGetMemoir.IS_MEMOIR_FOR_YOU , 1);
                SetResult (makeJson.getJsonString ());
                return;
            }
            catch (Exception ignored)
            {
            }
        }
        SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        SQL.Query.Select select = new SQL.Query.Select ();
        select.putSelect
                (
                        TMemoirList.NAME ,
                        TMemoirList.SUBJECT ,
                        TMemoirList.TEXT ,
                        TMemoirList.DATE ,
                        TMemoirList.TIME_RECORD ,
                        TMemoirList.TIME_LAST_EDIT
                );
        select.setNameTable (TMemoirList.NT);
        select.putWhere (TMemoirList.ID , idMemoir , "AND");
        select.putWhere (TMemoirList.OPEN , TMemoirList.DV.IS_OPEN_OR_CONFIRMATION , "AND");
        select.putWhere (TMemoirList.CONFIRMATION , TMemoirList.DV.IS_OPEN_OR_CONFIRMATION , null);
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
