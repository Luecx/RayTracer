package newproject.render.newrenderer.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Luecx on 23.08.2017.
 */
public class TimeFrame extends JFrame{

    private static double time = 0;
    private static boolean runs = true;

    private Long timestamp = System.currentTimeMillis();

    @Override
    public void repaint() {
        if(runs){
            time += (float)(System.currentTimeMillis() - timestamp) / 1000f;

            String minutes = "0"+((int)TimeFrame.time / 60);
            String secs = "0"+((int)TimeFrame.time % 60);
            String millis = "00" + (int)(1000 * (float)TimeFrame.time);
            label.setText(minutes.substring(minutes.length()-2) + ":" + secs.substring(secs.length()-2) + ":" + millis.substring(millis.length()-3));
            timestamp = System.currentTimeMillis();
            revalidate();
        }
    }

    public TimeFrame() throws HeadlessException {
        this.setDefaultCloseOperation(3);
        this.setSize(300,120);
        this.setTitle("Time");

        this.setLayout(new BorderLayout());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                updater.interrupt();
                super.windowClosed(e);
            }
        });

        label = new JLabel();
        label.setBackground(Color.red);
        label.setText("00:00");
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setSize(300,120);

        this.getContentPane().add(label, BorderLayout.CENTER);

        updater = new FrameUpdater(this, 50);
    }


    public void reset(){this.time = 0;}

    public void startTime(){this.runs = true;}

    public void stopTime(){this.runs = false;}

    private JLabel label;
    private FrameUpdater updater;
}
