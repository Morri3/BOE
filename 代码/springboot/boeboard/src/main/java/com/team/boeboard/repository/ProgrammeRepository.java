package com.team.boeboard.repository;

import com.team.boeboard.entity.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ProgrammeRepository extends JpaRepository<Programme, Integer> {
    //取得最大id
    @Query(value = "select max(p_id) from programme", nativeQuery = true)
    int findMaxId();

    //按节目名查重
    @Query(value = "select count(*) from programme where pname = ?", nativeQuery = true)
    int findByName(String p_name);

    //getall
    @Query(value = "select * from programme where pstatus<>'已删除'", nativeQuery = true)
    List<Programme> GetAll();

    //删除节目
    @Modifying
    @Query(value = "update programme set pstatus = '已删除' where pname = ?  ",nativeQuery = true)
    void deleteProgrammeByName(String p_name);

    //按名称模糊查找节目
    @Query(value = "select * from programme where pname like CONCAT('%',?1,'%')", nativeQuery = true)
    List<Programme> SelectByName(String name);

    //按dpi模糊查找节目
    @Query(value = "select * from programme where dpi=?", nativeQuery = true)
    List<Programme> SelectByDpi(String dpi);

    //按状态模糊查找节目
    @Query(value = "select * from programme where pstatus=?", nativeQuery = true)
    List<Programme> SelectByStatus(String status);

    //重命名节目
    @Modifying
    @Query(value = "update programme set pname = ?2 where pname = ?1 ",nativeQuery = true)
    void Rename(String old_name,String new_name);

    //获取节目数量
    @Query(value = "select count(*) from programme",nativeQuery = true)
    int CountProgramme();

    //添加一个计划，使用某个节目，更新状态
    @Transactional
    @Modifying
    @Query(value = "update programme set pstatus='使用中',updatetime=?1 where p_id=?2", nativeQuery = true)
    void updateProgramStatus(Date updatetime, long p_id);

    @Transactional
    @Modifying
    @Query(value = "update programme set pstatus='未使用',updatetime=?1 where p_id=?2", nativeQuery = true)
    void changeOldProgram(Date updatetime, long p_id);

    @Transactional
    @Modifying
    @Query(value = "update programme set pstatus='使用中',updatetime=?1 where p_id=?2", nativeQuery = true)
    void changeNewProgram(Date updatetime, long p_id);

    @Transactional
    @Modifying
    @Query(value = "update programme set pstatus='未使用',updatetime=?1 where p_id=?2", nativeQuery = true)
    void deleteProgramByDeleteScheme(Date updatetime, long p_id);
}