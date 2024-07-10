package com.example.alfaresto_customersapp.ui.components.trackOrder.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentChatBinding
import com.example.alfaresto_customersapp.ui.components.trackOrder.chat.adapter.ChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private val adapter by lazy { ChatAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        if (args.orderId.isEmpty()) {
            Toast.makeText(requireContext(), "Order ID is missing", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).popBackStack()
        }

        viewModel.setOrderId(args.orderId)

        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.toolbar.apply {
            ivToolbarTitle.visibility = View.GONE
            tvToolbarText.visibility = View.VISIBLE
            tvToolbarText.text = getString(R.string.chat)
            btnLogout.visibility = View.GONE
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                if (
                    findNavController().currentBackStack.value.size > 1
                ) {
                    findNavController().popBackStack()
                    return@setOnClickListener
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_chat_fragment_to_order_history_fragment)
                }
            }
            onEmbeddedBackPressed()
        }

        binding.imgBtnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.etChatInput.apply {
                    text.clear()
                }
            } else {
                Toast.makeText(requireContext(), "Message cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                adapter.submitList(messages) {
                    Timber.d("Messages: $messages")
                    binding.rvChat.post {
                        binding.rvChat.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
        }
    }

    private fun onEmbeddedBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            @SuppressLint("RestrictedApi")
            override fun handleOnBackPressed() {
                if (
                    findNavController().currentBackStack.value.size > 1
                ) {
                    findNavController().popBackStack()
                    return
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_chat_fragment_to_order_history_fragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
