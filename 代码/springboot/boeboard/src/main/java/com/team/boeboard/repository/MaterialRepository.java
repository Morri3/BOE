package com.team.boeboard.repository;

import com.team.boeboard.entity.Materials;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface MaterialRepository extends JpaRepository<Materials, Integer> {
    //取得最大id
    @Query(value = "select max(m_id) from materials", nativeQuery = true)
    int findMaxId();

    //获取material——list
    //获取活动List
    @Query(value = "select * from materials where mstatus!='已删除' ", nativeQuery = true)
    List<Materials> findAllMaterial();

    //按搜索内容模糊查找
    @Query(value = "select * from materials where mname like CONCAT('%',?1,'%')", nativeQuery = true)
    List<Materials> SelectByName(@Param("name") String name);

    //删除图片(软删除) 改变mstatus状态 未删除->已删除
    @Modifying
    @Query(value = "update materials set mstatus = '已删除' where mname = ?",nativeQuery = true)
    void deleteByName(String name);

    //修改文件名
    @Modifying
    @Query(value = "update materials set mname = ?2,updatetime = ?3 where mname = ?1",nativeQuery = true)
    void updateMaterial(String old_name, String new_name, Date updatetime);

    //获取素材大小list
    @Query(value = "select msize from materials ", nativeQuery = true)
    List<String> SelectMsize();

    //按名称获取素材大小
    @Query(value = "select msize from materials where mname = ?", nativeQuery = true)
    String GetMsizeByName(String mname);
}