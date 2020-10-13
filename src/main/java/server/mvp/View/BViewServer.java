package server.mvp.View;

import java.net.Socket;

public class BViewServer {
    static public IViewServer build(Socket s)
    {
        return new ViewServer(s);
    }
}
