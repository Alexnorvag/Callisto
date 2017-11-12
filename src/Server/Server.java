package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT  = 8080;
    public static List<Client> clients;

    private Socket client;
    private String name;

    public Server() {

        clients = new ArrayList<Client>();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");

            while (true) {
                client = serverSocket.accept();
                DataInputStream dis = new DataInputStream(client.getInputStream());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());

                name = dis.readUTF();
                System.out.println("Connected : " + name);
                Client user = new Client(name, dis, dos);
                clients.add(user);

                String enter_message = "{ \"name\" : \"" + "[ SERVER NOTICE ]" + "\", \"message\" : \"" + name +" Connected" + "\"}";
                System.out.println(enter_message);
                List<Client> entry = Server.clients;
                for (Client clnt : entry) {
                    DataOutputStream dataos = clnt.getDos();
                    dataos.writeUTF(enter_message);
                }

                System.out.println("Current User : " + Server.clients.size() + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
