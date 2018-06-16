/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drinkit.drinkit.dao;

import drinkit.drinkit.Juoma;
import drinkit.drinkit.database.Database;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author attekeit
 */
public class JuomaDao implements Dao<Juoma, Integer> {

    private Database database;

    public JuomaDao(Database database) {
        this.database = database;
    }

    @Override
    public Juoma findOne(Integer key) throws SQLException {
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT (nimi) FROM Juoma "
                    + "WHERE id = ?");
            statement.setInt(1, key);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Juoma(key, result.getString("nimi"));
            }
            return null;
        }
        


    }

    @Override
    public List<Juoma> findAll() throws SQLException {
        //Etsitään kaikki tietokannan drinkit.
        List<Juoma> juomat = new ArrayList<>();
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT id, nimi FROM Juoma;");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                juomat.add(new Juoma(set.getInt("id"), set.getString("nimi")));
            }
        }
        return juomat;
    }

    @Override
    public Juoma saveOrUpdate(Juoma object) throws SQLException {
        //Sovelluksssa voidaan vain lisätä drinkkejä, ei päivittää niitä.
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Juoma (nimi) "
                    + "VALUES (?)");
            statement.setString(1, object.getNimi());
            statement.executeUpdate();

        };

        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Juoma "
                    + "WHERE id = ?");
            statement.setInt(1, key);
            statement.executeUpdate();

        };

    }

}
