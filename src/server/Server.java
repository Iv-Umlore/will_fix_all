package server;

import java.sql.*;

public class Server implements ServerInterface{
    private String answer;
    private String Chat;
    
    private String table;

    // Database credentials
    static final String DB_URL = "jdbc:postgresql://dumbo.db.elephantsql.com:5432/evejlbgk";
    static final String USER = "evejlbgk";
    static final String PASS = "s2q8D9vPB9sa5QJTrgW2DaRU98JRHAjR";

    private Connection db;

    public Server(){
        Chat = "0 Hello, Happy New Year!\n1 Hello, Happy New Year too!\n";
        table = "100101 1 110101 1 120101 0";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            db = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    };

    @Override
    public String Registration(String Login, String pass, String car_model, String car_number) throws SQLException {
        // Must return true or false
        answer = "true";
        Integer idorder = 3221;
        final char dm = (char) 34;
        Statement statement = db.createStatement();
        ResultSet set = statement.executeQuery("SELECT " + dm + "LOGIN" + dm + " FROM " + dm + "Users" + dm);
        while(set.next())
        {
            if(set.getString(1) == Login)
            {
                answer = "false";
            }
            idorder += 1;
        }
        if(answer == "true")
        {
            CallableStatement call = db.prepareCall("{call writeuser(?,?,?,?,?,?)}");
            call.setInt(1, idorder);
            call.setString(2, Login);
            call.setString(3, pass);
            call.setString(4, car_model);
            call.setString(5, car_number);
            call.setInt(6,1);
            call.execute();
        }
        return answer;
    }

    @Override
    public String Autorization(String Login, String pass) throws SQLException {
        // Must return true or false
        // ok. We have user's root in our table. We send this root to client
        // or we will user_id + root 
        // id_user + root
        boolean flagLogin = false;
        boolean flagPass = false;
        Integer idorder = 0;
        Integer type = 0;
        final char dm = (char) 34;
        Statement statement = db.createStatement();
        ResultSet setLogin = statement.executeQuery("SELECT " + dm + "LOGIN" + dm + " FROM " + dm + "Users" + dm);
        while(setLogin.next())
        {
            String strdb = setLogin.getString(1);
            if(strdb.equalsIgnoreCase(Login) /*|| setPass.getString(1) != pass*/)
            {
                flagLogin = true;
            }
        }
        setLogin.close();
        ResultSet setPass = statement.executeQuery("SELECT " + dm + "PASSWORD" + dm + " FROM " + dm + "Users" + dm);
        while(setPass.next())
        {
            String strdb = setPass.getString(1);
            if(strdb.equalsIgnoreCase(pass))
            {
                flagPass = true;
            }
        }
        setPass.close();
        if(flagLogin == true && flagPass == true) {
            CallableStatement call_idorder = db.prepareCall("{call readuseridorder(?)}");
            call_idorder.setString(1, Login);
            call_idorder.execute();
            ResultSet set_idorder = call_idorder.getResultSet();
            while (set_idorder.next()) {
                idorder = set_idorder.getInt(1);
            }
            CallableStatement call_type = db.prepareCall("{call readusertype(?)}");
            call_type.setInt(1, idorder);
            call_type.execute();
            ResultSet set_type = call_type.getResultSet();
            while (set_type.next()) {
                type = set_type.getInt(1);
            }
            answer = idorder.toString() + " " + type.toString();
            System.out.println(answer);
        }
        else if(flagLogin == false || flagPass == false)
        {
            answer = "0 0";
        }
        return answer;
    }
    
    @Override
    public String SendCalendar() throws SQLException {
        // Must return calendar status
        // hhddmm (status 0 1) hhddmm (status 0 1) .... all entries
        // 100101 - 10:00 01 january
        String time_table = "";
        final char dm = (char) 34;
        Statement statement = db.createStatement();
        ResultSet setTime = statement.executeQuery("SELECT " + dm + "TIME" + dm + " FROM " + dm + "Orders" + dm);
        while (setTime.next())
        {
            time_table += setTime.getString(1) + " ";
        }
        answer = time_table;
        return answer;
    }
        
