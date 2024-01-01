package com.example.demo

data class TeamForm(
    var teamName: String? = null,
    var memberNames: MutableList<String?> = MutableList(3) { null }
)
