package com.example.demo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.context.annotation.Primary
@Entity
@Table(name = "team")
class TeamEntity (
    @Id
    val name: String,

    @Column
    val rank: Int?,

    @Column
    val preliminaryRank: Int?
){}