import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

public class Main2 {
    public static void main(String[] args) throws IOException {
        Enumeration<NetworkInterface> networks =  NetworkInterface.getNetworkInterfaces();
        while(networks.hasMoreElements()){
            NetworkInterface network = networks.nextElement();
            if(network.isUp()){
                System.out.println(network.getDisplayName());
                List<InterfaceAddress> addresses = network.getInterfaceAddresses();
                for(InterfaceAddress address : addresses){
                    System.out.println("  >>>"+address);
                }
            }
        }
    }
}
