package com.learn.cloud.common.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title MyInsertBatch
 * @package com.learn.cloud.common.mybatis
 * @description 批量插入方法
 * @date 2020/1/2 4:31 下午
 */
public class MyInsertBatch extends AbstractMethod {
	
	/**
	 * 批量插入方法组装 <BR>
	 * @param mapperClass
	 * @param modelClass
	 * @param tableInfo
	 * @return org.apache.ibatis.mapping.MappedStatement
	 * @throws Exception 异常
	 * @author zhangwei
	 * @createTime 2020/1/2 4:40 下午
	 */
	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		final String sql = "<script>insert into %s %s values %s</script>";
		final String fieldSql = prepareFieldSql(tableInfo);
		final String valueSql = prepareValuesSqlForMysqlBatch(tableInfo);
		final String sqlResult = String.format(sql, tableInfo.getTableName(), fieldSql, valueSql);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, "myInsertBatch", sqlSource, new NoKeyGenerator(), null, null);
	}
	
	
	/**
	 * 组装表字段 <BR>
	 * @param tableInfo
	 * @return java.lang.String
	 * @throws Exception 异常
	 * @author zhangwei
	 * @createTime 2020/1/2 4:39 下午
	 */
	private String prepareFieldSql(TableInfo tableInfo) {
		StringBuilder fieldSql = new StringBuilder();
		fieldSql.append(tableInfo.getKeyColumn()).append(",");
		tableInfo.getFieldList().forEach(x -> {
			fieldSql.append(x.getColumn()).append(",");
		});
		fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
		fieldSql.insert(0, "(");
		fieldSql.append(")");
		return fieldSql.toString();
	}
	
	/**
	 * 组装批量value数据<BR>
	 * @param tableInfo 表信息对象
	 * @return java.lang.String
	 * @author zhangwei
	 * @createTime 2020/1/2 4:38 下午
	 */
	private String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		final StringBuilder valueSql = new StringBuilder();
		valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
		valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
		tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
		valueSql.delete(valueSql.length() - 1, valueSql.length());
		valueSql.append("</foreach>");
		return valueSql.toString();
	}
}
