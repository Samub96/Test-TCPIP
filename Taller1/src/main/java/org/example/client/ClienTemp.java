package org.example.client;

import com.google.gson.Gson;
import org.example.Constants;
import org.example.model.BroadcastMessage;
import org.example.model.DirectMessage;
import org.example.model.IdentifyMessage;
import org.example.server.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class ClienTemp {
    static boolean exit;
    static Scanner scanner;
    static Socket socket;
    static String user;
    static Gson gson;
    static IdentifyMessage me;

    public static void main(String[] args) throws IOException {
        scanner = new Scanner(System.in);
        gson = new Gson();
        menu();

    }
    private static String fecha(){

        LocalDateTime dateTime = LocalDateTime.now();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-YY");
        String fecha = dateTime.format(formatters) + " : " + dateFormat.format(date);

        return fecha;
    }
    private static void recibirMensajes(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                // Recibir mensaje
                String recibido = new String(reader.readLine().getBytes(), StandardCharsets.UTF_8).trim();
                System.out.println( recibido);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("se desconecto  " );
    }

    private static void enviarMensajes(Socket socket) {

        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.println("""
                        [1] mensaje broadcast
                        [2] mensje direct
                        [3] salir
                        """);
                String optionTemp = scanner.nextLine();
                int option = Integer.parseInt(optionTemp);
                switch (option){
                    case 1 :
                        System.out.println("broadcast");
                        String mensajeTemp ="broadcast " + scanner.nextLine();
                        if (mensajeTemp.startsWith("broadcast")) {
                            String mensajeTemp2 = mensajeTemp.replace("broadcast","");
                            BroadcastMessage message = new BroadcastMessage(
                                    Constants.BROADCAST,
                                    user,
                                    mensajeTemp2
                            );
                            String json = gson.toJson(message);
                            writer.println(json);

                        }
                        break;
                    case 2:
                        System.out.println("direct");

                        System.out.println("Usuarios conectados\n " ); // preguntarle a domi esto

                        String usuarios = scanner.nextLine();

                        String mensajeTemp3 ="direct/" + usuarios + scanner.nextLine();
                        if (mensajeTemp3.startsWith("direct")) {
                            String temp =  mensajeTemp3.replace("/",":");
                            String username =  temp.split(":")[1];
                            System.out.println(username);

                            // enviar mensaje directo

                            DirectMessage directMessage = new DirectMessage(
                                    Constants.DIRECT,
                                    me.getItsme(),
                                    temp.split(":")[2],
                                    username
                            );
                            String json2 = gson.toJson(directMessage);
                            writer.println(json2);
                        }


                        break;
                    case 3:
                        socket.close();
                        menu();
                        break;
                }
                // Leer mensaje desde la consola
               // System.out.print("\nmensaje: ");
               // String mensaje = consoleReader.readLine();

                // Enviar mensaje al servidor
               // writer.println(mensaje);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
        private static void menu() throws IOException {


            System.out.println("""
                            welcome 
                    ----------------------
                    [1] conectar con el servidor
                    [2] salir de la aplicacion
                    
                    """);
            String optiontemp  = scanner.nextLine();
            int option = Integer.parseInt(optiontemp);

            switch (option){
                case 1 : connexion();
                    break;
                case 2:
                    System.out.println("Gracias, hasta luego ");

                    exit = true ;
                    break;
                default :
                    System.out.printf("Select an valid option");

            }


    }
    private static void connexion () throws IOException {

        System.out.println("escribe la direccion a conectar ");
        String address = scanner.nextLine();
        System.out.println("Nombre de usuario");
        user = scanner.nextLine();
        System.out.println("intentando conectar ");

        Socket socket = new Socket(address, 6000);

         me = new IdentifyMessage(user, Constants.AUTOID);

        String meJson = gson.toJson(me);
        socket.getOutputStream().write(meJson.getBytes());


        if(socket.isConnected()){
            System.out.println(rtt(address));
            System.out.println("Servidor conectado"
                    +"\nFecha de conexion "+fecha()
                    +"\n Redirigiendo...");
            new Thread(() -> recibirMensajes(socket)).start();
            new Thread(() -> enviarMensajes(socket)).start();


        }else {
            System.out.println("hubo un error");
        }

    }
    private static String rtt (String host){

        String ping = "";

        try {
            InetAddress address = InetAddress.getByName(host);

            // Realizar ping y medir el RTT
            long startTime = System.nanoTime();
            boolean reachable = address.isReachable(5000); // Timeout de 5 segundos
            long endTime = System.nanoTime();

            if (reachable) {
                long rttNanoSeconds = endTime - startTime;
                double rttMilliseconds = rttNanoSeconds / 1_000_000.0; // Convertir a milisegundos
                ping = "RTT: " + rttMilliseconds + " ms";
            } else {
                ping  = "El host no está alcanzable.";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ping;
    }
}
