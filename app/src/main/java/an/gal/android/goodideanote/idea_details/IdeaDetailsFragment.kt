package an.gal.android.goodideanote.idea_details

import an.gal.android.goodideanote.MainActivity
import an.gal.android.goodideanote.R
import an.gal.android.goodideanote.data.Idea
import an.gal.android.goodideanote.data.IdeaPurpose
import an.gal.android.goodideanote.databinding.FragmentIdeaDetailsBinding
import an.gal.android.goodideanote.storage.cursor.CursorDatabaseSQLiteOpenHelper
import an.gal.android.goodideanote.storage.room.RepositoryHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IdeaDetailsFragment : Fragment() {

    private var binding: FragmentIdeaDetailsBinding? = null

    /*private var _binding: FragmentIdeaDetailsBinding? = null
    private val binding get() = _binding!!*/

    private var id: Int? = null
    private var deleteIdea = false
    private var loadedIdea: Idea? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentIdeaDetailsBinding.inflate(inflater, container, false)
        return binding?.root

        /*_binding = FragmentIdeaDetailsBinding.inflate(inflater, container, false)
        return binding.root*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = IdeaDetailsFragmentArgs.fromBundle(requireArguments()).ideaId
        loadIdeaById()
        setToolbar()
    }

    private fun setToolbar() {
        activity?.let {
            val activityToolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.toolbar)
            activityToolbar.menu.clear()
            activityToolbar.inflateMenu(R.menu.menu_idea_detail)

            // toolbar menu onItemClick
            activityToolbar.setOnMenuItemClickListener {
                // on delete click
                if (it.itemId == R.id.action_delete) {
                    AlertDialog.Builder(requireContext(), R.style.AlertDialog_ChangeColors)
                        .setMessage(getString(R.string.delete_confirmation_question))
                        .setPositiveButton(getString(R.string.delete_confirmation_yes)) { dialog, _ ->
                            deleteIdea()
                            requireActivity().onBackPressed()
                            dialog.dismiss()
                        }
                        .setNegativeButton(getString(R.string.delete_confirmation_no)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                super.onOptionsItemSelected(it)
            }
        }
    }

    override fun onStop() {
        saveIdea()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun saveIdea() {
        if (!deleteIdea) {
            if (binding?.ideaEssence?.text.toString().isNotBlank() && loadedIdea != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(getString(R.string.settings_database_key), true)) {
                        // room
                        // Log.d("SettingsTest", "Room")
                        id?.let {
                            val repoRoom = RepositoryHolder.ideaRoomRepository()
                            repoRoom.updateIdea(id = it.toLong(),
                                title = binding?.ideaEssence?.text.toString(),
                                text = binding?.ideaText?.text.toString())
                        }
                    } else {
                        // cursor
                        // Log.d("SettingsTest", "Cursor")
                        id?.let {
                            val database = CursorDatabaseSQLiteOpenHelper(requireContext())
                            database.updateIdeaViaCursor(id = it.toLong(),
                                title = binding?.ideaEssence?.text.toString(),
                                text = binding?.ideaText?.text.toString(),
                                purposeId = loadedIdea?.purposeId ?: IdeaPurpose.getDefaultPurpose()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadIdeaById() {
        id?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val idea = if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(getString(R.string.settings_database_key), true)) {
                    // room
                    val repoRoom = RepositoryHolder.ideaRoomRepository()
                    repoRoom.getIdeaById(it.toLong())
                } else {
                    // cursor
                    val database = CursorDatabaseSQLiteOpenHelper(requireContext())
                    database.getCursorIdeaById(it.toLong())
                }

                loadedIdea = idea
                idea?.let {
                    withContext(Dispatchers.Main) {
                        binding?.apply {
                            ideaEssence.setText(idea.title)
                            ideaText.setText(idea.text)
                        }
                    }
                }
            }
        }
    }

    private fun deleteIdea() {
        deleteIdea = true
        id?.let {
            CoroutineScope(Dispatchers.IO).launch {
                if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(getString(R.string.settings_database_key), true)) {
                    // room
                    val repoRoom = RepositoryHolder.ideaRoomRepository()
                    repoRoom.deleteIdea(it.toLong())
                } else {
                    // cursor
                    val database = CursorDatabaseSQLiteOpenHelper(requireContext())
                    database.deleteIdeaViaCursor(id = it.toLong())
                }
            }
        }
    }
}

