package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Users;
import utils.Util;

public class UserDetailDaoImpl implements UserDetailDao  {

	@Override
	public String getUserStatusByMobile(String mobile,String environment) {
		
		String userStatus = "";
		SqlSession session = null;
		try {
		 session= Util.getSqlSessionFactory(environment).openSession();
		 String statement = "src.main.resources.UsersMapper.getUserStatusByMobile";
		 userStatus = session.selectOne(statement, mobile);
         session.commit();
         return userStatus;
     }catch(Exception e){
         e.printStackTrace();
         if(session != null) {
         	session.rollback();
         }
     }finally{
         if(session != null){
             session.close();
         }
     }

		return null;
	}

	@Override
	public List<Users> getUserDetailInfoByMobile(String mobile, String environment) {
		// TODO Auto-generated method stub
		
		SqlSession session = null;
		try {
			session= Util.getSqlSessionFactory(environment).openSession();
		    String statement = "src.main.resources.UsersMapper.getUserDetailInfoByMobile";
		    List<Users> user= session.selectList(statement, mobile);
	         session.commit();
	         return user;
	       }catch(Exception e){
	         e.printStackTrace();
	         if(session != null) {
	         	session.rollback();
	         }
	     }finally{
	         if(session != null){
	             session.close();
	         }
	     }
		return null;

	}
	
	
	

}
