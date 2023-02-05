package com.team.boeboard.controller;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.programme.GetProgrammesDto;
import com.team.boeboard.form.programme.ProgrammeDto;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/programme")
public class ProgrammeController {

    @Autowired
    private ProgrammeService programmeService;


    /**
     *
     * @param programmeDto      //web端新建节目
     *                          {
     *                              "p_name": "zzz",                 //节目名称
     *                              "dpi": "1920*1080",              //节目分辨率
     *                              "materials": [                   //素材 数组
     *                                  {
     *                                      "mname":"bg1.png",
     *                                      "murl":"http://usqn.lk/rspisf",
     *                                      "category":1
     *                                  },
     *                                  {
     *                                      "mname":"bg1.png",
     *                                      "murl":"http://usqn.lk/rspisf",
     *                                      "category":1
     *                                  },
     *                                  {
     *                                      "mname":"bg1.png",
     *                                      "murl":"http://usqn.lk/rspisf",
     *                                      "category":1
     *                                  }
     *                              ],
     *                              "plength": 5                    //节目时长(轮播时间)
     *                          }
     * @param user_token     //用户token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseData addProgramme(@RequestBody ProgrammeDto programmeDto,@RequestParam String user_token) {
        try {
            programmeService.AddProgramme(programmeDto,user_token);
            return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
        }catch (BoeBoardServiceException e){
            System.out.println(e);
        }
        return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
    }

    /**
     * get所有节目
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public ResponseData GetAllProgramme(){
        List<GetProgrammesDto> result = programmeService.GetAllProgramme();
        return new ResponseData(ExceptionMsg.SUCCESS,result);
    }

    /**
     * 删除节目
     * @param map
     *              {
     *                  "p_name":"zzz"  //要删除的节目名
     *              }
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseData DeleteProgramme(@RequestBody Map map){
        String p_name = (String)map.get("p_name");
        programmeService.DeleteProgramme(p_name);

        return new ResponseData(ExceptionMsg.SUCCESS,"删除成功");
    }

    /**
     * 按input节目名模糊查找节目
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search/byname",method = RequestMethod.GET)
    public ResponseData SearchByName(@RequestBody Map map){

        String search_content = (String)map.get("search_content");
        List<GetProgrammesDto> result = new ArrayList<>();
        try {
             result = programmeService.SearchByName(search_content);
        }catch (BoeBoardServiceException e){
            return  new ResponseData(ExceptionMsg.FAILED,e);
        }
        return new ResponseData(ExceptionMsg.SUCCESS,result);
    }

    /**
     * 按dpi查找节目
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search/bydpi",method = RequestMethod.GET)
    public ResponseData SearchByDpi(@RequestBody Map map){

        String search_content = (String)map.get("search_content");
        List<GetProgrammesDto> result = new ArrayList<>();
        try {
            result = programmeService.SearchByName(search_content);
        }catch (BoeBoardServiceException e){
            return  new ResponseData(ExceptionMsg.FAILED,e);
        }
        return new ResponseData(ExceptionMsg.SUCCESS,result);
    }

    /**
     * 按状态查找节目
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search/bystatus",method = RequestMethod.GET)
    public ResponseData SearchByStatus(@RequestBody Map map){

        String search_content = (String)map.get("search_content");
        List<GetProgrammesDto> result = new ArrayList<>();
        try {
            result = programmeService.SearchByName(search_content);
        }catch (BoeBoardServiceException e){
            return  new ResponseData(ExceptionMsg.FAILED,e);
        }
        return new ResponseData(ExceptionMsg.SUCCESS,result);
    }

    /**
     * 修改节目名称
     * @param map
     *              {
     *                  "old_name":"zzzz"
     *                  "new_name":"zzzzzz"
     *              }
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rename",method = RequestMethod.POST)
    public ResponseData RenameProgramme(@RequestBody Map map){
        String old_name = (String)map.get("old_name");
        String new_name = (String)map.get("new_name");
        programmeService.RenameProgramme(old_name,new_name);

        return new ResponseData(ExceptionMsg.SUCCESS,"修改成功");
    }

}
