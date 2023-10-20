package com.example.guitarclasses.database

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.example.guitarclasses.account.User


class DBHelper (
    context: Context?
) : SQLiteOpenHelper(context, "database.db", null, 1) {

    val sql = arrayOf(
        "CREATE TABLE utilizador (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)",
        "INSERT INTO utilizador (username, password) VALUES ('admin','admin')",

        "CREATE TABLE turma (id INTEGER PRIMARY KEY AUTOINCREMENT, year Integer, designation TEXT);",
        "INSERT INTO turma (year, designation) VALUES('2021','Classical')",
        "INSERT INTO turma (year, designation) VALUES('2022','Rock')",
        "INSERT INTO turma (year, designation) VALUES('2023','Blues')",


        "CREATE TABLE aluno ( id INTEGER PRIMARY KEY AUTOINCREMENT, num Integer UNIQUE, name TEXT, address TEXT, phone INT, email TEXT, classId INT, FOREIGN KEY(classId) REFERENCES turma(id));",
        "INSERT INTO aluno (num, name, address, phone, email, classId) VALUES(1,'Maria', 'Rua do Joao', '961527272', 'maria@email.pt',1)",
        "INSERT INTO aluno (num, name, address, phone, email, classId) VALUES(2,'João', 'Rua do Joao', '915928452', 'joao@email.pt',2)",
        "INSERT INTO aluno (num, name, address, phone, email, classId) VALUES(3,'Joana', 'Rua do Joao', '915965472', 'joana@email.pt',3)",
        "INSERT INTO aluno (num, name, address, phone, email, classId) VALUES(4,'Filipe', 'Rua do Joao', '915927272', 'filipe@email.pt',2)"

    )

    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE utilizador")
        db.execSQL("DROP TABLE classes")
        db.execSQL("DROP TABLE aluno")
        onCreate(db)
    }

    fun eliminarConta(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("utilizador", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun mudarPassword(id: Int, username: String, password: String): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("password", password)
        val res = db.update("utilizador", cv, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun registar(username: String, password: String): Long {
        val db = this.writableDatabase

        //verificar o numero de users
        val cursor = db.rawQuery("SELECT COUNT(*) FROM utilizador", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()

        if (count >= 3) {
            db.close()
            return -1
        }

        // Insert new user
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("password", password)
        val res = db.insert("utilizador", null, cv)
        db.close()
        return res
    }
    fun getUserCount(): Int {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT COUNT(*) FROM utilizador", null)
        c.moveToFirst()
        val count = c.getInt(0)
        db.close()
        return count
    }

    fun login(username: String, password: String): Int {
        val db = this.readableDatabase
        val c = db.rawQuery(
            "SELECT * FROM utilizador WHERE username=? AND password = ?",
            arrayOf(username, password)
        )
        if (c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val id = c.getInt(idIndex)
            db.close()
            return id
        }
        db.close()
        return -1;
    }

    fun select(id: Int): User? {
        val db = this.readableDatabase
        val c = db.rawQuery(
            "SELECT * FROM utilizador WHERE id= ?",
            arrayOf(id.toString())
        )

        val utilizador = User()
        if (c.count > 0) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val usernameIndex = c.getColumnIndex("username")
            val passwordIndex = c.getColumnIndex("password")
            utilizador.id = c.getInt(idIndex)
            utilizador.username = c.getString(usernameIndex)
            utilizador.password = c.getString(passwordIndex)
            db.close()
            return utilizador
        }
        db.close()
        return null;
    }

//    Aluno ================================================================================

    fun selectAll(): ArrayList<Aluno> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM aluno", null)
        val listaAluno = ArrayList<Aluno>()
        if (c.count > 0) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val numIndex = c.getColumnIndex("num")
            val nameIndex = c.getColumnIndex("name")
            val addressIndex = c.getColumnIndex("address")
            val phoneIndex = c.getColumnIndex("phone")
            val emailIndex = c.getColumnIndex("email")
            val classIdIndex = c.getColumnIndex("classId")

            do {
                val id = c.getInt(idIndex)
                val num = c.getInt(numIndex)
                val name = c.getString(nameIndex)
                val address = c.getString(addressIndex)
                val phone = c.getInt(phoneIndex)
                val email = c.getString(emailIndex)
                val classId = c.getInt(classIdIndex)
                listaAluno.add(Aluno(id, num, name, address, phone, email, classId))
            } while (c.moveToNext())
        }
        db.close()
        return listaAluno
    }

    fun selectByID(id: Int): Aluno {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM aluno", null)
        val aluno = Aluno()
        if (c.count > 0) {
            c.moveToFirst()

            val idIndex = c.getColumnIndex("id")
            val numIndex = c.getColumnIndex("num")
            val nameIndex = c.getColumnIndex("name")
            val addressIndex = c.getColumnIndex("address")
            val phoneIndex = c.getColumnIndex("phone")
            val emailIndex = c.getColumnIndex("email")
            val classIdIndex = c.getColumnIndex("classId")

            aluno.id = c.getInt(idIndex)
            aluno.num = c.getInt(numIndex)
            aluno.name = c.getString(nameIndex)
            aluno.address = c.getString(addressIndex)
            aluno.phone = c.getInt(phoneIndex)
            aluno.email = c.getString(emailIndex)
            aluno.classId = c.getInt(classIdIndex)
        }
        db.close()
        return aluno
    }
    fun insert(num: Int, name: String, address: String, phone: Int, email: String, classId: Int): Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("num", num.toString())
        cv.put("name", name.toString())
        cv.put("address", address.toString())
        cv.put("phone", phone.toString())
        cv.put("email", email.toString())
        cv.put("classId", classId.toString())

        val res = db.insert("aluno", null, cv)
        db.close()
        return res
    }

    fun update(id: Int, num: Int, name: String, address: String, phone: Int, email: String, classId: Int): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("num", num)
        cv.put("name", name)
        cv.put("address", address)
        cv.put("phone", phone)
        cv.put("email", email)
        cv.put("classId", classId)
        val res = db.update("aluno", cv, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun delete(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("aluno", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }



// Turma ========================================================================

    fun selectAllTurma(): ArrayList<Turma> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM turma", null)
        val listaTurma = ArrayList<Turma>()
        if (c.count > 0) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val yearIndex = c.getColumnIndex("year")
            val designationIndex = c.getColumnIndex("designation")

            do {
                val id = c.getInt(idIndex)
                val year = c.getInt(yearIndex)
                val designation = c.getString(designationIndex)
                listaTurma.add(Turma(id, year, designation))
            } while (c.moveToNext())
        }
        db.close()
        return listaTurma
    }

    fun selectByIDTurma(id: Int): Turma {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM turma", null)
        val turma = Turma()
        if (c.count > 0) {
            c.moveToFirst()

            val idIndex = c.getColumnIndex("id")
            val yearIndex = c.getColumnIndex("year")
            val designationIndex = c.getColumnIndex("designation")

            turma.id = c.getInt(idIndex)
            turma.year = c.getInt(yearIndex)
            turma.designation = c.getString(designationIndex)
        }
        db.close()
        return turma
    }
    fun insertTurma(year: Int, designation: String): Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("year", year.toString())
        cv.put("designation", designation.toString())

        val res = db.insert("turma", null, cv)
        db.close()
        return res
    }

    fun updateTurma(id: Int, year: Int, designation: String): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("year", year)
        cv.put("designation", designation)
        val res = db.update("turma", cv, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun deleteTurma(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("turma", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

}