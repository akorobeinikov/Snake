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
            System.out.printf("Send: x=%d, y=%d, state = %s \n", point.x, point.y, point.state);
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.writeValue(writer, point);
            System.out.println("On server :" + writer.toString() + '\n');
            dos.writeUTF(writer.toString());
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ViewServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Cell getCell() {
        try {
            String jsonString = dis.readUTF();
            StringReader reader = new StringReader(jsonString);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            Cell cell = mapper.readValue(reader, Cell.class);
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
