package com.amna.GroupChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket server;
    private Socket socket;

    public Server(ServerSocket server){
        this.server = server;
        this.socket = new Socket();


    }

    public void initiateServer(){
      try {
        while (!server.isClosed()){
            System.out.println("Server is waiting for clients..");
            socket = server.accept();
            System.out.println("New Client Entered");

            ClientHandler clientHandler = new ClientHandler(socket);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }
      } catch (IOException e) {
          closeServer();
      }
    }

    private void closeServer() {
        try {
            if(server != null){
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(2222));
        server.initiateServer();
    }

}
