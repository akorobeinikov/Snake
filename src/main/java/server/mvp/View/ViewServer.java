package server.mvp.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewServer implements IViewServer{

    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;

    public ViewServer(Socket _cs)
    {
        try {
            cs = _cs;
            dis = new DataInputStream(cs.getInputStream());
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getOp() {
        try {
            return dis.readInt();
        } catch (IOException ex) {
            Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public void setField(final int[][] gameField) {
        new Thread()
        {
            @Override
            public void run() {
                try {
                    dos.writeInt(gameField.length);
                    dos.writeInt(gameField[0].length);
                    for(int i = 0; i < gameField.length; i++) {
                        for(int j = 0; j < gameField[i].length; j++) {
                            dos.writeInt(gameField[i][j]);
                        }
                    }
                    dos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();

    }

    public Cell getCell() {
        try {
            int height = dis.readInt();
            int width = dis.readInt();
            int[][] field = new int[height][width];
            for(int i = 0; i < height; ++i) {
                for(int j = 0; j < width; ++j) {
                    field[i][j] = dis.readInt();
                }
            }
            return cell;
        } catch (IOException ex) {
            Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setOp(int op) {
        try {
            dos.writeInt(op);
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
