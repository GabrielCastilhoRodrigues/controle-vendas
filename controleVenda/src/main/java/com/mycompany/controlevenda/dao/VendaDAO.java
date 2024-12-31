package com.mycompany.controlevenda.dao;

import com.mycompany.controlevenda.control.ClienteController;
import com.mycompany.controlevenda.control.ItemVendaController;
import com.mycompany.controlevenda.database.DatabaseConnection;
import com.mycompany.controlevenda.model.Cliente;
import com.mycompany.controlevenda.model.ItemVenda;
import com.mycompany.controlevenda.model.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para as operações de Venda.
 *
 * @author gabri
 */
public class VendaDAO implements EntityDAO<Venda> {

    /**
     * Conexão com o banco de dados.
     */
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    /**
     * Nome da tabela Cliente.
     */
    private final String nomeTabela = Venda.class.getSimpleName();

    /**
     * Query padrão para consulta todas as colunas da tabela.
     */
    private final String SELECT_QUERY = "SELECT * FROM " + nomeTabela;

    /**
     * PreparedStatement para as operações no banco de dados.
     */
    private PreparedStatement operacao = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(Venda entidade) {
        String insertSql = ""
                + " INSERT INTO " + nomeTabela
                + "     (codigoCliente, dataVenda, valorTotal) "
                + " VALUES "
                + "     (?, ?, ?) ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(insertSql,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            operacao.setLong(1, entidade.getCliente().getCodigo());
            operacao.setTimestamp(2, Timestamp.valueOf(
                    entidade.getDataVenda().atTime(LocalTime.MIDNIGHT)));
            operacao.setBigDecimal(3, entidade.getValorTotal());

            operacao.executeUpdate();

            try (ResultSet rs = operacao.getGeneratedKeys()) {
                if (rs.next()) {
                    entidade.setCodigo(rs.getLong(1));
                }
            }

            return true;

        } catch (SQLException e) {
            Logger.getLogger(VendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean edit(Venda entidade) {
        String editSql = ""
                + " UPDATE " + nomeTabela
                + "     SET "
                + "         codigoCliente = ?, "
                + "         dataVenda = ?, "
                + "         valorTotal = ? "
                + " WHERE "
                + "    Codigo = ?  ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(editSql);

            operacao.setLong(1, entidade.getCliente().getCodigo());
            operacao.setTimestamp(2, Timestamp.valueOf(
                    entidade.getDataVenda().atTime(LocalTime.MIDNIGHT)));
            operacao.setBigDecimal(3, entidade.getValorTotal());
            operacao.setLong(4, entidade.getCodigo());

            operacao.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(VendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long codigo) {
        String deleteSql = ""
                + " DELETE FROM " + nomeTabela
                + " WHERE codigo = ? ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(deleteSql);
            operacao.setLong(1, codigo);

            operacao.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(VendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Venda> findAll() {
        Connection conexao = dbConnection.conexao();
        ResultSet resultSet = null;
        List<Venda> vendas = new ArrayList<>();

        try {
            operacao = conexao.prepareStatement(SELECT_QUERY);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                Venda venda = new Venda();
                venda.setCodigo(resultSet.getInt("codigo"));
                venda.setCliente(consultaCliente(
                        resultSet.getLong("codigoCliente")));
                venda.setDataVenda(resultSet.getTimestamp("dataVenda")
                        .toLocalDateTime().toLocalDate());
                venda.setValorTotal(resultSet.getBigDecimal("valorTotal"));

                vendas.add(venda);
            }
        } catch (SQLException e) {
            Logger.getLogger(VendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return vendas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Venda findByCodigo(Long codigo) {
        Connection conexao = dbConnection.conexao();

        String consultaSQL = SELECT_QUERY + " where codigo = ?";
        ResultSet resultSet = null;
        Venda venda = null;

        try {
            operacao = conexao.prepareStatement(consultaSQL);

            operacao.setLong(1, codigo);
            resultSet = operacao.executeQuery();

            if (resultSet.next()) {
                venda = new Venda();
                venda.setCodigo(resultSet.getInt("codigo"));
                venda.setCliente(consultaCliente(
                        resultSet.getLong("codigoCliente")));
                venda.setDataVenda(resultSet.getTimestamp("dataVenda")
                        .toLocalDateTime().toLocalDate());
                venda.setValorTotal(resultSet.getBigDecimal("valorTotal"));
            }
        } catch (SQLException e) {
            Logger.getLogger(VendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return venda;
    }

    /**
     * Realiza a consulta do Cliente vinculado a Venda.
     *
     * @param codigoCliente Código do Cliente que deseja Localizar.
     *
     * @return O Cliente localizado.
     */
    private Cliente consultaCliente(Long codigoCliente) {
        ClienteController clienteController = new ClienteController();

        return clienteController.retornaEntidadePeloCodigo(codigoCliente);
    }

    public List<Venda> findAllByCodigoCliente(Long codigoCliente) {
        Connection conexao = dbConnection.conexao();

        String consultaSQL = SELECT_QUERY + " where codigoCliente = ?";
        ResultSet resultSet = null;
        Venda venda = null;
        List<Venda> vendas = new ArrayList<>();

        try {
            operacao = conexao.prepareStatement(consultaSQL);

            operacao.setLong(1, codigoCliente);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                venda = new Venda();
                venda.setCodigo(resultSet.getInt("codigo"));
                venda.setCliente(consultaCliente(
                        resultSet.getLong("codigoCliente")));
                venda.setDataVenda(resultSet.getTimestamp("dataVenda")
                        .toLocalDateTime().toLocalDate());
                venda.setValorTotal(resultSet.getBigDecimal("valorTotal"));

                vendas.add(venda);
            }
        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return vendas;
    }
}
