package com.dotabeans.dota2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class MatchTeamData {
    private Long match_id;
    private Boolean radiant_win;
    private Boolean radiant;
    private Long start_time;
    private String league_name;
    private Long opposing_team_id;
    private String opposing_team_name;
    private String opposing_team_logo;
    private String formattedTime;



}
