package com.example.alfaresto_customersapp.ui.components.trackOrder.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = viewModel.getUserId()
        val orderId = args.orderId
        val restoId = "NrhoLsLLieXFly9dXj7vu2ETi1T2"
        if (orderId.isEmpty()) {
            Toast.makeText(requireContext(), "Order ID is missing", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
            return
        }

        viewModel.listenForMessages(orderId)

        binding.sendButton.setOnClickListener {
            val message = binding.chatInput.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(userId, orderId, message)
                binding.chatInput.text.clear()
            } else {
                Toast.makeText(requireContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messages.forEach { pair ->
                addMessageToChatView(pair.first, pair.second)
            }
        }
    }

    private fun addMessageToChatView(message: String, senderId: String) {
        val userId = viewModel.getUserId()
        val restoId = viewModel.restoId

        val layoutId = when (senderId) {
            userId -> {
                R.layout.customer_chat
            }
            restoId -> {
                R.layout.resto_chat
            }
            else -> {
                R.layout.customer_chat
            }
        }

        val textView = LayoutInflater.from(requireContext())
            .inflate(layoutId, binding.chatLinearLayout, false) as TextView
        textView.text = message
        binding.chatLinearLayout.addView(textView)
    }
}
