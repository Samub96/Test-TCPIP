package org.example.client;

import com.google.gson.Gson;
import org.example.Constants;
import org.example.model.BroadcastMessage;
import org.example.model.DirectMessage;
import org.example.model.IdentifyMessage;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 6000);

        //hilo de recibir mensajes
        new Thread(() -> {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    socket.getInputStream().read(buffer);
                    String recibido = new String(buffer, StandardCharsets.UTF_8).trim();
                    System.out.println(recibido);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("El otro se desconectó");
        }).start();


        Gson gson = new Gson();
        IdentifyMessage me = new IdentifyMessage("elvenao", Constants.AUTOID);
        String meJson = gson.toJson(me);
        socket.getOutputStream().write(meJson.getBytes());


        Scanner scanner = new Scanner(System.in);
        while (true) {
            String mensaje = scanner.nextLine();
            new Thread(() -> {
                try {

                    //broadcast : <mensaje>

                    if (mensaje.startsWith("broadcast")){
                        BroadcastMessage message = new BroadcastMessage(
                                Constants.BROADCAST,
                                "elvenao",
                                mensaje
                        );
                        String json = gson.toJson(message);
                        socket.getOutputStream().write(json.getBytes());

                    }else if(mensaje.startsWith("direct/")){
                       String temp =  mensaje.replace("/",":");
                        String username =  temp.split(":")[1];
                        System.out.println(username);

                        // enviar mensaje directo

                        DirectMessage directMessage = new DirectMessage(
                                Constants.DIRECT,
                                me.getItsme(),
                                temp.split(":")[2],
                                username
                        );
                        socket.getOutputStream().write(gson.toJson(directMessage).getBytes());
                    }



                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

}
