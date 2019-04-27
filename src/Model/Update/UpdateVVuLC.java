package Model.Update;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Controller.Request;
import Interface.ResultModel;
import Model.Database.HandlerDb;
import Model.Database.InfoDatabase.TMemoirList;
import Model.Get.GetNumberOfVVuLC;
import Other.Useful.UsefulLog;
import bardiademon.Interface.Model;
import bardiademon.Interface.bardiademon;

@bardiademon
// GetNumberOfVVuLC => Get V => Visit ; Vu => Visit user ; L => Like ; C => Comment
public class UpdateVVuLC implements Model
{

    private boolean isThereAResult;
    private Object result;

    private int idMemoir;
    private boolean updateVisit, updateVisitUser, updateLike, updateComment;

    private Connection connection;
    private Statement statement;

    private int newVisit, newVisitUser, newLike, newComment;

    private GetNumberOfVVuLC getNumberOfVVuLC;

    private boolean increaseVisit, increaseVisitUser, increaseLike, increaseComment;

    @bardiademon
    public UpdateVVuLC (int IdMemoir)
    {
        if (Request.RequestUser.IsLogin ()) this.idMemoir = IdMemoir;
    }

    @bardiademon
    public void setUpdateComment (boolean increaseComment)
    {
        this.updateComment = true;
        this.increaseComment = increaseComment;
    }

    @bardiademon
    public void setUpdateLike (boolean increaseLike)
    {
        this.updateLike = true;
        this.increaseLike = increaseLike;
    }

    @bardiademon
    public void setUpdateVisit (boolean increaseVisit)
    {
        this.updateVisit = true;
        this.increaseVisit = increaseVisit;
    }

    @bardiademon
    public void setUpdateVisitUser (boolean increaseVisitUser)
    {
        this.updateVisitUser = true;
        this.increaseVisitUser = increaseVisitUser;
    }

    @bardiademon
    public void apply ()
    {
        if (updateVisit || updateVisitUser || updateLike || updateComment) getVVuLC ();
        else SetResult (ResultModel.PublicResult.NULL);
    }

    @bardiademon
    private void getVVuLC ()
    {
        getNumberOfVVuLC = new GetNumberOfVVuLC (idMemoir);
        if (getNumberOfVVuLC.isFound ()) RunClass ();
        else
        {
            if (getNumberOfVVuLC.IsThereAResult ()) SetResult (getNumberOfVVuLC.Result ());
            else SetResult (ResultModel.PublicResult.PUBLIC_ERROR);
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
        updateNumber ();
        statement = connection.createStatement ();
        statement.execute (MakeQuery ());
        SetResult (ResultModel.PublicResult.IS_OK);
    }

    @bardiademon
    private void updateNumber ()
    {
        if (updateVisit)
            newVisit = getValue (getNumberOfVVuLC.getVisit () , increaseVisit);

        if (updateVisitUser)
            newVisitUser = getValue (getNumberOfVVuLC.getVisitUser () , increaseVisitUser);

        if (updateLike)
            newLike = getValue (getNumberOfVVuLC.getLike () , increaseLike);

        if (updateComment)
            newComment = getValue (getNumberOfVVuLC.getComment () , increaseComment);
    }

    @bardiademon
    private int getValue (int value , boolean increase)
    {
        if (increase) value++;
        else
        {
            if (value > 0) value--;
        }
        return value;
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
        return String.format ("UPDATE `%s` SET `%s`=%s,`%s`=%s,`%s`=%s,`%s`=%s WHERE `%s`=%s " ,
                TMemoirList.NT ,
                TMemoirList.VISIT , newVisit ,
                TMemoirList.VISIT_USER , newVisitUser ,
                TMemoirList.NUMBER_OF_LIKE , newLike ,
                TMemoirList.NUMBER_OF_COMMENT , newComment ,
                TMemoirList.ID , idMemoir
        );
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
