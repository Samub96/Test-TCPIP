
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException {

            var universal = InetAddress.getByName("127.0.0.1");
            var localping = universal.isReachable(1000);

            var compa = InetAddress.getByName("192.168.130.49");
            var compaPing = compa.isReachable(1000);
            var icesi = InetAddress.getByName("www.rincondelvago.com");
        System.out.println(icesi.getHostAddress());
        System.out.println(compaPing);
        System.out.println(localping);


    }
}