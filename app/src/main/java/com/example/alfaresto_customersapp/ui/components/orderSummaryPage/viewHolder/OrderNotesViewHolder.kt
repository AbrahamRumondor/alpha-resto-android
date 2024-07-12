package com.example.alfaresto_customersapp.ui.components.orderSummaryPage.viewHolder

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryNotesBinding
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener
import timber.log.Timber

class OrderNotesViewHolder(
    private val binding: OrderSummaryNotesBinding,
    private val itemListener: OrderSummaryItemListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.run {
            etNotes.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val etNotesText = etNotes.text.toString()
                        itemListener?.onNotesFilled(etNotesText)
                    }
                }
                setOnEditorActionListener { _, _, _ ->
                    val etNotesText = etNotes.text.toString()
                    Timber.tag("notes vh").d("Notes: $etNotesText")
                    itemListener?.onNotesFilled(etNotesText)

                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(windowToken, 0)

                    etNotes.clearFocus()
                    true
                }
            }
        }
    }
}
