package an.gal.android.goodideanote.idea_new

import an.gal.android.goodideanote.MainActivity
import an.gal.android.goodideanote.R
import an.gal.android.goodideanote.data.Idea
import an.gal.android.goodideanote.data.IdeaPurpose
import an.gal.android.goodideanote.databinding.FragmentNewIdeaBinding
import an.gal.android.goodideanote.storage.cursor.CursorDatabaseSQLiteOpenHelper
import an.gal.android.goodideanote.storage.room.RepositoryHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import kotlinx.coroutines.*

class NewIdeaFragment : Fragment() {

    private var binding: FragmentNewIdeaBinding? = null

    /*private var _binding: FragmentNewIdeaBinding? = null
    private val binding get() = _binding!!*/

    private var chosenPurpose: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewIdeaBinding.inflate(inflater, container, false)
        return binding?.root

        /*_binding = FragmentNewIdeaBinding.inflate(inflater, container, false)
        return binding.root*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpinner()

        binding?.addButton?.setOnClickListener {
            addIdea()
        }

        activity?.let {
            val activityToolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.toolbar)
            activityToolbar.menu.clear()
        }
    }

    private fun setSpinner() {
        val purposes = IdeaPurpose.getPurposeList()
        val adapter = PurposeAdapter(requireContext(), purposes)

        binding?.apply {
            spinnerPurpose.adapter = adapter

            spinnerPurpose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    chosenPurpose = IdeaPurpose.getIdByName(purposes[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    chosenPurpose = IdeaPurpose.getDefaultPurpose()
                }
            }
        }
    }

    private fun addIdea() {
        CoroutineScope(Dispatchers.IO).launch {
            val title = binding?.newIdeaTitle?.text.toString().trim()
            val text = binding?.newIdeaText?.text.toString().trim()
            val purposeId = chosenPurpose ?: IdeaPurpose.getDefaultPurpose()

            if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(getString(R.string.settings_database_key), true)) {
                // room
                // Log.d("SettingsTest", "Room")
                val repoRoom = RepositoryHolder.ideaRoomRepository()
                repoRoom.writeIdea(Idea(null, title, text, purposeId))
            } else {
                // cursor
                // Log.d("SettingsTest", "Cursor")
                val database = CursorDatabaseSQLiteOpenHelper(requireContext())
                database.insertIdeaViaCursor(title, text, purposeId)
            }
        }

        requireActivity().onBackPressed()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}