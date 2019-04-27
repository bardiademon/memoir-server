package Interface;

import bardiademon.Interface.bardiademon;
import bardiademon.Interface.IsPage;

@bardiademon
@IsPage
public interface Page
{
    @bardiademon
    void SendToController (boolean IsOkRequest);
}
