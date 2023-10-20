package com.example.guitarclasses.ui.classes

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.guitarclasses.database.DBHelper
import com.example.guitarclasses.database.Turma
import com.example.guitarclasses.databinding.FragmentClassesBinding


class ClassesFragment : Fragment() {

    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DBHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        db = DBHelper(requireActivity().applicationContext)

        //val root: View = binding.root

        val db = DBHelper(activity)


        var pos = -1
        val listaTurma = db.selectAllTurma()


        val adapter = ArrayAdapter(requireActivity().applicationContext, R.layout.simple_list_item_1, listaTurma)
        binding.listViewTurma.adapter = adapter


        binding.textContador.setText("Total: ${listaTurma.size} classes")
        binding.listViewTurma.setOnItemClickListener { adapterView, view, i, l ->
            binding.editYearClass.setText(listaTurma[i].year.toString())
            binding.editDesignationClass.setText(listaTurma[i].designation)
            pos = i
        }

        //Sort listaTurma
        val sortButton = binding.buttonSort
        var isAscOrder = true
        sortButton.setOnClickListener {
            if (isAscOrder) {
                listaTurma.sortByDescending { it.year }
                isAscOrder = false
            } else {
                listaTurma.sortBy { it.year }
                isAscOrder = true
            }
            adapter.notifyDataSetChanged()
        }

        binding.buttonInsertTurma.setOnClickListener {
            val year = binding.editYearClass.text.toString()
            val designation = binding.editDesignationClass.text.toString()
            if (year.isEmpty() || designation.isEmpty()) {
                Toast.makeText(requireActivity().applicationContext, "Fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var res = db.insertTurma(year.toInt(), designation)
                if (res > 0) {
                    listaTurma.add(Turma(res.toInt(), year.toInt(), designation))
                    Toast.makeText(requireActivity().applicationContext, "Class created successfully", Toast.LENGTH_SHORT).show()
                    binding.textContador.text = "Total: ${listaTurma.size} classes"
                } else {
                    Toast.makeText(requireActivity().applicationContext, "Error! Class number already exists.", Toast.LENGTH_SHORT).show()
                }
                adapter.notifyDataSetChanged()
            }
        }
        binding.buttonEditTurma.setOnClickListener {
            if (pos >= 0) {
                val id = listaTurma.get(pos).id
                val year = binding.editYearClass.text.toString()
                val designation = binding.editDesignationClass.text.toString()

                if (year.isEmpty() || designation.isEmpty()) {
                    Toast.makeText(requireActivity().applicationContext, "Fill in all fields", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val res = db.updateTurma(id, year.toInt(), designation)
                    if (res > 0) {
                        listaTurma.get(pos).year = year.toInt()
                        listaTurma.get(pos).designation = designation

                        Toast.makeText(requireActivity().applicationContext, "Class edited successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireActivity().applicationContext, "Error when editing class", Toast.LENGTH_SHORT).show()
                    }
                    adapter.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(requireActivity().applicationContext, "Select a class to edit", Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonDeleteTurma.setOnClickListener {
            if (pos >= 0) {
                val id = listaTurma.get(pos).id
                val res = db.deleteTurma(id)
                if (res > 0) {
                    listaTurma.removeAt(pos)
                    Toast.makeText(requireActivity().applicationContext, "Class Deleted Successfully", Toast.LENGTH_SHORT).show()
                    binding.textContador.setText("Total: ${listaTurma.size} classes")
                } else {
                    Toast.makeText(requireActivity().applicationContext, "Error when deleting class", Toast.LENGTH_SHORT).show()
                }
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireActivity().applicationContext, "Select the class to delete", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}