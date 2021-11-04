package com.amna.GroupChat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final String clientName;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public Client(Socket socket, String clientName) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clientName = clientName;
    }

    private void sendMsgs(){
       try {
           Scanner sc = new Scanner(System.in);
           writer.write(clientName);
           writer.newLine();
           writer.flush();
           while (socket.isConnected()){
               System.out.print(clientName+":");
               String msg = sc.nextLine();
               writer.write(clientName+": "+msg);
               writer.newLine();
               writer.flush();

           }
       } catch(IOException e) {
            close();
        }
    }

    private void close(){
        System.out.println();
        try {
            if(socket !=null){
                socket.close();
            }
            if(reader != null){
                reader.close();
            }
            if (writer != null){
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToMsgs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;
                while (socket.isConnected()){
                    try {

                        msg = reader.readLine();
                        System.out.println();
                        System.out.println(msg);

                    } catch (IOException e) {
                       close();
                    }
                }
            }
        }).start();
    }
    public static void main(String[] args) throws IOException {
        System.out.println("Want to enter group chat?\n Enter your username: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        Client c = new Client(new Socket("localhost",2222), username);
        //send msgs
        c.listenToMsgs();
        //listen to msgs
        c.sendMsgs();
    }
}
