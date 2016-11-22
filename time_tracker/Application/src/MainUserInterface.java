import javax.swing.*;

/**
 * Created by admin on 11/21/16.
 */
public class MainUserInterface extends javax.swing.JFrame {

    MainApplication application = null;

    /** Creates new form MainUserInterface */
    public MainUserInterface() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        application = new MainApplication();

        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        getData = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Celsius Converter");

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        getData.setText("Get data");
        getData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getDataButtonActionPerformed(evt);
            }
        });

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if(application != null)
                {
                    application.stopServices();
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(startButton)
                                                .addComponent(stopButton)
                                                .addComponent(getData)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED) .addContainerGap(27, Short.MAX_VALUE))
                                )));

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {startButton});

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(startButton)
                                .addComponent(stopButton)
                                        .addComponent(getData))));
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
//Parse degrees Celsius as a double and convert to Fahrenheit

        if(application != null)
        {
            application.startServices();
        }

        JOptionPane.showMessageDialog(null, "Start button pressed", "Info", JOptionPane.INFORMATION_MESSAGE);


    }//GEN-LAST:event_convertButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
    //Parse degrees Celsius as a double and convert to Fahrenheit

        if(application != null)
        {
            application.stopServices();
        }

        JOptionPane.showMessageDialog(null, "Stop button pressed", "Info", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_convertButtonActionPerformed

    private void getDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
        //Parse degrees Celsius as a double and convert to Fahrenheit

        if(application != null)
        {
            application.getData();
        }

        JOptionPane.showMessageDialog(null, "getData button pressed", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUserInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton getData;
    // End of variables declaration//GEN-END:variables

}
