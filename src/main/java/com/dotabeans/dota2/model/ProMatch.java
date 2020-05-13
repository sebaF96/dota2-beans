package com.dotabeans.dota2.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class ProMatch {

    private Long match_id;
    private Integer duration;
    private Long start_time;
    private Integer radiant_team_id;
    private String radiant_name;
    private Integer dire_team_id;
    private String dire_name;
    private Integer radiant_score;
    private Integer dire_score;
    private Boolean radiant_win;
}
