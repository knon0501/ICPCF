package repository

import entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository:JpaRepository<TeamEntity,String> {

}