package entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.context.annotation.Primary

@Entity
class MemberEntity (
    @Id
    val Id: String,

    @Column
    val team: String
){}