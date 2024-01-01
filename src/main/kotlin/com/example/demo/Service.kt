package com.example.demo

import DTO
import com.example.demo.entity.MemberEntity
import com.example.demo.feign.CodeForcesApi
import com.example.demo.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.demo.repository.TeamRepository
import jakarta.transaction.Transactional
import java.lang.Exception
import java.lang.Math.sqrt
import kotlin.math.pow

@Service
class Service {
    @Autowired
    lateinit var teamRepository: TeamRepository
    @Autowired
    lateinit var memberRepository: MemberRepository
    @Autowired
    lateinit var codeForcesApi: CodeForcesApi

    fun linearRegression(points: List<Pair<Double,Double>>): Pair<Double, Double> {
        val n = points.size
        val sumX = points.sumOf { it.first }
        val sumY = points.sumOf { it.second }
        val sumX2 = points.sumOf { it.first * it.first }
        val sumXY = points.sumOf { it.first * it.second }

        val slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX)
        val intercept = (sumY - slope * sumX) / n

        return Pair(slope, intercept)
    }
    fun getAllTeams():List<DTO.TeamInfo>{
        val teams =  teamRepository.findAll();
        return teams.map{
            DTO.TeamInfo(
                name = it.name,
                rank = it.rank,
                preliminaryRank =  it.preliminaryRank,
                message = "success"
            )
        }
    }
    @Transactional
    fun updateRating():DTO.Response{
        val members = memberRepository.findAll()
        for(member in members){
            member.rating = try{
                codeForcesApi.getUserRatings(member.id).result.maxOf { it.newRating }
            }catch (e: Exception){
                0
            }
        }
        return DTO.Response("success")
    }
    fun getTeamsAndMembers():List<DTO.TeamAndMemberInfo>{
        val teams = teamRepository.findAll()
        return teams.map {
            DTO.TeamAndMemberInfo(
                teamName = it.name,
                rank = it.rank,
                preliminaryRank = it.preliminaryRank,
                memberList = memberRepository.findAllByTeam(it.name).map{
                    DTO.MemberRating(it.id,it.rating!!)
                },
                message = "success"
            )
        }
    }
    @Transactional
    fun postMemberInfo(memberList: List<DTO.MemberInfo>):DTO.Response{
        if(memberList.size > 3 || memberList.isEmpty()){
            return DTO.Response("Fail")
        }
        memberRepository.deleteAllByTeam(memberList.first().teamName)
        memberRepository.saveAll(
            memberList.filter { it.id != "" }.map {
                MemberEntity(
                    id = it.id,
                    team = it.teamName,
                    rating = try{
                        codeForcesApi.getUserRatings(it.id).result.maxOf{it.newRating}
                    }catch(e:Exception){
                        0
                    }
                )
            }
        )
        return DTO.Response("success")
    }
    fun calculatePearsonCorrelation(x: List<Double>, y: List<Double>): Double {
        val n = x.size
        if (n != y.size || n <= 1) {
            throw IllegalArgumentException("Input lists must have the same size and contain at least 2 elements.")
        }

        val meanX = x.average()
        val meanY = y.average()

        val numerator = x.mapIndexed { i, xi -> (xi - meanX) * (y[i] - meanY) }.sum()
        val denominatorX = x.map { (it - meanX).pow(2) }.sum()
        val denominatorY = y.map { (it - meanY).pow(2) }.sum()

        return numerator / sqrt(denominatorX * denominatorY)
    }
    fun getPreCorrel():Double{
        val infoList = getTeamsAndMembers().filter { it.preliminaryRank != null && it.preliminaryRank <=100 }
        val rankList = infoList.mapNotNull{
            if(it.memberList.count{it.rating>0} == 0 )null
            else it.preliminaryRank?.toDouble()
        }
        val averageRatingList = infoList.mapNotNull {
            if(it.memberList.count{it.rating>0} == 0 || it.preliminaryRank == null)null
            else it.memberList.sumOf { it.rating }.toDouble() / it.memberList.count{it.rating > 0}
        }

        return try{
            calculatePearsonCorrelation(rankList,averageRatingList).pow(2)
        }catch(e: Exception){
            0.0
        }
    }

    fun getCorrel():Double{
        val infoList = getTeamsAndMembers()
        val rankList = infoList.mapNotNull{
            if(it.memberList.count{it.rating>0} == 0)null
            else it.rank?.toDouble()
        }
        val averageRatingList = infoList.mapNotNull {
            if(it.memberList.count{it.rating>0} == 0 || it.rank == null)null
            else it.memberList.sumOf { it.rating }.toDouble() / it.memberList.count{it.rating > 0}
        }

        return try{
            calculatePearsonCorrelation(rankList,averageRatingList).pow(2)
        }catch(e: Exception){
            0.0
        }
    }
}