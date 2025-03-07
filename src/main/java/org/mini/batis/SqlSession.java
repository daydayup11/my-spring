package org.mini.batis;

import org.mini.jdbc.core.JdbcTemplate;
import org.mini.jdbc.core.PreparedStatementCallback;

public interface SqlSession {
	void setJdbcTemplate(JdbcTemplate jdbcTemplate);
	void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
	Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
	int delete(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
	int update(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
}
