package com.example.cs4520_final_project.ui.view_listing

import com.example.cs4520_final_project.data.AuthRepository
import kotlin.collections.filterNot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs4520_final_project.data.CampusSwapRepository
import com.example.cs4520_final_project.data.CommentInfo
import com.example.cs4520_final_project.data.ListingInfo
import com.example.cs4520_final_project.data.ProfileInfo
import kotlin.collections.emptyList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.map

class CampusSwapViewModel(
    private val auth: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _listings = MutableStateFlow<List<ListingInfo>>(emptyList())
    val listings: StateFlow<List<ListingInfo>> = _listings.asStateFlow()

    private val _profileInfo = MutableStateFlow<ProfileInfo?>(null)
    val profileInfo: StateFlow<ProfileInfo?> = _profileInfo.asStateFlow()
    private var firestoreRepo: CampusSwapRepository? = null

    private var observeJob: Job? = null

    init {
        viewModelScope.launch {
            auth.observeAuthState().collect { user ->
                observeJob?.cancel()
                if (user != null) {
                    firestoreRepo = CampusSwapRepository(user.uid)
                    _profileInfo.value = firestoreRepo!!.loadProfile()
                    observeJob = viewModelScope.launch {
                        firestoreRepo!!.observeListings().collect { _listings.value = it }
                    }
                } else {
                    firestoreRepo = null
                    _listings.value = emptyList()
                    _profileInfo.value = null
                }
            }
        }
    }

    fun addListing(listing: ListingInfo) {
        viewModelScope.launch {
            val remote = firestoreRepo
            remote?.saveListing(listing)
        }
    }

    fun updateListing(listing: ListingInfo) {
        viewModelScope.launch {
            val remote = firestoreRepo
            remote?.updateListing(listing)
        }
    }

    fun observeComments(listingId: Long): Flow<List<CommentInfo>> {
        return firestoreRepo?.observeComments(listingId) ?: emptyFlow()
    }

    fun saveComment(comment: CommentInfo) {
        viewModelScope.launch {
            firestoreRepo?.saveComment(comment)
        }
    }

    fun signOut() {
        auth.signOut()
        firestoreRepo = null
        _listings.value = emptyList()
        _profileInfo.value = null
    }

    fun removeListing(id: Long) {
        viewModelScope.launch {
            val remote = firestoreRepo
            remote
        }
    }

    fun updateProfile(profile: ProfileInfo) {
        viewModelScope.launch {
            firestoreRepo?.saveProfile(profile)
            _profileInfo.value = profile
        }
    }
}