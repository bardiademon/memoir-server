package bardiademon.Other;

import java.io.File;

public abstract class InfoServer
{
    public static abstract class Address
    {
        public static abstract class TomcatAddress
        {
            private static final String[] LOCATION =
                    {
                            "E:" , "Programming" , "project" , "java" , "JavaEE"
                    };

            public static String GetProjectLocation ()
            {
                StringBuilder address = new StringBuilder ();
                for (String location : TomcatAddress.LOCATION)
                    address.append (location).append (File.separator);
                return address.toString ();
            }
        }
    }
}
