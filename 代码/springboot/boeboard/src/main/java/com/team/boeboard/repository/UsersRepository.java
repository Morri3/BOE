package com.team.boeboard.repository;

import com.team.boeboard.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
    Users findById(long id);

    @Query(value = "select * from users where username = ?1 and phone=?2", nativeQuery = true)
    Users findIsRegistered(String username, String phone);

    @Transactional
    @Modifying
    @Query(value = "insert into users(username,userpwd,organ,userrole,ustatus,realname,phone,useremail," +
            "updatetime,createtime,category) values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11)", nativeQuery = true)
    void register(String username, String userpwd, String organ, String userrole, String ustatus,
                  String realname, String phone, String useremail, Date updatetime, Date createtime, int category);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Users findIsRegisteredByUsername(String username);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Users findUserByUsername(String username);

    @Query(value = "select * from users where u_id = ?1", nativeQuery = true)
    Users findAUserByUId(long u_id);

    @Transactional
    @Modifying
    @Query(value = "update users set userpwd=?1,ustatus=?2,realname=?3,phone=?4,useremail=?5,updatetime=?6 " +
            "where username=?7 and phone=?8", nativeQuery = true)
    void modify(String userpwd, String ustatus, String realname, String phone, String useremail, Date updatetime,
                String username, String oldphone);

    @Transactional
    @Modifying
    @Query(value = "update users set ustatus='启用',updatetime=?1 where username=?2", nativeQuery = true)
    void enable(Date updatetime, String username);

    @Transactional
    @Modifying
    @Query(value = "update users set ustatus='停用',updatetime=?1 where username=?2", nativeQuery = true)
    void disable(Date updatetime, String username);

    @Transactional
    @Modifying
    @Query(value = "update users set ustatus='已删除',updatetime=?1 where username=?2", nativeQuery = true)
    void deleteUserByUsername(Date updatetime, String username);

    //ustatus为”所有“
    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and organ=?2 and userrole=?3 " +
            "and ustatus!='已删除'", nativeQuery = true)
    List<Users> searchUsers1(String username, String organ, String userrole);

    @Query(value = "select * from users where organ = ?1 and userrole=?2 and ustatus!='已删除'",
            nativeQuery = true)
    List<Users> searchUsers2(String organ, String userrole);

    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and userrole=?2 " +
            "and ustatus!='已删除'", nativeQuery = true)
    List<Users> searchUsers3(String username, String userrole);

    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and organ=?2 and " +
            "ustatus!='已删除'", nativeQuery = true)
    List<Users> searchUsers4(String username, String organ);

    @Query(value = "select * from users where userrole=?1 and ustatus!='已删除'", nativeQuery = true)
    List<Users> searchUsers5(String userrole);

    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and ustatus!='已删除'",
            nativeQuery = true)
    List<Users> searchUsers6(String username);

    @Query(value = "select * from users where organ=?1 and ustatus!='已删除'", nativeQuery = true)
    List<Users> searchUsers7(String organ);

    @Query(value = "select * from users where ustatus!='已删除'", nativeQuery = true)
    List<Users> searchUsers15();

    //ustatus有限制
    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and organ=?2 and userrole=?3 " +
            "and ustatus=?4", nativeQuery = true)
    List<Users> searchUsers8(String username, String organ, String userrole, String ustatus);

    @Query(value = "select * from users where organ = ?1 and userrole=?2 and ustatus=?3", nativeQuery = true)
    List<Users> searchUsers9(String organ, String userrole, String ustatus);

    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and userrole=?2 and " +
            "ustatus=?3", nativeQuery = true)
    List<Users> searchUsers10(String username, String userrole, String ustatus);

    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and organ=?2 and ustatus=?3",
            nativeQuery = true)
    List<Users> searchUsers11(String username, String organ, String ustatus);

    @Query(value = "select * from users where userrole=?1 and ustatus=?2", nativeQuery = true)
    List<Users> searchUsers12(String userrole, String ustatus);

    @Query(value = "select * from users where username like CONCAT('%',?1,'%') and ustatus=?2",
            nativeQuery = true)
    List<Users> searchUsers13(String username, String ustatus);

    @Query(value = "select * from users where organ=?1 and ustatus=?2", nativeQuery = true)
    List<Users> searchUsers14(String organ, String ustatus);

    @Query(value = "select * from users where ustatus=?1", nativeQuery = true)
    List<Users> searchUsers16(String ustatus);

    @Query(value = "select * from users", nativeQuery = true)
    List<Users> findListOfUsers();

}
