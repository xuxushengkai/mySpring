package com.sjj.spring.demo.service.impl;

import com.sjj.spring.demo.service.IModifyService;
import com.sjj.spring.formework.webmvc.annotation.SJService;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ModifyService
 * @Description TODO
 * Author shengjunjie
 * Date 2019/10/24 18:25
 **/
@SJService
@Slf4j
public class ModifyService implements IModifyService {

    /*** 增加 */
    @Override
    public String add(String name, String addr) {
        return "modifyService add,name=" + name + ",addr=" + addr;
    }

    /*** 修改 */
    @Override
    public String edit(Integer id, String name) {
        return "modifyService edit,id=" + id + ",name=" + name;
    }

    /*** 删除 */
    @Override
    public String remove(Integer id) {
        return "modifyService id=" + id;
    }
}
