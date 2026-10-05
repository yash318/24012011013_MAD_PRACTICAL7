package com.example.mad_24012011013_practical7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class PersonAdapter(
    private val context: Context,
    private val people: List<Person>,
    private val onEdit: (Person) -> Unit,
    private val onDelete: (Person) -> Unit
) : BaseAdapter() {
    override fun getCount() = people.size
    override fun getItem(position: Int) = people[position]
    override fun getItemId(position: Int) = people[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.single_item, parent, false)
        val person = getItem(position)
        view.findViewById<TextView>(R.id.nameText).text = person.name
        view.findViewById<TextView>(R.id.contactText).text = "${person.phone}\n${person.email}"
        view.findViewById<TextView>(R.id.addressText).text = person.address
        view.findViewById<Button>(R.id.editButton).setOnClickListener { onEdit(person) }
        view.findViewById<Button>(R.id.deleteButton).setOnClickListener { onDelete(person) }
        return view
    }
}
