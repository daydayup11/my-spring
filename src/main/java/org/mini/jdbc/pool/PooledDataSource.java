package org.mini.jdbc.pool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource{
	private BlockingQueue<PooledConnection> busy;
	private BlockingQueue<PooledConnection> idle;

	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private int initialSize = 2; // 初始连接数
	private int maxActive = 10; // 最大连接数
	private long maxWait = 5000; // 最大等待时间（毫秒）
	private Properties connectionProperties;
	private AtomicInteger size = new AtomicInteger(0); // 当前连接数

	public PooledDataSource() {
		this.busy = new LinkedBlockingQueue<>(maxActive);
		this.idle = new LinkedBlockingQueue<>(maxActive);
	}

	private void initPool() throws SQLException {
		for (int i = 0; i < initialSize; i++) {
			PooledConnection conn = createConnection();
			idle.offer(conn);
			size.incrementAndGet();
		}
	}
	private PooledConnection createConnection() throws SQLException {
		Properties mergedProps = new Properties();
		Properties connProps = getConnectionProperties();
		if (connProps != null) {
			mergedProps.putAll(connProps);
		}
		if (username != null) {
			mergedProps.setProperty("user", username);
		}
		if (password != null) {
			mergedProps.setProperty("password", password);
		}

		Connection conn = DriverManager.getConnection(url, mergedProps);
		return new PooledConnection(conn, this);
	}

	protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
		if (size.get() == 0) { // 如果连接池未初始化
			synchronized (this) {
				if (size.get() == 0) { // 双重检查锁
					initPool(); // 初始化连接池
				}
			}
		}
		if (idle.isEmpty() && size.get() < maxActive) {
			synchronized (this) {
				if (size.get() < maxActive) {
					PooledConnection conn = createConnection();
					busy.offer(conn);
					size.incrementAndGet();
					return conn;
				}
			}
		}

		long now = System.currentTimeMillis();
		while (true) {
			PooledConnection conn = idle.poll();
			if (conn != null) {
				busy.offer(conn);
				return conn;
			}

			if (System.currentTimeMillis() - now >= maxWait) {
				throw new SQLException("Timeout: Unable to fetch a connection in " + (maxWait / 1000) + " seconds.");
			}

			try {
				TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new SQLException("Thread interrupted while waiting for a connection.", e);
			}
		}
	}

	protected void returnConnection(PooledConnection conn) {
		if (busy.remove(conn)) {
			idle.offer(conn);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (size.get() == 0) { // 如果连接池未初始化
			synchronized (this) {
				if (size.get() == 0) { // 双重检查锁
					initPool(); // 初始化连接池
				}
			}
		}
		return getConnectionFromDriver(getUsername(), getPassword());
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		if (size.get() == 0) { // 如果连接池未初始化
			synchronized (this) {
				if (size.get() == 0) { // 双重检查锁
					initPool(); // 初始化连接池
				}
			}
		}
		return getConnectionFromDriver(username, password);
	}
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
		try {
			Class.forName(this.driverClassName);
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", ex);
		}
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}
	
	public Properties getConnectionProperties() {
		return connectionProperties;
	}
	public void setConnectionProperties(Properties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException {
		
	}

	@Override
	public void setLoginTimeout(int arg0) throws SQLException {
		
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}
}
