package com.example.guitarclasses.ui.students


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.guitarclasses.R
import com.example.guitarclasses.database.Aluno
import com.example.guitarclasses.database.DBHelper
import com.example.guitarclasses.database.Turma
import com.example.guitarclasses.databinding.FragmentStudentsBinding

class StudentsFragment : Fragment() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DBHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        db = DBHelper(requireActivity().applicationContext)

        // ListaAluno
        var pos = -1
        val listaAluno= db.selectAll()
        val adapter = ArrayAdapter(requireActivity().applicationContext, android.R.layout.simple_list_item_1, listaAluno)
        binding.listView.adapter = adapter

        // spinner classId
        val spinner = binding.spinnerClassId
        val turmaList = db.selectAllTurma()
        val turmaClassIdList = turmaList.map {Turma (it.id, it.year, it.designation) }
        val adaptert = ArrayAdapter(requireActivity().applicationContext, android.R.layout.simple_spinner_item, turmaClassIdList)
        spinner.adapter = adaptert


        //Ordenar listaAluno
        val sortButton = binding.buttonSort
        var isAscOrder = true // Flag to indicate the current order of the list
        sortButton.setOnClickListener {
            if (isAscOrder) {
                // Sort the list in descending order
                listaAluno.sortByDescending { it.num }
                isAscOrder = false
            } else {
                // Sort the list in ascending order
                listaAluno.sortBy { it.num }
                isAscOrder = true
            }
            adapter.notifyDataSetChanged()
        }

        binding.textCounter.text = "Total: ${listaAluno.size} students"
        binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            binding.editNum.setText(listaAluno[i].num.toString())
            binding.editName.setText(listaAluno[i].name)
            binding.editAddress.setText(listaAluno[i].address)
            binding.editPhone.setText(listaAluno[i].phone.toString())
            binding.editEmail.setText(listaAluno[i].email)
            binding.spinnerClassId.setSelection(turmaClassIdList.indexOfFirst {it.id == listaAluno[i].classId })
            pos = i
        }

        binding.buttonInsert.setOnClickListener {
            val num = binding.editNum.text.toString()
            val name = binding.editName.text.toString()
            val address = binding.editAddress.text.toString()
            val phone = binding.editPhone.text.toString()
            val email = binding.editEmail.text.toString()
            val selectedClass = binding.spinnerClassId.selectedItem as Turma
            val classId = selectedClass.id


            if (num.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() ) {
                Toast.makeText(requireActivity().applicationContext, "Fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var res = db.insert(num.toInt(), name, address, phone.toInt(), email, classId.toInt())
                if (res > 0) {
                    listaAluno.add(Aluno(res.toInt(), num.toInt(), name, address, phone.toInt(), email, classId.toInt()))
                    Toast.makeText(requireActivity().applicationContext, "Student created successfully", Toast.LENGTH_SHORT).show()
                    binding.textCounter.text = "Total: ${listaAluno.size} students"
                } else {
                    Toast.makeText(requireActivity().applicationContext, "Error - Student number already exists", Toast.LENGTH_SHORT).show()
                }
                adapter.notifyDataSetChanged()
                adaptert.notifyDataSetChanged()
            }
        }
        binding.buttonPhoneCall.setOnClickListener{
            val phonenumber = binding.editPhone.text.toString()
            if(phonenumber.isEmpty()){
                Toast.makeText(activity, "Select a student first", Toast.LENGTH_SHORT).show()
            }else{
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:$phonenumber")
                startActivity(dialIntent)
            }

        }

        binding.buttonEmailSend.setOnClickListener {
            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            selectorIntent.data = Uri.parse("mailto:")
            val emailSend = Intent(Intent.ACTION_SENDTO)
            emailSend.putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.editEmail.text.toString()))
            emailSend.putExtra(Intent.EXTRA_SUBJECT, "GuitarClasses")
            emailSend.putExtra(Intent.EXTRA_TEXT, "Submitted from Guitar Class")
            emailSend.selector = selectorIntent
            startActivity(emailSend)

        }
        binding.buttonEdit.setOnClickListener {
            if (pos >= 0) {
                val id = listaAluno.get(pos).id
                val num = binding.editNum.text.toString()
                val name = binding.editName.text.toString()
                val address = binding.editAddress.text.toString()
                val phone = binding.editPhone.text.toString()
                val email = binding.editEmail.text.toString()
                //val classId = binding.editClassId.text.toString()

                val selectedClass = binding.spinnerClassId.selectedItem as Turma
                val classId = selectedClass.id

                if (num.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() ) {
                    Toast.makeText(requireActivity().applicationContext, "Fill in all fields", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val res = db.update(id, num.toInt(), name, address, phone.toInt(), email, classId.toInt())
                    if (res > 0) {
                        listaAluno.get(pos).num = num.toInt()
                        listaAluno.get(pos).name = name
                        listaAluno.get(pos).address = address
                        listaAluno.get(pos).phone = phone.toInt()
                        listaAluno.get(pos).email = email
                        listaAluno.get(pos).classId = classId

                        Toast.makeText(requireActivity().applicationContext, "Student edited successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireActivity().applicationContext, "Error when editing student", Toast.LENGTH_SHORT).show()
                    }
                    adapter.notifyDataSetChanged()
                    adaptert.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(requireActivity().applicationContext, "Select a student to edit", Toast.LENGTH_SHORT).show()
            }
        }


        binding.buttonDelete.setOnClickListener {
            if (pos >= 0) {
                val id = listaAluno.get(pos).id
                val res = db.delete(id)
                if (res > 0) {
                    listaAluno.removeAt(pos)
                    Toast.makeText(requireActivity().applicationContext, "Student successfully deleted", Toast.LENGTH_SHORT).show()
                    binding.textCounter.setText("Total: ${listaAluno.size} students")
                    binding.editNum.setText("")
                    binding.editName.setText("")
                    binding.editAddress.setText("")
                    binding.editPhone.setText("")
                    binding.editEmail.setText("")
                    binding.spinnerClassId.setSelection(0)
                } else {
                    Toast.makeText(requireActivity().applicationContext, "Error deleting student", Toast.LENGTH_SHORT).show()
                }
                adapter.notifyDataSetChanged()
                adaptert.notifyDataSetChanged()
            } else {
                Toast.makeText(requireActivity().applicationContext, "Select a student to delete", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}