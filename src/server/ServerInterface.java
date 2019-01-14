package server;

import java.sql.SQLException;

public interface ServerInterface {

    String Registration(String Login, String pass, String car_model, String car_number) throws SQLException;
    // return true or false
    String Autorization(String Login, String pass) throws SQLException;
    // return user_root
    String SendCalendar() throws SQLException;

    String SendCarInfo(int id_rec) throws SQLException;

    String SendRecordInfo(int id_rec) throws SQLException;

    boolean ToBookATime(int id_rec, String time) throws SQLException;
    
    String SendChat(int id_rec) throws SQLException;
    
    boolean AddMessage(String message, int id_rec, int root) throws SQLException;
    
    // Manager

    String SendClientsInfoToManager(int id_manager) throws SQLException;

    String ChangeStatus(int id_rec, String status, int manager_id, String time) throws SQLException;

    String ChangeTime( int id_rec, String status, int manager_id, String time) throws SQLException;
    
    // AdminInterface

    String ChangeManager(int id_rec, String status, int manager_id, String time) throws SQLException;

    String SetManager(int id_user) throws SQLException;

    String RemoveManager(int id_user) throws SQLException;

    String SendAllUsersInfo() throws SQLException;
}
