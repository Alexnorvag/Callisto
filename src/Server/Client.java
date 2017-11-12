package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Client {

    private String name;
    private String message;
    private DataOutputStream dos;

    public DataOutputStream getDos() {
        return dos;
    }

    public Client(String name, DataInputStream dis, DataOutputStream dos) {
        this.name = name;
        this.dos = dos;

        new Thread(() -> {
            try {
                while (true) {
                    message = dis.readUTF();
                    System.out.println(message);
                    List<Client> entry = Server.clients;
                    for (Client clnt : entry) {
                        DataOutputStream dataos = clnt.getDos();
                        dataos.writeUTF(message);
                    }
                }
            } catch (IOException e) {
                try {
                    dis.close();
                    dos.close();
                    Server.clients = Server.clients.stream().filter(
                            clExmp -> {
                                if (!(clExmp == this)) {
                                    String exit_message = "{ \"name\" : \"" + "[ SERVER NOTICE ]" + "\", \"message\" : \"" + name + " Disconnected" + "\"}";
                                    System.out.println(exit_message);
                                    try {
                                        clExmp.getDos().writeUTF(exit_message);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                return !(clExmp == this);
                            }
                    ).collect(Collectors.toList());

                    System.out.println("[Current User : " + Server.clients.size() + "]");

                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }
}
