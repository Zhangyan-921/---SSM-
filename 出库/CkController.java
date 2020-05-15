package com.controller;
/**
 * 出库数据接收 处理转发
 */

import com.pojo.Ck;
import com.pojo.Rk;
import com.service.CkService;
import com.service.RkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ck")
public class CkController {
    @Autowired
    private CkService ckService;
    private RkService rkService;

    public CkController(RkService rkService ) {
        this.rkService = rkService;
    }

    @RequestMapping("/addMethod") //增加
    public String addMethod(HttpServletRequest request, Ck ck) {
        String ckbh=ck.getCkbh();
        String str = ckService.getSame(ckbh);
        if(str==null) {
            Rk rk = rkService.selectByBh(ck.getRkbh());
            int rksl = rk.getSl();
            int cksl = 0;
            String tmp = ckService.selectSl(ck.getRkbh());
            if(tmp!=null){
                cksl = Integer.parseInt(tmp);
            }
            if((rksl-cksl)>=ck.getSl()){
                ck.setMc(rk.getMc());
                ck.setFl(rk.getFl());
                ck.setRkjg(rk.getRkjg());
                ck.setXsjg(rk.getXsjg());
                float xszj = ck.getSl()*rk.getXsjg();
                float rkzj = ck.getSl()*rk.getRkjg();
                float lr = xszj-rkzj;
                ck.setXszj(xszj);
                ck.setLr(lr);
                int flag = ckService.insertSelective(ck);
                if(flag==1){
                    request.setAttribute("message", "操作成功！");
                    return "admin/ck/index";
                }
                else{
                    request.setAttribute("message", "操作失败！");
                    return "admin/ck/index";
                }
            }
            else{
                request.setAttribute("message", "库存不足！");
                return "admin/ck/add";
            }
        }
        else{
            request.setAttribute("message", "信息重复！");
            return "admin/ck/add";
        }
    }
    @RequestMapping("/del/{id}") //删除
    public String deleteMethod(HttpServletRequest request, @PathVariable("id") int id) {
        int flag = ckService.deleteByPrimaryKey(id);
        if(flag==1){
            request.setAttribute("message", "操作成功！");
            return "admin/ck/index";
        }
        else{
            request.setAttribute("message", "操作失败！");
            return "admin/ck/index";
        }
    }

}
