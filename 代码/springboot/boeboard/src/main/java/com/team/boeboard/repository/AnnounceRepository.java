package com.team.boeboard.repository;

import com.team.boeboard.entity.Announce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AnnounceRepository extends JpaRepository<Announce, Integer> {

    //取得最大id
    @Query(value = "select max(a_id) from announce", nativeQuery = true)
    int findMaxId();

    //删除公告
    @Modifying
    @Query(value = "update announce set astatus='已删除' where content = ?",nativeQuery = true)
    void deleteByName(String content);

    //获取所有公告
    @Query(value = "select * from announce", nativeQuery = true)
    List<Announce> GetAllAnnounces();

    //更新状态
    @Modifying
    @Query(value = "update announce set astatus=? where a_id = ?",nativeQuery = true)
    void updateStatus(String status,long id);
}
