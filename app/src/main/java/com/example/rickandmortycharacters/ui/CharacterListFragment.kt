package com.example.rickandmortycharacters.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortycharacters.viewmodels.CharacterListViewModel
import com.example.rickandmortycharacters.R
import com.example.rickandmortycharacters.adapters.CharacterAdapter
import com.example.rickandmortycharacters.adapters.OnCharacterListener
import com.example.rickandmortycharacters.models.Character
import kotlinx.android.synthetic.main.character_list_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class CharacterListFragment : Fragment(), OnCharacterListener {
    private val adapter = CharacterAdapter(this)
    private val layoutManager: RecyclerView.LayoutManager by inject()
    private val viewModel: CharacterListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.character_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initSearchView()
        observeCharacters()
        observeErrors()
        viewModel.getCharacters(1, "")
    }

    private fun subscribeObservers(){
        observeCharacters()
        observeErrors()

    }

    private fun observeCharacters() {
        viewModel.getCharactersLiveData().observe(this) {
            showCharacters(it)
        }
    }

    private fun observeErrors(){
        viewModel.getErrorLiveData().observe(this){ throwable ->
            throwable.message?.let { showErrorMessage(it) }
        }
    }


    private fun initRecyclerView(){
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)){
                    viewModel.searchNextPage()
                }
            }
        })
    }

    private fun initSearchView(){
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getCharacters(1, query)
                search_view.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun showErrorMessage(message: String){
        Toast.makeText(
            activity,
            "Error : $message",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showCharacters(characters: List<Character>){
        adapter.setCharacters(characters)
    }


    override fun onCharacterClick(position: Int) {
        val intent = Intent(activity, CharacterActivity::class.java)
        intent.putExtra("id", adapter.getSelectedCharacter(position)?.id)
        startActivity(intent)
    }
}
