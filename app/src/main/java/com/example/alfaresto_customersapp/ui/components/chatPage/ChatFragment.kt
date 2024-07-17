package com.example.alfaresto_customersapp.ui.components.chatPage

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
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.databinding.FragmentChatBinding
import com.example.alfaresto_customersapp.domain.model.Chat
import com.example.alfaresto_customersapp.ui.components.chatPage.adapter.ChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private val adapter by lazy { ChatAdapter() }

    private var messageCollection: List<Chat>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        if (args.orderId.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.order_id_missing),
                Toast.LENGTH_SHORT
            ).show()
            Navigation.findNavController(binding.root).popBackStack()
        }

        viewModel.setOrderId(args.orderId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChat.adapter = adapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true

        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.itemAnimator = null

        binding.toolbar.apply {
            ivToolbarTitle.visibility = View.GONE
            tvToolbarText.visibility = View.VISIBLE
            tvToolbarText.text = getString(R.string.chat)
            btnLogout.visibility = View.GONE
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            onEmbeddedBackPressed()
        }

        binding.run {
            etChatInput.apply {
                setOnClickListener {
                    messageCollection?.let {
                        adapter.submitList(messageCollection) {
                            binding.rvChat.post {
                                binding.rvChat.scrollToPosition(adapter.itemCount - 1)
                            }
                        }
                    }
                }
                setOnEditorActionListener { _, _, _ ->
                    val message = binding.etChatInput.text.toString()
                    onBtnSendClicked(message)

                    true
                }
            }
        }

        binding.imgBtnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString()
            onBtnSendClicked(message)
        }

        lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                messageCollection = messages
                adapter.submitList(messages) {
                    binding.rvChat.post {
                        binding.rvChat.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
        }

        setConnectionBehaviour()
        binding.inclInternet.btnInetTryAgain.setOnClickListener {
            setConnectionBehaviour()
        }
    }

    private fun onBtnSendClicked(message: String) {
        if (message.isNotEmpty()) {
            viewModel.sendMessage(message)
            binding.etChatInput.apply {
                text.clear()
            }
        }
    }

    private fun setConnectionBehaviour() {
        if (NetworkUtils.isConnectedToNetwork.value == false) {
            binding.inclInternet.root.visibility = View.VISIBLE
            binding.clBase.visibility = View.GONE
            Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
        } else {
            binding.inclInternet.root.visibility = View.GONE
            binding.clBase.visibility = View.VISIBLE
        }
    }

    private fun onEmbeddedBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
