package entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.context.annotation.Primary

@Entity
class TeamEntity (
    @Id
    val name: String,

    @Column
    val rank: Int?,

    @Column
    val preliminaryRandk: Int?
){}