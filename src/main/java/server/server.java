package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.mvp.Presenter.BPresenter;
import server.mvp.Presenter.IPresenter;
//import server.mvp.Presenter.Presenter;
import server.mvp.View.BViewServer;
import server.mvp.Model.BModelServer;
import server.mvp.View.IViewServer;
import server.mvp.Model.IModelServer;


public class server {
    int port = 888;
    InetAddress ip = null;


    public void serverStart(){
        IModelServer game_model = BModelServer.model();

        ServerSocket ss;
        Socket cs;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ss = new ServerSocket(port, 0, ip);
            System.out.println("Server start");
            while(true)
            {
                cs = ss.accept();
                System.out.println("Has connect");
                IViewServer v = BViewServer.build(cs);
                IPresenter p = BPresenter.build(game_model, v);
            }
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        new server().serverStart();
    }
}
