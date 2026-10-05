package com.example.mad_24012011013_practical7

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class PersonAdapter(
    private var persons: ArrayList<Person>,
    private val onDeleteClickListener: (Person) -> Unit
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val phoneTextView: TextView = itemView.findViewById(R.id.phone)
        val emailTextView: TextView = itemView.findViewById(R.id.email)
        val addressTextView: TextView = itemView.findViewById(R.id.address)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.listview_component,
                parent,
                false
            )

        return PersonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return persons.size
    }

    override fun onBindViewHolder(
        holder: PersonViewHolder,
        position: Int
    ) {

        val person = persons[position]

        holder.nameTextView.text = person.name
        holder.phoneTextView.text = person.phoneNo
        holder.emailTextView.text = person.emailId
        holder.addressTextView.text = person.address

        holder.deleteButton.setOnClickListener {
            onDeleteClickListener(person)
        }

        val obj = person as Serializable
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MapActivity::class.java).apply {
                putExtra("Object", obj)
            }
            context.startActivity(intent)
        }
    }

    fun updateData(newPersons: ArrayList<Person>) {
        persons = newPersons
        notifyDataSetChanged()
    }
}