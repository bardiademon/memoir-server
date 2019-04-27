package Model.Check;

import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import Model.Get.GetPictureProfile;
import Other.Useful.UsefulLog;
import bardiademon.Interface.IsModel;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@bardiademon
@IsModel
public class CheckSerialNumber implements Model
{
    private String serial;
    private Connection connection;
    private Statement statement;
    private ResultSet infoGet;

    private boolean isThereAResult;
    private Object result;
    private boolean found;
    private boolean justCheck;

    public CheckSerialNumber (String Serial)
    {
        this (Serial , false);
    }

    public CheckSerialNumber (String Serial , boolean JustCheck)
    {
        this.serial = Serial;
        this.justCheck = JustCheck;
        RunClass ();
    }

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

    @Override
    public boolean GetConnection () throws SQLException
    {
        connection = HandlerDb.GetConnection ();
        return (connection != null && !connection.isClosed ());
    }


    @Override
    public void CommunicationWithTheDatabase () throws SQLException
    {
        statement = connection.createStatement ();
        infoGet = statement.executeQuery (MakeQuery ());
        found = (HandlerDb.GetCountSelectedRow (infoGet) > 0 && infoGet.next ());
        if (isFound ())
        {
            if (justCheck) return;
            int status = infoGet.getInt (InfoDatabase.TAccount.STATUS);
            if (status == InfoDatabase.TAccount.DV.STATUS_ACTIVE)
            {
                String picture = infoGet.getString (InfoDatabase.TAccount.NAME_PIC);
                if (picture.equals (InfoDatabase.TAccount.DV.NAME_PIC__NO_PIC))
                    SetResult (ResultModel.CheckSerialNumber.ACTIVE_ACCOUNT);
                else
                    SetResult (getPic (picture));
            }
            else SetResult (ResultModel.CheckSerialNumber.DEACTIVE_ACCOUNT);
        }
        else SetResult (ResultModel.CheckSerialNumber.NOT_FOUND_SERIAL);
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement , infoGet);
    }

    private String getPic (String namePic)
    {
        return new GetPictureProfile (namePic).getLinkPic ();
    }

    @Override
    public String MakeQuery ()
    {
        return String.format ("SELECT `%s`,`%s` FROM `%s` WHERE `%s`='%s'" ,

                InfoDatabase.TAccount.NAME_PIC ,
                InfoDatabase.TAccount.STATUS ,

                InfoDatabase.TAccount.NT ,

                InfoDatabase.TAccount.SERIAL , serial
        );
    }

    @Override
    public void SetResult (Object Result)
    {
        isThereAResult = true;
        result = Result;
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

    public boolean isFound ()
    {
        return found;
    }
}
