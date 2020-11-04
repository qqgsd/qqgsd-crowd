package com.qqgsd.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO implements Serializable {

    private static final long serialVersionUID = 2L;

    private String loginacct;

    private String userpswd;

    private String email;

    private String username;

}
