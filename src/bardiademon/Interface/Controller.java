package bardiademon.Interface;

@bardiademon
public interface Controller extends JustController
{
    void SendToModel ();

    void GetResultFromModel ();

    void SetResult (Object Result , int Status);

    void SendToView (Object Result);
}
