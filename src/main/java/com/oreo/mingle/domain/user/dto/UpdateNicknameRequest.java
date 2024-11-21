package com.oreo.mingle.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNicknameRequest {
    private String nickname;
    private String name;
}
