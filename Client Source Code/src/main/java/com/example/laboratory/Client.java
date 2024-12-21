package com.example.laboratory;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
   private DataOutputStream dos;
   private DataInputStream dis;
    private String type = "";



    public String getType() {
        return type;
    }




    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream((socket.getInputStream()));
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error Creating client.");
            e.printStackTrace();
            closeEverything(socket, dis, dos);
        }


    }


    public void sendMessageToServer(String messageToServer) {
        try {
            dos.writeUTF(messageToServer);
            dos.flush();

        } catch (IOException e) {
            System.out.println("error sending message to server");
            closeEverything(socket, dis, dos);

        }
    }
    public String readMessageFromServer(){
        String messageFromServer=null;
        try{
           messageFromServer= dis.readUTF();
        }catch (IOException e){
            System.out.println("Error in reading from server");
            closeEverything(socket,dis,dos);
        }
        return messageFromServer;

    }






    // Add a callback to handle user type notification
    private Consumer<String> onUserTypeReceived;

    public void setOnUserTypeReceived(Consumer<String> onUserTypeReceived) {
        this.onUserTypeReceived = onUserTypeReceived;
    }


    public void closeEverything(Socket socket, DataInputStream dis, DataOutputStream dos) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (dis != null)
                dis.close();
            if (dos != null)
                dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
