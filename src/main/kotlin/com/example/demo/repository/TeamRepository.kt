package com.example.demo.repository

import com.example.demo.entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TeamRepository:JpaRepository<TeamEntity,String> {
    @Query("""
        select * 
        from team t
        join members m
        on t.name = m.team
    """, nativeQuery = true)
    fun getAllTeamAndMembers(
    )
}