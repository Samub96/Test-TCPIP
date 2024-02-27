package org.example;

/**
 * Hello world!
 *
 */
import java.io.IOException;
import java.net.InetAddress;

public class App {

    public static void main(String[] args) {
        String host = "127.0.0.1"; // Cambia esto por la dirección del host que desees probar

        try {
            InetAddress address = InetAddress.getByName(host);

            // Realizar ping y medir el RTT
            long startTime = System.nanoTime();
            boolean reachable = address.isReachable(5000); // Timeout de 5 segundos
            long endTime = System.nanoTime();

            if (reachable) {
                long rttNanoSeconds = endTime - startTime;
                double rttMilliseconds = rttNanoSeconds / 1_000_000.0; // Convertir a milisegundos
                System.out.println("RTT: " + rttMilliseconds + " ms");
            } else {
                System.out.println("El host no está alcanzable.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
