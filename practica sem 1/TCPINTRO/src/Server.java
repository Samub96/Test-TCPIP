import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        //Esperar solicitudes
        ServerSocket server = new ServerSocket(6000);
        System.out.println("Esperando");
        Socket socket = server.accept();
        System.out.println("Conectado");

        //crear proceso de recepcion
        new Thread(()-> {
            while (true){
                //recibir mensaje
                try {
                    byte[] buffer = new byte[1024];
                    socket.getInputStream().read(buffer);
                    String recibido = new String(buffer).trim();
                    System.out.println(recibido);
                } catch (IOException ex) {
                    ex.printStackTrace();

                }
        }
        }).start();

        System.out.println("integrador 1");



    }
}