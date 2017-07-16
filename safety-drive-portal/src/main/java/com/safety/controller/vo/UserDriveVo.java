package com.safety.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by fanwenbin on 2017/7/15.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDriveVo {
    String level;
    String message;
}
