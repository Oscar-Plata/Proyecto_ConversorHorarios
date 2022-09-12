/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computacionfim.conversorhorarios;

import java.io.File;

/**
 *
 * @author oscar
 */
public class InterfazConversor extends javax.swing.JFrame {
    String pathFile;
    String nameFile;
    File mainFile;
    /**
     * Creates new form InterfazConversor
     */
    public InterfazConversor() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        explorador_FC = new javax.swing.JFileChooser();
        Control_CC = new computacionfim.conversorhorarios.ControlConversor();
        jPanel1 = new javax.swing.JPanel();
        convertir_BTN = new javax.swing.JButton();
        selector_BTN = new javax.swing.JButton();
        directorio_LBL = new javax.swing.JLabel();
        archivo_LBL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);
        setSize(new java.awt.Dimension(500, 300));

        jPanel1.setBackground(java.awt.SystemColor.controlLtHighlight);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        convertir_BTN.setBackground(new java.awt.Color(255, 51, 51));
        convertir_BTN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        convertir_BTN.setForeground(new java.awt.Color(255, 255, 255));
        convertir_BTN.setText("CONVERTIR");
        convertir_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertir_BTNActionPerformed(evt);
            }
        });
        jPanel1.add(convertir_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 150, -1));

        selector_BTN.setBackground(new java.awt.Color(0, 153, 0));
        selector_BTN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        selector_BTN.setForeground(new java.awt.Color(255, 255, 255));
        selector_BTN.setText("SELECCIONAR");
        selector_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selector_BTNActionPerformed(evt);
            }
        });
        jPanel1.add(selector_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        directorio_LBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        directorio_LBL.setForeground(new java.awt.Color(51, 51, 51));
        directorio_LBL.setText("X:\\");
            jPanel1.add(directorio_LBL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 470, -1));

            archivo_LBL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
            archivo_LBL.setForeground(new java.awt.Color(51, 51, 51));
            archivo_LBL.setText("Archivo:");
            jPanel1.add(archivo_LBL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 470, 20));

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void convertir_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertir_BTNActionPerformed
        boolean validFile=Control_CC.leerXLS(mainFile);
        if(!validFile){
           archivo_LBL.setText(nameFile + " ARCHIVO NO VALIDO"); 
        }
                
    }//GEN-LAST:event_convertir_BTNActionPerformed

    private void selector_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selector_BTNActionPerformed
        int estado = explorador_FC.showOpenDialog(this);
            if (estado != explorador_FC.CANCEL_OPTION) {
                mainFile= explorador_FC.getSelectedFile();
                pathFile=mainFile.getAbsolutePath();
                nameFile=mainFile.getName();
                archivo_LBL.setText(nameFile);
                directorio_LBL.setText(pathFile);
            }
    }//GEN-LAST:event_selector_BTNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazConversor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazConversor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazConversor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazConversor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazConversor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private computacionfim.conversorhorarios.ControlConversor Control_CC;
    private javax.swing.JLabel archivo_LBL;
    private javax.swing.JButton convertir_BTN;
    private javax.swing.JLabel directorio_LBL;
    private javax.swing.JFileChooser explorador_FC;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton selector_BTN;
    // End of variables declaration//GEN-END:variables
}