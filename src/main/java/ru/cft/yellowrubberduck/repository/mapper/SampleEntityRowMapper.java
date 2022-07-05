package ru.cft.yellowrubberduck.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.cft.yellowrubberduck.repository.model.SampleEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SampleEntityRowMapper implements RowMapper<SampleEntity> {
    @Override
    public SampleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SampleEntity result = new SampleEntity();
        result.id = rs.getLong("id");
        result.firstName = rs.getString("first_name");
        result.lastName = rs.getString("last_name");
        return result;
    }
}
