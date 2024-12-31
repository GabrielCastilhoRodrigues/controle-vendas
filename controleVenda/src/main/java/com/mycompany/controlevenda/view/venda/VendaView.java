package com.mycompany.controlevenda.view.venda;

import com.mycompany.controlevenda.constants.ModelConstants;
import com.mycompany.controlevenda.constants.TitulosConstants;
import com.mycompany.controlevenda.constants.ValidacoesConstants;
import com.mycompany.controlevenda.constants.model.ClienteConstants;
import com.mycompany.controlevenda.constants.model.ItemVendaConstants;
import com.mycompany.controlevenda.constants.model.VendaConstants;
import com.mycompany.controlevenda.control.ClienteController;
import com.mycompany.controlevenda.control.ProdutoController;
import com.mycompany.controlevenda.control.VendaController;
import com.mycompany.controlevenda.model.Cliente;
import com.mycompany.controlevenda.model.ItemVenda;
import com.mycompany.controlevenda.model.Produto;
import com.mycompany.controlevenda.model.Venda;
import com.mycompanyy.controlevenda.exceptions.ValorInvalidoException;
import com.mycompanyy.controlevenda.utilities.LimiteCreditoUtilities;
import com.mycompanyy.controlevenda.utilities.NumberUtilities;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 * View para Cadastro e Edição da entidade {@link Produto}.
 *
 * @author gabri
 */
public class VendaView extends JFrame {

    /**
     * Produto presente em tela.
     */
    private Venda venda = null;

    /**
     * Controller do Produto.
     */
    private final VendaController vendaController = new VendaController();

    /**
     * Variável de controle, se foi realizado Cadastro ou Edição da Entidade.
     */
    private boolean realizouCadastro = false;

    /**
     * Variável de controle, caso ocorreu exceção dentro de algum processo.
     */
    private boolean teveExcecao = false;

    /**
     * Model para a lista de Clientes.
     */
    private DefaultListModel defaultListModelCliente = new DefaultListModel();

    /**
     * Model para a lista de Produtos.
     */
    private DefaultListModel defaultListModelProduto = new DefaultListModel();

    /**
     * Lista dos Clientes.
     */
    private List<Cliente> clientes = new ArrayList<>();

    /**
     * Cliente selecionado para a venda.
     */
    private Cliente clienteSelecionado = null;

    /**
     * Lista dos Produtos.
     */
    private List<Produto> produtos = new ArrayList<>();

    /**
     * Lista dos Produtos Selecionados.
     */
    private List<ItemVenda> itens = new ArrayList<>();

    /**
     * Limite de Crédito do CLiente informado.
     */
    private LimiteCreditoUtilities limiteCreditoUtilities = null;

    /**
     * Creates new form ProdutoView
     */
    public VendaView() {
        initComponents();
        init();
    }

    /**
     * Cria um novo VendaView.
     *
     * @param venda Venda com os dados para edição.
     */
    public VendaView(Venda venda) {
        this.venda = venda;

        initComponents();
        init();
    }

