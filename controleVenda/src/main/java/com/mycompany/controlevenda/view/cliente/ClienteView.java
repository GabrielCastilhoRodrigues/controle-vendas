package com.mycompany.controlevenda.view.cliente;

import com.mycompany.controlevenda.constants.ModelConstants;
import com.mycompany.controlevenda.constants.TitulosConstants;
import com.mycompany.controlevenda.constants.ValidacoesConstants;
import com.mycompany.controlevenda.constants.model.ClienteConstants;
import com.mycompany.controlevenda.control.ClienteController;
import com.mycompany.controlevenda.model.Cliente;
import com.mycompanyy.controlevenda.exceptions.ValorInvalidoException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * View para Cadastro e Edição da entidade {@link Cliente}.
 *
 * @author gabri
 */
public class ClienteView extends JFrame {

    /**
     * Cliente presente em tela.
     */
    private Cliente cliente;

    /**
     * Controller do Cliente.
     */
    private final ClienteController clienteController = new ClienteController();

    /**
     * Variável de controle, se foi realizado Cadastro ou Edição da Entidade.
     */
    private boolean realizouCadastro = false;

    /**
     * Variável de controle, caso ocorreu exceção dentro de algum processo.
     */
    private boolean teveExcecao = false;

    /**
     * DateFormat para o campo DataLimiteFechamento.
     */
    private final SimpleDateFormat dateFormat
            = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Creates new form ClienteView
     */
    public ClienteView() {
        initComponents();
        init();
    }

    /**
     * Cria um novo ClienteView.
     *
     * @param cliente Cliente com os dados para edição.
     */
    public ClienteView(Cliente cliente) {
        this.cliente = cliente;

        initComponents();
        init();
    }

    /**
     * Inicia os componentes presentes em tela.
     */
    private void init() {
        setTitle(TitulosConstants.CADASTRO);
        lblNome.setText(ClienteConstants.NOME);
        lblLimiteCompra.setText(ClienteConstants.VALOR_LIMITE_COMPRA);
        lblDataFechamento.setText(ClienteConstants.DATA_FECHAMENTO_FATURA);

        btnCancelar.setText(TitulosConstants.CANCELAR);
        btnCancelar.addActionListener(btn -> cancelar());

        if (cliente != null) {
            DateTimeFormatter dateTimeFormatter
                    = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateString = dateTimeFormatter
                    .format(cliente.getDataFechamentoFatura());
            txtNome.setText(cliente.getNome());
            txtLimiteCompra.setText(String.valueOf(
                    cliente.getValorLimiteCompra()).replace(".", ","));
            txtDataFechamento.setText(dateString);

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
     * Realiza o cadastro do Cliente.
     */
    private void cadastrar() {
        if (camposPreenchidosCorretamente()) {
            Cliente cliente = new Cliente();
            DateTimeFormatter dateTimeFormatter
                    = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            cliente.setDataFechamentoFatura(LocalDate.parse(
                    txtDataFechamento.getText(), dateTimeFormatter));
            try {
                cliente.setValorLimiteCompra(
                        converteValor(txtLimiteCompra.getText()));
            } catch (ValorInvalidoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }

            if (clienteController.cadastraEntidade(cliente)) {
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
     * Realiza a edição do Cliente.
     */
    private void editar() {
        if (camposPreenchidosCorretamente()) {
            cliente.setNome(txtNome.getText());
            DateTimeFormatter dateTimeFormatter
                    = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            cliente.setDataFechamentoFatura(LocalDate.parse(
                    txtDataFechamento.getText(), dateTimeFormatter));

            try {
                cliente.setValorLimiteCompra(
                        converteValor(txtLimiteCompra.getText()));
            } catch (ValorInvalidoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }

            if (clienteController.editarEntidade(cliente)) {
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
        if (txtNome.getText().isBlank()
                || txtLimiteCompra.getText().isBlank()
                || txtDataFechamento.getText().isBlank()) {

            teveExcecao = true;

            JOptionPane.showMessageDialog(this,
                    ValidacoesConstants.TODOS_CAMPOS_DEVEM_SER_PREENCHIDOS);
            return false;
        }

        try {
            double preco = converteValor(txtLimiteCompra.getText());

            if (preco <= 0) {
                teveExcecao = true;
                JOptionPane.showMessageDialog(this,
                        ValidacoesConstants.VALOR_DEVE_SER_MAIOR_QUE_ZERO);
                return false;
            }
        } catch (ValorInvalidoException e) {
            teveExcecao = true;
            JOptionPane.showMessageDialog(this, e.getMessage());
            return false;
        }

        try {
            if (dateFormat.parse(txtDataFechamento.getText())
                    .before(new Date())) {
                teveExcecao = true;
                JOptionPane.showMessageDialog(this,
                        ValidacoesConstants.DATA_DEVE_SER_MAIOR_QUE_HOJE);
                return false;
            }
        } catch (ParseException e) {
            teveExcecao = true;
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
    private Double converteValor(String valorInformado)
            throws ValorInvalidoException {
        NumberFormat format = NumberFormat.getInstance(
                new Locale.Builder().setLanguage("pt").setRegion("BR").build());
        Number preco = null;

        try {
            preco = format.parse(valorInformado);
        } catch (ParseException e) {
            teveExcecao = true;
            throw new ValorInvalidoException(
                    ValidacoesConstants.VALOR_INFORMADO_INCORRETAMENTE);
        }

        return preco == null ? 0.0 : preco.doubleValue();
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

        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblLimiteCompra = new javax.swing.JLabel();
        txtLimiteCompra = new javax.swing.JTextField();
        lblDataFechamento = new javax.swing.JLabel();
        txtDataFechamento = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(450, 200));
        setPreferredSize(new java.awt.Dimension(450, 200));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblNome.setText("lblNome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblNome, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtNome, gridBagConstraints);

        lblLimiteCompra.setText("lblLimiteCompra");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblLimiteCompra, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtLimiteCompra, gridBagConstraints);

        lblDataFechamento.setText("lblDataFechamento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblDataFechamento, gridBagConstraints);

        try {
            txtDataFechamento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtDataFechamento, gridBagConstraints);

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
    private javax.swing.JLabel lblDataFechamento;
    private javax.swing.JLabel lblLimiteCompra;
    private javax.swing.JLabel lblNome;
    private javax.swing.JFormattedTextField txtDataFechamento;
    private javax.swing.JTextField txtLimiteCompra;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
