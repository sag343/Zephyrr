package greenberg.moviedbshell.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import greenberg.moviedbshell.R
import greenberg.moviedbshell.adapters.CrewListAdapter
import greenberg.moviedbshell.base.BaseFragment
import greenberg.moviedbshell.models.ui.CrewMemberItem
import greenberg.moviedbshell.state.CrewState
import greenberg.moviedbshell.state.PersonDetailArgs
import greenberg.moviedbshell.viewmodel.CrewViewModel
import timber.log.Timber

class CrewFragment : BaseFragment() {

    private val viewModel: CrewViewModel by fragmentViewModel()

    private lateinit var crewRecycler: RecyclerView
    private lateinit var crewAdapter: CrewListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.crew_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crewRecycler = view.findViewById(R.id.crew_members_recycler)
        layoutManager = GridLayoutManager(activity, 3)
        crewRecycler.layoutManager = layoutManager
        crewAdapter = CrewListAdapter(onClickListener = this::onClickListener)
        crewRecycler.adapter = crewAdapter
    }

    private fun showDetails(state: CrewState) {
        if (state.crewMembersJson.isNotEmpty()) {
            val castList = Gson().fromJson<List<CrewMemberItem>>(
                    state.crewMembersJson,
                    object : TypeToken<List<CrewMemberItem>>() {}.type
            )
            crewAdapter.crewMemberList = castList
            crewAdapter.notifyDataSetChanged()
        }
    }

    override fun invalidate() {
        withState(viewModel) { state ->
            showDetails(state)
        }
    }

    private fun onClickListener(personId: Int) {
        navigate(
                R.id.action_movieDetailFragment_to_personDetailFragment,
                PersonDetailArgs(personId)
        )
    }

    override fun log(message: String) {
        Timber.d(message)
    }

    override fun log(throwable: Throwable) {
        Timber.e(throwable)
    }
}