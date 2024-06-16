package be.ucll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import be.ucll.model.Address;

@Repository
public class AddressRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Address addAddress(Address address) {
        String sql = "INSERT INTO ADDRESS(street, number, place) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "ID" });
            ps.setString(1, address.getStreet());
            ps.setInt(2, address.getNumber()); // Use setInt for number
            ps.setString(3, address.getPlace());
            return ps;
        }, keyHolder);

        // Retrieve the generated ID and set it to the address object
        address.setId(keyHolder.getKey().longValue());

        return address;
    }

    public Address findAddressById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM ADDRESS WHERE ID = ?", new AddressRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void assignExistingAddressToExistingStable(Long addressId, Long stableId) {
        jdbcTemplate.update("update stable set address_id = ? where id = ?", addressId, stableId);
    }

    public Address updateAddress(Address address) {
        String sql = "UPDATE ADDRESS SET street = ?, number = ?, place = ? WHERE id = ?";

        jdbcTemplate.update(sql, address.getStreet(), address.getNumber(), address.getPlace(), address.getId());

        return address;
    }

    public List<Address> findAddressesFromStablesWithNumber(int animalCount) {
        String sql = "SELECT a.id, a.street, a.number, a.place " +
                "FROM address AS a " +
                "INNER JOIN stable AS st ON st.address_id = a.id " +
                "INNER JOIN my_animals AS an ON an.stable_id = st.id " +
                "GROUP BY a.id " +
                "HAVING COUNT(an.name) >= ?";

        return jdbcTemplate.query(sql, new AddressRowMapper(), animalCount);
    }

}
