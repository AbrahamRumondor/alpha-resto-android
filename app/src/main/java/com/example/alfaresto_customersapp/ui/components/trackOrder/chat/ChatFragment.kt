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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private val chatViews = mutableListOf<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderId = args.orderId

        if (orderId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Order ID is missing", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
            return
        }

        viewModel.listenForMessages(orderId)

        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            binding.chatLinearLayout.removeAllViews()
            messages.forEach { message ->
                addMessageToChatView(message.first, message.second)
            }
        }
    }

    private fun sendMessage() {
        val orderId = args.orderId
        val message = binding.chatInput.text.toString()
        if (message.isNotBlank()) {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid

            if (userId != null) {
                viewModel.sendMessage(userId, orderId, message)
                addMessageToChatView(message, true)
                binding.chatInput.text.clear()
                Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addMessageToChatView(message: String, isSent: Boolean) {
        val layoutId = if (isSent) R.layout.customer_chat else R.layout.resto_chat
        val textView = LayoutInflater.from(requireContext())
            .inflate(layoutId, binding.chatLinearLayout, false) as TextView
        textView.text = message
        binding.chatLinearLayout.addView(textView)
        chatViews.add(textView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeListener()
    }
}