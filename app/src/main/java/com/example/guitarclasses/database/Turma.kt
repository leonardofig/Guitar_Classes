package com.example.guitarclasses.database

class Turma (var id: Int = 0, var year: Int = 0, var designation: String = "") {
    override fun toString(): String {
        return "$year - $designation"
    }
}