package com.mycompany.controlevenda.view.produto;

import com.mycompany.controlevenda.constants.ModelConstants;
import com.mycompany.controlevenda.constants.TitulosConstants;
import com.mycompany.controlevenda.constants.ValidacoesConstants;
import com.mycompany.controlevenda.constants.model.ProdutoConstants;
import com.mycompany.controlevenda.control.ProdutoController;
import com.mycompany.controlevenda.model.Produto;
import com.mycompanyy.controlevenda.exceptions.ValorInvalidoException;
import com.mycompanyy.controlevenda.utilities.NumberUtilities;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * View para Cadastro e Edição da entidade {@link Produto}.
 *
 * @author gabri
 */
public class ProdutoView extends JFrame {

    /**
     * Produto presente em tela.
     */
    private Produto produto = null;

    /**
     * Controller do Produto.
     */
    private final ProdutoController produtoController = new ProdutoController();

    /**
     * Variável de controle, se foi realizado Cadastro ou Edição da Entidade.
     */
    private boolean realizouCadastro = false;

    /**
     * Variável de controle, caso ocorreu exceção dentro de algum processo.
     */
    private boolean teveExcecao = false;

    /**
     * Creates new form ProdutoView
     */
    public ProdutoView() {
        initComponents();
        init();
    }

    /**
     * Cria um novo ProdutoView.
     *
     * @param produto Produto com os dados para edição.
     */
    public ProdutoView(Produto produto) {
        this.produto = produto;

        initComponents();
        init();
    }

    /**
     * Inicia os componentes presentes em tela.
     */
    private void init() {
        setTitle(TitulosConstants.CADASTRO);
        lblDescricao.setText(ProdutoConstants.DESCRICAO);
        lblPreco.setText(ProdutoConstants.PRECO);

        btnCancelar.setText(TitulosConstants.CANCELAR);
        btnCancelar.addActionListener(btn -> cancelar());

        if (produto != null) {
            txtDescricao.setText(produto.getDescricao());
            txtPreco.setText(String.valueOf(produto.getPreco())
                    .replace(".", ","));

            btnCadastrar.setText(TitulosConstants.EDITAR);
            btnCadastrar.addActionListener(btn -> editar());
        } else {
            btnCadastrar.setText(TitulosConstants.CADASTRAR);
            btnCadastrar.addActionListener(btn -> cadastrar());
        }
    }

    /**
     * Cancela a operação presente.
     */
    private void cancelar() {
        this.dispose();
    }

    /**
     * Realiza o cadastro do Produto.
     */
    private void cadastrar() {
        if (camposPreenchidosCorretamente()) {
            Produto produtoCadastro = new Produto();
            produtoCadastro.setDescricao(txtDescricao.getText());

            try {
                produtoCadastro.setPreco(converteValor(txtPreco.getText()));
            } catch (ValorInvalidoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }

            if (produtoController.cadastraEntidade(produtoCadastro)) {
                JOptionPane.showMessageDialog(this,
                        ModelConstants.CADASTRO_REALIZADO_COM_SUCESSO);
                realizouCadastro = true;

                this.dispose();
            }
        } else if (!teveExcecao) {
            JOptionPane.showMessageDialog(this,
                    ValidacoesConstants.TODOS_CAMPOS_DEVEM_SER_PREENCHIDOS);
        }
    }

    /**
     * Realiza a edição do Produto.
     */
    private void editar() {
        if (camposPreenchidosCorretamente()) {
            produto.setDescricao(txtDescricao.getText());

            try {
                produto.setPreco(converteValor(txtPreco.getText()));
            } catch (ValorInvalidoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }

            if (produtoController.editarEntidade(produto)) {
                JOptionPane.showMessageDialog(this,
                        ModelConstants.EDICAO_REALIZADA_COM_SUCESSO);
                realizouCadastro = true;

                this.dispose();
            }
        } else if (!teveExcecao) {
            JOptionPane.showMessageDialog(this,
                    ValidacoesConstants.TODOS_CAMPOS_DEVEM_SER_PREENCHIDOS);
        }
    }

    public boolean isRealizouCadastro() {
        return realizouCadastro;
    }

    /**
     * Operação de Controle, se todos os dados foram preenchidos como o
     * esperado.
     *
     * @return <code>True</code> caso todos os campos foram preenchidos
     * corretamente. <code>False</code> caso um ou mais campos não foram
     * preenchidos corretamente.
     */
    private boolean camposPreenchidosCorretamente() {
        if (txtDescricao.getText().isBlank()
                || txtPreco.getText().isBlank()) {

            JOptionPane.showMessageDialog(this,
                    ValidacoesConstants.TODOS_CAMPOS_DEVEM_SER_PREENCHIDOS);
            return false;
        }

        try {
            BigDecimal preco = converteValor(txtPreco.getText());

            if (NumberUtilities.greatherThanZero(preco)) {
                JOptionPane.showMessageDialog(this,
                        ValidacoesConstants.VALOR_DEVE_SER_MAIOR_QUE_ZERO);
                return false;
            }
        } catch (ValorInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Converte o campo valor, para o padrão utilizado que é com ",".
     *
     * @param valorInformado O valor que deseja converter.
     *
     * @return O valor já convertido.
     */
    private BigDecimal converteValor(String valorInformado)
            throws ValorInvalidoException {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(
                new Locale.Builder().setLanguage("pt").setRegion("BR").build());
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        
        DecimalFormat decimalFormat =
                new DecimalFormat("#,##0.00", decimalFormatSymbols);
        
        Number preco = null;
        BigDecimal precoConvertido = BigDecimal.ZERO;

        try {
            preco = decimalFormat.parse(valorInformado);
            precoConvertido = BigDecimal.valueOf(preco.doubleValue());
        } catch (ParseException e) {
            teveExcecao = true;
            throw new ValorInvalidoException(
                    ValidacoesConstants.VALOR_INFORMADO_INCORRETAMENTE);
        }

        return precoConvertido;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblDescricao = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        lblPreco = new javax.swing.JLabel();
        txtPreco = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(450, 200));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblDescricao.setText("lblDescricao");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblDescricao, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtDescricao, gridBagConstraints);

        lblPreco.setText("lblPreco");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblPreco, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtPreco, gridBagConstraints);

        btnCancelar.setText("btnCancelar");
        jPanel1.add(btnCancelar);

        btnCadastrar.setText("btnCadastrar");
        jPanel1.add(btnCadastrar);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblPreco;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtPreco;
    // End of variables declaration//GEN-END:variables
}
