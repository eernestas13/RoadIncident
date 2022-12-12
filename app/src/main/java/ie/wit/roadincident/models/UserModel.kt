package ie.wit.roadincident.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var userName: String = "",
                     var userPassword: String = "") : Parcelable

