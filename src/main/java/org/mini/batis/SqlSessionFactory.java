package org.mini.batis;

public interface SqlSessionFactory {
	SqlSession openSession();
	MapperNode getMapperNode(String name);
}
