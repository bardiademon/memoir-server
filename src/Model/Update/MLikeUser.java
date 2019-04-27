package Model.Update;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Model.Check.CheckLV;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase.TLike;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

@bardiademon
public class MLikeUser implements Model
{

    private boolean isThereAResult;
    private Object result;

    private int idUser, idMemoir;
    private Connection connection;
    private Statement statement;
    private CheckLV checkLV;

    @bardiademon
    public MLikeUser (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.idMemoir = IdMemoir;
            this.idUser = Request.RequestUser.GetId ();
            checkLV = new CheckLV (idUser , idMemoir , CheckLV.L);
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
        return HandlerDb.CheckConnection ((connection = HandlerDb.GetConnection ()));
    }

    @bardiademon
    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        statement = connection.createStatement ();

        assert checkLV != null;
        if (checkLV.isLV ()) statement.execute (makeQueryUnLike ());
        else statement.execute (MakeQuery ());

        SetResult (ResultModel.PublicResult.IS_OK);
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement);
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        return String.format ("INSERT INTO `%s` VALUES (null,%s,%s)" , TLike.NT , idMemoir , idUser);
    }

    @bardiademon
    private String makeQueryUnLike ()
    {
        return String.format ("DELETE FROM `%s` WHERE `%s`=%s AND `%s`=%s" , TLike.NT , TLike.ID_MEMOIR , idMemoir , TLike.ID_USER , idUser);
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
