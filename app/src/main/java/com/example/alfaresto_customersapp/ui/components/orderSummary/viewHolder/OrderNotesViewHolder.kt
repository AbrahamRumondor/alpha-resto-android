package com.example.alfaresto_customersapp.ui.components.orderSummary.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryNotesBinding
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class OrderNotesViewHolder(
    private val binding: OrderSummaryNotesBinding,
    private val itemListener: OrderSummaryItemListener?
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.run {
            etNotes.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val etNotesText = etNotes.text.toString()
                    itemListener?.onNotesFilled(etNotesText)
                }
            }

            etNotes.setOnEditorActionListener { _, _, _ ->
                val etNotesText = etNotes.text.toString()
                itemListener?.onNotesFilled(etNotesText)
                etNotes.clearFocus()
                true
            }
        }
    }
}
