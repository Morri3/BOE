package com.team.boeboard.service.impl;

import com.team.boeboard.entity.Devices;
import com.team.boeboard.entity.Groupings;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.groupings.GroupingsAddAndEditInputDto;
import com.team.boeboard.form.groupings.GroupingsDeleteInputDto;
import com.team.boeboard.form.groupings.GroupingsDto;
import com.team.boeboard.repository.DevicesRepository;
import com.team.boeboard.repository.GroupingsRepository;
import com.team.boeboard.service.GroupingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupingsServiceImpl implements GroupingsService {
    private final Logger logger = LoggerFactory.getLogger(GroupingsServiceImpl.class);

    @Autowired
    private GroupingsRepository groupingsRepository;
    @Autowired
    private DevicesRepository devicesRepository;

    @Override
    public GroupingsDto add(GroupingsAddAndEditInputDto groupingsAddAndEditInputDto) throws BoeBoardServiceException {
        GroupingsDto res = new GroupingsDto();

        if (groupingsAddAndEditInputDto != null) {
            //获取传入的dto
            String gname = groupingsAddAndEditInputDto.getGname();//分组名称
            String inst = groupingsAddAndEditInputDto.getInst();//所属机构
            String memo = groupingsAddAndEditInputDto.getMemo();//分组描述
            String dname = groupingsAddAndEditInputDto.getDname();//设备名称

            //查找是否存在gname
            Groupings group = groupingsRepository.searchGroupingsByGname(gname);
            if (group != null) {
                //构造返回的dto
                res.setG_id(0);
                res.setMemo("分组已经存在，添加分组失败");
            } else {//可以添加
                //根据dname找device
                Devices devices = devicesRepository.searchDeviceByDname(dname);
                if (devices != null) {
                    //添加分组记录
                    groupingsRepository.addAGrouping(gname, memo, inst);

                    //更新设备的分组字段
                    devicesRepository.updateGroupings(gname, dname);

                    //获取刚创建的分组
                    long g_id = groupingsRepository.searchCreatedGroupings();
                    Groupings groupings = groupingsRepository.findGroupingsByGid(g_id);//根据id找分组实体

                    //构造返回的dto
                    res.setG_id(groupings.getId());
                    res.setGname(groupings.getGname());
                    res.setInst(groupings.getInst());
                    res.setMemo(groupings.getMemo());
                } else {
                    //构造返回的dto
                    res.setG_id(0);
                    res.setMemo("添加分组失败");
                }
            }
        } else {
            //构造返回的dto
            res.setG_id(0);
            res.setMemo("添加分组失败");
        }

        return res;
    }

    @Override
    public GroupingsDto edit(GroupingsAddAndEditInputDto groupingsAddAndEditInputDto) throws BoeBoardServiceException {
        GroupingsDto res = new GroupingsDto();

        if (groupingsAddAndEditInputDto != null) {
            //获取传入的dto
            String gname = groupingsAddAndEditInputDto.getGname();//分组名称
            String inst = groupingsAddAndEditInputDto.getInst();//所属机构
            String memo = groupingsAddAndEditInputDto.getMemo();//分组描述
            String dname = groupingsAddAndEditInputDto.getDname();//设备名称

            //查找是否存在gname
            Groupings groupings = groupingsRepository.searchGroupingsByGname(gname);
            if (groupings != null) {
                //构造返回的dto
                res.setG_id(0);
                res.setMemo("分组已经存在，编辑分组失败");
            } else {//不存在gname，可以修改分组
                //根据dname找device
                Devices devices = devicesRepository.searchDeviceByDname(dname);

                if (devices != null) {
                    //获取设备修改前的分组name
                    String name = devices.getGroupings();

                    //根据旧name找到编辑的那个实体
                    Groupings curgroup = groupingsRepository.searchGroupingsByGname(name);

                    if (curgroup != null) {//找到当前的分组实体
                        //更新分组
                        groupingsRepository.updateAGrouping(gname, memo, inst, curgroup.getId());

                        //更新设备的分组字段
                        devicesRepository.updateGroupings(gname, dname);

                        //构造返回的dto
                        res.setG_id(curgroup.getId());
                        res.setGname(gname);
                        res.setInst(inst);
                        res.setMemo(memo);
                    } else {
                        //构造返回的dto
                        res.setG_id(0);
                        res.setMemo("编辑分组失败");
                    }
                } else {
                    //构造返回的dto
                    res.setG_id(0);
                    res.setMemo("编辑分组失败");
                }
            }
        } else {
            //构造返回的dto
            res.setG_id(0);
            res.setMemo("编辑分组失败");
        }
        return res;
    }

    @Override
    public GroupingsDto delete(GroupingsDeleteInputDto groupingsDeleteInputDto) throws BoeBoardServiceException {
        GroupingsDto res = new GroupingsDto();

        if (groupingsDeleteInputDto != null) {
            //获取传入的gname
            String gname = groupingsDeleteInputDto.getGname();

            //根据gname找Groupings实体
            Groupings curgroup = groupingsRepository.searchGroupingsByGname(gname);

            if (curgroup != null) {//存在分组实体
                //根据分组的名称在设备中找是否有设备使用该分组，若有就把对应设备的分组置为null
                Devices devices = devicesRepository.searchDeviceByGname(gname);

                if (devices != null) {//有设备使用该分组
                    devicesRepository.updateGroupings(null, devices.getDname());
                }

                //根据g_id删除分组
                groupingsRepository.deleteGroupingsByGid(curgroup.getId());

                //构造返回的dto
                res.setG_id(curgroup.getId());
                res.setGname(curgroup.getGname());
                res.setInst(curgroup.getInst());
                res.setMemo(curgroup.getMemo());
            } else {
                //构造返回的dto
                res.setG_id(0);
                res.setMemo("删除分组失败");
            }
        }
        return res;
    }

    @Override
    public List<GroupingsDto> getAll() throws BoeBoardServiceException {
        List<GroupingsDto> res = new ArrayList<>();

        List<Groupings> list = groupingsRepository.getAllGroupings();
        for (Groupings item : list) {
            GroupingsDto dto = new GroupingsDto();
            dto.setG_id(item.getId());
            dto.setGname(item.getGname());
            dto.setMemo(item.getMemo());
            dto.setInst(item.getInst());
            res.add(dto);
        }
        return res;
    }
}