    /**
     * Inicia os componentes presentes em tela.
     */
    private void init() {
        setTitle(TitulosConstants.CADASTRO);
        lblCliente.setText(VendaConstants.INFORME_CLIENTE);
        lblCodigoCliente.setText(VendaConstants.CODIGO_CLIENTE);
        lblNomeCliente.setText(ClienteConstants.NOME);
        lblCreditoDisponivel.setText(ClienteConstants.VALOR_LIMITE_COMPRA);
        lblProduto.setText(VendaConstants.INFORME_PRODUTO);
        lblQuantidade.setText(ItemVendaConstants.QUANTIDADE);

        txtCodigoCliente.setEditable(false);
        txtNomeCliente.setEditable(false);
        txtCreditoDisponivel.setEditable(false);

        btnCancelar.setText(TitulosConstants.CANCELAR);
        btnCancelar.addActionListener(btn -> cancelar());

        btnExcluir.setText(ItemVendaConstants.EXCLUIR_ITEM);
        btnExcluir.addActionListener(btn -> removerItem());

        if (venda != null) {
            ClienteController clienteController = new ClienteController();
            txtCodigoCliente.setText(String.valueOf(
                    venda.getCliente().getCodigo()));
            txtNomeCliente.setText(venda.getCliente().getNome());

            clienteSelecionado = venda.getCliente();
            limiteCreditoUtilities = clienteController
                    .validarLimiteCredito(clienteSelecionado);

            if (limiteCreditoUtilities != null) {
                txtCreditoDisponivel.setText(
                        limiteCreditoUtilities.getValorDisponivel().toString());
            } else {
                txtCreditoDisponivel.setText(String.valueOf(
                        venda.getCliente().getValorLimiteCompra()));
            }

            btnCadastrar.setText(TitulosConstants.EDITAR);
            btnCadastrar.addActionListener(btn -> editar());

            if (!venda.getItensVenda().isEmpty()) {
                itens = venda.getItensVenda();
            }
        } else {
            btnCadastrar.setText(TitulosConstants.CADASTRAR);
            btnCadastrar.addActionListener(btn -> cadastrar());
        }

        btnInserir.setText(TitulosConstants.INSERIR);
        btnInserir.addActionListener(btn -> preencheTable());

        popupCliente.add(panelCliente);
        listCliente.setModel(defaultListModelCliente);

        popupProduto.add(panelProduto);
        listProduto.setModel(defaultListModelProduto);

        configuraTableProduto();
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
            Venda vendaCadastro = new Venda();
            vendaCadastro.setCliente(clienteSelecionado);
            vendaCadastro.setDataVenda(LocalDate.now());
            vendaCadastro.setItensVenda(itens);
            vendaCadastro.setValorTotal(calculaTotalVenda());

            if (limiteCreditoUtilities != null) {
                DateTimeFormatter dateTimeFormatter
                        = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                JOptionPane.showMessageDialog(this,
                        "Limite de crédito excedido!\n"
                        + "Valor disponível: "
                        + limiteCreditoUtilities.getValorDisponivel()
                        + "\n"
                        + "Data do próximo fechamento: "
                        + dateTimeFormatter.format(limiteCreditoUtilities
                                .getDataProximoFechamento()));
            }

            if (vendaController.cadastraEntidade(vendaCadastro)) {
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
            venda.setCliente(clienteSelecionado);
            venda.setDataVenda(LocalDate.now());
            venda.setItensVenda(itens);
            venda.setValorTotal(calculaTotalVenda());

            if (limiteCreditoUtilities != null) {
                DateTimeFormatter dateTimeFormatter
                        = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                JOptionPane.showMessageDialog(this,
                        "Limite de crédito excedido!\n"
                        + "Valor disponível: "
                        + limiteCreditoUtilities.getValorDisponivel()
                        + "\n"
                        + "Data do próximo fechamento: "
                        + dateTimeFormatter.format(limiteCreditoUtilities
                                .getDataProximoFechamento()));
            }

            if (vendaController.editarEntidade(venda)) {
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
        if (txtCodigoCliente.getText().isBlank() || itens.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    ValidacoesConstants.TODOS_CAMPOS_DEVEM_SER_PREENCHIDOS);
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

        DecimalFormat decimalFormat
                = new DecimalFormat("#,##0.00", decimalFormatSymbols);

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

        panelCliente = new javax.swing.JPanel();
        scrollPaneCliente = new javax.swing.JScrollPane();
        listCliente = new javax.swing.JList<>();
        popupCliente = new javax.swing.JPopupMenu();
        panelProduto = new javax.swing.JPanel();
        scrollPaneProduto = new javax.swing.JScrollPane();
        listProduto = new javax.swing.JList<>();
        popupProduto = new javax.swing.JPopupMenu();
        lblCliente = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        lblProduto = new javax.swing.JLabel();
        txtProduto = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();
        lblCodigoCliente = new javax.swing.JLabel();
        txtCodigoCliente = new javax.swing.JTextField();
        lblNomeCliente = new javax.swing.JLabel();
        txtNomeCliente = new javax.swing.JTextField();
        lblCreditoDisponivel = new javax.swing.JLabel();
        txtCreditoDisponivel = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        scrollPanelListaProduto = new javax.swing.JScrollPane();
        tableProduto = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        lblQuantidade = new javax.swing.JLabel();
        txtQuantidade = new javax.swing.JTextField();
        btnInserir = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();

        panelCliente.setLayout(new java.awt.BorderLayout());

        listCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listClienteMouseClicked(evt);
            }
        });
        scrollPaneCliente.setViewportView(listCliente);

