package Other;

import bardiademon.Other.InfoServer;

import java.io.File;

public class InfoProject
{
    public static final String NAME_ROUTER = "Memoir_war_exploded";
    public static final String NAME = "Memoir";

    public static class Url
    {
        public static final String Protocol = "http";
        public static final String Domain = "10.0.2.2";
        public static final int Port = 8080;
        public static final String Router = InfoProject.NAME_ROUTER;

        public static String GetMainUrl ()
        {
            return (String.format ("%s://%s:%d/%s" , Protocol , Domain , Port , Router));
        }

        public static String GetMainUrlPlus (String Url)
        {
            return (String.format ("%s/%s" , GetMainUrl () , Url));
        }
    }

    public static class Address
    {
        private static String P_ARCHIVES = "Archives";
        private static String P_LOG = "Log";
        public static final String PATH_PROJECT = String.format ("%s%s" , InfoServer.Address.TomcatAddress.GetProjectLocation () , NAME);

        public static String GetPathLog ()
        {
            return String.format ("%s%s%s%s%s" , PATH_PROJECT , File.separator , P_ARCHIVES , File.separator , P_LOG);
        }


    }

}
