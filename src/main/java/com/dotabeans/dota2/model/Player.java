package com.dotabeans.dota2.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Player implements Serializable {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;
}
