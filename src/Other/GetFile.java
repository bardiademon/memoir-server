package Other;

import Page.Response;

import java.io.File;

public class GetFile
{
    private static final String PATH_FILE = "File";

    private String nameFile;

    private File file;

    public GetFile (String nameFile)
    {
        this.nameFile = nameFile;
        get ();
    }

    private void get ()
    {
        File file = new File (Response.GetServletContext ().getRealPath (PATH_FILE));
        if (file.exists ()) this.file = new File (file.getPath () + File.separator + nameFile);
        else
        {
            if (file.mkdirs ())
                get ();
        }
    }

    public File getFile ()
    {
        return file;
    }
}
