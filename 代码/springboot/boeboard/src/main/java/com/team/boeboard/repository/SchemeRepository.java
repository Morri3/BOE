package com.team.boeboard.repository;

import com.team.boeboard.entity.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface SchemeRepository extends JpaRepository<Scheme, Long>, JpaSpecificationExecutor<Scheme> {
    Scheme findById(long id);

    @Query(value = "select * from scheme where sname = ?1", nativeQuery = true)
    Scheme findSchemeIfAdded(String sname);

    @Transactional
    @Modifying
    @Query(value = "insert into scheme(d_id,p_id,sname,sstatus,playmode,cycles,strategy,synchronize,startday,endday," +
            "starttime,endtime,author,reviewer,updatetime,createtime) values(?1,?2,?3,'已发布',?4,?5,?6,?7,?8,?9,?10,?11,?12," +
            "?13,?14,?15)", nativeQuery = true)
    void addAScheme(long d_id, long p_id, String sname, int playmode, int cycles, int strategy, int synchronize, Date startday,
                    Date endday, Date starttime, Date endtime, String author, String reviewer, Date updatetime, Date createtime);

    @Transactional
    @Modifying
    @Query(value = "insert into scheme(d_id,p_id,sname,sstatus,playmode,strategy,synchronize,author,reviewer," +
            "updatetime,createtime) values(?1,?2,?3,'已发布',?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
    void addAScheme2(long d_id, long p_id, String sname, int playmode, int strategy, int synchronize,
                     String author, String reviewer, Date updatetime, Date createtime);

    @Query(value = "select MAX(s_id) from scheme", nativeQuery = true)
    long searchCreatedScheme();

    @Query(value = "select * from scheme where s_id=?1", nativeQuery = true)
    Scheme searchSchemeBySid(long s_id);

    @Query(value = "select * from scheme where sstatus!='已失效'", nativeQuery = true)
    List<Scheme> findListOfSchemes();

    @Transactional
    @Modifying
    @Query(value = "update scheme set d_id=?1,p_id=?2,sname=?3,sstatus='待发布',playmode=?4,cycles=?5,strategy=?6,synchronize=?7," +
            "startday=?8,endday=?9,starttime=?10,endtime=?11,updatetime=?12 where s_id=?13", nativeQuery = true)
    void updateAScheme(long d_id, long p_id, String sname, int playmode, int cycles, int strategy, int synchronize, Date startday,
                       Date endday, Date starttime, Date endtime, Date updatetime, long s_id);

    @Transactional
    @Modifying
    @Query(value = "update scheme set d_id=?1,p_id=?2,sname=?3,sstatus='待发布',playmode=?4,strategy=?5,synchronize=?6," +
            "updatetime=?7 where s_id=?8", nativeQuery = true)
    void updateAScheme2(long d_id, long p_id, String sname, int playmode, int strategy, int synchronize,
                        Date updatetime, long s_id);

    @Transactional
    @Modifying
    @Query(value = "update scheme set sstatus='已失效',updatetime=?1 where s_id=?2", nativeQuery = true)
    void deleteSchemeBySId(Date curtime, long s_id);

    @Query(value = "select count(*) from scheme", nativeQuery = true)
    int countScheme();

    @Query(value = "select * from scheme", nativeQuery = true)
    List<Scheme> searchSchemeNoLimited();

    @Query(value = "select * from scheme where sname like CONCAT('%',?1,'%')", nativeQuery = true)
    List<Scheme> searchSchemeBySname(String sname);

    @Query(value = "select * from scheme where sstatus=?1", nativeQuery = true)
    List<Scheme> searchSchemeBySstatus(String sstatus);

    @Query(value = "select * from scheme where sname like CONCAT('%',?1,'%') and sstatus=?2", nativeQuery = true)
    List<Scheme> searchSchemeBySnameAndSstatus(String sname, String sstatus);

    @Transactional
    @Modifying
    @Query(value = "insert into scheme(d_id,p_id,sname,sstatus,playmode,cycles,strategy,synchronize,startday,endday," +
            "starttime,endtime,author,reviewer,updatetime,createtime) values(?1,?2,?3,'待发布',?4,?5,?6,?7,?8,?9,?10," +
            "?11,?12,?13,?14,?15)", nativeQuery = true)
    void reproduceAScheme(long d_id, long p_id, String sname, int playmode, int cycles, int strategy, int synchronize,
                          Date startday, Date endday, Date starttime, Date endtime, String author, String reviewer,
                          Date updatetime, Date createtime);

    @Transactional
    @Modifying
    @Query(value = "insert into scheme(d_id,p_id,sname,sstatus,playmode,strategy,synchronize,author,reviewer," +
            "updatetime,createtime) values(?1,?2,?3,'待发布',?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
    void reproduceAScheme2(long d_id, long p_id, String sname, int playmode, int strategy, int synchronize,
                           String author, String reviewer, Date updatetime, Date createtime);

    @Transactional
    @Modifying
    @Query(value = "update scheme set sstatus='已发布',author=?1,reviewer=?2,updatetime=?3 " +
            "where s_id=?4", nativeQuery = true)
    void republish(String author, String reviewer, Date updatetime, long s_id);
}
