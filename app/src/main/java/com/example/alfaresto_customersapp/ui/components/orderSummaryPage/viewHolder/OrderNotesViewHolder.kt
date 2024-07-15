package com.example.alfaresto_customersapp.ui.components.orderSummaryPage.viewHolder

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
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
                // Adding TextWatcher to observe every character written
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // Do something before the text is changed
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // Do something when the text is being changed
                        itemListener?.onNotesFilled(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {
                        // Do something after the text has been changed
                    }
                })
            }
        }
    }
}
