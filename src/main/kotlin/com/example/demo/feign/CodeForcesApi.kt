package com.example.demo.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "CodeForcesApi" ,url = "https://codeforces.com/api")
interface CodeForcesApi {
    @GetMapping("/user.rating")
    fun getUserRatings(
        @RequestParam handle: String
    ):DTO.UserRating
}