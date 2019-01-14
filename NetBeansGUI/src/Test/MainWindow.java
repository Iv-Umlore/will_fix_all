/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import client.Calendar;
import client.Client;
import client.ClientInterface;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author Umlore
 */
public class MainWindow extends javax.swing.JFrame {

    private ClientInterface CI;
    private Calendar calend;
            
    private int _root;
    private String temp;
    private String answ;
    
    boolean flag;
    boolean IsLogin;
    /**
     * Creates new form Test
     */
    public MainWindow() {
        try {
            int port = 1234;
            String host = "localhost";
            CI = new Client(host,port);
        } catch (IOException ex) {
            System.out.println("Не создал Клиента\n Test()");
        }        
        flag = false;
        IsLogin = false;
        _root = 0;
        initComponents();
        MyCar.setVisible(false);
        MyOrders.setVisible(false);
        AllUsers.setVisible(false);
        Exit.setVisible(false);
        Autorization.setVisible(false);
        Orders.setVisible(false);
        Users.setVisible(false);
        MyCarWindow.setVisible(false);
        RegistrationPanel.setVisible(false);
        
        EmptyDate.setVisible(false);
        Help.setVisible(false);
        Reserved.setVisible(false);
        
        AllManagers.setVisible(false);
        ChangeManager.setVisible(false);
                
        calend = new Calendar(this);
        calend.start();
    }

    public void UpdateInfo(){
        UpdateCalendar();
        
        if (_root > 0) UpdateChat();
        if (_root > 1) UpdateOrders();
        if (_root > 2) UpdateUsers();
    }
    
    private void UpdateUsers() {
        answ = CI.OpenAllUsers();
        
        String temp2 = "";
        
        StringTokenizer stok = new StringTokenizer(answ, " ");
        int row = 0;
        
        ChooseClientUser.removeAllItems();
        ChooseManagerUser.removeAllItems();
        
        while (stok.hasMoreTokens()) {
            AllUsersTable.setValueAt(stok.nextToken(), row, 0);
            temp = stok.nextToken();
            AllUsersTable.setValueAt(temp, row, 1);
            temp2 = stok.nextToken();
            AllUsersTable.setValueAt(temp2, row, 2);
            
            switch (temp2){
                case "1": {
                    ChooseClientUser.addItem(temp);
                    break;
                }
                case "2": {
                    ChooseManagerUser.addItem(temp);
                    break;
                }
                default: break;
            }
            row++;
        }
        
        if (ChooseClientUser.getItemCount() == 0) ChooseClientUser.addItem("У нас нет клиентов (");
        if (ChooseManagerUser.getItemCount() == 0) ChooseManagerUser.addItem("У нас нет работников");
        AllManagers.setModel(ChooseManagerUser.getModel());
    }
    
    private void UpdateOrders(){
        answ = CI.OpenMyClients();
        StringTokenizer stok = new StringTokenizer(answ, " ");
        
        int i = 0;
        
        Clients.removeAllItems();
                
        while (stok.hasMoreTokens()){
            AllCars.setValueAt(stok.nextToken(), i, 0);
            temp = stok.nextToken();
            Clients.addItem(temp);
            AllCars.setValueAt(temp, i, 1);
            AllCars.setValueAt(stok.nextToken(), i, 2);
            AllCars.setValueAt(stok.nextToken(), i, 3);
            i++;
        }
        
        i = 0;
    }
    
    private void UpdateChat() {
        String chat = "";        
        String answ = ""; 
        StringTokenizer stok;
        if (IsLogin)
            answ = CI.OpenChat();
             stok = new StringTokenizer(answ, " ");
            
            while (stok.hasMoreTokens()){
                switch (stok.nextToken(" ")){
                    case "0": {
                       chat+= "Вы: ";
                       break;
                    }
                    case "1": {
                       chat+= "Менеджер: "; 
                       break;
                    }
                    case "2": {
                       chat+= "Администратор: "; 
                       break;
                    }
                    default: break;
                }
                
                chat += stok.nextToken("\n");
                chat+= "\n";
            }
            
            Chat.setText(chat);
    }
    
