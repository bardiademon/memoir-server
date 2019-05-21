package Model;

import Controller.Request;
import Model.Database.InfoDatabase;
import Other.Useful.UsefulLog;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsModel;
import Interface.ResultModel;
import Model.Database.HandlerDb;
import bardiademon.Interface.Model;
import bardiademon.Other.GetTimeStamp;
import bardiademon.Other.Log;

import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@IsModel
@bardiademon
public class MRecordChangeMemoir implements Model
{
    private boolean isThereAResult;
    private Object result;
    private String name, link, date, text;
    private int subject, id;
    private boolean open, change;

    private Connection connection;
    private Statement statement;

    @bardiademon
    public MRecordChangeMemoir (String Name , int Subject , String Link , String Date , String Text , boolean Open , boolean Change , int Id)
    {
        if (Request.RequestUser.IsLogin ())
        {
            this.name = Name;
            this.subject = Subject;
            this.link = Link;
            this.date = Date;
            this.text = Text;
            this.open = Open;
            this.change = Change;
            this.id = Id;
            RunClass ();
        }
        else
        {
            Log.NL (HttpServletResponse.SC_BAD_REQUEST , ResultModel.RecordChangeMemoir.SC200.NOT_RECORDED , Thread.currentThread ().getStackTrace () , "");
            SetResult (ResultModel.RecordChangeMemoir.SC200.NOT_RECORDED);
        }
    }

    @bardiademon
    @Override
    public void RunClass ()
    {
        try
        {
            if (GetConnection ()) CommunicationWithTheDatabase ();
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLException (Thread.currentThread ().getStackTrace () , e);
            SetResult (ResultModel.RecordChangeMemoir.SC200.NOT_RECORDED);
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
        if (!change)
        {
            statement.execute (MakeQuery ());
            SetResult (ResultModel.RecordChangeMemoir.SC200.RECORDED);
        }
        else
        {
            statement.execute (makeQueryChange ());
            SetResult (ResultModel.RecordChangeMemoir.SC200.CHANGED);
        }
    }

    @bardiademon
    @Override
    public String MakeQuery ()
    {
        return String.format ("INSERT INTO `%s`" +
                "(`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`) VALUES (%d,'%s',%d,'%s',%b,'%s',%d,'%d','%s','%s','%s',%d,%d,%d);" ,
            InfoDatabase.TMemoirList.NT ,

            InfoDatabase.TMemoirList.ID_USER ,
            InfoDatabase.TMemoirList.NAME ,
            InfoDatabase.TMemoirList.SUBJECT ,
            InfoDatabase.TMemoirList.DATE ,
            InfoDatabase.TMemoirList.OPEN ,
            InfoDatabase.TMemoirList.LINK ,
            InfoDatabase.TMemoirList.VISIT ,
            InfoDatabase.TMemoirList.VISIT_USER ,
            InfoDatabase.TMemoirList.TEXT ,
            InfoDatabase.TMemoirList.TIME_RECORD ,
            InfoDatabase.TMemoirList.TIME_LAST_EDIT ,
            InfoDatabase.TMemoirList.CONFIRMATION ,
            InfoDatabase.TMemoirList.NUMBER_OF_LIKE ,
            InfoDatabase.TMemoirList.NUMBER_OF_COMMENT ,

            Request.RequestUser.GetId () , // Id User
            name , subject , date , open , link ,
            0 /* Visit */ , 0 /* Visit User */ , text , GetTimeStamp.get () /* Time Record */ , "0" /* Time Last Edit */ , 0 /* Confirmation */ ,
            0 /* Number Of Like */ , 0 /* Number Of Comment */
        );
    }

    private String makeQueryChange ()
    {
        return String.format ("UPDATE `%s` SET `%s`='%s',`%s`=%d,`%s`='%s',`%s`='%s',`%s`='%s',`%s`=%b,`%s`='%s' WHERE `%s`=%d AND `%s`=%d" ,
            InfoDatabase.TMemoirList.NT ,
            InfoDatabase.TMemoirList.NAME , name ,
            InfoDatabase.TMemoirList.SUBJECT , subject ,
            InfoDatabase.TMemoirList.TEXT , text ,
            InfoDatabase.TMemoirList.DATE , date ,
            InfoDatabase.TMemoirList.LINK , link ,
            InfoDatabase.TMemoirList.OPEN , open ,
            InfoDatabase.TMemoirList.TIME_LAST_EDIT , GetTimeStamp.get () ,

            InfoDatabase.TMemoirList.ID , id ,
            InfoDatabase.TMemoirList.ID_USER , Request.RequestUser.GetId ()
        );
    }

    @bardiademon
    @Override
    public void CloseConnectionDb ()
    {
        HandlerDb.CloseConnection (connection , statement);
    }

    @bardiademon
    @Override
    public void SetResult (Object Result)
    {
        Log.NL (HttpServletResponse.SC_OK , result , Thread.currentThread ().getStackTrace () , ResultModel.PublicResult.ForLog.SHOW_RESULT_TO_CLIENT);
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
