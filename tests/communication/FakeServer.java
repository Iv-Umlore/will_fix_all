package communication;

import server.ServerInterface;

public class FakeServer implements ServerInterface {
    private String answer;

    public FakeServer(){};

    @Override
    public String Registration(String Login, String pass, String car_model, String car_number) {
        if (Login.equals("TrueLogin") && pass.equals("TruePassword"))
            answer = "true";
        else
            answer = "false";
        return answer;
    }

    @Override
    public String Autorization(String Login, String pass) {
        if (Login.equals("UserLogin") && pass.equals("UserPassword"))
        {
            answer = "1";
        }
        else {
            if (Login.equals("ManagerLogin") && pass.equals("ManagerPassword"))
                answer = "2";
            else {
                if (Login.equals("AdminLogin") && pass.equals("AdminPassword"))
                    answer = "3";
                else
                    answer = "0";
            }
        }
        return answer;
    }

    @Override
    public String SendCalendar() {
        // hhddmm (status 0 1) hhddmm (status 0 1) .... all entries
        // 100101 - 10:00 01 january
        answer = "100101 1 110101 1 120101 0";
        return answer;
    }

    @Override
    public String SendCarInfo(int id_rec) {
        //Wrong id_rec
        if (id_rec == -1 )
            answer = "ERROR ERROR -1";
        else
            answer = "lada r367vo 0";
        return answer;
    }

    @Override
    public String SendRecordInfo(int id_rec) {
        //Wrong id_rec
        if (id_rec == -1 )
            answer = "ERROR ERROR -1";
        else
            answer = "toyota r367vo 0";
        return answer;
    }

    @Override
    public String SendChat(int id_rec) {
        return null;
    }

    @Override
    public boolean AddMessage(String message, int id_rec, int root) {
        return false;
    }

    @Override
    public boolean ToBookATime(int id_rec, String time) {
        // Wrong or booked time
        if (time.equalsIgnoreCase("100101"))
            return true;
        else
            return false;
    }

    // Manager

    @Override
    public String SendClientsInfoToManager(int id_manager) {
        if (id_manager == 1)
            answer = "1 1 username1 2 3 username2";
        else
            answer = "-1 -1 ERROR";
        return answer;
    }

    @Override
    public String ChangeStatus(int id_rec, String status, int id_manager, String time) {
        if (id_rec == -1 )
            answer = "false";
        else
            answer = "true";
        return answer;
    }


    @Override
    public String ChangeTime(int id_rec, String status, int id_manager, String time) {
        if (id_rec == -1 || time.equalsIgnoreCase("100000" ) )
            answer = "false";
        else
            answer = "true";
        return answer;
    }

    // Admin Interface

    @Override
    public String ChangeManager(int id_rec, String status, int id_manager, String time) {
        if (id_rec == -1 || id_manager == -1 )
            answer = "false";
        else
            answer = "true";
        return answer;
    }

    @Override
    public String SetManager(int id_user) {
        if (id_user == -1)
            answer = "false";
        else
            answer = "true";
        return answer;
    }

    @Override
    public String RemoveManager(int id_user) {
        if (id_user == -1)
            answer = "false";
        else
            answer = "true";
        return answer;
    }

    @Override
    public String SendAllUsersInfo() {
        answer = "username1 1 username2 2";
        return answer;
    }
}
