package client.mvc.Model;

import client.mvc.View.IObserver;
import resources.Cell;

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

    public void addEvent(IObserver o)
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
                                op = dis.readInt();
                                int x = dis.readInt();
                                int y = dis.readInt();
                                String state = dis.readUTF();  // !!!
                                System.out.printf("Received: x=%d; y=%d state = %s\n", x, y, state);
                                point = new Cell(x, y, state);
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

    Cell point;

    public Cell getPoint(){
        System.out.println(cs);
        if(cs == null) return null;

        return point;
    }

    public void setCell(Cell point)
    {
        if(cs == null) return;
        try {
            dos.writeInt(1);
            dos.writeInt(point.x);
            dos.writeInt(point.y);
            dos.writeUTF(point.state);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ModelClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
