package com.xds.project.entity;

import java.util.List;

/**
 * @TODO
 * @date 2020/7/19.
 * @email
 */
public class SearchBean {


    /**
     * code : 0
     * count : 2
     * message : 查询成功
     * data : [{"id":"07820c46-1c78-4719-9af3-807de4381771","name":null,"content":"进入系统，性能测试","keyss":"未建立索引","typeid":"3fd6fec9-a53c-493d-af58-26381f269c7f","typename":"性能测试","createTime":null},{"id":"eefbb79c-36f0-476b-befc-d2543c099bcf","name":null,"content":"进入自动化测试app检测系统,","keyss":"未建立索引","typeid":"247d2096-c2a4-496b-9a27-6f491c006ac0","typename":"常规测试","createTime":null}]
     */

    public int code;
    public int count;
    public String message;
    public List<TestCase> data;

}
