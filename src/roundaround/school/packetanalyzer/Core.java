package roundaround.school.packetanalyzer;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class Core {
    public static void main(String[] args) {
        ArrayList<PcapIf> devices = new ArrayList<PcapIf>();
        StringBuilder errbuf = new StringBuilder();

        int r = Pcap.findAllDevs(devices, errbuf);
        if (r == Pcap.NOT_OK || devices.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for (PcapIf device : devices) {
            Sniffer sniffer = new Sniffer(device);
            executor.execute(sniffer);
        }
    }

    public static String formatMACAddress(byte[] macAddress) {
        StringBuilder sb = new StringBuilder();

        String prefix = "";
        for (byte b : macAddress) {
            sb.append(prefix);
            prefix = "-";
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }
}
