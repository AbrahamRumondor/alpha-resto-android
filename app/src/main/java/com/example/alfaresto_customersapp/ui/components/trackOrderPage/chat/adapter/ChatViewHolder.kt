package com.example.alfaresto_customersapp.ui.components.trackOrderPage.chat.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.BubbleChatItemBinding
import com.example.alfaresto_customersapp.domain.model.Chat

class ChatViewHolder(private val binding: BubbleChatItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat) {

        val tvUsernameParams = binding.tvUsername.layoutParams as LinearLayout.LayoutParams
        val tvMessageParams = binding.tvMessage.layoutParams as LinearLayout.LayoutParams

        if (chat.userName == "Alfa Resto") {
            tvUsernameParams.gravity = Gravity.START
            tvMessageParams.gravity = Gravity.START

            binding.tvUsername.layoutParams = tvUsernameParams
            binding.tvMessage.let {
                it.layoutParams = tvMessageParams
                it.setBackgroundResource(R.drawable.resto_chat_shape)
            }
        } else {
            tvUsernameParams.gravity = Gravity.END
            tvMessageParams.gravity = Gravity.END

            binding.tvUsername.layoutParams = tvUsernameParams
            binding.tvMessage.let {
                it.layoutParams = tvMessageParams
                it.setBackgroundResource(R.drawable.sender_chat_shape)
            }
        }

        binding.tvUsername.text = chat.userName
        binding.tvMessage.text = chat.message
    }

    companion object {
        fun create(view: ViewGroup): ChatViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = BubbleChatItemBinding.inflate(inflater, view, false)
            return ChatViewHolder(binding)
        }
    }
}