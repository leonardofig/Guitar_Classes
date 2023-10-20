package com.example.guitarclasses.database

class Aluno (var id: Int = 0, var num: Int = 0, var name: String = "", var address: String = "", var phone: Int = 0, var email: String = "", var classId: Int = 0) {
    override fun toString(): String {
        return "$num - $name"
    }
}

