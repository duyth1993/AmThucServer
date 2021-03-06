/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amthuc.view;

import com.amthuc.dao.BillDAO;
import com.amthuc.dao.CategoryDAO;
import com.amthuc.dao.DishDAO;
import com.amthuc.dao.OrderDAO;
import com.amthuc.dao.OrderDetailsDAO;
import com.amthuc.dao.TableDAO;
import com.amthuc.dao.UserDAO;
import com.amthuc.model.Table;
import com.amthuc.model.User;
import com.amthuc.server.Client;
import com.amthuc.server.Message;
import com.amthuc.utils.GLOBAL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.jtattoo.plaf.bernstein.BernsteinLookAndFeel;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

/**
 *
 * @author Pia
 */
public class ServerFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public ServerFrame() {
        initComponents();
        menuPanel = new MenuPanel(this);
        try {
            // mở cổng đón các kết nối từ client
            myServer = new ServerSocket(port);

//            // Fake danh sách người dung
//            for (int i = 1; i <= 5; i++) {
//                User user = new User(i, "Bôi bàn " + i, "123456", 1, 1);
//                this.arrUsers.add(user);
//            }
            System.out.print("Server is running ... ");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void listening() throws NullPointerException, IOException {
        // vòng lặp vô hạn, đón các kết nối mọi lúc
        while (true) {
            // chấp nhận kết nối từ client
            Socket socket = myServer.accept();
            System.out.println(socket.getInetAddress() + " connected");
            // tạo 1 uuid để phân biệt giữa các client vs nhau
            UUID uuid = UUID.randomUUID();
            // khởi tạo 1 đối tượng đại diện cho client vừa kết nối tới
            // new ProcessMessage()
            Client client = new Client(this, socket, new ProcessMessage(),
                    uuid);
            // start client lên
            client.start();
            // quản lý các client hiện đang kết nối bằng cách thêm nó vào mảng các client
            arrClients.add(client);
        }
    }

    /*
     Hàm xử lý các message ở đây
     */
    public void ProcessMessage(String msg, Client client) throws SQLException, ClassNotFoundException {
        System.out.println("Dữ liệu từ client : ");
        System.out.println(msg);
        Gson gson = new GsonBuilder().create();
        // dữ liệu từ client theo định dạng Json, cần fai đọc dữ liệu json bằng hàm fromJson và ép kiểu về Message
        Message message = gson.fromJson(msg, Message.class);
        switch (message.getMsgID()) {
            case GLOBAL.FROM_CLIENT.CONNECT:
                confirmConnection(message, client);
                break;
            case GLOBAL.FROM_CLIENT.LOGIN:
                checkLogin(message, client);
                updateUsersList();
                break;

            case GLOBAL.FROM_CLIENT.LOGOUT:
                doLogout(message, client);
                updateUsersList();
                break;
            case GLOBAL.FROM_CLIENT.CHAT:
                sendChatMessage(message, client);
                break;

            case GLOBAL.FROM_CLIENT.LIST_CATEGORY:
                sendListCategory(message, client);
                break;

            case GLOBAL.FROM_CLIENT.LOAD_TABLES:
                sendListTable(message, client);
                break;

            case GLOBAL.FROM_CLIENT.RELOAD_TABLES:
                reloadTable(message, client);
                break;

            case GLOBAL.FROM_CLIENT.ADD_ORDER:
                addOrder(message, client);
                break;

            case GLOBAL.FROM_CLIENT.SHOW_ORDER:
                showOrder(message, client);
                break;

            case GLOBAL.FROM_CLIENT.UPDATE_ORDER:
                updateOrder(message, client);
                break;

            case GLOBAL.FROM_CLIENT.CANCEL_ORDER:
                cancelOrder(message, client);

            case GLOBAL.FROM_CLIENT.ADD_LINE_TO_ORDER:
                addLineOrder(message, client);
                break;

            case GLOBAL.FROM_CLIENT.UPDATE_LINE_OF_ORDER:
//                updateLineOrder(message, client);
                break;

            case GLOBAL.FROM_CLIENT.DELETE_LINE_OF_ORDER:
//                deleteLineOrder(message, client);
                break;

            case GLOBAL.FROM_CLIENT.LOAD_ORDER_DETAILS:
                loadOrderDetails(message, client);
                break;

            case GLOBAL.FROM_CLIENT.RELOAD_ORDER_DETAILS:
                reloadOrderDetails(message, client);
                break;

            case GLOBAL.FROM_CLIENT.UPDATE_ORDER_DETAILS_STATUS:
                updateOrderDetails(message, client);
                break;

            case GLOBAL.FROM_CLIENT.BILL:
                addBill(message, client);
                break;
        }
    }

    public void sendChatMessage(Message message, Client client) {
        try {
            System.out.println(message.getUser().getUsername() + " Gửi tin nhắn đến người dùng "
                    + message.getTarget().getUsername());
            for (Client cl : arrClients) {
                if (cl.getUser().getUsername().equals(message.getTarget().getUsername())) {
                    message.setMsgID(GLOBAL.TO_CLIENT.RECEIVED_CHAT_MESSAGE);
                    cl.sendMessage(message);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLogin(Message msg, Client client) throws SQLException, ClassNotFoundException {
        Message rs = new Message();
        User user = userDao.login(msg.getUser());
        rs.setMsgID(GLOBAL.TO_CLIENT.LOGIN);
        if (user != null) {
            rs.setMsg(GLOBAL.CONFIG.SUCCESS);
            user.setStatus(1);
            user.setPassword("");
            rs.setUser(user);
            client.setUser(user);
            this.arrUsers.add(user);
            System.out.println("Them nguoi choi " + arrUsers.size());
        } else {
            rs.setMsg(GLOBAL.CONFIG.FAIL);
            rs.setUser(user);
        }
        sendMessageToClient(rs, client);
    }

    private void doLogout(Message msg, Client client) {
        System.out.println("Người dùng đăng xuất");
        System.out.println(client.getUser().toString());
        for (User user : arrUsers) {
            if (user.getUsername().equals(client.getUser().getUsername())) {
                arrUsers.remove(user);
                break;
            }
        }
    }

    public void removeDisconnectedClient(Client client) {
        System.out.println("Disconneted " + client.getClient().getInetAddress()
                + " - " + client.getClient().getPort());
        for (User user : arrUsers) {
            if (user.getUsername().equals(client.getUser().getUsername())) {
                arrUsers.remove(user);
                break;
            }
        }
        this.arrClients.remove(client);
        updateUsersList();
    }

    private void updateUsersList() {
        System.out.println("Cập nhật danh sách người dùng ( "
                + arrUsers.size() + " ) : ");
        for (User player : arrUsers) {
            System.out.println(" ► " + player.getId() + " - "
                    + player.getUsername());
        }
        System.out.println("Cập nhật số kết nối ( " + arrClients.size()
                + ") : ");
        for (Client client : arrClients) {
            System.out.println(" ► " + client.getClient().getInetAddress()
                    + " - " + client.getClient().getPort());
            Message msg = new Message();
            msg.setArrUsers(arrUsers);
            msg.setMsgID(GLOBAL.TO_CLIENT.UPDATE_USERS);
            sendMessageToClient(msg, client);
        }
    }

    private void confirmConnection(Message msg, Client client) {
        sendMessageToClient(new Message(GLOBAL.TO_CLIENT.CONNECT, GLOBAL.CONFIG.SUCCESS),
                client);
    }

    private void sendMessageToClient(Message msg, Client client) {
        client.sendMessage(msg);
    }

    private void sendListCategory(Message message, Client client) throws ClassNotFoundException, SQLException {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.LIST_CATEGORY);
        mes.setArrCategories(categoryDAO.getAll());
        sendMessageToClient(mes, client);
    }

    private void sendListTable(Message message, Client client) throws ClassNotFoundException, SQLException {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.LOAD_TABLES);
        mes.setArrTables(MenuPanel.allTables);
        sendMessageToClient(mes, client);
    }

    private void addOrder(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.ADD_ORDER);
        try {
            orderDAO.insert(message.getOrder());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
            for (int i = 0; i < MenuPanel.allTables.size(); i++) {
                if (MenuPanel.allTables.get(i).getId() == message.getOrder().getOrderTable().getId()) {
                    MenuPanel.allTables.set(i, message.getOrder().getOrderTable());
                }
            }
            // reinit tables
            menuPanel.initAgain();
            // send to client list of new order details
            reloadOrderDetails(message, client);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            sendMessageToClient(mes, client);
        }
    }

    private void updateOrder(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.UPDATE_ORDER);
        try {
            orderDAO.update(message.getOrder());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            sendMessageToClient(mes, client);
        }
    }

//    private void addLineOrder(Message message, Client client) {
//        Message mes = new Message();
//        mes.setMsgID(GLOBAL.TO_CLIENT.ADD_LINE_TO_ORDER);
//        try {
//            orderDetailsDAO.insert(message.getOrderDetails());
//            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
//            mes.setMsg(GLOBAL.CONFIG.FAIL);
//        } catch (SQLException ex) {
//            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
//            mes.setMsg(GLOBAL.CONFIG.FAIL);
//        } finally {
//            sendMessageToClient(mes, client);
//        }
//    }
    private void updateLineOrder(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.UPDATE_LINE_OF_ORDER);
        try {
            orderDetailsDAO.update(message.getOrderDetails());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            sendMessageToClient(mes, client);
        }
    }