        panelCliente.add(scrollPaneCliente, java.awt.BorderLayout.CENTER);

        popupCliente.setFocusable(false);

        panelProduto.setLayout(new java.awt.BorderLayout());

        listProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listProdutoMouseClicked(evt);
            }
        });
        scrollPaneProduto.setViewportView(listProduto);

        panelProduto.add(scrollPaneProduto, java.awt.BorderLayout.CENTER);

        popupProduto.setFocusable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 400));
        setPreferredSize(new java.awt.Dimension(700, 400));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblCliente.setText("lblCliente");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblCliente, gridBagConstraints);

        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtClienteKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtCliente, gridBagConstraints);

        lblProduto.setText("lblProduto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblProduto, gridBagConstraints);

        txtProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProdutoKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtProduto, gridBagConstraints);

        btnCancelar.setText("btnCancelar");
        jPanel1.add(btnCancelar);

        btnCadastrar.setText("btnCadastrar");
        jPanel1.add(btnCadastrar);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        lblCodigoCliente.setText("lblCodigoCliente");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblCodigoCliente, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtCodigoCliente, gridBagConstraints);

        lblNomeCliente.setText("lblNomeCliente");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblNomeCliente, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtNomeCliente, gridBagConstraints);

        lblCreditoDisponivel.setText("lblCreditoDisponivel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblCreditoDisponivel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtCreditoDisponivel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jSeparator1, gridBagConstraints);

        tableProduto.setAutoCreateRowSorter(true);
        tableProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Descrição", "Vlr. Unit.", "Qtde.", "Tot. Item"
            }
        ));
        scrollPanelListaProduto.setViewportView(tableProduto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(scrollPanelListaProduto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jSeparator2, gridBagConstraints);

        lblQuantidade.setText("lblQuantidade");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblQuantidade, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtQuantidade, gridBagConstraints);

        btnInserir.setText("btnInserir");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnInserir, gridBagConstraints);

        btnExcluir.setText("btnExcluir");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnExcluir, gridBagConstraints);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyReleased
        if (txtCliente.getText().length() >= 3
                && !txtCliente.getText().isBlank()) {
            clientes.clear();
            ClienteController clienteController = new ClienteController();
            clientes = clienteController
                    .retornaEntidadePorNomeParecido(txtCliente.getText());

            if (!clientes.isEmpty()) {
                defaultListModelCliente.removeAllElements();

                for (Cliente cliente : clientes) {
                    defaultListModelCliente.addElement(cliente.getNome());
                }

                ajustaAlturaList(defaultListModelCliente,
                        scrollPaneCliente, listCliente);

                popupCliente.show(txtCliente, 0,
                        txtCliente.getHeight());
            }
        } else {
            popupCliente.setVisible(false);
        }
    }//GEN-LAST:event_txtClienteKeyReleased

    private void listClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listClienteMouseClicked
        int index = listCliente.getSelectedIndex();
        clienteSelecionado = clientes.get(index);

        ClienteController clienteController = new ClienteController();
        LimiteCreditoUtilities limiteCreditoCliente
                = clienteController.validarLimiteCredito(clienteSelecionado);

        if (limiteCreditoCliente != null) {
            limiteCreditoUtilities = limiteCreditoCliente;
        }

        txtCliente.setText("");
        txtCreditoDisponivel.setText(String.valueOf(
                clienteSelecionado.getValorLimiteCompra()).replace(".", ","));

        txtCodigoCliente.setText(
                String.valueOf(clienteSelecionado.getCodigo()));
        txtNomeCliente.setText(clienteSelecionado.getNome());
        if (limiteCreditoUtilities != null) {
            txtCreditoDisponivel.setText(
                    limiteCreditoUtilities.getValorDisponivel().toString());
        } else {
            txtCreditoDisponivel.setText(String.valueOf(
                    venda.getCliente().getValorLimiteCompra()));
        }

        popupCliente.setVisible(false);
    }//GEN-LAST:event_listClienteMouseClicked

    private void listProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listProdutoMouseClicked
        int index = listProduto.getSelectedIndex();
        Produto produtoSelecionado = produtos.get(index);

        txtProduto.setText(produtoSelecionado.getDescricao());

        popupProduto.setVisible(false);
    }//GEN-LAST:event_listProdutoMouseClicked

    private void txtProdutoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProdutoKeyReleased
        if (txtProduto.getText().length() >= 3
                && !txtProduto.getText().isBlank()) {
            produtos.clear();
            ProdutoController produtoController = new ProdutoController();
            produtos = produtoController
                    .retornaEntidadePorNomeParecido(txtProduto.getText());

            if (!produtos.isEmpty()) {
                defaultListModelProduto.removeAllElements();

                for (Produto produto : produtos) {
                    defaultListModelProduto.addElement(produto.getDescricao());
                }

                ajustaAlturaList(defaultListModelProduto,
                        scrollPaneProduto, listProduto);

                popupProduto.show(txtProduto, 0,
                        txtProduto.getHeight());
            }
        } else {
            popupProduto.setVisible(false);
        }
    }//GEN-LAST:event_txtProdutoKeyReleased

    /**
     * Ajusta a altura da List apontada.
     *
     * @param listModel Model da List que deve ser ajustada.
     * @param scrollPane ScrollPane da List que deve ser ajustada.
     * @param lista A List que deve ser ajustada.
     */
    private void ajustaAlturaList(DefaultListModel listModel,
            JScrollPane scrollPane, JList lista) {
        int linhas = listModel.getSize();
        int tamanhoLinha = 16;
        lista.setFixedCellHeight(tamanhoLinha);

        int alturaTotalLista = linhas * lista.getFixedCellHeight();
        int larguraMaxima = calcularLarguraMaxima(listModel, lista);

        Dimension dimensaoLista
                = new Dimension(larguraMaxima, alturaTotalLista);
        lista.setPreferredSize(dimensaoLista);

        Dimension dimensaoScroll = new Dimension(
                larguraMaxima + tamanhoLinha,
                alturaTotalLista + tamanhoLinha);
        scrollPane.setPreferredSize(dimensaoScroll);

        lista.revalidate();
        lista.repaint();
    }

    /**
     * Calcula a Largura Máxima do PopupMenu.
     *
     * @param listModel Model da Lista que será considerada.
     * @param lista A Lista que será considerada.
     *
     * @return A largura máxima.
     */
    private int calcularLarguraMaxima(DefaultListModel listModel, JList lista) {
        FontMetrics metrics = lista.getFontMetrics(lista.getFont());
        int larguraMaxima = 0;

        for (int i = 0; i < listModel.getSize(); i++) {
            String item = listModel.getElementAt(i).toString();
            int larguraTexto = metrics.stringWidth(item);
            if (larguraTexto > larguraMaxima) {
                larguraMaxima = larguraTexto;
            }
        }

        // Adiciona uma margem para evitar corte do texto
        return larguraMaxima + 20; // 20 pixels de margem
    }

    /**
     * Configura TableProduto.
     */
    private void configuraTableProduto() {
        tableProduto.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableModel tableModelProduto = new DefaultTableModel() {

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };

        tableModelProduto.addColumn("Descrição");
        tableModelProduto.addColumn("Vlr. Unit.");
        tableModelProduto.addColumn("Qtde.");
        tableModelProduto.addColumn("Total Item");
        tableModelProduto.setNumRows(0);

        tableProduto.setModel(tableModelProduto);

        //Cria uma ordenação para o JTable
        tableProduto.setRowSorter(new TableRowSorter(tableModelProduto));

        //Define o tamanho das colunas do JTable.
        TableColumnModel tableColumnModelProduto
                = tableProduto.getColumnModel();

        tableColumnModelProduto.getColumn(0).setPreferredWidth(260);
        tableColumnModelProduto.getColumn(1).setPreferredWidth(100);
        tableColumnModelProduto.getColumn(2).setPreferredWidth(100);
        tableColumnModelProduto.getColumn(3).setPreferredWidth(100);

        //Define que os registros irão aparecer centralizados.
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tableColumnModelProduto.getColumnCount(); i++) {
            tableColumnModelProduto.getColumn(i).setCellRenderer(cellRenderer);
        }

        if (venda != null && !venda.getItensVenda().isEmpty()) {
            for (ItemVenda itemVenda : venda.getItensVenda()) {
                tableModelProduto.addRow(new Object[]{
                    itemVenda.getProduto().getDescricao(),
                    itemVenda.getProduto().getPreco().toString(),
                    itemVenda.getQuantidade().toString(),
                    itemVenda.getTotalItem()
                });
            }
        }
    }

    /**
     * Preenche a tabela presente em tela.
     */
    private void preencheTable() {
        if (!produtos.isEmpty() && !txtQuantidade.getText().isBlank()) {
            DefaultTableModel tableModelProduto
                    = (DefaultTableModel) tableProduto.getModel();
            Produto produtoSelecionado
                    = produtos.get(listProduto.getSelectedIndex());

            ItemVenda itemVenda
                    = insereItem(produtoSelecionado, txtQuantidade.getText());

            if (itemVenda != null) {
                tableModelProduto.addRow(new Object[]{
                    produtoSelecionado.getDescricao(),
                    produtoSelecionado.getPreco()
                    .toString(),
                    itemVenda.getQuantidade().toString(),
                    itemVenda.getTotalItem()
                });

                itens.add(itemVenda);

                txtProduto.setText("");
                txtQuantidade.setText("");
            }
        }
    }

    /**
     * Insere o item na tabela da View, além de armazena-lo no List que é
     * utilizada como controle.
     *
     * @param produto O produto que será inserido.
     * @param quantidade Quantidade do Item.
     *
     * @return O ItemVenda gerado com as informações.
     */
    private ItemVenda insereItem(Produto produto, String quantidade) {
        ItemVenda itemVenda = null;
        if (itens.stream().anyMatch(
                item -> item.getProduto().getCodigo() == produto.getCodigo())) {
            JOptionPane.showMessageDialog(this,
                    ItemVendaConstants.PRODUTO_DUPLICADO);
        } else {
            itemVenda = new ItemVenda();
            try {
                BigDecimal quantidadeItem = converteValor(quantidade);
                itemVenda.setQuantidade(quantidadeItem);
                itemVenda.setProduto(produto);
                itemVenda.setVenda(venda);
                itemVenda.setTotalItem(produto.getPreco()
                        .multiply(quantidadeItem));
            } catch (ValorInvalidoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }

        return itemVenda;
    }

    /**
     * Calcula o valor total do item na venda.
     *
     * @return O valor total do item na venda.
     */
    private BigDecimal calculaTotalVenda() {
        BigDecimal totalVenda = BigDecimal.ZERO;

        for (ItemVenda itemVenda : itens) {
            totalVenda = totalVenda.add(itemVenda.getTotalItem());
        }

        return totalVenda;
    }

    private void removerItem() {
        if (tableProduto.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this,
                    ValidacoesConstants.SELECIONE_AO_MENOS_UM_REGISTRO);
        } else {
            DefaultTableModel tableModelProduto
                    = (DefaultTableModel) tableProduto.getModel();
            itens.remove(tableProduto.getSelectedRow());

            tableModelProduto.removeRow(tableProduto.getSelectedRow());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnInserir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblCodigoCliente;
    private javax.swing.JLabel lblCreditoDisponivel;
    private javax.swing.JLabel lblNomeCliente;
    private javax.swing.JLabel lblProduto;
    private javax.swing.JLabel lblQuantidade;
    private javax.swing.JList<String> listCliente;
    private javax.swing.JList<String> listProduto;
    private javax.swing.JPanel panelCliente;
    private javax.swing.JPanel panelProduto;
    private javax.swing.JPopupMenu popupCliente;
    private javax.swing.JPopupMenu popupProduto;
    private javax.swing.JScrollPane scrollPaneCliente;
    private javax.swing.JScrollPane scrollPaneProduto;
    private javax.swing.JScrollPane scrollPanelListaProduto;
    private javax.swing.JTable tableProduto;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodigoCliente;
    private javax.swing.JTextField txtCreditoDisponivel;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JTextField txtProduto;
    private javax.swing.JTextField txtQuantidade;
    // End of variables declaration//GEN-END:variables
}
