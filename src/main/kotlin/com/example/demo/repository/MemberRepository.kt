package com.example.demo.repository

import com.example.demo.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<MemberEntity,String>{
    @Query(
        """
            select id 
            from member m
            where m.team = :team
        """, nativeQuery = true
    )
    fun findAllIdByTeam(team: String):List<String>

    @Query(
        """
            select *
            from member m
            where m.team = :team
        """, nativeQuery = true
    )
    fun findAllByTeam(team: String):List<MemberEntity>
    @Modifying
    @Query(
        """
            delete 
            from member m
            where m.team = :team
        """, nativeQuery = true
    )
    fun deleteAllByTeam(team: String)
}