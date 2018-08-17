package greenberg.moviedbshell.mosbyImpl

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import greenberg.moviedbshell.models.PopularMoviesModels.PopularMovieResultsItem
import greenberg.moviedbshell.viewHolders.PopularMovieAdapter
import greenberg.moviedbshell.R
import timber.log.Timber

class PopularMoviesFragment :
        MvpFragment<PopularMoviesView, PopularMoviesPresenter>(),
        PopularMoviesView,
        SwipeRefreshLayout.OnRefreshListener {

    private var popularMovieActionBar: Toolbar? = null
    private var popularMovieRecycler: RecyclerView? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var popularMovieAdapter: PopularMovieAdapter? = null
    private var popularMovieRefresher: SwipeRefreshLayout? = null
    private var popularMovieLoadingBar: ProgressBar? = null
    private var loadingSnackbar: Snackbar? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        Timber.d("onCreate")
        navController = findNavController(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popular_movies_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularMovieRecycler = view.findViewById(R.id.popularMovieRecycler)
        popularMovieRefresher = view.findViewById(R.id.popularMovieRefresher)
        popularMovieLoadingBar = view.findViewById(R.id.popularMovieProgressBar)
        popularMovieRefresher?.setOnRefreshListener(this)

        //TODO: look into proper context for this; i.e. application or base
        linearLayoutManager = LinearLayoutManager(activity)
        popularMovieRecycler?.layoutManager = linearLayoutManager
        //TODO: revisit this initialization
        popularMovieAdapter = PopularMovieAdapter(presenter = presenter)
        popularMovieRecycler?.adapter = popularMovieAdapter

        presenter.initRecyclerPagination(popularMovieRecycler)
    }

    override fun createPresenter(): PopularMoviesPresenter = presenter ?: PopularMoviesPresenter()

    override fun showLoading(pullToRefresh: Boolean) {
        Timber.d("Show Loading")
        popularMovieRefresher?.visibility = View.GONE
        popularMovieRecycler?.visibility = View.GONE
        popularMovieLoadingBar?.visibility = View.VISIBLE
        presenter.loadPopularMovies(pullToRefresh)
    }

    override fun setMovies(items: List<PopularMovieResultsItem?>) {
        Timber.d("Setting Movies")
        popularMovieAdapter?.popularMovieList = items.toMutableList()
        popularMovieAdapter?.notifyDataSetChanged()
    }

    override fun addMovies(items: List<PopularMovieResultsItem?>) {
        Timber.d("Adding movies")
        items.map { popularMovieAdapter?.popularMovieList?.add(it) }
        popularMovieAdapter?.notifyDataSetChanged()
    }

    override fun showMovies() {
        Timber.d("Showing Movies")
        popularMovieRefresher?.isRefreshing = false
        popularMovieLoadingBar?.visibility = View.GONE
        popularMovieRefresher?.visibility = View.VISIBLE
        popularMovieRecycler?.visibility = View.VISIBLE
    }

    override fun showError(throwable: Throwable, pullToRefresh: Boolean) {
        Timber.d("Showing Error")
        popularMovieRefresher?.isRefreshing = false
    }

    override fun onRefresh() {
        Timber.d("On Refresh")
        presenter.refreshPage(popularMovieAdapter)
    }

    override fun showPageLoad() {
        Timber.d("Showing Page Load")
        loadingSnackbar = popularMovieRecycler?.let {
            Snackbar.make(it, getString(R.string.generic_loading_text), Snackbar.LENGTH_INDEFINITE)
        }
        loadingSnackbar?.show()
    }

    override fun hidePageLoad() {
        Timber.d("Showing Page Load")
        loadingSnackbar?.dismiss()
    }

    override fun showDetail(bundle: Bundle) {
        navController?.navigate(R.id.action_popularMoviesFragment_to_movieDetailFragment, bundle)
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        Timber.d("onPause")
        super.onPause()
    }

    override fun onStop() {
        Timber.d("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        super.onDestroy()
    }

    companion object {
        @JvmField
        val TAG: String = PopularMoviesFragment::class.java.simpleName
    }

}