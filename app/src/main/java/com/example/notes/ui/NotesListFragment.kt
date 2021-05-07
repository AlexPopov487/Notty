package com.example.notes.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
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
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.menu_switch.*
import java.util.logging.Level.INFO

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

        fun onAlertDialog(view: View) {
            AlertDialog.Builder(view.context, R.style.AlertDialogCustom)
                .setMessage(getString(R.string.delete_all_alert_dialog))
                .setPositiveButton(getString(R.string.delete_all_alert_confirmation)) { dialog, id ->
                    viewModel.removeAll()
                    Snackbar.make(view, getString(R.string.all_notes_deleted_snackbar), Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottom_fab)
                        .show()
                }
                .setNegativeButton(
                    getString(R.string.delete_all_alert_cancellation)
                ) { dialog, id ->
                    // User cancelled the dialog
                }
                .show()
        }

        binding.toolbar.inflateMenu(R.menu.menu_notes_list)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete_all -> {
                    onAlertDialog(binding.notesListRv)
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


        viewModel.noteData.observe(viewLifecycleOwner, Observer {
            viewModel.setNoteData()
        })

        viewModel.completeNoteList.observe(viewLifecycleOwner) { listToShow ->
            binding.layoutEmptyState.visibility =
                if (listToShow.isNullOrEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(listToShow)
        }

        viewModel.isEmptyNoteAdded.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(
                    binding.notesListRv,
                    getText(R.string.create_empty_note_error),
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(R.id.bottom_fab).show()

                viewModel.resetEmptyNoteAdded()
            }
        }

        binding.bottomFab.setOnClickListener {
            findNavController().navigate(R.id.action_notesListFragment_to_createEditNoteFragment)
        }


        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_choose_not_urgent -> {
                    menuItem.isChecked = !menuItem.isChecked

                    if (menuItem.isChecked) {
                        Log.i("NotesListFragment", "DEBUG: green checked")
                        menuItem.setIcon(R.drawable.ic_note_chooser_not_urgent_checked)
                    }  else {
                        Log.i("NotesListFragment", "DEBUG: green unchecked")
                        menuItem.setIcon(R.drawable.ic_note_chooser_not_urgent)
                    }

                    viewModel.filterByUrgency(1, menuItem.isChecked)
                    true
                }
                R.id.action_choose_urgent -> {
                    menuItem.isChecked = !menuItem.isChecked


                    if (menuItem.isChecked) {
                        Log.i("NotesListFragment", "DEBUG: yellow checked")
                        menuItem.setIcon(R.drawable.ic_note_chooser_urgent_checked)
                    }
                    else{
                        Log.i("NotesListFragment", "DEBUG: yellow unchecked")
                        menuItem.setIcon(R.drawable.ic_note_chooser_urgent)
                    }
                    viewModel.filterByUrgency(2, menuItem.isChecked)
                    true
                }

                R.id.action_choose_very_urgent -> {
                    menuItem.isChecked = !menuItem.isChecked


                    if (menuItem.isChecked) {
                        Log.i("NotesListFragment", "DEBUG: red checked")
                        menuItem.setIcon(R.drawable.ic_note_chooser_very_urgent_checked)
                    }
                    else{
                        Log.i("NotesListFragment", "DEBUG: red unchecked")
                        menuItem.setIcon(R.drawable.ic_note_chooser_very_urgent)
                    }
                    viewModel.filterByUrgency(3, menuItem.isChecked)
                    true
                }
                else -> false
            }
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
