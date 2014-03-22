package roundaround.school.packetanalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

public class Core {
    public static void main(String[] args) {
        ArrayList<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }

        for (PcapIf device : alldevs) {
            try {
                System.out.println(formatMACAddress(device.getHardwareAddress()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            int snaplen = 64 * 1024; // Capture all packets, no truncation
            int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
            int timeout = 10 * 1000; // 10 seconds in millis
            Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

            if (pcap == null) {
                System.err.printf("Error while opening device for capture: " + errbuf.toString());
                return;
            }

            PcapPacketHandler<LinkedBlockingQueue<PcapPacket>> handler = new PcapPacketHandler<LinkedBlockingQueue<PcapPacket>>() {
                @Override
                public void nextPacket(PcapPacket packet, LinkedBlockingQueue<PcapPacket> queue) {
                    PcapPacket permanent = new PcapPacket(packet);

                    queue.offer(permanent);
                }
            };

            LinkedBlockingQueue<PcapPacket> queue = new LinkedBlockingQueue<PcapPacket>();

            pcap.loop(10, handler, queue);

            System.out.println("This MAC address has processed " + queue.size() + " packets, and placed them in the queue.");

            pcap.close();
        }
    }

    public static String formatMACAddress(byte[] macAddress) {
        StringBuilder sb = new StringBuilder();

        String prefix = "";
        for (byte b : macAddress) {
            sb.append(prefix);
            prefix = ":";
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }
}
