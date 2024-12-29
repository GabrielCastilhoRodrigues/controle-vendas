package com.mycompany.controlevenda.database;

import com.mycompany.controlevenda.constants.DatabaseConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Arquivo para conexão com o banco de dados.
 *
 * @author gabri
 */
public class DatabaseConnection {

    /**
     * Realiza a conexão com o banco de dados.
     *
     * @return A conexão com o banco de dados.
     */
    public Connection conexao() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/"
                    + DatabaseConstants.DATABASE_NAME,
                    DatabaseConstants.DATABASE_USER,
                    DatabaseConstants.DATABASE_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível conectar a base de "
                    + "dados!");
        }

        return conn;
    }

    /**
     * Encerra a conexão com o banco de dados.
     *
     * @param conexao A conexão que está aberta.
     */
    public static void fecharConexao(Connection conexao) {
        try {
            if (conexao != null) {
                conexao.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    /**
     * Encerra a conexão com o banco de dados.
     *
     * @param conexao A conexão que está aberta.
     * @param operacao A operação no banco de dados que está aberta.
     */
    public static void fecharConexao(Connection conexao,
            PreparedStatement operacao) {

        fecharConexao(conexao);

        try {
            if (operacao != null) {
                operacao.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    /**
     * Encerra a conexão com o banco de dados.
     *
     * @param conexao A conexão que está aberta.
     * @param operacao A operação no banco de dados que está aberta.
     * @param resultSet Retorno que está aberto no banco de dados.
     */
    public static void fecharConexao(Connection conexao,
            PreparedStatement operacao, ResultSet resultSet) {

        fecharConexao(conexao, operacao);

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }
}
