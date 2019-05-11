package Model.Get;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Controller.Request;
import Interface.ResultModel;
import Interface.ResultModel.KJR.KJRGetComment;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.MakeJson;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

@bardiademon
public class MGetComment implements Model
{

    private int idMemoir;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private Object result;
    private boolean isThereAResult;

    private JSONObject jsonInfoUser;
    private List <Integer> ids;

    private int lenGet;

    @bardiademon
    public MGetComment (int IdMemoir)
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
        resultSet = statement.executeQuery (MakeQuery ());
        lenGet = HandlerDb.GetCountSelectedRow (resultSet);
        if (lenGet > 0 && resultSet.first ())
        {
            JSONObject allInfo = new JSONObject ();
            MakeJson makeJson;
            String comment, time;
            int idUser, idComment;
            int counter = 0;
            do
            {
                makeJson = new MakeJson ();

                idComment = resultSet.getInt (InfoDatabase.TComment.ID);
                idUser = resultSet.getInt (InfoDatabase.TComment.ID_USER);
                comment = resultSet.getString (InfoDatabase.TComment.TXT_COMMENT);
                time = resultSet.getString (InfoDatabase.TComment.TIME);

                makeJson.put (KJRGetComment.ID_USER , idUser);
                makeJson.put (KJRGetComment.ID , idComment);
                makeJson.put (KJRGetComment.TXT_COMMENT , comment);
                makeJson.put (KJRGetComment.TIME , time);
                getInfoUser (idUser);

                try
                {
                    allInfo.put (String.valueOf (counter++) , makeJson.apply ());
                }
                catch (JSONException e)
                {
                    UsefulLog.ForJSONException (Thread.currentThread ().getStackTrace () , e);
                    SetResult (ResultModel.PublicResult.NOT_FOUND);
                    return;
                }
            }
            while (resultSet.next ());
            allInfo.put (KJRGetComment.JSON_INFO_USER , jsonInfoUser);
            SetResult (allInfo);
            System.gc ();
        }
        else SetResult (ResultModel.PublicResult.NOT_FOUND);
    }


    @bardiademon
    private void getInfoUser (int idUser) throws SQLException
    {
        Statement statementGetUsername;
        ResultSet resultSetGetUsername;
        if (jsonInfoUser == null) jsonInfoUser = new JSONObject ();
        if (ids == null) ids = new ArrayList <> ();

        Object[] arrId = ids.toArray ();
        Arrays.sort (arrId);
        int i = Arrays.binarySearch (arrId , idUser);
        if (!(i >= 0) || !(i <= ids.size ()))
        {
            MakeJson makeJson = new MakeJson ();
            ids.add (idUser);
            statementGetUsername = connection.createStatement ();
            resultSetGetUsername = statementGetUsername.executeQuery (makeQuery2 (idUser));
            String username, picUser;
            if (resultSetGetUsername.first ())
            {
                username = resultSetGetUsername.getString (InfoDatabase.TAccount.USERNAME);
                picUser = resultSetGetUsername.getString (InfoDatabase.TAccount.NAME_PIC);

                picUser = new GetPictureProfile (picUser).getLinkPic ();

                makeJson.put (KJRGetComment.USERNAME , username);
                makeJson.put (KJRGetComment.PIC , picUser);

                jsonInfoUser.put (String.valueOf (idUser) , makeJson.apply ());
            }
            resultSetGetUsername.close ();
            statementGetUsername.close ();
        }
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , resultSet);
    }

    @bardiademon
    private String makeQuery2 (int idUser)
    {
        return String.format ("SELECT `%s`,`%s` FROM `%s` WHERE `%s`=%d" , InfoDatabase.TAccount.USERNAME , InfoDatabase.TAccount.NAME_PIC , InfoDatabase.TAccount.NT , InfoDatabase.TComment.ID , idUser);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        return String.format ("SELECT * FROM `%s` WHERE `%s`=%d" , InfoDatabase.TComment.NT , InfoDatabase.TComment.ID_MEMOIR , idMemoir);
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

    @Override
    public Object Result ()
    {
        return result;
    }

    @bardiademon
    public boolean isFound ()
    {
        return (isThereAResult && !(Result ().equals (ResultModel.PublicResult.NOT_FOUND)));
    }

    @bardiademon
    public int getLenGet ()
    {
        return lenGet;
    }
}
