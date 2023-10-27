import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Controller("")
class Controller {
    @Autowired
    lateinit var service: Service

    @GetMapping("/teams")
    fun getAllTeams():List<DTO.TeamInfo>{
        return service.getAllTeams()
    }
    @GetMapping("")
    fun sex():String{
        return "sex"
    }
}