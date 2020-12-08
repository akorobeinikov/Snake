package server.mvp.View;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import resources.Cell;
import resources.CellState;

import java.io.*;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        try {
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.writeValue(writer, point);

            dos.writeUTF(writer.toString());
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Integer getDirection() {
        try {
            return dis.readInt();
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
