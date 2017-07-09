package com.safety.controller.vo;

import lombok.*;

/**
 * Created by fanwenbin on 2017/7/2.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    String token;
    String username;
}
