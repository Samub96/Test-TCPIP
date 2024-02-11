import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client2 {

    public static void main(String[] args) {
        try {
            // Conectar al servidor
            Socket socket = new Socket("127.0.0.1", 6000);
            System.out.println("Conectado al servidor.");

            // Crear procesos de recepción y envío en hilos separados
            new Thread(() -> recibirMensajes(socket)).start();
            new Thread(() -> enviarMensajes(socket)).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void recibirMensajes(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                // Recibir mensaje
                String recibido = reader.readLine();
                System.out.println("\nMensaje recibido del servidor: " + recibido);
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
                System.out.print("\nIngrese un mensaje para enviar al servidor: ");
                String mensaje = consoleReader.readLine();

                // Enviar mensaje al servidor
                writer.println(mensaje);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
