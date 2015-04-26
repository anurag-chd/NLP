package NLPProject;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

public class IbatisDriver {
	public static void main(String args[]){
		SqlMapClient sqlMap = TwitterSqlMapConfig.getSqlMapInstance();
		System.out.println(sqlMap);
		try {
			sqlMap.getCurrentConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
