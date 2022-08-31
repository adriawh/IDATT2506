package ntnu.adriawh.oving4

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    private var mListener: OnFragmentInteractionListener? = null

    private var movies: Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movies = resources.getStringArray(R.array.movies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(position: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            activity as OnFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity must implement OnFragmentInteractionListener"
            )
        }
    }


    fun setSelection(position: Int){
        val listView = requireView().findViewById<ListView>(R.id.listView)
        listView.setItemChecked(position, true)
    }

    private fun initList() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_activated_1, movies)
        val listView = requireView().findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View, position: Int, id: Long ->
                mListener!!.onFragmentInteraction(position)
            }
    }




}