package client;

import java.io.IOException;
import java.util.StringTokenizer;

public class Client implements ClientInterface{

    private int Status;
    private int id;
    private int _id_rec;         // if it's manager. we must remember his client
    private ClientSpeaker CS;
    
    String time;
    
    private String answer;
    
    public Client(String _host, int _port) throws IOException {
        CS = new ClientSpeaker(_host, _port);
        id = 0;
        _id_rec = 0;
    }

    public String GetTime(){
        return time;
    }
    
    @Override
    public void RemoveIdRec() {
        _id_rec = 0;
    }
    
    @Override
    public int GetStatus(){
        return Status;
    }

    @Override
    public boolean Registration(String Login, String pass, String CarModel, String CarNumb) {
        try {
            return CS.Registration(Login, pass, CarModel, CarNumb);
        } catch (IOException ex) {
            System.out.println("Error Client.Registration()");
            return false;
        }
    }

    @Override
    public String Autorization(String Login, String pass) {
        try {
            answer = CS.Autorization(Login, pass);
            System.out.println(answer);
            StringTokenizer stok = new StringTokenizer(answer, " ");
            id = Integer.parseInt(stok.nextToken());
            Status = Integer.parseInt(stok.nextToken());
            String tmp = stok.nextToken();
             if (time == "null")
                time = "Время не назначено";            
            else time = tmp.substring(0,2) + ":00 " + tmp.substring(2,4) + ".01";
            return Status + " " + time + "\n";
        } catch (IOException ex) {
            System.out.println("Error Client.Autorization()");
            return "0 0 0";
        }
    }

    @Override
    public String UpdateCalendar() {
        try {
            return CS.GetCalendar();
        } catch (IOException ex) {
            System.out.println("Client.UpdateCalendar() ERROR");
            return "";
        }
    }

    @Override
    public String GetMyCar() {
        try {
            return CS.GetCarInfo(id);
            //return CS.GetMyCar(id);
        } catch (IOException ex) {
            System.out.println("Client.GetMyCar() ERROR");
        }
        return "Connection error Connection_Error";
    }

    
    @Override
    public String OpenRecord(int id_rec) {
        try {            
            
            if (id_rec != -1) {
                _id_rec = id_rec;
                return CS.GetRecordInfo(id_rec);
            }
            else return CS.GetRecordInfo(_id_rec);
        } catch (IOException ex) {
            System.out.println("Client.OpenRecord() ERROR");
            return "Connection Error";
        }
    }

    @Override
    public void ToBookATime(String str) {
        try {
            System.out.println(CS.ToBookATime(id, str));
        } catch (IOException ex) {
            System.out.println("Client.ToBookTime() ERROR");
        }
    }

    @Override
    public String OpenChat() {
        try {
            return CS.GetChat(id);
        } catch (IOException ex) {
            System.out.println("Client.OpenChat() ERROR");
            return "Connection Error";
        }        
    }
    
    
    @Override
    public boolean SendMessage(String message) {
        try {
            message += "\n";
            if (_id_rec == 0) return CS.SendMessage(message, id, 1);
            return CS.SendMessage(message, _id_rec, Status);
        } catch (IOException ex) {
            System.out.println("Client.SendMessage() ERROR. Can't send message!");
            return false;
        }
    }
    
    // Manager

    @Override
    public String OpenMyClients() {
        try {
            return CS.GetMyClientsInfo(id);
        } catch (IOException ex) {
            System.out.println("Client.OpenMyClient() ERROR");
            return "Connection failed";
        }
    }

    @Override
    public void ChangeStatus(String status) {
        try {
            CS.ChangeStatus(_id_rec, status);
        } catch (IOException ex) {
            System.out.println("Client.ChangeStatus() ERROR");
        }
    }

    @Override
    public void ChangeTime(String time) {
        try {
            CS.ChangeTime(_id_rec, time );
        } catch (IOException ex) {
            System.out.println("Client.ChangeTime() ERROR");
        }
    }

    // Administrator

    @Override
    public void ChangeManager(int id_manager) {
        try {
            if (_id_rec == 0) System.out.println(CS.ChangeManager(id, id_manager));
            else System.out.println(CS.ChangeManager(_id_rec, id_manager));
        } catch (IOException ex) {
            System.out.println("Client.ChangeManager() ERROR");
        }
    }

    @Override
    public void SetManager(int id_user) {
        try {
            System.out.println(CS.SetManager(id_user));
        } catch (IOException ex) {
            System.out.println("Client.SetManager() ERROR");
        }
    }

    @Override
    public void RemoveManager(int id_user) {
        try {
            System.out.println(CS.RemoveManager(id_user));
        } catch (IOException ex) {
            System.out.println("Client.RemoveManager() ERROR");
        }
    }

    @Override
    public String OpenAllUsers() {
        try {
            return CS.GetAllUsersInfo();
        } catch (IOException ex) {
            System.out.println("Client.OpenAllUsers() ERROR");
            return "Connection Error";
        }
    }


    


}
