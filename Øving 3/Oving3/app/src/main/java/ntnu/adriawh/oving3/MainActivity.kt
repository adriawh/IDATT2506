package ntnu.adriawh.oving3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var venner : Array<Friend> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFriendList()
        initAddFriendButton()
        initCancelEditButton()
    }

    private fun initAddFriendButton( ){
        findViewById<Button>(R.id.addFriendButton).setOnClickListener{
            val name = findViewById<EditText>(R.id.nameInput).text.toString()
            val birthday = findViewById<EditText>(R.id.birthdayInput).text.toString()

            venner = venner.plus(Friend(name, birthday))
            findViewById<EditText>(R.id.nameInput).setText("", TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.birthdayInput).setText("", TextView.BufferType.EDITABLE)

            initFriendList()
        }
    }


    private fun initEditFriend(position: Int) {
        showEditFriendButton()

        findViewById<EditText>(R.id.nameInput).setText(venner[position].navn, TextView.BufferType.EDITABLE)
        findViewById<EditText>(R.id.birthdayInput).setText(venner[position].fødselsdato, TextView.BufferType.EDITABLE)

        findViewById<Button>(R.id.editFriendButton).setOnClickListener{

            val name = findViewById<EditText>(R.id.nameInput).text.toString()
            val birthday = findViewById<EditText>(R.id.birthdayInput).text.toString()

            venner[position] = (Friend(name, birthday))
            findViewById<EditText>(R.id.nameInput).setText("", TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.birthdayInput).setText("", TextView.BufferType.EDITABLE)

            initFriendList()
            showAddFriendButton()
        }
    }

    private fun initFriendList() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, venner)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>?, valgt: View, position: Int, id: Long ->
                initEditFriend(position)
            }
    }


    private fun initCancelEditButton() {
        findViewById<Button>(R.id.cancelEditButton).setOnClickListener {
            findViewById<EditText>(R.id.nameInput).setText("", TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.birthdayInput).setText("", TextView.BufferType.EDITABLE)
            showAddFriendButton()

            initFriendList()
        }
    }

    private fun showAddFriendButton(){
        findViewById<Button>(R.id.addFriendButton).visibility = View.VISIBLE
        findViewById<Button>(R.id.editFriendButton).visibility = View.GONE
        findViewById<Button>(R.id.cancelEditButton).visibility = View.GONE
    }

    private fun showEditFriendButton(){
        findViewById<Button>(R.id.addFriendButton).visibility = View.GONE
        findViewById<Button>(R.id.editFriendButton).visibility = View.VISIBLE
        findViewById<Button>(R.id.cancelEditButton).visibility = View.VISIBLE
    }
}