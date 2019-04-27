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
                            "E:" , "Programming" , "127.0.0.1" , "Java" , "Apache Software Foundation" , "Tomcat 9.0_ServerJavaTomcat9"
                    };

            private static final String[] ProjectLocation = {"webapps" , "ROOT"};

            public static String GetProjectLocation ()
            {
                return String.format ("%s%s%s" , MakeAddress.Make (LOCATION) , File.separator , MakeAddress.Make (ProjectLocation));
            }
        }

        private static class MakeAddress
        {
            public static String Make (String[] Location)
            {
                StringBuilder address = new StringBuilder ();
                for (String location : Location) address.append (location).append (File.separator);
                return address.toString ();
            }
        }
    }
}
