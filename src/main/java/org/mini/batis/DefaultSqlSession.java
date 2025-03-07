package org.mini.batis;

import org.mini.beans.factory.annotation.Autowired;
import org.mini.jdbc.core.JdbcTemplate;
import org.mini.jdbc.core.PreparedStatementCallback;

import javax.sql.DataSource;

public class DefaultSqlSession implements SqlSession{

	JdbcTemplate jdbcTemplate;
	private DataSource readDataSource;
	private DataSource writeDataSource;

	public void setReadDataSource(DataSource readDataSource) {
		this.readDataSource = readDataSource;
	}

	public void setWriteDataSource(DataSource writeDataSource) {
		this.writeDataSource = writeDataSource;
	}

	public DataSource getReadDataSource() {
		return readDataSource;
	}

	public DataSource getWriteDataSource() {
		return writeDataSource;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	SqlSessionFactory sqlSessionFactory;
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	public SqlSessionFactory getSqlSessionFactory() {
		return this.sqlSessionFactory;
	}
	
	@Override
	public Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
		System.out.println(sqlid);
		String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
		System.out.println(sql);
		MapperNode mapperNode = this.sqlSessionFactory.getMapperNode(sqlid);
		if (mapperNode.getSqlType().equals("0")) {
			jdbcTemplate.setDataSource(readDataSource);
		}
		return jdbcTemplate.query(sql, args, pstmtcallback);
	}

	@Override
	public int delete(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
		MapperNode mapperNode = this.sqlSessionFactory.getMapperNode(sqlid);
		if (mapperNode.getSqlType().equals("1")) {
			jdbcTemplate.setDataSource(writeDataSource);
		}
		return jdbcTemplate.delete(mapperNode.getSql(), args, pstmtcallback);
	}

	@Override
	public int update(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
		MapperNode mapperNode = this.sqlSessionFactory.getMapperNode(sqlid);
		if (mapperNode.getSqlType().equals("2")) {
			jdbcTemplate.setDataSource(writeDataSource);
		}
		return jdbcTemplate.update(mapperNode.getSql(), args,pstmtcallback);

	}


	private void buildParameter(){
	}
	
	private Object resultSet2Obj() {
		return null;
	}

}
