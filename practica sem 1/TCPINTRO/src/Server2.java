import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server2 {
    static ServerSocket server;
    static Socket socket;

    public static void main(String[] args) throws IOException {
        System.out.println("Esperando conexion...");

        server = new ServerSocket(6000);
        socket = server.accept();

        if(socket.isConnected()){

            conexionEstablecida();

        }

    }
    private static void conexionEstablecida() throws IOException{


            while (true) {
                System.out.println("Conexion establecida con " + socket.getInetAddress());

                ExecutorService executorService = Executors.newFixedThreadPool(2);
                executorService.submit(() -> recibirMensajes(socket));
                executorService.submit(() -> enviarMensajes(socket));
            }

    }
    private static void recibirMensajes(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                // Recibir mensaje
                String recibido = reader.readLine();
                System.out.println("\nMensaje recibido del cliente: " + recibido);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void enviarMensajes(Socket socket) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Leer mensaje desde la consola
                System.out.print("\nIngrese un mensaje para enviar al cliente: ");
                String mensaje = consoleReader.readLine();

                // Enviar mensaje al cliente
                writer.println(mensaje);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
