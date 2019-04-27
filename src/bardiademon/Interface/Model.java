package bardiademon.Interface;

import java.sql.SQLException;

@bardiademon
public interface Model
{
    void RunClass ();

    boolean GetConnection () throws SQLException;

    void CommunicationWithTheDatabase () throws SQLException;

    void CloseConnectionDb ();

    String MakeQuery ();

    void SetResult (Object Result);

    boolean IsThereAResult ();

    Object Result ();

}
