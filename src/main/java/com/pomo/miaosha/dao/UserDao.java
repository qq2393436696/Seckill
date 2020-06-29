package com.pomo.miaosha.dao;

import com.pomo.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
	/*
	 * 根据ID查询用户
	 * */
	@Select("select * from user where id = #{id}")
	public User getById(@Param("id") int id);

	/*
	 * 插入用户
	 * */
	@Insert("insert into user(id, name) values (#{id}, #{name})")
	public int insert(User user);
}
