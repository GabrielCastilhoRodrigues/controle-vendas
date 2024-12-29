/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.controlevenda.view;

import com.mycompany.controlevenda.constants.TitulosConstants;
import com.mycompany.controlevenda.view.cliente.PrincipalClienteView;
import javax.swing.JFrame;

public class TelaPrincipal extends JFrame {

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();

        setTitle(TitulosConstants.CONTROLE_VENDA);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPrincipal = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuItemCliente = new javax.swing.JMenuItem();
        menuItemProduto = new javax.swing.JMenuItem();
        menuItemVenda = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Controle de vendas");
        setPreferredSize(new java.awt.Dimension(1000, 600));

        jMenu1.setText("Cadastro");

        menuItemCliente.setText("Cliente");
        menuItemCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemClienteActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemCliente);

        menuItemProduto.setText("Produto");
        jMenu1.add(menuItemProduto);

        menuItemVenda.setText("Venda");
        jMenu1.add(menuItemVenda);

        menuPrincipal.add(jMenu1);

        setJMenuBar(menuPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Chama a listagem de Cliente.
     *
     * @param evt Evento que chamou a operaçao.
     */
    private void menuItemClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemClienteActionPerformed
        PrincipalClienteView clienteView = new PrincipalClienteView();
        clienteView.setVisible(true);
    }//GEN-LAST:event_menuItemClienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem menuItemCliente;
    private javax.swing.JMenuItem menuItemProduto;
    private javax.swing.JMenuItem menuItemVenda;
    private javax.swing.JMenuBar menuPrincipal;
    // End of variables declaration//GEN-END:variables
}
