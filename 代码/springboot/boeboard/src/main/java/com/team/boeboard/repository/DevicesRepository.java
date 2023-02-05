package com.team.boeboard.repository;

import com.team.boeboard.entity.Devices;
import com.team.boeboard.form.devices.ApkInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface DevicesRepository extends JpaRepository<Devices, Long>, JpaSpecificationExecutor<Devices> {
    Devices findById(long id);

    @Query(value = "select * from devices where mac1 = ?1", nativeQuery = true)
    Devices findDeviceByMac(String mac1);

    @Transactional
    @Modifying
    @Query(value = "insert into devices(dname,inst,groupings,dstatus,dmodel,ver1,dip,ver2,mac1,mac2,ram,sn," +
            "storespace,lat,createtime,dpi,instlarea,showway) values(?1,?2,null,'离线','HiDPTAndroid Hi3751V553'," +
            "'BOE_iGallery32_V13103_V5.2.0','192.168.43.1','1.3.1','A0BB3ED3861D',?3,'0.96 GB',?4," +
            "'4.70 GB可用（共 5.39 GB）','2022-06-23 10:40:12','2022-06-23 11:28:01','1920x1080'," +
            "'中国浙江省杭州市拱墅区上塘街道东教路','横屏')", nativeQuery = true)
    void addDevice(String dname, String inst, String mac2, String sn);

    @Query(value = "select MAX(d_id) from devices", nativeQuery = true)
    long searchCreatedDevice();

    @Query(value = "select * from devices where d_id=?1", nativeQuery = true)
    Devices searchDeviceByDid(long d_id);

    @Transactional
    @Modifying
    @Query(value = "update devices set dname=?1,groupings=?2 where d_id=?3", nativeQuery = true)
    void editDevice(String dname, String groupings, long d_id);

    @Query(value = "select * from devices", nativeQuery = true)
    List<Devices> findListOfDevices();

    @Query(value = "select count(*) from devices", nativeQuery = true)
    int countDevices();

    @Query(value = "select inst,SUM(case when inst is not NULL then 1 else 0 end ) 'cnt' " +
            "from devices group by inst", nativeQuery = true)
    Map<String, Integer> inst();

    @Query(value = "select groupings,SUM(case when groupings is not NULL then 1 else 0 end ) 'cnt' " +
            " from devices group by groupings", nativeQuery = true)
    Map<String, Integer> groupings();

    @Transactional
    @Modifying
    @Query(value = "update devices set dstatus=?1 where d_id=1", nativeQuery = true)
    void upAndDown(String dstatus);

    @Query(value = "select * from devices where d_id=1", nativeQuery = true)
    Devices findDeviceByAbsLoc();

    @Query(value = "select * from devices where dname=?1", nativeQuery = true)
    Devices searchDeviceByDname(String dname);

    @Transactional
    @Modifying
    @Query(value = "update devices set groupings=?1 where dname=?2", nativeQuery = true)
    void updateGroupings(String groupings, String dname);

    @Query(value = "select * from devices where groupings=?1", nativeQuery = true)
    Devices searchDeviceByGname(String gname);

    @Transactional
    @Modifying
    @Query(value = "update devices set d_update = ? where d_id =1", nativeQuery = true)
    void UpdateDevice(String d_update);

}
