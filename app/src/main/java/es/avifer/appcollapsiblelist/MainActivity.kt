package es.avifer.appcollapsiblelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.avifer.collapsiblelist.CollapsibleList

class MainActivity : AppCompatActivity() {

    private val listNames = listOf(
        "Oliver",
        "Jack",
        "Harry",
        "Jacob",
        "Charlie",
        "Thomas",
        "George",
        "Oscar",
        "James",
        "William",
        "Charlie",
        "Thomas",
        "George",
        "Oscar",
        "James",
        "William",
    )

    private var collapsibleList1: CollapsibleList? = null
    private var collapsibleList2: CollapsibleList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCollapsibleList()
    }

    private fun initCollapsibleList() {
        collapsibleList1 = findViewById(R.id.activity_main__collapsible_list__first_list)
        collapsibleList1?.setAdapter(AdapterListCollapsible(listNames))

        collapsibleList2 = findViewById(R.id.activity_main__collapsible_list__second_list)
        collapsibleList2?.setAdapter(AdapterListCollapsible(listNames))
    }
}