    private void deleteLineOrder(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.DELETE_LINE_OF_ORDER);
        try {
            orderDetailsDAO.delete(message.getOrderDetails());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } catch (SQLException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            sendMessageToClient(mes, client);
        }
    }

    private void reloadTable(Message message, Client client) {
        if (message.getArrTables() != null && message.getArrTables().size() > 0) {
            MenuPanel.allTables = message.getArrTables();
            // reinit tables
            menuPanel.initAgain();
            for (Client c : arrClients) {
                System.out.println(" ► " + c.getClient().getInetAddress()
                        + " - " + c.getClient().getPort());
                Message msg = new Message();
                msg.setMsgID(GLOBAL.TO_CLIENT.RELOAD_TABLES);
                msg.setArrTables(MenuPanel.allTables);
                sendMessageToClient(msg, client);

            }
        }
    }

    private void showOrder(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.SHOW_ORDER);
        try {
            int tableId = Integer.parseInt(message.getMsg().trim());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
            mes.setOrder(orderDAO.getOrderByTableID(tableId));
        } catch (Exception ex) {
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            client.sendMessage(mes);
        }
    }

    private void cancelOrder(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.CANCEL_ORDER);
        try {
            orderDAO.cancel(message.getOrder());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
            for (int i = 0; i < MenuPanel.allTables.size(); i++) {
                if (MenuPanel.allTables.get(i).getId() == message.getOrder().getOrderTable().getId()) {
                    MenuPanel.allTables.set(i, message.getOrder().getOrderTable());
                }
            }
            reloadTable(message, client);
            reloadOrderDetails(message, client);
        } catch (Exception ex) {
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            client.sendMessage(mes);
        }
    }

    private void addBill(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.BILL);
        try {
            billDAO.insert(message.getBill());
            message.getBill().getOrder().setStatus(GLOBAL.ORDER_AND_TABLE_STATUS.ORDER_BILLED);
            orderDAO.update(message.getBill().getOrder());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
        } catch (Exception ex) {
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            client.sendMessage(mes);
        }
    }

    private void addLineOrder(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.ADD_LINE_TO_ORDER);

    }

    private void loadOrderDetails(Message message, Client client) throws ClassNotFoundException, SQLException {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.LOAD_ORDER_DETAILS);
        mes.setArrOrderDetails(orderDetailsDAO.getAll());
        sendMessageToClient(mes, client);
    }

    private void updateOrderDetails(Message message, Client client) {
        Message mes = new Message();
        mes.setMsgID(GLOBAL.TO_CLIENT.UPDATE_ORDER_DETAILS_STATUS);
        try {
            orderDetailsDAO.update(message.getOrderDetails());
            mes.setMsg(GLOBAL.CONFIG.SUCCESS);
            mes.setOrderDetails(message.getOrderDetails());
        } catch (Exception ex) {
            mes.setMsg(GLOBAL.CONFIG.FAIL);
        } finally {
            sendMessageToClient(mes, client);
        }
    }

    private void reloadOrderDetails(Message message, Client client) throws ClassNotFoundException, SQLException {
        for (Client c : arrClients) {
            System.out.println(" ► " + c.getClient().getInetAddress()
                    + " - " + c.getClient().getPort());
            Message msg = new Message();
            msg.setMsgID(GLOBAL.TO_CLIENT.LOAD_ORDER_DETAILS);
            msg.setArrOrderDetails(orderDetailsDAO.getAll());
            sendMessageToClient(msg, client);
        }
    }

    /*
     Inner class ProcessMessage, override method MessageReceived trong interface onMessageReceived
     để lắng nghe sự kiện message gửi dến từ client
     */
    public class ProcessMessage implements Client.onMessageReceived {

        public void MessageReceived(String msg, Client uuid) {
            try {
                ProcessMessage(msg, uuid);
            } catch (SQLException ex) {
                Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainSplitPane = new javax.swing.JSplitPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );

        mainSplitPane.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        try {
//            UIManager.setLookAndFeel(new BernsteinLookAndFeel());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            ServerFrame server = null;
            server = new ServerFrame();
            server.setVisible(true);
            server.setDefaultCloseOperation(EXIT_ON_CLOSE);
            server.setResizable(false);
            server.setLocationRelativeTo(null);
            server.listening();
        } catch (NullPointerException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
//            java.awt.EventQueue.invokeLater(new Runnable() {
//                public void run() {
//                    
//                }
//            });
        //</editor-fold>
        //</editor-fold>
    }

    public JSplitPane getMainSplitPane() {
        return mainSplitPane;
    }

    public void setMainSplitPane(JSplitPane mainSplitPane) {
        this.mainSplitPane = mainSplitPane;
    }

    private MenuPanel menuPanel;
    private int port = 2015;
    private ServerSocket myServer;
    private ArrayList<Client> arrClients = new ArrayList<Client>();
    private ArrayList<User> arrUsers = new ArrayList<User>();
    private UserDAO userDao = new UserDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private DishDAO dishDAO = new DishDAO();
    private TableDAO tableDAO = new TableDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
    private BillDAO billDAO = new BillDAO();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane mainSplitPane;
    // End of variables declaration//GEN-END:variables
}