    @Override
    public String SendCarInfo(int id_rec) throws SQLException {
        //  Must return model number status
        String car_info = "";
        CallableStatement call_model = db.prepareCall("{call readusermodel(?)}");
        call_model.setInt(1, id_rec);
        call_model.execute();
        ResultSet set_model = call_model.getResultSet();

        CallableStatement call_number = db.prepareCall("{call readusernumber(?)}");
        call_number.setInt(1, id_rec);
        call_number.execute();
        ResultSet set_number = call_number.getResultSet();

        CallableStatement call_status = db.prepareCall("{call readorderstatus(?)}");
        call_status.setInt(1, id_rec);
        call_status.execute();
        ResultSet set_status = call_status.getResultSet();

        while (set_model.next() && set_number.next() && set_status.next()) {
            car_info = set_model.getString(1) + " " + set_number.getString(1) + " " + set_status.getString(1);
        }
        answer = car_info;
        return answer;
    }

    @Override
    public String SendRecordInfo(int id_rec) throws SQLException {
        //  Must return model number status
        String car_info = "";
        CallableStatement call_model = db.prepareCall("{call readusermodel(?)}");
        call_model.setInt(1, id_rec);
        call_model.execute();
        ResultSet set_model = call_model.getResultSet();

        CallableStatement call_number = db.prepareCall("{call readusernumber(?)}");
        call_number.setInt(1, id_rec);
        call_number.execute();
        ResultSet set_number = call_number.getResultSet();

        CallableStatement call_status = db.prepareCall("{call readorderstatus(?)}");
        call_status.setInt(1, id_rec);
        call_status.execute();
        ResultSet set_status = call_status.getResultSet();

        while (set_model.next()) {
            while (set_number.next())
            {
                while (set_status.next())
                {
                    car_info = set_model.getString(1) + " " + set_number.getString(1) + " " + set_status.getString(1);
                }
            }
        }
        set_model.close();
        set_number.close();
        set_status.close();
        answer = car_info;
        return answer;
    }

    @Override
    public boolean ToBookATime(int id_rec, String time) throws SQLException {
        // Time is hhddmm  (101201 - 10:00 12 january)
        // Must return true or false
        boolean flag = true;
        Integer managerID = 0;
        final char dm = (char) 34;
        Statement statement = db.createStatement();
        ResultSet setManagerID = statement.executeQuery("SELECT " + dm + "ID_ORDER" + dm + " FROM " + dm + "Users" + dm
                                                            + " WHERE " + dm + "TYPE" + dm + " = 2");
        while (setManagerID.next())
        {
            managerID = setManagerID.getInt(1);
        }
        CallableStatement call = db.prepareCall("{call writeorder(?,?,?,?)}");
        call.setInt(1, id_rec);
        call.setString(2, "waiting");
        call.setInt(3, managerID);
        call.setString(4, time);
        call.execute();
        table += " ";
        
        return flag;
    }

    @Override
    public String SendChat(int id_rec) throws SQLException {
        answer = "";
        String mes = "";
        Statement statement = db.createStatement();
        final char dm = (char) 34;
        ResultSet set = statement.executeQuery("SELECT " + dm + "MESSAGE" + dm + " FROM " + dm + "Chat" + dm);
        while(set.next())
        {
            mes += set.getString(1);
            answer += mes;
        }
        set.close();
        return answer;
    }
    
    @Override
    public boolean AddMessage(String message, int id_rec, int root) throws SQLException {
        Integer i = 0;
        if ((root < 1) || (root > 3)) return false;
        switch(root){
            case 1: {
                i+=1;
                CallableStatement call = db.prepareCall("{call writeorder(?,?,?,?)}");
                call.setInt(1, id_rec);
                call.setInt(2, i);
                call.setString(3, message);
                call.execute();
                Chat+= "0 " + message;
                break;
            }
            case 2: {
                i+=1;
                CallableStatement call = db.prepareCall("{call writeorder(?,?,?,?)}");
                call.setInt(1, id_rec);
                call.setInt(2, i);
                call.setString(3, message);
                call.execute();
                Chat+= "1 " + message;
                break;
            }
            case 3: {
                i+=1;
                CallableStatement call = db.prepareCall("{call writeorder(?,?,?,?)}");
                call.setInt(1, id_rec);
                call.setInt(2, i);
                call.setString(3, message);
                call.execute();
                Chat+= "2 " + message;
                break;
            }
            default: break;
        }
        Chat += message + "\n";
        
        return true;
    }
    
