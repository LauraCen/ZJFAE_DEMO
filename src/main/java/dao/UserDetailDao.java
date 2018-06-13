package dao;

import java.util.List;

import mybatis.Users;

public interface UserDetailDao {
	
	 String getUserStatusByMobile(String mobile,String environment);
	 
	 List<Users> getUserDetailInfoByMobile(String mobile,String environment);

}
