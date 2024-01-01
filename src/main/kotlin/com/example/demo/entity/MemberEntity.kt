package com.example.demo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.context.annotation.Primary

@Entity
@Table(name = "member")
class MemberEntity (
    @Id
    val id: String,

    @Column
    val team: String,

    @Column
    var rating: Int?
){}