    // Manager

    @Override
    public String SendClientsInfoToManager(int id_manager) throws SQLException {
        // Must return orderid login CarModel CarNumber, orderid login CarModel CarNumber
        Integer idorder = 0;
        final char dm = (char) 34;
        answer = "";
        String login = "";
        String model = "";
        String number = "";

        Statement stateorders = db.createStatement();
        ResultSet setorders = stateorders.executeQuery("SELECT " + dm + "IDORDER" + dm + " FROM " + dm + "Orders" + dm + " WHERE  " + dm + "MANAGERID" + dm + " = " + id_manager);

        while(setorders.next())
        {
            idorder = setorders.getInt(1);

            CallableStatement call_login = db.prepareCall("{call readuserlogin(?)}");
            call_login.setInt(1, idorder);
            call_login.execute();
            ResultSet set_login = call_login.getResultSet();

            CallableStatement call_model = db.prepareCall("{call readusermodel(?)}");
            call_model.setInt(1, idorder);
            call_model.execute();
            ResultSet set_model = call_model.getResultSet();

            CallableStatement call_number = db.prepareCall("{call readusernumber(?)}");
            call_number.setInt(1, idorder);
            call_number.execute();
            ResultSet set_number = call_number.getResultSet();

            while (set_login.next())
            {
                while (set_model.next())
                {
                    while (set_number.next())
                    {
                        login = set_login.getString(1);
                        model = set_model.getString(1);
                        number = set_number.getString(1);
                        answer += idorder.toString() + login  + model + number + ", ";
                    }
                }
            }
        }
        setorders.close();

        //answer = "1 username1 Lada e228ye 2 username2 Toyota e007uu 3 username3 Lada e228ye 4 username4 Toyota e007uu 5 username5 Lada e228ye 6 username6 Toyota e007uu";
        return answer;
    }

    @Override
    public String ChangeStatus(int id_rec, String status, int manager_id, String time) throws SQLException {
        // Must return true or false
        answer = "true";

        CallableStatement call_delete_status = db.prepareCall("{call deleteorder(?)}");
        call_delete_status.setInt(1, id_rec);
        call_delete_status.execute();
        if(!call_delete_status.execute())
        {
            answer = "false";
        }

        CallableStatement call_write_status = db.prepareCall("{call writeorder(?)}");
        call_write_status.setInt(1, id_rec);
        call_write_status.setString(2, status);
        call_write_status.setInt(3, manager_id);
        call_write_status.setString(4, time);
        call_write_status.execute();
        if(!call_write_status.execute())
        {
            answer = "false";
        }

        return answer;
    }

    @Override
    public String ChangeTime( int id_rec, String status, int manager_id, String time ) throws SQLException {
        // Must return true or false
        answer = "true";

        CallableStatement call_delete_status = db.prepareCall("{call deleteorder(?)}");
        call_delete_status.setInt(1, id_rec);
        call_delete_status.execute();
        if(!call_delete_status.execute())
        {
            answer = "false";
        }

        CallableStatement call_write_status = db.prepareCall("{call writeorder(?)}");
        call_write_status.setInt(1, id_rec);
        call_write_status.setString(2, status);
        call_write_status.setInt(3, manager_id);
        call_write_status.setString(4, time);
        call_write_status.execute();
        if(!call_write_status.execute())
        {
            answer = "false";
        }

        System.out.println(time);
        return answer;
    }

    // Admin Interface

    @Override
    public String ChangeManager(int id_rec, String status, int manager_id, String time) throws SQLException {
        // Must return true or false
        System.out.println("For order " + id_rec + " this manager " + manager_id + " is main now");
        answer = "true";

        CallableStatement call_delete_status = db.prepareCall("{call deleteorder(?)}");
        call_delete_status.setInt(1, id_rec);
        call_delete_status.execute();
        if(!call_delete_status.execute())
        {
            answer = "false";
        }

        CallableStatement call_write_status = db.prepareCall("{call writeorder(?)}");
        call_write_status.setInt(1, id_rec);
        call_write_status.setString(2, status);
        call_write_status.setInt(3, manager_id);
        call_write_status.setString(4, time);
        call_write_status.execute();
        if(!call_write_status.execute())
        {
            answer = "false";
        }

        return answer;
    }

