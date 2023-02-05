package com.team.boeboard.repository;

import com.team.boeboard.entity.AndroidApk;
import com.team.boeboard.entity.Announce;
import com.team.boeboard.form.devices.ApkInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AndroidApkRepository extends JpaRepository<AndroidApk, Integer> {

    @Query(value = "select * from android_apk where id=1",nativeQuery = true)
    AndroidApk GetApkInfo();
}
