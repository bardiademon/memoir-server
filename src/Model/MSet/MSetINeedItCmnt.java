package Model.MSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Model.Check.MCheckIsInINeedCmnt;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

@bardiademon
public class MSetINeedItCmnt implements Model
{
    private int idUser, idComment;

    private Connection connection;
    private Statement statement;

    private boolean set;

    private boolean isThereAResult;
    private Object result;

    @bardiademon
    public MSetINeedItCmnt (int IdUser , int IdComment)
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
        MCheckIsInINeedCmnt mCheckIsInINeedCmnt = new MCheckIsInINeedCmnt (idUser , idComment);
        if (mCheckIsInINeedCmnt.IsThereAResult ())
        {
            int firstCheck = (int) mCheckIsInINeedCmnt.Result ();
            set = (mCheckIsInINeedCmnt.Result ().equals (ResultModel.PublicResult.NOT_FOUND));
            statement = connection.createStatement ();
            statement.execute (MakeQuery ());
            CloseConnectionDb ();

            mCheckIsInINeedCmnt.RunClass ();
            if (mCheckIsInINeedCmnt.IsThereAResult ())
            {
                if (firstCheck != (int) mCheckIsInINeedCmnt.Result ())
                {
                    if (set) SetResult (ResultModel.PublicResult.SET);
                    else SetResult (ResultModel.PublicResult.UNSET);
                    return;
                }
            }
        }
        SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
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
        if (set)
            return String.format ("INSERT INTO `%s` VALUES (null,%d,%d);" ,
                    InfoDatabase.TINeedItCmnt.NT , idUser , idComment);
        else
            return String.format ("DELETE FROM `%s` WHERE `%s`=%d AND `%s`=%d;" ,
                    InfoDatabase.TINeedItCmnt.NT ,
                    InfoDatabase.TINeedItCmnt.ID_USER , idUser ,
                    InfoDatabase.TINeedItCmnt.ID_COMMENT , idComment);
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
