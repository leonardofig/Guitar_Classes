package com.example.guitarclasses.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.guitarclasses.databinding.FragmentAboutBinding
import com.example.guitarclasses.ui.students.StudentsViewModel

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val studentsViewModel =
            ViewModelProvider(this).get(AboutViewModel::class.java)

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView1: TextView = binding.textExtra1
        studentsViewModel.textExtra1.observe(viewLifecycleOwner) {
            textView1.text = it
        }
        val textView2: TextView = binding.textExtra2
        studentsViewModel.textExtra2.observe(viewLifecycleOwner) {
            textView2.text = it
        }
        val textView3: TextView = binding.textExtra3
        studentsViewModel.textExtra3.observe(viewLifecycleOwner) {
            textView3.text = it
        }
        val textView4: TextView = binding.textExtra4
        studentsViewModel.textExtra4.observe(viewLifecycleOwner) {
            textView4.text = it
        }
        val textView5: TextView = binding.textExtra5
        studentsViewModel.textExtra5.observe(viewLifecycleOwner) {
            textView5.text = it
        }
        val textView6: TextView = binding.textExtra6
        studentsViewModel.textExtra6.observe(viewLifecycleOwner) {
            textView6.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}