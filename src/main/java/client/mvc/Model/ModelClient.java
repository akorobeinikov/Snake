package client.mvc.Model;

import client.mvc.View.IObserver;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import resources.Cell;
import resources.CellState;

import java.io.*;
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
    public Integer winner = 0;

    public void addObserver(IObserver o)
    {
        list_o.add(o);
    }
    void refresh()
    {
        for (IObserver observer : list_o) {
            observer.refresh();
        }
    }

    public void setIp() {
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ModelClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void init()
    {
        if(cs != null) return;
        setIp();
        connect();
    }

    public void connect() {
        try {
            cs = new Socket(ip, port);
            System.out.printf("Client start with ip %s \n", ip);

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
                                String jsonString = dis.readUTF();

                                StringReader reader = new StringReader(jsonString);

                                ObjectMapper mapper = new ObjectMapper();
                                mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

                                point = mapper.readValue(reader, Cell.class);
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
        if(cs == null) return null;

        return point;
    }

    public void setDirection(Integer direction) {
        if(cs == null) return;
        try {
            dos.writeInt(1); // op
            dos.flush();
            dos.writeInt(direction);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ModelClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
