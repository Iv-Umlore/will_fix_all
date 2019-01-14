package communication;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

import server.*;
import client.*;

import java.io.IOException;

public class ClientServerCommunicationTest {
    String host = "localhost";
    int port = 1234;

    @BeforeClass
    public static void SetupServerEnvironment() {
        try {
            ServerProtocol protocol = new ServerProtocol(1234,new FakeServer());
            protocol.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SuccessRegistrationTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.Registration("TrueLogin", "TruePassword", "Lada", "e330ye");

        Assert.assertTrue(answer);
    }

    @Test
    public void FailRegistrationTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.Registration("FalseLogin", "FalsePassword", "Lada", "e330ye");

        Assert.assertFalse(answer);
    }

    @Test
    public void UserAutorizationTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.Autorization("UserLogin", "UserPassword");

        Assert.assertEquals(answer, "1");
    }

    @Test
    public void ManagerAutorizationTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.Autorization("ManagerLogin", "ManagerPassword");

        Assert.assertEquals(answer, "2");
    }

    @Test
    public void AdminAutorizationTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.Autorization("AdminLogin", "AdminPassword");
        //Assert.assertTrue(answer.equalsIgnoreCase("3"));
        Assert.assertEquals(answer, "3");
    }

    @Test
    public void FailAutorizationTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.Autorization("FailLogin", "FailPassword");

        Assert.assertEquals(answer, "0");
    }

    @Test
    public void sendCalendarTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetCalendar();

        Assert.assertEquals(answer, "100101 1 110101 1 120101 0");
        // Parse to
        // 10:00 01 jan - reserved
        // 11:00 01 jan - reserved
        // 12:00 01 jan - free
    }

    @Test
    public void SuccessSendCarInfoTest() throws IOException  {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetCarInfo( 1 );

        Assert.assertEquals(answer, "lada r367vo 0");
    }

    @Test
    public void FailSendCarInfoTest() throws IOException  {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetCarInfo( -1 );

        Assert.assertEquals(answer, "ERROR ERROR -1");
    }

    @Test
    public void SuccessSendRecordInfo() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetRecordInfo( 1 );

        Assert.assertEquals(answer, "toyota r367vo 0");
    }

    @Test
    public void FailSendRecordInfoTest() throws IOException  {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetRecordInfo( -1 );

        Assert.assertEquals(answer, "ERROR ERROR -1");
    }


    @Test
    public void SuccessToBookATimeTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ToBookATime( 1, "10:00 01.01" );

        Assert.assertTrue(answer);
    }

    @Test
    public void FailToBookATimeTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ToBookATime( 1, "10:00 02.01" );

        Assert.assertFalse(answer);
    }

    @Test
    public void SuccessSendClientInfoToManagerTest()  throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetMyClientsInfo( 1);

        // iserid orderid login, userid orderid login ...
        Assert.assertEquals(answer, "1 1 username1 2 3 username2");
    }

    @Test
    public void FailSendClientInfoToManagerTest()  throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetMyClientsInfo( -1);

        // iserid orderid login, userid orderid login ...
        Assert.assertEquals(answer, "-1 -1 ERROR");
    }

    /*@Test
    public void SuccessChangeStatusTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ChangeStatus( 1, "status");

        Assert.assertTrue(answer);
    }*/

    /*@Test
    public void FalseChangeStatusTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ChangeStatus( -1, "status");

        Assert.assertFalse(answer);
    }*/

    /*@Test
    public void SuccessChangeTimeTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ChangeTime( 1,  "10:00 10.10");

        Assert.assertTrue(answer);
    }*/

    /*@Test
    public void FalseChangeTimeTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ChangeTime( -1, "10:00 10.10");

        Assert.assertFalse(answer);
    }*/

    /*@Test
    public void SuccessChangeManagerTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ChangeManager( 1,  1);

        Assert.assertTrue(answer);
    }*/

    /*@Test
    public void FalseChangeManagerTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.ChangeManager( -1, 1);

        Assert.assertFalse(answer);
    }*/

    @Test
    public void SuccessSetManagerTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.SetManager( 1);

        Assert.assertTrue(answer);
    }

    @Test
    public void FalseSetManagerTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.SetManager( -1);

        Assert.assertFalse(answer);
    }

    @Test
    public void SuccessRemoveManagerTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.RemoveManager( 1);

        Assert.assertTrue(answer);
    }

    @Test
    public void FalseRemoveManagerTest() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        boolean answer = speaker.RemoveManager( -1);

        Assert.assertFalse(answer);
    }

    @Test
    public void SendAllUsersInfo() throws IOException {
        ClientSpeaker speaker = new ClientSpeaker(host, port);
        String answer = speaker.GetAllUsersInfo();

        Assert.assertEquals(answer, "username1 1 username2 2");
    }
}
