package com.team.boeboard.repository;

import com.team.boeboard.entity.Groupings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface GroupingsRepository extends JpaRepository<Groupings, Integer> {
    @Transactional
    @Modifying
    @Query(value = "insert into groupings(gname,memo,inst) values(?1,?2,?3)", nativeQuery = true)
    void addAGrouping(String gname, String memo, String inst);

    @Query(value = "select MAX(g_id) from groupings", nativeQuery = true)
    long searchCreatedGroupings();

    @Query(value = "select * from groupings where g_id=?1", nativeQuery = true)
    Groupings findGroupingsByGid(long g_id);

    @Query(value = "select * from groupings where gname=?1", nativeQuery = true)
    Groupings searchGroupingsByGname(String gname);

    @Transactional
    @Modifying
    @Query(value = "update groupings set gname=?1,memo=?2,inst=?3 where g_id=?4", nativeQuery = true)
    void updateAGrouping(String gname, String memo, String inst, long g_id);

    @Transactional
    @Modifying
    @Query(value = "delete from groupings where g_id=?1", nativeQuery = true)
    void deleteGroupingsByGid(long g_id);

    @Query(value = "select * from groupings", nativeQuery = true)
    List<Groupings> getAllGroupings();

}
