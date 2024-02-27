package org.example.server;

import com.google.gson.Gson;
import org.example.model.DirectMessage;
import org.example.model.IdentifyMessage;
import org.example.model.Type;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Session {

    private Socket socket;

    public Session(Socket socket){
        this.socket = socket;
    }

    public void runSession(){
        new Thread(() -> {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    socket.getInputStream().read(buffer);
                    String recibido = new String(buffer, StandardCharsets.UTF_8).trim();
                    System.out.println(recibido);

                    //Deserializar el tipo
                    Gson gson = new Gson();
                    Type type = gson.fromJson(recibido, Type.class);

                    switch (type.getType()){
                        case "broadcast":
                            Server.sendBroadcast(recibido);
                            break;
                        case "autoid":
                            //Anunciarlo en tod0 chat
                            IdentifyMessage user = gson.fromJson(recibido, IdentifyMessage.class);
                            Server.sendBroadcast("Se ha conectado " + user.getItsme() +"  ["+ fecha()+" }");
                            //Registrar en hashmap
                            Server.user.put(user.getItsme(), this);

                            //Miercoles
                            break;
                        case "direct":
                            //{to : }

                            DirectMessage direct = gson.fromJson(recibido, DirectMessage.class);
                            Session s = Server.user.get(direct.getTo());
                            if(s != null){
                                String finalmessage = direct.getAuthor() + "te dijo" + direct.getMessage();
                                s.getSocket().getOutputStream().write(finalmessage.getBytes());
                            }else{

                                socket.getOutputStream().write(("el usuario" + direct.getTo() + "no esta conectado").getBytes());
                            }

                            break;
                    }


                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("El otro se desconectó");
        }).start();
    }

    public Socket getSocket() {
        return socket;
    }
    private static String fecha(){

        LocalDateTime dateTime = LocalDateTime.now();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-YY");
        String fecha = dateTime.format(formatters) + " : " + dateFormat.format(date);

        return fecha;
    }
}
