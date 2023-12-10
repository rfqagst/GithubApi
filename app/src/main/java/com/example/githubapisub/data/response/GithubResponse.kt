package com.example.githubapisub.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class GithubResponse(

    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @field:SerializedName("items")
    val items: List<ItemsItem?>? = null
) : Parcelable

@Parcelize
data class ItemsItem(

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("login")
    val login: String? = null,
) : Parcelable
