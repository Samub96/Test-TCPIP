import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",6000);

        Scanner scanner = new Scanner(System.in);


        while (true){
            if (socket.isConnected()) {
                System.out.println("Conexion establecida" + "\n escribir un mensaje");
                var mensaje = scanner.nextLine();
                //Enviar datos
                socket.getOutputStream().write(
                        mensaje.getBytes()
                );

            }
        }

    }
}