    private void UpdateCalendar() {
        
        String calendar = CI.UpdateCalendar();
        
        if (!flag){
            for (int i = 0; i < 7; i++) {
                Table1.setValueAt((i+12) + " января", 0, i);
                Table2.setValueAt((i+20) + " января", 0, i);
            }
            flag = true;
        }       
        
        for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 8; j++) {
                    temp = "1" + j + ":00";
                    Table1.setValueAt(temp, j+1, i);
                    Table2.setValueAt(temp, j+1, i);
                }
            }
        
        EmptyDate.removeAllItems();
        
        for (int date = 12; date < 26; date++)
            for (int hour = 10; hour < 18; hour++)
                EmptyDate.addItem(hour + ":00 " + date + ".01");
        
        StringTokenizer stok = new StringTokenizer(calendar, " ");
        String date;
        String str = "";
        String newS = "";
        while (stok.hasMoreTokens()) {
            date = stok.nextToken();
            str = date.substring(2,4);
                        
            if (Integer.parseInt(str) < 19 ) {
                newS = stok.nextToken();
                if (newS.equalsIgnoreCase("0")) {
                    Table1.setValueAt((date.substring(0,2)) + ":00", Integer.parseInt(date.substring(0,2)) - 9, Integer.parseInt(date.substring(2,4))- 12);
                }
                if (newS.equalsIgnoreCase("1")) {
                    Table1.setValueAt("Reserved", Integer.parseInt(date.substring(0,2)) - 9, Integer.parseInt(date.substring(2,4))- 12);
                    EmptyDate.removeItem((date.substring(0,2)) + ":00 " + date.substring(2,4) + ".01");
                }
            }
            else {
                newS = stok.nextToken();
                if (newS.equalsIgnoreCase("0")) {
                    Table2.setValueAt((date.substring(0,2)) + ":00", Integer.parseInt(date.substring(0,2)) - 9, Integer.parseInt(date.substring(2,4))- 19);
                }
                if (newS.equalsIgnoreCase("1")) {
                    Table2.setValueAt("Reserved", Integer.parseInt(date.substring(0,2)) - 9, Integer.parseInt(date.substring(2,4))- 19);
                    EmptyDate.removeItem((date.substring(0,2)) + ":00 " + date.substring(2,4) + ".01");
                }
            }            
        }
        if (EmptyDate.getItemCount() == 0) EmptyDate.addItem("Свободного времени нет");
        EmptyDate2.setModel(EmptyDate.getModel());       
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Levels = new javax.swing.JLayeredPane();
        TimeTable = new javax.swing.JPanel();
        Registration = new javax.swing.JButton();
        AutorizationButton = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        Table1 = new javax.swing.JTable();
        Table2 = new javax.swing.JTable();
        Reserved = new javax.swing.JButton();
        Help = new javax.swing.JLabel();
        MyCar = new javax.swing.JButton();
        MyOrders = new javax.swing.JButton();
        AllUsers = new javax.swing.JButton();
        EmptyDate = new javax.swing.JComboBox<>();
        Autorization = new javax.swing.JPanel();
        Send = new javax.swing.JButton();
        LoginWind = new javax.swing.JTextField();
        LogHelp = new javax.swing.JLabel();
        PassHelp = new javax.swing.JLabel();
        PassWind = new javax.swing.JPasswordField();
        CanselAutorization = new javax.swing.JButton();
        MyCarWindow = new javax.swing.JPanel();
        CanselMyCar = new javax.swing.JButton();
        MyCarModel = new javax.swing.JLabel();
        MyCarStatus = new javax.swing.JLabel();
        ChatPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Chat = new javax.swing.JTextArea();
        Message = new javax.swing.JTextField();
        SendMessage = new javax.swing.JButton();
        ChangeTimeB = new javax.swing.JButton();
        ChangeStatus = new javax.swing.JButton();
        NewStatusH = new javax.swing.JLabel();
        NewOrderStatus = new javax.swing.JTextField();
        EmptyDate2 = new javax.swing.JComboBox<>();
        ChangeManager = new javax.swing.JButton();
        AllManagers = new javax.swing.JComboBox<>();
        Orders = new javax.swing.JPanel();
        Clients = new javax.swing.JComboBox<>();
        CanselOrders = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        AllCars = new javax.swing.JTable();
        OpenOrder = new javax.swing.JButton();
        Users = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AllUsersTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        ChooseClientUser = new javax.swing.JComboBox<>();
        SetManager = new javax.swing.JButton();
        DeleteManager = new javax.swing.JButton();
        CanselUsers = new javax.swing.JButton();
        ChooseManagerUser = new javax.swing.JComboBox<>();
        RegistrationPanel = new javax.swing.JPanel();
        LoginH = new javax.swing.JLabel();
        PassH = new javax.swing.JLabel();
        CarModelH = new javax.swing.JLabel();
        CarNumbH = new javax.swing.JLabel();
        SentForRegistration = new javax.swing.JButton();
        CanselRegistration = new javax.swing.JButton();
        RegLogin = new javax.swing.JTextField();
        RegCarModel = new javax.swing.JTextField();
        RegCarNumb = new javax.swing.JTextField();
        RegistrationStatus = new javax.swing.JTextField();
        Password1 = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        Password2 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Registration.setBackground(new java.awt.Color(180, 180, 180));
        Registration.setText("Регистрация");
        Registration.setFocusable(false);
        Registration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegistrationActionPerformed(evt);
            }
        });

        AutorizationButton.setBackground(new java.awt.Color(180, 180, 180));
        AutorizationButton.setText("Авторизоваться");
        AutorizationButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AutorizationButton.setFocusable(false);
        AutorizationButton.setMaximumSize(new java.awt.Dimension(123, 28));
        AutorizationButton.setMinimumSize(new java.awt.Dimension(123, 28));
        AutorizationButton.setPreferredSize(new java.awt.Dimension(123, 28));
        AutorizationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutorizationButtonActionPerformed(evt);
            }
        });

        Exit.setBackground(new java.awt.Color(180, 180, 180));
        Exit.setText("Выход из учётной записи");
        Exit.setBorderPainted(false);
        Exit.setFocusable(false);
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });

        Table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Привет", "Привет", "Привет", "Привет", "Привет", "Привет", "Привет"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Table1.setEnabled(false);
        Table1.setFocusable(false);
        Table1.getTableHeader().setReorderingAllowed(false);

        Table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "8 Января", "9 Января", "10 Января", "11 Января", "12 Января", "13 Января", "14 Января"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Table2.setFocusable(false);
        Table2.getTableHeader().setReorderingAllowed(false);

        Reserved.setBackground(new java.awt.Color(180, 200, 180));
        Reserved.setText("Зарезервировать время");
        Reserved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReservedActionPerformed(evt);
            }
        });

        Help.setText("Выберите удобное свободное время и нажмите на");

        MyCar.setBackground(new java.awt.Color(180, 180, 180));
        MyCar.setText("Машина");
        MyCar.setFocusable(false);
        MyCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyCarActionPerformed(evt);
            }
        });

        MyOrders.setBackground(new java.awt.Color(180, 180, 180));
        MyOrders.setText("Заказы");
        MyOrders.setFocusable(false);
        MyOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyOrdersActionPerformed(evt);
            }
        });

        AllUsers.setBackground(new java.awt.Color(180, 180, 180));
        AllUsers.setText("Все пользователи");
        AllUsers.setFocusable(false);
        AllUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllUsersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TimeTableLayout = new javax.swing.GroupLayout(TimeTable);
        TimeTable.setLayout(TimeTableLayout);
        TimeTableLayout.setHorizontalGroup(
            TimeTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TimeTableLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TimeTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TimeTableLayout.createSequentialGroup()
                        .addComponent(MyCar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MyOrders)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AllUsers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(Exit)
                        .addGap(2, 2, 2)
                        .addComponent(AutorizationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(Registration))
                    .addGroup(TimeTableLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(Help)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EmptyDate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Reserved, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addComponent(Table2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Table1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        TimeTableLayout.setVerticalGroup(
            TimeTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TimeTableLayout.createSequentialGroup()
                .addGroup(TimeTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Registration)
                    .addComponent(AutorizationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Exit)
                    .addComponent(MyCar)
                    .addComponent(MyOrders)
                    .addComponent(AllUsers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Table1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Table2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(TimeTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Reserved)
                    .addComponent(Help)
                    .addComponent(EmptyDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Autorization.setLayout(null);

        Send.setText("Отправить");
        Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendActionPerformed(evt);
            }
        });
        Autorization.add(Send);
        Send.setBounds(200, 120, 89, 23);
        Autorization.add(LoginWind);
        LoginWind.setBounds(280, 30, 120, 30);

        LogHelp.setText("Логин");
        Autorization.add(LogHelp);
        LogHelp.setBounds(210, 40, 78, 14);

        PassHelp.setText("Пароль");
        Autorization.add(PassHelp);
        PassHelp.setBounds(210, 80, 78, 14);
        Autorization.add(PassWind);
        PassWind.setBounds(280, 70, 120, 30);

        CanselAutorization.setText("Отмена");
        CanselAutorization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CanselAutorizationActionPerformed(evt);
            }
        });
        Autorization.add(CanselAutorization);
        CanselAutorization.setBounds(310, 120, 90, 23);

        CanselMyCar.setText("Назад");
        CanselMyCar.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CanselMyCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CanselMyCarActionPerformed(evt);
            }
        });

        MyCarModel.setText("Марка машины + номер");
        MyCarModel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        MyCarStatus.setText("Статус заказа");
        MyCarStatus.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        Chat.setColumns(20);
        Chat.setRows(5);
        Chat.setFocusable(false);
        jScrollPane2.setViewportView(Chat);

        Message.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MessageKeyPressed(evt);
            }
        });

        SendMessage.setText("Отправить");
        SendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendMessageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ChatPanelLayout = new javax.swing.GroupLayout(ChatPanel);
        ChatPanel.setLayout(ChatPanelLayout);
        ChatPanelLayout.setHorizontalGroup(
            ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ChatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(Message, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ChatPanelLayout.setVerticalGroup(
            ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ChatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SendMessage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ChangeTimeB.setText("Изменить время заказа");
        ChangeTimeB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeTimeBActionPerformed(evt);
            }
        });

        ChangeStatus.setText("Изменить статус");
        ChangeStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeStatusActionPerformed(evt);
            }
        });

        NewStatusH.setText("Новый статус:");

        NewOrderStatus.setText("Готово");

        ChangeManager.setText("Сменить менеджера");
        ChangeManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeManagerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MyCarWindowLayout = new javax.swing.GroupLayout(MyCarWindow);
        MyCarWindow.setLayout(MyCarWindowLayout);
        MyCarWindowLayout.setHorizontalGroup(
            MyCarWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChatPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(MyCarWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MyCarWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MyCarWindowLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CanselMyCar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MyCarWindowLayout.createSequentialGroup()
                        .addComponent(AllManagers, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(MyCarWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(MyCarWindowLayout.createSequentialGroup()
                                .addComponent(ChangeManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(EmptyDate2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChangeTimeB))
                            .addGroup(MyCarWindowLayout.createSequentialGroup()
                                .addComponent(NewStatusH, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(NewOrderStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChangeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MyCarWindowLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(MyCarModel, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MyCarStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        MyCarWindowLayout.setVerticalGroup(
            MyCarWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MyCarWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CanselMyCar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MyCarWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MyCarStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MyCarModel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MyCarWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ChangeStatus)
                    .addComponent(NewOrderStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NewStatusH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(MyCarWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ChangeTimeB)
                    .addComponent(EmptyDate2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChangeManager)
                    .addComponent(AllManagers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        CanselOrders.setText("Назад");
        CanselOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CanselOrdersActionPerformed(evt);
            }
        });

        AllCars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Номер клиента", "Никнейм", "Модель машины", "Номер машины"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(AllCars);

        OpenOrder.setText("Открыть заказ");
        OpenOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OrdersLayout = new javax.swing.GroupLayout(Orders);
        Orders.setLayout(OrdersLayout);
        OrdersLayout.setHorizontalGroup(
            OrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(OrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CanselOrders, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Clients, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OpenOrder, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        OrdersLayout.setVerticalGroup(
            OrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(OrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(OrdersLayout.createSequentialGroup()
                        .addComponent(CanselOrders)
                        .addGap(18, 18, 18)
                        .addComponent(Clients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OpenOrder)))
                .addGap(62, 62, 62))
        );

        AllUsersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Пользователя", "Имя пользователя", "Права пользователя"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(AllUsersTable);

        jLabel1.setText("Пользователь");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        SetManager.setText("Сделать менеджером");
        SetManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetManagerActionPerformed(evt);
            }
        });

        DeleteManager.setText("Отменить статус менеджера");
        DeleteManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteManagerActionPerformed(evt);
            }
        });

        CanselUsers.setText("Назад");
        CanselUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CanselUsersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UsersLayout = new javax.swing.GroupLayout(Users);
        Users.setLayout(UsersLayout);
        UsersLayout.setHorizontalGroup(
            UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChooseManagerUser, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(UsersLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(SetManager, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DeleteManager, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ChooseClientUser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(UsersLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, UsersLayout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(CanselUsers)))))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        UsersLayout.setVerticalGroup(
            UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsersLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(UsersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChooseClientUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SetManager)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ChooseManagerUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DeleteManager)
                .addGap(18, 18, 18)
                .addComponent(CanselUsers)
                .addGap(34, 34, 34))
        );

        LoginH.setText("Логин");

        PassH.setText("Пароль");

        CarModelH.setText("Марка машины");

        CarNumbH.setText("Номер машины");

        SentForRegistration.setText("Зарегистрироваться");
        SentForRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SentForRegistrationActionPerformed(evt);
            }
        });

        CanselRegistration.setText("Назад");
        CanselRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CanselRegistrationActionPerformed(evt);
            }
        });

        RegistrationStatus.setText("Заполните все поля для успешной регистрации");
        RegistrationStatus.setEnabled(false);

        jLabel2.setText("Подтвердите пароль");

        jLabel3.setText("Все поля не должны  содержать пробелов");

        jLabel4.setText("Пример: Лада, Lada, BMW");

        jLabel5.setText("Пример: н901по");

        javax.swing.GroupLayout RegistrationPanelLayout = new javax.swing.GroupLayout(RegistrationPanel);
        RegistrationPanel.setLayout(RegistrationPanelLayout);
        RegistrationPanelLayout.setHorizontalGroup(
            RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegistrationPanelLayout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RegistrationPanelLayout.createSequentialGroup()
                        .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(RegistrationPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(RegistrationPanelLayout.createSequentialGroup()
                                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(LoginH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PassH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CarNumbH, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                    .addComponent(CarModelH, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(RegLogin)
                                    .addComponent(RegCarModel)
                                    .addComponent(RegCarNumb)
                                    .addComponent(Password1)
                                    .addComponent(Password2)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RegistrationPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(137, 137, 137))
                    .addGroup(RegistrationPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(SentForRegistration)
                        .addGap(29, 29, 29)
                        .addComponent(CanselRegistration, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(240, 240, 240))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RegistrationPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RegistrationStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        RegistrationPanelLayout.setVerticalGroup(
            RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegistrationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LoginH, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PassH, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Password1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Password2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CarModelH, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegCarModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CarNumbH, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegCarNumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(44, 44, 44)
                .addGroup(RegistrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SentForRegistration)
                    .addComponent(CanselRegistration))
                .addGap(18, 18, 18)
                .addComponent(RegistrationStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
        );

        Levels.setLayer(TimeTable, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Levels.setLayer(Autorization, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Levels.setLayer(MyCarWindow, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Levels.setLayer(Orders, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Levels.setLayer(Users, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Levels.setLayer(RegistrationPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout LevelsLayout = new javax.swing.GroupLayout(Levels);
        Levels.setLayout(LevelsLayout);
        LevelsLayout.setHorizontalGroup(
            LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(TimeTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Autorization, javax.swing.GroupLayout.PREFERRED_SIZE, 667, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 10, Short.MAX_VALUE)
                    .addComponent(MyCarWindow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 11, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Orders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Users, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(RegistrationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LevelsLayout.setVerticalGroup(
            LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(TimeTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Autorization, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(MyCarWindow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Orders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LevelsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Users, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(LevelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LevelsLayout.createSequentialGroup()
                    .addGap(0, 11, Short.MAX_VALUE)
                    .addComponent(RegistrationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Levels)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Levels)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegistrationActionPerformed
        TimeTable.setVisible(false);
        RegistrationPanel.setVisible(true);
    }//GEN-LAST:event_RegistrationActionPerformed

    private void AutorizationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AutorizationButtonActionPerformed
        Autorization.setVisible(true);
        TimeTable.setVisible(false);        
    }//GEN-LAST:event_AutorizationButtonActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        SetVisionButton(0);
    }//GEN-LAST:event_ExitActionPerformed

    private void ReservedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReservedActionPerformed
        
        CI.ToBookATime(EmptyDate.getItemAt(EmptyDate.getSelectedIndex()));
        // Изменить параметр в скором времени
    }//GEN-LAST:event_ReservedActionPerformed

    private void SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendActionPerformed
        // send login pass
        // resive User_id + root
        // _root = root;
        _root = CI.Autorization(LoginWind.getText(), PassWind.getText());
        if (_root > 0) {
            LoginWind.setText("");
            PassWind.setText("");
        }
        SetVisionButton(_root);
        UpdateInfo();
    }//GEN-LAST:event_SendActionPerformed

    private void CanselAutorizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CanselAutorizationActionPerformed
        TimeTable.setVisible(true);
        Autorization.setVisible(false);
    }//GEN-LAST:event_CanselAutorizationActionPerformed

    private void MyOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyOrdersActionPerformed
        Orders.setVisible(true);
        
        // Куда-то это деть потом
        TimeTable.setVisible(false);
    }//GEN-LAST:event_MyOrdersActionPerformed

    private void AllUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllUsersActionPerformed
        Users.setVisible(true);
        
        CI.OpenAllUsers();
        // Куда-то это деть потом
        TimeTable.setVisible(false);
    }//GEN-LAST:event_AllUsersActionPerformed

    private void MyCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyCarActionPerformed
        EmptyDate2.setVisible(false);
        if (_root > 1) {            
                ChangeTimeB.setVisible(false);
                ChangeStatus.setVisible(false);
                NewStatusH.setVisible(false);
                NewOrderStatus.setVisible(false);
        }
        
        MyCarWindow.setVisible(true);
        
        temp = CI.GetMyCar();
        StringTokenizer stok = new StringTokenizer(temp, " ");
        MyCarModel.setText(stok.nextToken() + " " + stok.nextToken());
        MyCarStatus.setText(stok.nextToken("\r?\n"));
                
        TimeTable.setVisible(false);
    }//GEN-LAST:event_MyCarActionPerformed

    private void CanselOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CanselOrdersActionPerformed
        Orders.setVisible(false);
        TimeTable.setVisible(true);
    }//GEN-LAST:event_CanselOrdersActionPerformed

    private void CanselUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CanselUsersActionPerformed
        Users.setVisible(false);
        TimeTable.setVisible(true);
    }//GEN-LAST:event_CanselUsersActionPerformed

    private void CanselRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CanselRegistrationActionPerformed
        TimeTable.setVisible(true);
        RegistrationPanel.setVisible(false);
    }//GEN-LAST:event_CanselRegistrationActionPerformed

    private void SentForRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SentForRegistrationActionPerformed
        if (Password1.getText().equalsIgnoreCase(Password2.getText())) {
            if ((RegLogin.getText().length() < 80) && (Password1.getText().length() < 80) && (RegCarModel.getText().length() < 80) && (RegCarNumb.getText().length() < 80)) {
                if ((RegLogin.getText().length() > 0) && (Password1.getText().length() > 0) && (RegCarModel.getText().length() > 0) && (RegCarNumb.getText().length() > 0) ) { 
                    if ((RegLogin.getText().indexOf(' ') != -1) && (Password1.getText().indexOf(' ') != -1) && (RegCarModel.getText().indexOf(' ') != -1) && (RegCarNumb.getText().indexOf(' ') != -1)) {
                        RegistrationStatus.setText("Отправляю");
                        boolean result = CI.Registration(RegLogin.getText(), Password1.getText(), RegCarModel.getText(), RegCarNumb.getText());
                        if (result) RegistrationStatus.setText("Регистрация прошла успешно!");
                        else RegistrationStatus.setText("Ошибка регистрации. Попробуйте изменить пароль/логин");
                    }
                    else RegistrationStatus.setText("Поля не должны содержать пробелов");
                }
                else RegistrationStatus.setText("Пожалуйста, заполните все поля");
            }
            else RegistrationStatus.setText("Слишком длинные данные(больше 80 симв.)");
        }
        else RegistrationStatus.setText("Пароли не совпадают");
    }//GEN-LAST:event_SentForRegistrationActionPerformed

    private void CanselMyCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CanselMyCarActionPerformed
        Message.setText("");
        EmptyDate2.setVisible(true);
        
        if (_root > 1) {            
                ChangeTimeB.setVisible(true);
                ChangeStatus.setVisible(true);
                NewStatusH.setVisible(true);
                NewOrderStatus.setVisible(true);
                CI.RemoveIdRec();
        }        
        MyCarWindow.setVisible(false);
        TimeTable.setVisible(true);
    }//GEN-LAST:event_CanselMyCarActionPerformed

    private void SendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendMessageActionPerformed
        if (CI.SendMessage(Message.getText())) {
            answ = CI.OpenChat();        
            Chat.setText(answ);
            Message.setText("");
        }
    }//GEN-LAST:event_SendMessageActionPerformed

    private void OpenOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenOrderActionPerformed
        int id = 0;
        for (int i = 0;i < 10; i++) {
            if (AllCars.getValueAt(i, 1).equals(Clients.getItemAt(Clients.getSelectedIndex()))) {
                id = Integer.parseInt((String) AllCars.getValueAt(i,0));
                break;
            }
        // id нужного нам пользователя
        }
        
        answ = CI.OpenRecord(id);
                        
        MyCarWindow.setVisible(true);
        
        StringTokenizer stok = new StringTokenizer(answ, " ");
        MyCarModel.setText(stok.nextToken() + " " + stok.nextToken());
        MyCarStatus.setText(stok.nextToken("\r?\n"));
        EmptyDate2.setVisible(true);
        Orders.setVisible(false);        
    }//GEN-LAST:event_OpenOrderActionPerformed

    private void ChangeStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeStatusActionPerformed
        CI.ChangeStatus(NewOrderStatus.getText());
    }//GEN-LAST:event_ChangeStatusActionPerformed

    private void ChangeTimeBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeTimeBActionPerformed
        CI.ChangeTime(EmptyDate.getItemAt(EmptyDate.getSelectedIndex()));
    }//GEN-LAST:event_ChangeTimeBActionPerformed

    private void SetManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetManagerActionPerformed
        int id = 0;
        for (int i = 0;i < 24; i++) {
            if (AllUsersTable.getValueAt(i, 1).equals(ChooseClientUser.getItemAt(ChooseClientUser.getSelectedIndex()))) {
                id = Integer.parseInt((String) AllCars.getValueAt(i,0));
                break;
            }
        // id нужного нам пользователя
        }
        
        CI.SetManager(id);
        
    }//GEN-LAST:event_SetManagerActionPerformed

    private void DeleteManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteManagerActionPerformed
        int id = 0;
        for (int i = 0;i < 24; i++) {
            if (AllUsersTable.getValueAt(i, 1).equals(ChooseManagerUser.getItemAt(ChooseManagerUser.getSelectedIndex()))) {
                id = Integer.parseInt((String) AllCars.getValueAt(i,0));
                break;
            }
        // id нужного нам пользователя
        }
        
        CI.RemoveManager(id);
        
    }//GEN-LAST:event_DeleteManagerActionPerformed

    private void ChangeManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeManagerActionPerformed
        int id = 0;
        for (int i = 0;i < 24; i++) {
            if (AllUsersTable.getValueAt(i, 1).equals(ChooseManagerUser.getItemAt(ChooseManagerUser.getSelectedIndex()))) {
                id = Integer.parseInt((String) AllCars.getValueAt(i,0));
                break;
            }
        // id нужного нам пользователя
        }
        
        CI.ChangeManager(id);
        
    }//GEN-LAST:event_ChangeManagerActionPerformed

    private void MessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MessageKeyPressed
        if (Message.getText().length() >= 78) SendMessage.setEnabled(false);
        if (Message.getText().length() < 78) SendMessage.setEnabled(true);
    }//GEN-LAST:event_MessageKeyPressed

    private void SetVisionButton(int root){
        switch (root){
            case 0: {
                AutorizationButton.setVisible(true);
                Registration.setVisible(true);
                Exit.setVisible(false);
                MyCar.setVisible(false);
                MyOrders.setVisible(false);
                AllUsers.setVisible(false);
                
                EmptyDate.setVisible(false);
                Help.setVisible(false);
                Reserved.setVisible(false);
                
                AllManagers.setVisible(false);
                ChangeManager.setVisible(false);
                
                break;
            }
            case 1: {
                TimeTable.setVisible(true);
                MyCar.setVisible(true);
                AutorizationButton.setVisible(false);
                Registration.setVisible(false);
                MyOrders.setVisible(false);
                AllUsers.setVisible(false);
                Exit.setVisible(true);
                ChangeTimeB.setVisible(false);
                ChangeStatus.setVisible(false);
                NewStatusH.setVisible(false);
                NewOrderStatus.setVisible(false);
                Autorization.setVisible(false);
                
                EmptyDate.setVisible(true);
                Help.setVisible(true);
                Reserved.setVisible(true);
                
                AllManagers.setVisible(false);
                ChangeManager.setVisible(false);
                
                break;
            }
            case 2: {
                TimeTable.setVisible(true);
                Exit.setVisible(true);
                MyCar.setVisible(true);
                MyOrders.setVisible(true);
                AllUsers.setVisible(false);            
                AutorizationButton.setVisible(false);
                Registration.setVisible(false);                
                Autorization.setVisible(false);
                
                EmptyDate.setVisible(true);
                Help.setVisible(true);
                Reserved.setVisible(true);
                
                AllManagers.setVisible(false);
                ChangeManager.setVisible(false);
                
                break;
            }
            case 3: {
                TimeTable.setVisible(true);
                Exit.setVisible(true);
                MyCar.setVisible(true);
                MyOrders.setVisible(true);
                AllUsers.setVisible(true);            
                AutorizationButton.setVisible(false);
                Registration.setVisible(false);                
                Autorization.setVisible(false);
                
                EmptyDate.setVisible(true);
                Help.setVisible(true);
                Reserved.setVisible(true);
                
                AllManagers.setVisible(true);
                ChangeManager.setVisible(true);
                
                break; 
            }
            default: {
                break;
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AllCars;
    private javax.swing.JComboBox<String> AllManagers;
    private javax.swing.JButton AllUsers;
    private javax.swing.JTable AllUsersTable;
    private javax.swing.JPanel Autorization;
    private javax.swing.JButton AutorizationButton;
    private javax.swing.JButton CanselAutorization;
    private javax.swing.JButton CanselMyCar;
    private javax.swing.JButton CanselOrders;
    private javax.swing.JButton CanselRegistration;
    private javax.swing.JButton CanselUsers;
    private javax.swing.JLabel CarModelH;
    private javax.swing.JLabel CarNumbH;
    private javax.swing.JButton ChangeManager;
    private javax.swing.JButton ChangeStatus;
    private javax.swing.JButton ChangeTimeB;
    private javax.swing.JTextArea Chat;
    private javax.swing.JPanel ChatPanel;
    private javax.swing.JComboBox<String> ChooseClientUser;
    private javax.swing.JComboBox<String> ChooseManagerUser;
    private javax.swing.JComboBox<String> Clients;
    private javax.swing.JButton DeleteManager;
    private javax.swing.JComboBox<String> EmptyDate;
    private javax.swing.JComboBox<String> EmptyDate2;
    private javax.swing.JButton Exit;
    private javax.swing.JLabel Help;
    private javax.swing.JLayeredPane Levels;
    private javax.swing.JLabel LogHelp;
    private javax.swing.JLabel LoginH;
    private javax.swing.JTextField LoginWind;
    private javax.swing.JTextField Message;
    private javax.swing.JButton MyCar;
    private javax.swing.JLabel MyCarModel;
    private javax.swing.JLabel MyCarStatus;
    private javax.swing.JPanel MyCarWindow;
    private javax.swing.JButton MyOrders;
    private javax.swing.JTextField NewOrderStatus;
    private javax.swing.JLabel NewStatusH;
    private javax.swing.JButton OpenOrder;
    private javax.swing.JPanel Orders;
    private javax.swing.JLabel PassH;
    private javax.swing.JLabel PassHelp;
    private javax.swing.JPasswordField PassWind;
    private javax.swing.JPasswordField Password1;
    private javax.swing.JPasswordField Password2;
    private javax.swing.JTextField RegCarModel;
    private javax.swing.JTextField RegCarNumb;
    private javax.swing.JTextField RegLogin;
    private javax.swing.JButton Registration;
    private javax.swing.JPanel RegistrationPanel;
    private javax.swing.JTextField RegistrationStatus;
    private javax.swing.JButton Reserved;
    private javax.swing.JButton Send;
    private javax.swing.JButton SendMessage;
    private javax.swing.JButton SentForRegistration;
    private javax.swing.JButton SetManager;
    private javax.swing.JTable Table1;
    private javax.swing.JTable Table2;
    private javax.swing.JPanel TimeTable;
    private javax.swing.JPanel Users;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
