package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.bean.Menu;

/**
 * @author 咸鱼
 * @date 2018/6/21 21:11
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDto extends Menu {
}
