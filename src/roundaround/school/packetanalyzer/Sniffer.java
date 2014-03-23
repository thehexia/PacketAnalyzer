package roundaround.school.packetanalyzer;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

public class Sniffer implements Runnable {
    private final int snaplen = 64 * 1024; // Capture all packets, no truncation
    private final int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
    private final int timeout = 10 * 1000; // 10 seconds in millis

    private PcapIf device;
    private Pcap pcap;
    private StringBuilder errbuf = new StringBuilder();
    private LinkedBlockingQueue<PcapPacket> queue = new LinkedBlockingQueue<PcapPacket>();
    private ArrayList<PcapPacket> list = new ArrayList<PcapPacket>();

    public Sniffer(PcapIf device) {
        this.device = device;
    }

    @Override
    public void run() {
        this.pcap = Pcap.openLive(this.device.getName(), this.snaplen, this.flags, this.timeout, this.errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: " + errbuf.toString());
            return;
        }

        while (true) {
            try {

                PcapPacketHandler<LinkedBlockingQueue<PcapPacket>> handler = new PcapPacketHandler<LinkedBlockingQueue<PcapPacket>>() {
                    @Override
                    public void nextPacket(PcapPacket packet, LinkedBlockingQueue<PcapPacket> queue) {
                        PcapPacket permanent = new PcapPacket(packet);
                        queue.offer(permanent);
                    }
                };

                if (this.pcap.loop(10, handler, this.queue) == 0) {
                    while (!this.queue.isEmpty()) {
                        PcapPacket packet = new PcapPacket(this.queue.poll());

                        System.out.println(packet.toString());
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }

                        this.list.add(new PcapPacket(packet));
                    }
                }

            } catch (RuntimeException ignore) {
            }
        }
    }
}
