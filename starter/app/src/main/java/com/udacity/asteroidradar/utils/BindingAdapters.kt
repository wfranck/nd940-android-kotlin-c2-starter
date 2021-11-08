package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

@BindingAdapter("imageData")
fun bindPictureOfDayImage(imageView: ImageView, data: PictureOfDay?) {
    val context = imageView.context
    val picasso = Picasso.get()
    picasso.setIndicatorsEnabled(true)
    picasso.load(data?.url)
        .placeholder(R.drawable.placeholder_picture_of_day)
        .placeholder(R.drawable.ic_image_searching)
        .into(imageView)
    if (data != null) {
        imageView.contentDescription = String.format(context.getString(
            R.string.nasa_picture_of_day_content_description_format),
            data.title)
    } else {
        imageView.contentDescription = context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }

}


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, asteroid: Asteroid) {
    val context = imageView.context
    if (asteroid.isPotentiallyHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = String.format(context.getString(
            R.string.asteroid_status_potentially_hazardous),
            asteroid.codename)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = String.format(context.getString(
            R.string.asteroid_status_normal),
            asteroid.codename)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}