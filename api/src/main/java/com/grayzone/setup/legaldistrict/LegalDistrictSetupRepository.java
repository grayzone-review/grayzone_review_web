package com.grayzone.setup.legaldistrict;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LegalDistrictSetupRepository {
  private final DataSource dataSource;

  public void save(List<LegalDistrict> legalDistricts) throws SQLException {
    String query = "INSERT INTO legal_districts(address) VALUES (?)";

    Connection connection = null;
    PreparedStatement preparedStatement = null;

    try {
      connection = dataSource.getConnection();
      preparedStatement = connection.prepareStatement(query);

      connection.setAutoCommit(false);

      int count = 0;

      for (LegalDistrict legalDistrict : legalDistricts) {
        preparedStatement.setString(1, legalDistrict.getAddress());

        preparedStatement.addBatch();

        preparedStatement.clearParameters();

        if (++count % 1000 == 0) {
          preparedStatement.executeBatch();
          preparedStatement.clearBatch();
        }

      }
      preparedStatement.executeBatch();
      connection.commit();

    } catch (Exception e) {
      connection.rollback();
      throw new UpException(UpError.SERVER_ERROR);
    } finally {
      JdbcUtils.closeConnection(connection);
      JdbcUtils.closeStatement(preparedStatement);
    }

  }
}
