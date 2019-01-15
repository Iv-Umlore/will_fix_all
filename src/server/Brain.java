package server;
import java.sql.*;

public class Brain {

    // Database credentials
    static final String DB_URL = "jdbc:postgresql://dumbo.db.elephantsql.com:5432/evejlbgk";
    static final String USER = "evejlbgk";
    static final String PASS = "s2q8D9vPB9sa5QJTrgW2DaRU98JRHAjR";

    public static void main(String[] argv) throws SQLException {

        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
            CallableStatement callableStatement = null;
            System.out.println();
            Statement statement = connection.createStatement();
            final char dm = (char) 34;
            ResultSet set_users = statement.executeQuery("SELECT " + dm + "ID_ORDER"+ dm + ", " + dm + "LOGIN" + dm + ", "+ dm +
                    "PASSWORD" + dm + ", "+ dm + "MODEL" + dm + ", "+ dm + "NUMBER" + dm + ", "+ dm + "TYPE" + dm + " FROM " + dm + "Users" + dm);
            System.out.println("Данные из таблицы пользователей");
            System.out.println("ID_ORDER" + " " +"LOGIN" + " " + "PASSWORD" + " " + "MODEL" + " " + "NUMBER" + " " + "TYPE");
            while(set_users.next())
            {
                System.out.println(set_users.getInt(1) + "  " + set_users.getString(2) + "  "
                        + set_users.getString(3) + "    " + set_users.getString(4) + "  " + set_users.getString(5) + "  " + set_users.getInt(6));
            }
            set_users.close();
            System.out.println();

            ResultSet set_orders = statement.executeQuery("SELECT " + dm + "IDORDER"+ dm + ", " + dm + "STATUS" + dm + ", "+ dm +
                    "MANAGERID" + dm + ", "+ dm + "TIME" + dm + " FROM " + dm + "Orders" + dm);
            System.out.println("Данные из таблицы заказов");
            System.out.println("ID_ORDER" + " " + "STATUS" + " " + "MANAGERID" + " " + "TIME");
            while(set_orders.next())
            {
                System.out.println(set_orders.getInt(1) + "   " + set_orders.getString(2) + "  " + set_orders.getInt(3) + "  " + set_orders.getString(4));
            }
            set_orders.close();

            System.out.println();
            ResultSet set_chat = statement.executeQuery("SELECT " + dm + "IDORDER"+ dm + ", " + dm + "NUMMES" + dm + ", "+ dm +
                    "MESSAGE" + dm + " FROM " + dm + "Chat" + dm);
            System.out.println("Данные из таблицы чата");
            System.out.println("ID_ORDER" + "       " + "NUMBER_OF_MESSAGE" + "     " + "MESSAGE");
            while(set_chat.next())
            {
                System.out.println(set_chat.getInt(1) + "   " + set_chat.getInt(2) + "  " + set_chat.getString(3));
            }
            set_chat.close();
            System.out.println();

        } else {
            System.out.println("Failed to make connection to database");
        }
    }

}
