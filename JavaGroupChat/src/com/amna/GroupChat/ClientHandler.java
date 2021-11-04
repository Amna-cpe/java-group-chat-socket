package com.amna.GroupChat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    private final Socket socket;
    private final String clientName;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.clientName = reader.readLine();
        clientHandlers.add(this);
        broadcastMsg("SERVER: "+ clientName+" has entered the chat Room");
    }

    private void broadcastMsg(String msg) throws IOException {
        for (ClientHandler c: clientHandlers) {
            if(!c.clientName.equals(clientName)){
                //user their writer's to write
                    c.writer.write(msg);
                    c.writer.newLine();
                    c.writer.flush();
            }

        }

    }

    private void close(){
        removeFromChat();
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

    private void removeFromChat(){
        try {
            clientHandlers.remove(this);
            broadcastMsg(this.clientName+" has left the group chat!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        String msgToSend;
        while (socket.isConnected()){
            // read then broadcast
            try {
                msgToSend = reader.readLine();
                broadcastMsg(msgToSend);
            } catch (IOException e) {
                // end the while loop when closing everything
                close();
                break;
            }

        }

    }
}
