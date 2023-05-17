package com.tonyk.android.movieo.fragments


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.tonyk.android.movieo.R
import com.tonyk.android.movieo.databinding.FragmentPosterDialogBinding

class PosterDialogFragment: DialogFragment() {
    private var _binding: FragmentPosterDialogBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val args: PosterDialogFragmentArgs by navArgs()

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentPosterDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(activity, R.style.TransparentDialog)
        builder.setView(binding.root)


        binding.imageView.load(args.posterLink)
        binding.imageView.setOnClickListener {
            findNavController().popBackStack()
        }

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}