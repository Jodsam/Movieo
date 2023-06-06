package com.tonyk.android.movieo.view.detailsmovie.detaildialogs

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import com.tonyk.android.movieo.R

class RatingDialogFragment : DialogFragment() {
    private lateinit var ratingBar: RatingBar
    private lateinit var vibrator: Vibrator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rating_dialog, container, false)

        ratingBar = view.findViewById(R.id.rating_bar)
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            val result = Bundle().apply {
                putFloat("rating", rating)
            }
            parentFragmentManager.setFragmentResult(REQUEST_RATING, result)
            dismiss()
            vibrate()
        }
        return view
    }

    companion object {
        const val REQUEST_RATING = "REQUEST_RATING"
    }
    private fun vibrate() {
        // Vibrate the device briefly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }
}
