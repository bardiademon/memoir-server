package Model.Get;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Interface.ResultModel.KJR.KJRGetComment;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.MakeJson;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;

public class MGetComment implements Model
{

    private int idMemoir;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private Object result;
    private boolean isThereAResult;

    public MGetComment (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idMemoir = IdMemoir;
            RunClass ();
        }
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
            JSONArray allInfo = new JSONArray ();
            MakeJson makeJson;
            String comment, time;
            int idUser, idComment;
            String username, picUser;
            ResultSet resultSetGetUsername;
            do
            {
                makeJson = new MakeJson ();

                idComment = resultSet.getInt (InfoDatabase.TComment.ID);
                idUser = resultSet.getInt (InfoDatabase.TComment.ID_USER);
                comment = resultSet.getString (InfoDatabase.TComment.TXT_COMMENT);
                time = resultSet.getString (InfoDatabase.TComment.TIME);

                resultSetGetUsername = statement.executeQuery (makeQuery2 (idUser));
                username = resultSetGetUsername.getString (InfoDatabase.TAccount.USERNAME);
                picUser = resultSetGetUsername.getString (InfoDatabase.TAccount.NAME_PIC);
                resultSetGetUsername.close ();

                picUser = new GetPictureProfile (picUser).getLinkPic ();

                makeJson.put (KJRGetComment.ID , idComment);
                makeJson.put (KJRGetComment.TXT_COMMENT , comment);
                makeJson.put (KJRGetComment.TIME , time);
                makeJson.put (KJRGetComment.USERNAME , username);
                makeJson.put (KJRGetComment.PIC , picUser);

                try
                {
                    allInfo.put (makeJson.getJsonString ());
                }
                catch (JSONException e)
                {
                    UsefulLog.ForJSONException (Thread.currentThread ().getStackTrace () , e);
                    SetResult (ResultModel.PublicResult.NOT_FOUND);
                    return;
                }
            }
            while (resultSet.next ());
            SetResult (allInfo.toString ());
            System.gc ();
        }
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }

    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultSet);
    }

    private String makeQuery2 (int idUser)
    {
        return String.format ("SELECT `%s`,`%s` FROM `%s` WHERE `%s`=%d" , InfoDatabase.TAccount.USERNAME , InfoDatabase.TAccount.NAME_PIC , InfoDatabase.TAccount.NT , InfoDatabase.TComment.ID , idUser);
    }

    @Override
    public String MakeQuery ()
    {
        return String.format ("SELECT * FROM `%s` WHERE `%s`=%d" , InfoDatabase.TComment.NT , InfoDatabase.TComment.ID_MEMOIR , idMemoir);
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
