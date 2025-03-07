package org.mini.test.service;



import org.mini.batis.SqlSession;
import org.mini.batis.SqlSessionFactory;
import org.mini.beans.factory.annotation.Autowired;
import org.mini.jdbc.core.JdbcTemplate;
import org.mini.jdbc.core.RowMapper;
import org.mini.test.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SqlSessionFactory sqlSessionFactory;

	public User getUserInfoBatis(int userid) {
		//final String sql = "select id, name,birthday from users where id=?";
		String sqlid = "org.mini.test.entity.User.getUserInfo";
		SqlSession sqlSession = sqlSessionFactory.openSession();
		return (User)sqlSession.selectOne(sqlid, new Object[]{new Integer(userid)},
				(pstmt)->{
					ResultSet rs = pstmt.executeQuery();
					User rtnUser = null;
					if (rs.next()) {
						rtnUser = new User();
						rtnUser.setId(userid);
						rtnUser.setName(rs.getString("name"));
						rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
					} else {
					}
					return rtnUser;
				}
		);
	}
	//delete
	public int deleteUserInfoBatis(int userid) {
		String sqlid = "org.mini.test.entity.User.deleteUser";
		return sqlSessionFactory.openSession().delete(sqlid, new Object[]{new Integer(userid)},
				(pstmt)->{
					return pstmt.executeUpdate();
				}
		);
	}
	//update
	public int updateUserInfoBatis(int userid, String name){
		String sqlid = "org.mini.test.entity.User.updateUser";
		return sqlSessionFactory.openSession().update(sqlid, new Object[]{name,new Integer(userid)},
				(pstmt)->{
					return pstmt.executeUpdate();
				}
		);
	}


	public User getUserInfo(int userid) {
		final String sql = "select id, name,birthday from user where id=?";
		return (User)jdbcTemplate.query(sql, new Object[]{new Integer(userid)},
				(pstmt)->{			
					ResultSet rs = pstmt.executeQuery();
					User rtnUser = null;
					if (rs.next()) {
						rtnUser = new User();
						rtnUser.setId(userid);
						rtnUser.setName(rs.getString("name"));
						rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
					} else {
					}
					return rtnUser;
				}
		);
	}
	
	public List<User> getUsers(int userid) {
		final String sql = "select id, name,birthday from users where id>?";
		return (List<User>)jdbcTemplate.query(sql, new Object[]{new Integer(userid)},
						new RowMapper<User>(){
							public User mapRow(ResultSet rs, int i) throws SQLException {
								User rtnUser = new User();
								rtnUser.setId(rs.getInt("id"));
								rtnUser.setName(rs.getString("name"));
								rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
		
								return rtnUser;
							}
						}
		);
	}
}