    @Override
    public String SetManager(int idorder) throws SQLException {
        // Must return true or false
        System.out.println("User " + idorder + " is manager now/");
        answer = "true";

        CallableStatement call_login = db.prepareCall("{call readuserlogin(?)}");
        call_login.setInt(1, idorder);
        call_login.execute();
        ResultSet set_login = call_login.getResultSet();
        String login = set_login.getString(1);

        CallableStatement call_pass = db.prepareCall("{call readuserpass(?)}");
        call_pass.setInt(1, idorder);
        call_pass.execute();
        ResultSet set_pass = call_pass.getResultSet();
        String pass = set_pass.getString(1);

        CallableStatement call_model = db.prepareCall("{call readusermodel(?)}");
        call_model.setInt(1, idorder);
        call_model.execute();
        ResultSet set_model = call_model.getResultSet();
        String car_model = set_model.getString(1);

        CallableStatement call_number = db.prepareCall("{call readusernumber(?)}");
        call_number.setInt(1, idorder);
        call_number.execute();
        ResultSet set_number = call_model.getResultSet();
        String car_number = set_number.getString(1);

        CallableStatement call = db.prepareCall("{call updatemanager(?,?,?,?,?,?)}");
        call.setInt(1, idorder);
        call.setString(2, login);
        call.setString(3, pass);
        call.setString(4, car_model);
        call.setString(5, car_number);
        call.setInt(6,2);
        call.execute();

        if(!(call.execute() || call_login.execute() || call_pass.execute() || call_model.execute() || call_number.execute()))
        {
            answer = "false";
        }

        set_login.close();
        set_model.close();
        set_number.close();
        set_pass.close();
        return answer;
    }

    @Override
    public String RemoveManager(int idorder) throws SQLException {
        // Must return true or false
        System.out.println("Remove user " + idorder + " status manager.");
        answer = "true";

        CallableStatement call_login = db.prepareCall("{call readuserlogin(?)}");
        call_login.setInt(1, idorder);
        call_login.execute();
        ResultSet set_login = call_login.getResultSet();
        String login = set_login.getString(1);

        CallableStatement call_pass = db.prepareCall("{call readuserpass(?)}");
        call_pass.setInt(1, idorder);
        call_pass.execute();
        ResultSet set_pass = call_pass.getResultSet();
        String pass = set_pass.getString(1);

        CallableStatement call_model = db.prepareCall("{call readusermodel(?)}");
        call_model.setInt(1, idorder);
        call_model.execute();
        ResultSet set_model = call_model.getResultSet();
        String car_model = set_model.getString(1);

        CallableStatement call_number = db.prepareCall("{call readusernumber(?)}");
        call_number.setInt(1, idorder);
        call_number.execute();
        ResultSet set_number = call_model.getResultSet();
        String car_number = set_number.getString(1);

        CallableStatement call = db.prepareCall("{call updatemanager(?,?,?,?,?,?)}");
        call.setInt(1, idorder);
        call.setString(2, login);
        call.setString(3, pass);
        call.setString(4, car_model);
        call.setString(5, car_number);
        call.setInt(6,1);
        call.execute();

        if(!(call.execute() || call_login.execute() || call_pass.execute() || call_model.execute() || call_number.execute()))
        {
            answer = "false";
        }

        set_login.close();
        set_model.close();
        set_number.close();
        set_pass.close();

        return answer;
    }

    @Override
    public String SendAllUsersInfo() throws SQLException {
        // Must return id_user username root_user id_user username root_user
        answer = "";
        final char dm = (char) 34;
        Statement statement = db.createStatement();
        ResultSet set = statement.executeQuery("SELECT " + dm + "ID_ORDER" + dm + ", " + dm + "LOGIN" + dm + ", " + dm + "TYPE" + dm + " FROM " + dm + "Users" + dm);
        while(set.next())
        {
            Integer idorder = set.getInt(1);
            String login = set.getString(2);
            String type = set.getString(3);
            answer += idorder.toString() + " " + login + " " + type + " ";
        }
        return answer;
    }

    

    
}
