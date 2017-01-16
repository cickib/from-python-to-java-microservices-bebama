package analyticsService.dao;

import analyticsService.model.Webshop;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public interface WebshopDao {
    void add(Webshop webshop) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException;
    Webshop findByApyKey(String apiKey) throws SQLException;
    List<Webshop> getAll() throws SQLException;
}
