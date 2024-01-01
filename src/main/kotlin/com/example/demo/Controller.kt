package com.example.demo

import DTO
import com.example.demo.repository.TeamRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class Controller {
    @Autowired
    lateinit var service: Service
    @Autowired
    lateinit var teamRepository: TeamRepository

    @GetMapping("/team")
    fun showForm(model: Model): String {
        model.addAttribute("teamForm", TeamForm())
        model.addAttribute("teamNames",teamRepository.findAll().map{it.name})
        val teams = service.getTeamsAndMembers()

        // 선형 회귀 계산 (위에서 제공한 코틀린 함수를 사용할 수 있습니다.)
        val prePoints = teams.filter{it.memberList.isNotEmpty()}.filter { it.preliminaryRank != null }.filter { it.preliminaryRank!! <= 100 }.
        map{ Pair(it.preliminaryRank!!.toDouble(),it.memberList.sumOf { it.rating }.toDouble() / it.memberList.filter { it.rating > 0 }.size)}
        val (preSlope, preIntercept) = service.linearRegression(prePoints)

        model.addAttribute("pre_points", prePoints)
        model.addAttribute("pre_slope", preSlope)
        model.addAttribute("pre_intercept", preIntercept)
        model.addAttribute("pre_rSquared",service.getPreCorrel())

        val points = teams.filter{it.memberList.isNotEmpty() && it.rank != null}.
        map{ Pair(it.rank!!.toDouble(),it.memberList.sumOf { it.rating }.toDouble() / it.memberList.filter { it.rating > 0 }.size)}
        val (slope, intercept) = service.linearRegression(points)
        // Thymeleaf에 데이터 전달
        model.addAttribute("points", points)
        model.addAttribute("slope", slope)
        model.addAttribute("intercept", intercept)
        model.addAttribute("rSquared",service.getCorrel())

        return "teamForm"
    }
    @PostMapping("/team")
    fun submitForm(@ModelAttribute teamForm: TeamForm, model: Model): String {
        if(teamForm.teamName != null)
            service.postMemberInfo(teamForm.memberNames.filterNotNull().map{
                DTO.MemberInfo(
                    teamForm.teamName!!,
                    it
                )
            })

        model.addAttribute("teamForm", teamForm)
        return "redirect:/team"
    }


    @GetMapping("teams")
    fun getAllTeams():List<DTO.TeamInfo>{
        return service.getAllTeams()
    }

    @GetMapping("teams-members")
    fun getTeamsMembers():List<DTO.TeamAndMemberInfo>{
        return service.getTeamsAndMembers()
    }
    @GetMapping("update/rating")
    fun updateRating():DTO.Response{
        return service.updateRating()
    }
    @PostMapping("members")
    fun postMemberInfo(@RequestBody memberList: List<DTO.MemberInfo>):DTO.Response{
        return service.postMemberInfo(memberList)
    }

    @GetMapping("correl")
    fun getCorrel():Double{
        return service.getCorrel()
    }

}