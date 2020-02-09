package com.dotabeans.dota2.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TeamData extends Team{

    private Double rating;
    private Integer wins;
    private Integer losses;
    private Long last_match_time;
    private Boolean following = false;
    private String formattedDate;
    private Integer winrate;

}
