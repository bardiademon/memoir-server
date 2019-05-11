package Model.Get;

import Other.InfoProject.Url;
import Page.Req;
import bardiademon.Interface.IsModel;
import bardiademon.Interface.bardiademon;

import java.io.File;

@bardiademon
@IsModel
public class GetPictureProfile
{
    private String namePicture;
    private String linkPic = "no_pic";
    private static final String PATH_DIR_PIC_PROFILE_GYM = "Pic/User";

    private static final String IF_NO_PIC = "no_pic";

    @bardiademon
    public GetPictureProfile (String NamePicture)
    {
        if (NamePicture != null && !NamePicture.isEmpty ())
        {
            this.namePicture = NamePicture;
            getPic ();
        }
        else linkPic = IF_NO_PIC;
    }

    private void getPic ()
    {
        File file = new File (Req.GetRequest ().getServletContext ().getRealPath (PATH_DIR_PIC_PROFILE_GYM));
        if (file.exists ())
        {
            File pic = new File (file.getPath () + "/" + namePicture);
            if (pic.exists ())
                linkPic = Url.GetMainUrlPlus (String.format ("%s/%s" , PATH_DIR_PIC_PROFILE_GYM , namePicture));
            else
                linkPic = IF_NO_PIC;
        }
        else
        {
            boolean make = file.mkdirs ();
            if (make) getPic ();
            else linkPic = IF_NO_PIC;
        }
    }

    public String getLinkPic ()
    {
        return linkPic;
    }
}
