package com.dotabeans.dota2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PlayerData {

    private Long account_id;
    private String name;
    private Integer games_played;
    private Integer wins;
    private Boolean is_current_team_member;

}
