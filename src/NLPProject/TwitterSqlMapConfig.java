package NLPProject;

import java.io.Reader;

import org.apache.ibatis.io.Resources;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;


public class TwitterSqlMapConfig{
	public static SqlMapClient sqlMap= null;
	static {
	try {
	String resource ="SqlMapConfigExample.xml";
	Reader reader = Resources.getResourceAsReader(resource);
	sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
	} 
	catch (Exception e) {}
	}
	public static SqlMapClient getSqlMapInstance () {
		return sqlMap;
		}

}
