package de.westnordost.streetcomplete.edithistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import de.westnordost.streetcomplete.Injector
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.edithistory.Edit
import de.westnordost.streetcomplete.data.edithistory.EditHistorySource
import de.westnordost.streetcomplete.view.insets_animation.respectSystemInsets
import kotlinx.android.synthetic.main.fragment_edit_history_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditHistoryFragment : Fragment(R.layout.fragment_edit_history_list) {

    @Inject internal lateinit var editHistorySource: EditHistorySource

    private val adapter = EditHistoryAdapter(this::onSelected, this::onUndo)

    private val editHistoryListener = object : EditHistorySource.Listener {
        override fun onAdded(edit: Edit) {
            lifecycleScope.launch { adapter.onAdded(edit) }
        }

        override fun onSynced(edit: Edit) {
            lifecycleScope.launch { adapter.onSynced(edit) }
        }

        override fun onDeleted(edits: List<Edit>) {
            lifecycleScope.launch { adapter.onDeleted(edits) }
            // TODO if empty now, close
        }

        override fun onInvalidated() {
            lifecycleScope.launch {
                val edits = withContext(Dispatchers.IO) { editHistorySource.getAll() }
                adapter.setEdits(edits)
            }
            // TODO if empty now, close
        }
    }

    init {
        Injector.applicationComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editHistorySource.addListener(editHistoryListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editHistoryList.respectSystemInsets { left, top, right, bottom ->
            setPadding(left, top, 0, bottom)
        }
        lifecycleScope.launch {
            val edits = withContext(Dispatchers.IO) { editHistorySource.getAll() }
            adapter.setEdits(edits)
            editHistoryList.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editHistorySource.removeListener(editHistoryListener)
    }

    private fun onSelected(edit: Edit) {
        // TODO
    }

    private fun onUndo(edit: Edit) {
        UndoDialog(requireContext(), edit).show()
    }
}
