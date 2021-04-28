package com.example.notes.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.Note
import com.example.notes.R
import com.example.notes.adapter.InteractionListeners
import com.example.notes.adapter.NoteAdapter
import com.example.notes.databinding.FragmentNotesListBinding
import com.example.notes.utils.SwipeToDeleteCallback
import com.example.notes.viewModel.NoteViewModel
import com.google.android.material.snackbar.Snackbar

class NotesListFragment : Fragment() {
    private val viewModel: NoteViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNotesListBinding.inflate(inflater, container, false)

        
        binding.toolbar.inflateMenu(R.menu.menu_notes_list)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete_all -> {
                    Toast.makeText(context, "Lol", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val adapter = NoteAdapter(object : InteractionListeners {
            override fun onNoteClicked(note: Note) {

                val action =
                    NotesListFragmentDirections.actionNotesListFragmentToCreateEditNoteFragment(
                        note.id
                    )
                findNavController().navigate(action)
            }
        })

        binding.notesListRv.adapter = adapter


        viewModel.noteData.observe(viewLifecycleOwner, Observer { notesList ->
            binding.layoutEmptyState.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(notesList)
        })

        viewModel.isEmptyNoteAdded.observe(viewLifecycleOwner, {
            if (it) {
                Snackbar.make(
                    binding.notesListRv,
                    getText(R.string.create_empty_note_error),
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(R.id.bottom_fab).show()

                viewModel.resetEmptyNoteAdded()
            }
        })

        binding.bottomFab.setOnClickListener {
            findNavController().navigate(R.id.action_notesListFragment_to_createEditNoteFragment)
        }


        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                viewModel.removeNote(adapter.currentList[pos])
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesListRv)
        return binding.root
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        // it's important to explicitly tell our fragment that we are going to display a menu
//        setHasOptionsMenu(true)
//        super.onCreate(savedInstanceState)
//
//    }
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_notes_list, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_delete_all -> {
//                Toast.makeText(context, "Lol", Toast.LENGTH_SHORT).show()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}