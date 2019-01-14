package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSpeaker {
    private Socket server;
    private DataInputStream _dis;
    private DataOutputStream _dos;

    private String command;
    
    public ClientSpeaker(String _host, int _port) throws IOException {
        server = new Socket(_host, _port);

        _dis = new DataInputStream(server.getInputStream());
        _dos = new DataOutputStream(server.getOutputStream());
    }

    public boolean Registration(String Login, String pass, String CarModel, String CarNumb) throws IOException {

        command = "Registration " + Login + ' ' + pass + ' ' + CarModel + ' ' + CarNumb;
        _dos.writeUTF(command);
        _dos.flush();

        // "Success/Error type"
        return Boolean.parseBoolean(_dis.readUTF());
    }

    public String Autorization(String Login, String pass) throws IOException {

        command = "Autorization " + Login + ' ' + pass;
        _dos.writeUTF(command);
        _dos.flush();

        // Permissions (0 - nobody, 1 - client, 2 -manager, 3 -admin), Login
        return _dis.readUTF();
    }

    public String GetCalendar() throws IOException {

        command = "GetCalendar";
        _dos.writeUTF(command);
        _dos.flush();

        // hhddmm (status 0 1) hhddmm (status 0 1) .... all entries
        return _dis.readUTF();
    }

    public String GetCarInfo(int id_rec) throws IOException {

        command = "GetCarInfo " + id_rec;
        _dos.writeUTF(command);
        _dos.flush();

        // model number status
        return _dis.readUTF();
    }

    public String GetRecordInfo(int id_rec) throws IOException {

        command = "GetRecordInfo "+id_rec;
        _dos.writeUTF(command);
        _dos.flush();

        // model number status
        return _dis.readUTF();
    }

    public boolean ToBookATime(int id_rec, String time) throws IOException {
        // Time is hhddmm  (101201 - 10:00 12 january)
        // 10:00 01.01 1
        command = "ToBookATime " + id_rec + ' ' + time.substring(0, 2) + time.substring(6, 8) + time.substring(9, 11);
        
        System.out.println(command);
        
        _dos.writeUTF(command);
        _dos.flush();

        // true/false (BookTime)
        return _dis.readBoolean();
    }

    public String GetChat(int id_rec) throws IOException{
        command = "GetChat " + id_rec;
        _dos.writeUTF(command);
        _dos.flush();
        
        return _dis.readUTF();
    }
    
    public boolean SendMessage(String message, int id_rec, int root) throws IOException{
        command = "AddMessage " + id_rec + ' ' + root + ' ' + message;
        _dos.writeUTF(command);
        _dos.flush();
        
        return _dis.readBoolean();
    }
    
    // Manager

    public String GetMyClientsInfo(int id_manager) throws IOException{

        command = "GetMyClientsInfo " + id_manager;
        _dos.writeUTF(command);
        _dos.flush();

        // iserid orderid login, userid orderid login ...
        return _dis.readUTF();

    }

    public boolean ChangeStatus(int id_rec, String status, int id_manager, String time) throws IOException {

        command = "ChangeStatus " + id_rec + ' ' + status + ' ' + id_manager + ' ' + time;
        _dos.writeUTF(command);
        _dos.flush();

        // true false
        return Boolean.parseBoolean(_dis.readUTF());
    }

    public boolean ChangeTime(int id_rec, String time ) throws IOException {
        
        command = "ChangeTime " + id_rec + ' ' + time.substring(0, 2) + time.substring(6, 8) + time.substring(9, 11);
        
        _dos.writeUTF(command);
        _dos.flush();
        // true false
        return Boolean.parseBoolean(_dis.readUTF());
    }

    // Administrator

    public boolean ChangeManager(int id_rec, int id_manager) throws IOException {

        command = "ChangeManager " + id_rec + ' ' + id_manager;
        _dos.writeUTF(command);
        _dos.flush();

        // true false
        return Boolean.parseBoolean(_dis.readUTF());
    }

    public boolean SetManager(int id_user) throws IOException{

        command = "SetManager " + id_user;
        _dos.writeUTF(command);
        _dos.flush();

        // true false
        return Boolean.parseBoolean(_dis.readUTF());
    }

    public boolean RemoveManager(int id_user) throws IOException{
        command = "RemoveManager " + id_user;
        _dos.writeUTF(command);
        _dos.flush();

        // true false
        return Boolean.parseBoolean(_dis.readUTF());
    }

    public String GetAllUsersInfo() throws IOException {
        command = "GetAllUsersInfo";
        _dos.writeUTF(command);
        _dos.flush();

        // login id_user login id_user ...
        return _dis.readUTF();
    }

}
