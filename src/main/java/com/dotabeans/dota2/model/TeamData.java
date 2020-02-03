package com.dotabeans.dota2.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TeamData {
    private Long team_id;
    private Double rating;
    private Integer wins;
    private Integer losses;
    private Long last_match_time;
    private String name;
    private String logo_url;
    private Boolean following = false;
    private String formattedDate;
    private Integer winrate;

}
