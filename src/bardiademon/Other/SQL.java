package bardiademon.Other;

import Model.Database.HandlerDb;
import Model.Database.InfoDatabase;
import bardiademon.Interface.bardiademon;
import bardiademon.Interface.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@bardiademon
public abstract class SQL
{
    @bardiademon
    public static abstract class Query
    {
        public static class Select
        {
            private ValueQuery valueQuery;
            private StringBuilder query;

            public void putSelect (String... nameRow)
            {
                checkNew ();
                if (valueQuery.select == null) valueQuery.select = new ArrayList <> ();
                for (String row : nameRow)
                    valueQuery.select.add (String.format ("`%s`" , row));
            }

            public void setNameTable (String nameTable)
            {
                checkNew ();
                valueQuery.nameTable = nameTable;
            }


            public void putWhere (Object[]... info)
            {
                String row, AndOr;
                Object value;
                for (Object[] inf : info)
                {
                    row = (String) inf[0];
                    value = inf[1];
                    AndOr = (String) inf[2];
                    if (value instanceof Integer) putWhere (row , value , AndOr);
                    else putWhere (row , value , AndOr);
                }
            }

            public void putWhere (String row , Object value , String AndOr)
            {
                checkNew ();
                if (valueQuery.where == null) valueQuery.where = new ArrayList <> ();

                String where = String.format ("`%s` =" , row);
                if (value instanceof Integer) where += " " + value;
                else where = String.format ("%s '%s'" , where , value);

                if (AndOr != null)
                    where = String.format ("%s %s " , where , AndOr.toUpperCase ());

                valueQuery.where.add (where);
            }

            private void checkNew ()
            {
                if (valueQuery == null) valueQuery = new ValueQuery ();
            }

            private class ValueQuery
            {
                List <String> select;
                String nameTable;
                List <String> where;
            }

            public String apply ()
            {
                if (valueQuery == null || valueQuery.nameTable == null || valueQuery.nameTable.equals (""))
                    return null;
                else
                {
                    query = new StringBuilder ();

                    query.append ("SELECT");
                    if (valueQuery.select == null || valueQuery.select.size () == 0)
                        query.append (" * ");
                    else
                    {
                        query.append (" ");
                        for (int i = 0, len = valueQuery.select.size (); i < len; i++)
                        {
                            query.append (valueQuery.select.get (i));
                            if (i + 1 < len) query.append (",");
                        }
                    }
                    query.append (" ").append ("FROM").append (" ").append ("`").append (valueQuery.nameTable).append ("`");
                    if (valueQuery.where != null && valueQuery.where.size () > 0)
                    {
                        query.append (" ").append ("WHERE").append (" ");
                        for (String where : valueQuery.where) query.append (where);
                    }
                    query.append (";");
                    return query.toString ();
                }
            }
        }
    }


    @bardiademon
    public static abstract class Ready
    {
        @bardiademon
        public static class CheckValueInSQL implements Model
        {
            private boolean is;

            private int count;

            private Connection connection;
            private Statement statement;
            private ResultSet resultSet;

            private String nameTable, nameRow;
            private Object value;
            private int type;

            private int id;

            private boolean getId;
            private String nameRowId = "";

            public void setGetId ()
            {
                setGetId (InfoDatabase.PublicRow.ID);
            }

            public void setGetId (String nameRowId)
            {
                this.nameRowId = nameRowId;
                getId = true;
            }

            @bardiademon
            public boolean is (String NameTable , String NameRow , Object Value)
            {
                return (is (NameTable , NameRow , Value , Type.TYPE_STR));
            }

            @bardiademon
            public boolean is (String NameTable , String NameRow , Object Value , int Type)
            {
                this.nameTable = NameTable;
                this.nameRow = NameRow;
                this.value = Value;
                this.type = Type;
                RunClass ();
                return is;
            }

            @bardiademon
            public int getCount ()
            {
                return count;
            }

            @bardiademon
            @Override
            public void RunClass ()
            {
                try
                {
                    if (GetConnection ()) CommunicationWithTheDatabase ();
                    else is = false;
                }
                catch (SQLException e)
                {
                    is = false;
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
                resultSet = statement.executeQuery (MakeQuery ());
                count = HandlerDb.GetCountSelectedRow (resultSet);
                is = (count > 0);
                if (getId && resultSet.first ()) id = resultSet.getInt (nameRowId);
            }

            @bardiademon
            @Override
            public String MakeQuery ()
            {
                Query.Select select = new Query.Select ();
                select.putSelect (nameRow , ((getId) ? nameRowId : ""));
                select.setNameTable (nameTable);
                select.putWhere (nameRow , value , null);
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
            }

            @bardiademon
            @Override
            public boolean IsThereAResult ()
            {
                return false;
            }

            @bardiademon
            @Override
            public Object Result ()
            {
                return null;
            }

            @bardiademon
            public static abstract class Type
            {
                public static final int TYPE_STR = 0, TYPE_OTHER = 1;
            }

            public int getId ()
            {
                return id;
            }
        }
    }
}
