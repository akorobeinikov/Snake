package server.mvp.View;

import resources.Cell;
import resources.CellState;

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

    public void setCell(final Cell point) {
        new Thread()
        {
            @Override
            public void run() {
                try {
                    System.out.printf("Send: x=%d, y=%d, state = %s", point.x, point.y, point.state);
                    dos.writeInt(point.x);
                    dos.writeInt(point.y);
                    dos.writeInt(point.state.ordinal());
                    dos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();

    }

    public Cell getCell() {
        try {
            int x = dis.readInt();
            int y = dis.readInt();
            CellState state = CellState.fromInteger(dis.readInt());
            Cell cell = new Cell(x, y, state);
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
