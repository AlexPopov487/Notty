package com.example.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hellow.utils.hideKeyboard
import com.example.notes.R
import com.example.notes.databinding.FragmentCreateEditNoteBinding
import com.example.notes.viewModel.NoteViewModel


class CreateEditNoteFragment : Fragment() {

    private val viewModel: NoteViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private var noteUrgencyIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreateEditNoteBinding.inflate(inflater, container, false)

        val args: CreateEditNoteFragmentArgs by navArgs()

        val noteIdArgs = args.noteId

        // if note id = -1L, we are creating a new note
        if (noteIdArgs != -1L) {
            binding.appBarTitleText.text = getString(R.string.editNote_toolbarTitle)
            val noteToEdit = viewModel.getNoteById(noteIdArgs)
            viewModel.onNoteEdit(noteToEdit)

            val deleteMenuItem = binding.toolbar.menu[0]
            deleteMenuItem.setOnMenuItemClickListener {
                viewModel.removeNote(noteToEdit)
                findNavController().navigateUp()
                true
            }


            if (noteToEdit.title.isNotBlank()) binding.noteTitleEt.setText(noteToEdit.title)
            if (noteToEdit.content.isNotBlank()) binding.noteContentEt.setText(noteToEdit.content)
            noteUrgencyIndex = noteToEdit.urgencyLevel
        } else {
            // change toolbar title if we are creating a new note
            binding.toolbar.menu[0].isVisible = false
            binding.appBarTitleText.text = getString(R.string.createNote_toolbarTitle)
        }

        viewModel.editedNote.observe(viewLifecycleOwner,{
            when (it.urgencyLevel){
                1 -> binding.toggleButton.check(R.id.notUrgent_button)
                2 -> binding.toggleButton.check(R.id.urgent_button)
                3 -> binding.toggleButton.check(R.id.very_Urgent_button)
            }
        })

        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->

            noteUrgencyIndex = when (checkedId) {
                binding.notUrgentButton.id -> 1
                binding.urgentButton.id -> 2
                binding.veryUrgentButton.id ->3
                else -> 0
            }
            // allow to uncheck any toggle button even if it was previously checked
            if (!isChecked) noteUrgencyIndex = 0

        }

        with(binding) {
            saveNoteFab.setOnClickListener {
                val title = noteTitleEt.text.toString()
                val content = noteContentEt.text.toString()
                viewModel.changeNoteContent(title, content, noteUrgencyIndex)
                viewModel.saveNote()
                noteUrgencyIndex = 0
                it.hideKeyboard()
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

}
