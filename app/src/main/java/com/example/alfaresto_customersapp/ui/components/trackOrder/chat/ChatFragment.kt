package com.example.alfaresto_customersapp.ui.components.trackOrder.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentChatBinding
import com.example.alfaresto_customersapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : BaseFragment() {
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

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.loadingLayout.progressBar.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
        }

        binding.toolbar.apply {
            ivToolbarTitle.visibility = View.GONE
            tvToolbarText.visibility = View.VISIBLE
            tvToolbarText.text = getString(R.string.chat)
            btnLogout.visibility = View.GONE
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        val userId = viewModel.getUserId()
        val orderId = args.orderId
        if (orderId.isEmpty()) {
            Toast.makeText(requireContext(), "Order ID is missing", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).popBackStack()
            return
        }

        viewModel.listenForMessages(orderId)

        binding.imgBtnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(userId, orderId, message)
                binding.etChatInput.text.clear()
            } else {
                Toast.makeText(requireContext(), "Message cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messages.forEach { pair ->
                addMessageToChatView(pair.first, pair.second)
            }
        }
    }

    private fun addMessageToChatView(message: String, senderId: String) {
        lifecycleScope.launch {
            val userId = viewModel.getUserId()
            viewModel.setLoadingTrue()
            viewModel.restoID.collectLatest {
                delay(500)
                viewModel.setLoadingFalse()

                val layoutId = when (senderId) {
                    userId -> {
                        R.layout.customer_chat
                    }

                    it -> {
                        R.layout.resto_chat
                    }

                    else -> {
                        R.layout.customer_chat
                    }
                }

                val chatBubble = layoutInflater.inflate(layoutId, binding.llChatLayout, false)
                val messageTextView = chatBubble.findViewById<TextView>(R.id.customerChat)
                    ?: chatBubble.findViewById(R.id.restoChat)
                messageTextView.text = message
                binding.llChatLayout.addView(chatBubble)

                binding.svChatScroll.post {
                    binding.svChatScroll.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
