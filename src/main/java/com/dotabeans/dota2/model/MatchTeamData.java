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
    private Integer duration;
    private Long start_time;
    private String league_name;
    private Long opposing_team_id;
    private String opposing_team_name;
    private String opposing_team_logo;
    private String formattedTime;
    private String actual_team_name;
    private String actual_team_logo;
    private Boolean actual_team_won;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchTeamData)) return false;

        MatchTeamData that = (MatchTeamData) o;

        if (!getMatch_id().equals(that.getMatch_id())) return false;
        if (!getRadiant_win().equals(that.getRadiant_win())) return false;
        if (!getStart_time().equals(that.getStart_time())) return false;
        return getLeague_name().equals(that.getLeague_name());
    }

    @Override
    public int hashCode() {
        int result = getMatch_id().hashCode();
        result = 31 * result + getRadiant_win().hashCode();
        result = 31 * result + getStart_time().hashCode();
        return result;
    }
}
