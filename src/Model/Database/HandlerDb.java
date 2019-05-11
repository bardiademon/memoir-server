package Model.Database;

import Other.Useful.UsefulLog;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class HandlerDb
{
    private static final String username = "root", password = "", dbName = "memoir", domain = "localhost", nameSQL = "mysql";
    private static final int port = 3306;

    public static Connection GetConnection () throws SQLException
    {
        try
        {
            Class.forName ("com.mysql.jdbc.Driver");
            String urlConnection = String.format ("jdbc:%s://%s:%d/%s?useUnicode=true&characterEncoding=utf-8" , nameSQL , domain , port , dbName);
            return (DriverManager.getConnection (urlConnection , username , password));
        }
        catch (ClassNotFoundException ignored)
        {
            return null;
        }
    }

    public static boolean CheckConnection (Connection connection)
    {
        try
        {
            return (connection != null && !connection.isClosed ());
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLExceptionServer (Thread.currentThread ().getStackTrace () , e);
            return false;
        }
    }

    public static Integer GetCountSelectedRow (ResultSet resultSet)
    {
        int row = 0;
        try
        {
            if (resultSet.last ())
            {
                row = resultSet.getRow ();
                resultSet.beforeFirst ();
            }
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLExceptionServer (Thread.currentThread ().getStackTrace () , e);
        }
        return row;
    }

    public static void CloseConnection (Connection connection , Statement statement)
    {
        CloseConnection (connection , statement , null);
    }

    public static void CloseConnection (Statement statement , ResultSet resultSet)
    {
        CloseConnection (null , statement , resultSet);
    }

    public static void CloseConnection (Connection connection)
    {
        CloseConnection (connection , null , null);
    }

    public static void CloseConnection (Connection connection , Statement statement , ResultSet resultSet)
    {
        try
        {
            if (connection != null && !connection.isClosed ()) connection.close ();
            if (statement != null && !statement.isClosed ()) statement.close ();
            if (resultSet != null && !resultSet.isClosed ()) resultSet.close ();
        }
        catch (SQLException e)
        {
            UsefulLog.ForSQLExceptionServer (Thread.currentThread ().getStackTrace () , e);
        }
    }
}
