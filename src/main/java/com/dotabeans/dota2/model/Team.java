package com.dotabeans.dota2.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Team implements Serializable {
    @Id
    private Long team_id;
    private String name;
    @Column(name = "avatar_url")
    private String logo_url;

    public Team(Long team_id) {
        this.team_id = team_id;
    }
}
