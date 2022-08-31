package ntnu.adriawh.oving4

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), ListFragment.OnFragmentInteractionListener  {

    private var currentPosition = 0

    private lateinit var informationFragment : InformationFragment
    private lateinit var listFragment: ListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOrientation(resources.configuration)
        informationFragment = supportFragmentManager.findFragmentById(R.id.fragment2) as InformationFragment
        listFragment = supportFragmentManager.findFragmentById(R.id.fragment1) as ListFragment
    }

    //SETTING SELECTED MOVIE ON FIRST
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        informationFragment.setMovie(currentPosition)
        listFragment.setSelection(currentPosition)
    }

    //MENU
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_previous -> showPrevious()
            R.id.menu_next -> showNext()
        }
        return true
    }

    //MENU BUTTONS
    private fun showNext(){
        currentPosition++
        if(currentPosition >= 5){
            currentPosition = 0
        }
        informationFragment.setMovie(currentPosition)
        listFragment.setSelection(currentPosition)
    }

    private fun showPrevious(){
        currentPosition--
        if(currentPosition < 0){
            currentPosition = 4
        }
        informationFragment.setMovie(currentPosition)
        listFragment.setSelection(currentPosition)
    }


    override fun onFragmentInteraction(position: Int) {
        currentPosition = position
        informationFragment.setMovie(position)
    }

    //ORIENTATION
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setOrientation(newConfig)
    }

    private fun setOrientation(config: Configuration) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
           findViewById<LinearLayout>(R.id.container).orientation = LinearLayout.VERTICAL
        } else {
            findViewById<LinearLayout>(R.id.container).orientation = LinearLayout.HORIZONTAL
        }
        transaction.commit()
    }

}