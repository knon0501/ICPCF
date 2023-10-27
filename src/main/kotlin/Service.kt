import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import repository.TeamRepository

@Service
class Service {
    @Autowired
    lateinit var teamRepository: TeamRepository
    fun getAllTeams():List<DTO.TeamInfo>{
        val teams =  teamRepository.findAll();
        return teams.map{
            DTO.TeamInfo(
                it.name,
                it.preliminaryRandk,
                it.rank
            )
        }
    }
}