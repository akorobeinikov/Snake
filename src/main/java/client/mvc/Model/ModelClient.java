package client.mvc.Model;

import client.mvc.View.IObserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ModelClient {
    int port = 888;
    InetAddress ip = null;

    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;

    ArrayList<IObserver> list_o = new ArrayList<>();

    public void addEvelt(IObserver o)
    {
        list_o.add(o);
    }
    void refresh()
    {
        for (IObserver observer : list_o) {
            observer.refresh();
        }
    }

    public ModelClient()
    {
    }
    public void init()
    {
        if(cs != null) return;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ModelClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            cs = new Socket(ip, port);
            System.out.append("Client start \n");

            dis = new DataInputStream(cs.getInputStream());
            dos = new DataOutputStream(cs.getOutputStream());

            new Thread(){
                @Override
                public void run() {
                    try {
                        while(true)
                        {
                            int op = dis.readInt();
                            if(op == 1)
                            {
                                int height = dis.readInt();
                                int width = dis.readInt();
                                field = new int[height][width];
                                for(int i = 0; i < height; ++i) {
                                    for(int j = 0; j < width; ++j) {
                                        field[i][j] = dis.readInt();
                                    }
                                }
                                refresh();
                            }
                            if(op == -1)
                            {
                                break;
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ModelClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }.start();

        } catch (IOException ex) {
            Logger.getLogger(ModelClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        refresh();
    }

    int[][] field;

    public int[][] getField(){
        System.out.println(cs);
        if(cs == null) return null;

        return field;
    }

    public void setText(String mes)
    {
        if(cs == null) return;
        try {
            dos.writeInt(1);
            dos.writeUTF(mes);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ModelClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
