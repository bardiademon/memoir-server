package bardiademon.Other;

import java.io.*;

public class GetValueFile
{
    private File file;

    private String valueFile;

    public GetValueFile (File file)
    {
        this.file = file;
        get();
    }

    private void get ()
    {
        if (file.exists())
        {
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                StringBuilder valueFile;
                valueFile = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    valueFile.append("\n").append(line);

                reader.close();
                this.valueFile = valueFile.toString();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getValueFile ()
    {
        return valueFile;
    }
}
