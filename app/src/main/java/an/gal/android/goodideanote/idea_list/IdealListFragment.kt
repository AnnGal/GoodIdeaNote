package an.gal.android.goodideanote.idea_list

import an.gal.android.goodideanote.MainActivity
import an.gal.android.goodideanote.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import an.gal.android.goodideanote.databinding.FragmentIdeaListBinding
import an.gal.android.goodideanote.data.IdeaPurpose
import an.gal.android.goodideanote.databinding.FragmentIdeaDetailsBinding
import an.gal.android.goodideanote.storage.cursor.CursorDatabaseSQLiteOpenHelper
import an.gal.android.goodideanote.storage.room.RepositoryHolder
import android.content.SharedPreferences
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import kotlinx.coroutines.*

class IdealListFragment : Fragment() {

    private var binding: FragmentIdeaListBinding? = null

    /*private var _binding: FragmentIdeaListBinding? = null
    private val binding get() = _binding!!*/

    private lateinit var adapter: IdeaAdapter
    private val allowedIdeas = mutableSetOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //_binding = FragmentIdeaListBinding.inflate(inflater, container, false)
        binding = FragmentIdeaListBinding.inflate(inflater, container, false)

        adapter  = IdeaAdapter {  openIdeaPage(it) }

        binding?.let {
            it.recyclerView.adapter = adapter
            it.fabAddIdea.setOnClickListener {
                findNavController().navigate(IdealListFragmentDirections.toNewIdeaFragment())
            }
        }

        return binding?.root
    }

    private fun openIdeaPage(ideaId: Int) {
        findNavController().navigate(IdealListFragmentDirections.toDetailsIdeaFragment(ideaId))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            val activityToolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.toolbar)
            activityToolbar?.let{
                activityToolbar.menu.clear()
                activityToolbar.inflateMenu(R.menu.menu_main)

                // toolbar menu onItemClick
                activityToolbar.setOnMenuItemClickListener {
                    // on delete click
                    if (it.itemId == R.id.action_settings) {
                        findNavController().navigate(R.id.toSettingsFragment)
                    }
                    super.onOptionsItemSelected(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadIdeasFromDB()
    }

    private fun readIdeaPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        getPurposePreference(getString(R.string.settings_skill_key), prefs)
        getPurposePreference(getString(R.string.settings_home_key), prefs)
        getPurposePreference(getString(R.string.settings_work_key), prefs)
    }

    private fun getPurposePreference(key: String,prefs: SharedPreferences) {
        if (prefs.getBoolean(key, true)) {
            allowedIdeas.add(IdeaPurpose.getIdByName(key))
        } else {
            allowedIdeas.remove(IdeaPurpose.getIdByName(key))
        }
    }

    private fun loadIdeasFromDB() {
        readIdeaPreferences()

        CoroutineScope(Dispatchers.IO).launch {
            val list = if (PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(getString(R.string.settings_database_key), true)){
                // room
                // Log.d("SettingsTest", "Room")
                val repoRoom = RepositoryHolder.ideaRoomRepository()
                repoRoom.getAllIdeas().filter { allowedIdeas.contains(it.purposeId) }
            } else {
                // cursor
                // Log.d("SettingsTest", "Cursor")
                val database = CursorDatabaseSQLiteOpenHelper(requireContext())
                database.getIdeasFromCursor().filter { allowedIdeas.contains(it.purposeId) }
            }

            withContext(Dispatchers.Main){
                adapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}