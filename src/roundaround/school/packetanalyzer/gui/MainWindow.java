package roundaround.school.packetanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;

public class MainWindow {

    private JFrame frmPacketAnalyzer;
    private JTable table;
    private JMenu mnFile;
    private JMenuItem mntmNew;
    private JMenuItem mntmSave;
    private JMenuItem mntmOpen;
    private JMenuItem mntmExit;
    private JMenu mnWindow;
    private JCheckBoxMenuItem chckbxmntmNewCheckItem;
    private JCheckBoxMenuItem chckbxmntmNewCheckItem_1;
    private JCheckBoxMenuItem chckbxmntmStatistics;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.frmPacketAnalyzer.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmPacketAnalyzer = new JFrame();
        frmPacketAnalyzer.setTitle("Packet Analyzer");
        frmPacketAnalyzer.setBounds(100, 100, 450, 300);
        frmPacketAnalyzer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        table = new JTable();
        frmPacketAnalyzer.getContentPane().add(table, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        frmPacketAnalyzer.setJMenuBar(menuBar);
        
        mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        mntmNew = new JMenuItem("New");
        mnFile.add(mntmNew);
        
        mntmOpen = new JMenuItem("Open");
        mnFile.add(mntmOpen);
        
        mntmSave = new JMenuItem("Save");
        mnFile.add(mntmSave);
        
        mntmExit = new JMenuItem("Exit");
        mnFile.add(mntmExit);
        
        mnWindow = new JMenu("Window");
        menuBar.add(mnWindow);
        
        chckbxmntmStatistics = new JCheckBoxMenuItem("Statistics Analyzer");
        mnWindow.add(chckbxmntmStatistics);
        
        chckbxmntmNewCheckItem_1 = new JCheckBoxMenuItem("Graph Visualization");
        mnWindow.add(chckbxmntmNewCheckItem_1);
        
        chckbxmntmNewCheckItem = new JCheckBoxMenuItem("Packet Decomposer");
        mnWindow.add(chckbxmntmNewCheckItem);
    }

}
