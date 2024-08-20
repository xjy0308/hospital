package com.aaa.mapper;

import com.aaa.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectAllUser(User user);

    User getUserByParam(UserVo userVo);

    int updateUser(User user);

    //查询
    List<Role> initUserRole(Role role);


    //添加用户
    int addUser(User user);


    //删除用户所拥有的角色
    void deleteUser(Integer userid);

    int deleteUserById(Integer userid);

    int deleteRoleUser(Integer userid);

    User getUserRoleByParam(Integer userId, Integer roleId);

    //重置密码
    int resetUserPwd(User user);

    //查询所有用户角色
    DataGridView queryUserRole(Integer userid);

    //查询所有角色
    List<Role> queryAllRole();

    //按照用户id查询角色
    List<Role> queryRoleById(Integer uid);

    //保存角色和菜单
    void saveRoleMenu(RoleMenu roleMenu);

    //保存用户和角色
    void insertUserRole(@Param("uid") Integer userid, @Param("rid") Integer rid);


    List<User> updateLogin(User user);

    int editLogin(User user);

    int editPwd(User user);

    //检查唯一
    int checkUser(User user);

    @Select("SELECT * FROM sys_user WHERE identity = #{id}")
    User selectUserByIdcode(String id);

    @Select("SELECT MAX(userid) FROM sys_user")
    int getUserNum();

    @Select("SELECT * FROM sys_user WHERE loginname = #{loginname}")
    User getUserByLoginname(String loginname);

    @Insert("INSERT INTO sys_user (userid, realname, identity, loginname, pwd, available, salt) VALUES (#{userid}, #{realname}, #{identity}, #{loginname}, #{pwd}, #{available}, #{salt})")
    void toaddaUser(int userid, String realname, String identity, String loginname, String pwd, Integer available, String salt);




}
